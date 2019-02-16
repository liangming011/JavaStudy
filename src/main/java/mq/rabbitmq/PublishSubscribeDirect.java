package mq.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.ArrayList;
import java.util.List;

public class PublishSubscribeDirect {

    /**
     * 《直接交换》
     * 我们上一个教程中的日志记录系统向所有消费者广播所有消息。我们希望扩展它以允许根据消息的严重性过滤消息。
     * 例如，我们可能需要一个程序将日志消息写入磁盘以仅接收严重错误，而不是在警告或信息日志消息上浪费磁盘空间。
     *
     * 我们使用的是扇出交换，它没有给我们太大的灵活性 - 它只能进行无意识的广播。
     *
     * 我们将使用直接交换。直接交换背后的路由算法很简单 - 消息进入队列，其  绑定密钥与消息的路由密钥完全匹配。
     *
     * 《多个绑定》
     * 使用相同的绑定密钥绑定多个队列是完全合法的。在我们的例子中，我们可以在X和Q1之间添加绑定键黑色的绑定。
     * 在这种情况下，直接交换将表现得像扇出一样，并将消息广播到所有匹配的队列。路由密钥为黑色的消息将传送到  Q1和Q2。
     * */
}

class EmitLogDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            //创建 exchange 并添加 exchange 类型
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            List<String> severity = new ArrayList();
            severity.add("info");
            severity.add("error");
            severity.add("debug");

            for (int i=0;i<severity.size();i++){
                String message = severity.get(i)+"：hello,"+severity.get(i)+"message";
                channel.basicPublish(EXCHANGE_NAME, severity.get(i), null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + severity + "':'" + message + "'");

            }

        }
    }
//..
}

class ReceiveLogsDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //创建 exchange 并指定类型
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        //获取队列名称
        String queueName = channel.queueDeclare("queueNAME",true,false,false,null).getQueue();

        //《直接交换》
        List<String> severity0 = new ArrayList();
        severity0.add("info");

        for (String severity : severity0) {
            //绑定队列和 exchange 和 routingkey
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}


class ReceiveLogsDirect0 {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //创建 exchange 并指定类型
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        //获取队列名称
        String queueName = channel.queueDeclare().getQueue();

        //《多个绑定》
        List<String> severity0 = new ArrayList();
        severity0.add("info");
        severity0.add("error");
        severity0.add("debug");

        for (String severity : severity0) {
            //绑定队列和 exchange 和 routingkey
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}