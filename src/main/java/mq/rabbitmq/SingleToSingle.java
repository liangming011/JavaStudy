package mq.rabbitmq;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.recovery.QueueRecoveryListener;

import java.io.IOException;

public class SingleToSingle {


}

/**
 * Producer
 * */
class Send {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {

        try {
            //创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            //添加连接工厂连接地址
            factory.setHost("localhost");
            //获取连接工厂的连接
            Connection connection = factory.newConnection();
            //创建channel
            Channel channel = connection.createChannel();

            //创建队列
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            //声明消息，可进行业务操作创建消息
            String message ="Hello World！";
            //将消息通过通道发送到 mqserver
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());

            System.out.println(" [x] Sent '" + message + "'");
            channel.close();
            connection.close();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

/**
 * Consumer
 * */
class Recv {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        try {
            //创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            //添加连接工厂连接地址
            factory.setHost("localhost");
            //获取连接工厂的连接
            Connection connection = factory.newConnection();
            //创建channel
            Channel channel = connection.createChannel();
            //创建队列
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            // 我们即将告诉服务器从队列中传递消息。因为它会异步地向我们发送消息，所以我们以对象的形式提供一个回调，
            // 它将缓冲消息，直到我们准备好使用它们。这就是DeliverCallback子类的作用。
            DeliverCallback deliverCallback = (String consumerTag,Delivery delivery) ->{
                String message = new String(delivery.getBody(), "UTF-8");

                System.out.println(" [x] Received '" + message + "'");
            };

            channel.basicConsume(QUEUE_NAME,true,deliverCallback,consumerTag -> { });


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}