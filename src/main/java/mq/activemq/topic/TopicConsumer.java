package mq.activemq.topic;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;


public class TopicConsumer {

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer messageConsumer;
    // 消息返回者
    private MessageProducer messageProducer;

    public static void main(String[] args) {
        /**
         * Pub/Sub模型中，消息可被多个对象接收，不同于P2P模型
         */
        TopicConsumer consumer1 = new TopicConsumer();
        consumer1.doReceive();
//        TopicConsumer consumer2 = new TopicConsumer();
//        consumer2.doReceive();
    }

    public void doReceive() {
        try {
            connectionFactory = new ActiveMQConnectionFactory();
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic(TopicProducer.TOPIC_NAME);
            messageConsumer = session.createConsumer(destination);


            // 方式一：监听接收
            receiveByListener();

            // 方式二：阻塞接收
            //receiveByManual();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过注册监听器的方式接收消息，属于被动监听
     */
    private void receiveByListener() {
        try {
            messageConsumer.setMessageListener(new MessageListener() {

                @Override
                public void onMessage(Message message) {
                    if (message instanceof TextMessage) {
                        try {
                            TextMessage msg = (TextMessage) message;
                            System.out.println("Received:“" + msg.getText()
                                    + "”");
                            messageProducer = session.createProducer(msg.getJMSReplyTo());
                            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                            if (message != null) {
                                String response = msg.getText()+"response";

                                TextMessage responseMessage = session.createTextMessage(msg.getText()+"response");
                                responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
                                messageProducer.send(responseMessage);
                            }
                            messageProducer.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过手动去接收消息的方式，属于主动获取
     */
    private void receiveByManual() {
        while (true) {
            try {
                /**
                 * 通过receive()方法阻塞接收消息，参数为超时时间（单位：毫秒）
                 */
                TextMessage message = (TextMessage) messageConsumer.receive(6000);

                if (message != null) {
                    System.out.println("Received:“" + message.getText()
                            + "”");
                    String response = message.getText()+"response";

                    TextMessage responseMessage = session.createTextMessage(message.getText()+"response");
                    responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
                    messageProducer.send(responseMessage);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
