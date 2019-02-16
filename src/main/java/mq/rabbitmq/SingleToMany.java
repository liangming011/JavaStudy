package mq.rabbitmq;

import com.rabbitmq.client.*;

public class SingleToMany {
}

/**
 * Producer
 * */
class Send0 {

    //声明一个工作队列名称
    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {


        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //给连接工厂添加连接地址
        factory.setHost("localhost");
        //从工厂中获取连接和连接通道
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            /*
            * 消息持久性：我们已经学会了如何确保即使消费者死亡，任务也不会丢失。但是如果RabbitMQ服务器停止，我们的任务仍然会丢失。
            *
            * 当RabbitMQ退出或崩溃时，它将忘记队列和消息，除非你告诉它不要。
            * 确保消息不会丢失需要做两件事：我们需要将《队列》和《消息》都标记为持久。
            *
            * 将消息标记为持久性并不能完全保证消息不会丢失。虽然它告诉RabbitMQ将消息保存到磁盘，
            * 但是当RabbitMQ接受消息并且尚未保存消息时，仍然有一个短时间窗口。
            * 此外，RabbitMQ不会为每条消息执行fsync（2） - 它可能只是保存到缓存而不是真正写入磁盘。
            * 持久性保证不强，但对于我们简单的任务队列来说已经足够了。如果您需要更强的保证，那么您可以使用 发布者确认。
            * */
            //创建消息队列,第一个《队列》的持久化打开
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);


            for (int i=0;i<10;i++) {
                argv = new String[]{"第."+i+"条.消.息.!."};
                //参数处理，join 可使用分隔符连接数组中不同的参数
                String message = String.join(" ", argv);
                channel.basicPublish("", TASK_QUEUE_NAME,
                        //现在我们需要将《消息》标记为持久性 - 通过将MessageProperties（实现BasicProperties）设置为值PERSISTENT_TEXT_PLAIN。
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }

        }
    }

}

//循环调度:默认情况下，RabbitMQ将按顺序将每条消息发送给下一个消费者。
// 平均而言，每个消费者将获得相同数量的消息。这种分发消息的方式称为循环法。与三个或更多工人一起尝试。
/**
 * Consumer
 * */
class Recv0 {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        //创建连接工厂，给连接工厂添加连接地址，获取 final 类型连接和连接通道，final 类型不可更改
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        //创建消息队列
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        /*
        * 公平派遣：您可能已经注意到调度仍然无法完全按照我们的意愿运行。
        * 例如，在有两个工人的情况下，当所有奇怪的消息都很重，甚至消息很轻时，
        * 一个工人将经常忙碌而另一个工作人员几乎不会做任何工作。好吧，RabbitMQ对此一无所知，仍然会均匀地发送消息。
        *
        * 发生这种情况是因为RabbitMQ只是在消息进入队列时调度消息。
        * 它不会查看消费者未确认消息的数量。它只是盲目地向第n个消费者发送每个第n个消息。
        *
        * 为了打败我们可以使用basicQos方法和  prefetchCount = 1设置。
        * 这告诉RabbitMQ不要一次向一个worker发送一条消息。或者，换句话说，
        * 在处理并确认前一个消息之前，不要向工作人员发送新消息。相反，它会将它发送给下一个仍然不忙的工人。
        * */
        //设置prefetchCount，允许一次性获取队列中的几条数据，均匀公平派遣队列
        channel.basicQos(1);


        //对队列进行监听，获取队列参数，进行处理
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } finally {
                System.out.println(" [x] Done");

              /*
               消息确认:执行任务可能需要几秒钟。
               您可能想知道如果其中一个消费者开始执行长任务并且仅在部分完成时死亡会发生什么。
               使用我们当前的代码，一旦RabbitMQ向客户发送消息，它立即将其标记为删除。
               在这种情况下，如果你杀死一个工人，我们将丢失它刚刚处理的消息。
               我们还将丢失分发给这个特定工作者但尚未处理的所有消息。

               为了确保消息永不丢失，RabbitMQ支持 消息确认。
               消费者发回ack（nowledgement）以告知RabbitMQ已收到，处理了特定消息，并且RabbitMQ可以自由删除它。

               如果消费者死亡（其通道关闭，连接关闭或TCP连接丢失）而不发送确认，RabbitMQ将理解消息未完全处理并将重新排队。
               如果同时有其他在线消费者在线，则会迅速将其重新发送给其他消费者。这样你就可以确保没有消息丢失，即使工人偶尔会死亡。

               默认情况下，手动消息确认已打开。

               在示例中，我们通过autoAck = true 标志明确地将它们关闭。
               一旦我们完成任务，就应该将此标志设置为false并从工作人员发送适当的确认。

               使用此代码，我们可以确定即使您在处理消息时使用CTRL + C杀死一名工作人员，也不会丢失任何内容。
               工人死后不久，所有未经确认的消息将被重新传递。

               确认必须在收到的交付的同一信道上发送。尝试使用不同的通道进行确认将导致通道级协议异常。
              */
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        //开启队列
        channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });
    }

    //假装工作划水
    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

class Recv1 {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        //创建连接工厂，给连接工厂添加连接地址，获取 final 类型连接和连接通道，final 类型不可更改
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        //创建消息队列
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        //设置prefetchCount，允许一次性获取队列中的几条数据
        channel.basicQos(1);


        //对队列进行监听，获取队列参数，进行处理
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } finally {
                System.out.println(" [x] Done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        //开启队列
        channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });
    }

    //假装工作划水
    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}