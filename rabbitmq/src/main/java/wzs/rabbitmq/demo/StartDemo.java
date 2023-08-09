package wzs.rabbitmq.demo;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * 踩坑记录
 * direct类型的交换机进行routingKey精确匹配,demo.#的routingKey就是demo.#,不会匹配demo.xxx
 *
 */
public class StartDemo {

    private static final String HOST = "192.168.246.132";

    private static final Integer PORT = 5672;

    private static final String USER_NAME = "root";

    private static final String PASSWORD = "root";

    static final String EXCHANGE_NAME = "exchange-demo";

    static final String QUEUE_NAME = "queue-demo";

    static ConnectionFactory connectionFactory;


    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            try {
                messageConsumer();
            } catch (Exception e) {

            }
        }).start();
        System.out.println("demo message consumer started...");
        messageProvider();
    }

    /**
     * 消息生产者
     *
     * @throws Exception
     */
    public static void messageProvider() throws Exception {
        // 创建链接工厂属性
        ConnectionFactory connectionFactory = connectionFactory();
        // 获取链接
        Connection connection = connectionFactory.newConnection();
        // 获取信道
        Channel channel = connection.createChannel();
        // 声明创建交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        // 创建队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 绑定交换机与队列
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "demo-test");
        // 发送一条demo消息
        for (int i = 0; i < 10; i++) {
            String message = "这是rabbitmq的一条demo消息" + i;
            channel.basicPublish(EXCHANGE_NAME, "demo-test", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("message send..." + i);
            TimeUnit.SECONDS.sleep(1);
        }
        // 资源关闭
        channel.close();
        connection.close();
//        connectionFactory.clone();
    }

    /**
     * 消息消费者
     */
    public static void messageConsumer() throws Exception {
        ConnectionFactory connectionFactory = connectionFactory();
        Connection connection = connectionFactory.newConnection();
        System.out.println(connection);
        Channel channel = connection.createChannel();
        channel.basicQos(64);
        Consumer demoConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("consumer receive message:" + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        // 绑定消费者和信道
        channel.basicConsume(QUEUE_NAME, demoConsumer);
        System.out.println("consumer wait...");
        TimeUnit.SECONDS.sleep(50L);
        channel.close();
        connection.close();
    }

    /**
     * 连接工厂
     *
     * @return rabbitmqConnectionFactory实例
     */
    static ConnectionFactory connectionFactory() {
        if (connectionFactory != null) {
            return connectionFactory;
        }
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(HOST);
        connectionFactory.setPort(PORT);
        connectionFactory.setUsername(USER_NAME);
        connectionFactory.setPassword(PASSWORD);
        StartDemo.connectionFactory = connectionFactory;
        return connectionFactory;
    }
}
