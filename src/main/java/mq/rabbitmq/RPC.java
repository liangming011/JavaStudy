package mq.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class RPC {

    /**
     * SingleToMany我们学习了如何使用工作队列在多个工作人员之间分配耗时的任务。
     * 但是如果我们需要在远程计算机上运行一个函数并等待结果呢？嗯，这是一个不同的故事。此模式通常称为远程过程调用或RPC。
     *
     * 在本教程中，我们将使用RabbitMQ构建RPC系统：客户端和可伸缩的RPC服务器。
     * 由于我们没有任何值得分发的耗时任务，我们将创建一个返回Fibonacci数字的虚拟RPC服务。
     *
     * 我们的RPC将这样工作：
     *
     * 1. 对于RPC请求，客户端发送带有两个属性的消息：replyTo，设置为仅为请求创建的匿名独占队列;以及correlationId，设置为每个请求的唯一值。
     * 2. 请求被发送到rpc_queue队列。
     * 3. RPC worker（aka：server）正在等待该队列上的请求。当出现请求时，它会执行该作业，并使用来自replyTo字段的队列将带有结果的消息发送回客户端。
     * 客户端等待回复队列上的数据。出现消息时，它会检查correlationId属性。如果它与请求中的值匹配，则返回对应用程序的响应。
     * */
}

/**
 * 《客户端界面》
 *
 * 为了说明如何使用RPC服务，我们将创建一个简单的客户端类。
 * 它将公开一个名为call的方法，该方法发送一个RPC请求并阻塞，直到收到答案为止：
 *
 * 《关于RPC的说明》
 * 尽管RPC在计算中是一种非常常见的模式，但它经常受到批评。
 * 当程序员不知道函数调用是本地的还是慢的RPC时，会出现问题。
 * 这样的混淆导致系统不可预测，并增加了调试的不必要的复杂性。
 * 错误使用RPC可以导致不可维护的意大利面条代码，而不是简化软件。
 *
 * 考虑到这一点，请考虑以下建议：
 *
 * 确保显而易见哪个函数调用是本地的，哪个是远程的。记录您的系统。使组件之间的依赖关系变得清晰。
 * 处理错误案例。当RPC服务器长时间停机时，客户端应该如何反应？
 * 如有疑问，请避免使用RPC。如果可以，您应该使用异步管道 - 而不是类似RPC的阻塞，将结果异步推送到下一个计算阶段。
 * */

class RPCClient implements AutoCloseable {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";

    public RPCClient() throws IOException, TimeoutException {
        //我们建立了一个连接和渠道。
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public static void main(String[] argv) {
        try (RPCClient fibonacciRpc = new RPCClient()) {
            for (int i = 0; i < 32; i++) {
                String i_str = Integer.toString(i);
                System.out.println(" [x] Requesting fib(" + i_str + ")");
                //我们的call方法生成实际的RPC请求。
                String response = fibonacciRpc.call(i_str);
                System.out.println(" [.] Got '" + response + "'");
            }
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();

        }
    }

    public String call(String message) throws IOException, InterruptedException {
        //在这里，我们首先生成一个唯一的correlationId 数并保存它 - 我们的消费者回调将使用此值来匹配相应的响应。
        final String corrId = UUID.randomUUID().toString();

        //获取响应队列的名称:我们为回复创建一个专用的独占队列并订阅它
        String replyQueueName = channel.queueDeclare().getQueue();

        //为应答队列填写参数，绑定 将请求和响应关联的id 和响应队列:我们发布请求消息，其中包含两个属性：  replyTo和correlationId。
        AMQP.BasicProperties props = new AMQP.BasicProperties
                //deliveryMode：将消息标记为持久性（值为2）或瞬态（任何其他值）。您可能会记住第二个教程中的这个属性。
                //contentType：用于描述编码的mime类型。例如，对于经常使用的JSON编码，将此属性设置为：application / json是一种很好的做法。
                //replyTo：通常用于命名回调队列
                //correlationId：用于将RPC响应与请求相关联。
                .Builder()
                /**
                 * correlationId
                 * 在上面介绍的方法中，我们建议为每个RPC请求创建一个回调队列。
                 * 这是非常低效的，但幸运的是有更好的方法 - 让我们为每个客户端创建一个回调队列。
                 *
                 * 这引发了一个新问题，在该队列中收到响应后，不清楚响应属于哪个请求。那是在使用correlationId属性的时候 。
                 * 我们将为每个请求将其设置为唯一值。稍后，当我们在回调队列中收到一条消息时，我们将查看此属性，并根据该属性，
                 * 我们将能够将响应与请求进行匹配。如果我们看到未知的 correlationId值，我们可以安全地丢弃该消息 - 它不属于我们的请求。
                 *
                 * 您可能会问，为什么我们应该忽略回调队列中的未知消息，而不是失败并出现错误？这是由于服务器端可能存在竞争条件。
                 * 虽然不太可能，但RPC服务器可能会在向我们发送答案之后，但在发送请求的确认消息之前死亡。如果发生这种情况，
                 * 重新启动的RPC服务器将再次处理请求。这就是为什么在客户端上我们必须优雅地处理重复的响应，理想情况下RPC应该是幂等的。
                 * */
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        //提交参数消息，绑定 routingkey（请求队列名称队列）
        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

        //创建阻塞队列，并声明容量
        /*
        * 由于我们的消费者交付处理是在一个单独的线程中进行的，因此我们需要在响应到来之前暂停主线程。
        * 使用BlockingQueue是一种可能的解决方案。这里我们创建了ArrayBlockingQueue ，容量设置为1，因为我们只需要等待一个响应。
         */
        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        //创建消费监听:消费者正在做一个非常简单的工作，对于每个消费的响应消息，它检查correlationId 是否是我们正在寻找的那个。
        // 如果是这样，它会将响应置于BlockingQueue。
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                //同时主线程正在等待响应从BlockingQueue获取它。
                response.offer(new String(delivery.getBody(), "UTF-8"));
            }
        };
        //创建应答队列的消费者
        String ctag = channel.basicConsume(replyQueueName, true, deliverCallback, consumerTag -> { });

        String result = response.take();
        //取消消费者。调用消费者的{@link Consumer＃handleCancelOk} 方法。
        channel.basicCancel(ctag);
        return result;
    }

    public void close() throws IOException {
        connection.close();
    }
}

class RPCServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    private static int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] argv) throws Exception {

        //像往常一样，我们首先建立连接，通道和声明队列。
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            //清除给定队列的内容。
            channel.queuePurge(RPC_QUEUE_NAME);
            //我们可能希望运行多个服务器进程。为了在多个服务器上平均分配负载，我们需要在channel.basicQos中设置  prefetchCount设置。
            channel.basicQos(1);

            System.out.println(" [x] Awaiting RPC requests");

            Object monitor = new Object();
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                String response = "";

                try {
                    String message = new String(delivery.getBody(), "UTF-8");
                    int n = Integer.parseInt(message);

                    System.out.println(" [.] fib(" + message + ")");
                    response += fib(n);
                } catch (RuntimeException e) {
                    System.out.println(" [.] " + e.toString());
                } finally {
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    // RabbitMq使用者工作线程通知RPC服务器所有者线程
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };
            //我们使用basicConsume来访问队列，我们​​以对象（DeliverCallback）的形式提供回调，它将完成工作并发回响应。
            channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> { }));
            // 等待并准备好使用来自RPC客户端的消息。
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}