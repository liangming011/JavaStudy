package mq.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class PublishSubscribeFanout {

    /**
     * 《exchange》：
     * 在本教程的前几部分中，我们向队列发送消息和从队列接收消息。现在是时候在Rabbit中引入完整的消息传递模型了。
     *
     * 让我们快速回顾一下前面教程中介绍的内容：
     *
     * 甲 生产者是发送消息的用户的应用程序。
     * 甲 队列是存储消息的缓冲器。
     * 甲 消费者是接收消息的用户的应用程序。
     *
     * RabbitMQ中消息传递模型的核心思想是生产者永远不会将任何消息直接发送到队列。
     * 实际上，生产者通常甚至不知道消息是否会被传递到任何队列。
     *
     * 相反，生产者只能向exchange发送消息。交换是一件非常简单的事情。
     * 一方面，它接收来自生产者的消息，另一方面将它们推送到队列。交易所必须确切知道如何处理收到的消息。
     * 它应该附加到特定队列吗？它应该附加到许多队列吗？或者它应该被丢弃。其规则由交换类型定义 。
     *
     * 有几种交换类型可供选择：direct，topic，headers 和fanout。我们将专注于最后一个 - fanout。
     * 让我们创建一个这种类型的交换，并将其称为日志：
     * channel.exchangeDeclare（“logs”，“fanout”）;
     *
     * fanout交换非常简单。正如您可以从名称中猜到的那样，它只是将收到的所有消息广播到它知道的所有队列中。而这正是我们记录器所需要的。
     *
     * channel.basicPublish（“”，“hello”，null，message.getBytes（））;
     * 第一个参数是交换的名称。空字符串表示默认或无名交换：消息被路由到具有routingKey指定名称的队列（如果存在）。
     *
     * 现在，我们可以发布到我们的命名交换：
     * channel.basicPublish（“logs”，“”，null，message.getBytes（））;
     *
     * 《临时队列》：
     *
     * channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
     * 第一个参数是队列名称
     * 第二个参数表示是否持久化队列
     * 第三个参数表示是否是独占队列
     * 第四个参数表示是否是临时队列，（声明连接关闭或消失时（例如，由于底层TCP连接丢失），将删除独占队列)
     *
     * 获取队列名称：String queueName = channel.queueDeclare（）.getQueue（）;
     *
     * 《banding》绑定
     * 我们已经创建了一个fanout交换和一个队列。现在我们需要告诉交换机将消息发送到我们的队列。交换和队列之间的关系称为绑定。
     * channel.queueBind（queueName，“logs”，“”）;从现在开始，logs交换会将消息附加到我们的队列中。
     *
     * 下面将它们放在一起
     **/
}

/**
 * Producer
 * */
class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            //exchange 并制定类型
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            String message = argv.length < 1 ? "info: Hello World!":String.join(" ", argv);

            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}

/**
 * Consumer
 * */
class ReceiveLogs {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //exchange 并制定类型
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //获取队列名称
        String queueName = channel.queueDeclare().getQueue();
        //将队列和 exchange 交换器绑定 因为是 fanout 类型，routingkey 没有意义
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}

class ReceiveLogs1 {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //exchange 并制定类型
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //获取队列名称
        String queueName = channel.queueDeclare().getQueue();
        //将队列和 exchange 交换器绑定 因为是 fanout 类型，routingkey 没有意义
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}