package mq.rabbitmq;

import com.rabbitmq.client.*;

import java.util.ArrayList;
import java.util.List;

public class PublishSubscribeTopics {

    /**
     * 《Topic交流》
     *
     * 发送到主题交换的消息不能具有任意  routing_key - 它必须是由点分隔的单词列表。
     * 单词可以是任何内容，但通常它们指定与消息相关的一些功能。
     * 一些有效的路由密钥示例：“ stock.usd.nyse ”，“ nyse.vmw ”，“ quick.orange.rabbit ”
     * 。路由密钥中可以包含任意数量的单词，最多可达255个字节。
     *
     * banding_key也必须采用相同的形式。主题交换背后的逻辑 类似于直接交换-
     * 使用特定路由密钥发送的消息将被传递到与匹配绑定密钥绑定的所有队列。但是，绑定键有两个重要的特殊情况：
     *
     *  主题交换功能强大，可以像其他交易所一样。
     *
     *  【*】（星号）可以替代一个单词。： 当特殊字符“ * ”（星号）和“ ＃ ”（哈希）未在绑定中使用时，主题交换的行为就像直接交换一样。
     *  【＃】（hash）可以替换零个或多个单词。：当队列与“ ＃ ”（哈希）绑定密钥绑定时 - 它将接收所有消息，而不管路由密钥 - 如扇出交换。
     *
     *  把它们放在一起
     * 我们将在日志记录系统中使用主题交换。我们将首先假设日志的路由键有两个词：“ <facility>。<severity> ”。
     *
     * 代码与上一个教程PublishSubscribeDirect中的代码几乎相同 。
     *
     * */
}


class EmitLogTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            List<String> routingKey = new ArrayList();
            routingKey.add("info.key.black");
            routingKey.add("error.key.orange");
            routingKey.add("debug.key.orange");

            for (int i=0;i<routingKey.size();i++) {
                String message ="message"+routingKey.get(i);

                channel.basicPublish(EXCHANGE_NAME, routingKey.get(i), null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
            }
        }
    }
    //..
}

class ReceiveLogsTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //创建 exchange 并指定类型
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //获取队列名称
        String queueName = channel.queueDeclare().getQueue();

        //《直接交换》
        List<String> severity0 = new ArrayList();
        severity0.add("*.key.*");

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


class ReceiveLogsTopic0 {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //创建 exchange 并指定类型
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        //获取队列名称
        String queueName = channel.queueDeclare().getQueue();

        //《多个绑定》
        List<String> severity0 = new ArrayList();
        severity0.add("info.#");

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