import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@Slf4j
public class RocketMQTests {

    private final static String PRODUCER_GROUP = "ProducerGroupTest";

    private final static String CONSUMER_GROUP = "consumerGroupTest";

    private final static String NAME_SERVER_ADDRESS = "192.168.0.84:9876";

    private final static String TOPIC = "TopicTest";

    private final static String TAG_A = "TagA";

    private final static String TAG_B = "TagB";

    private final static String TAG_C = "TagC";

    private final static String TAG_D = "TagD";

    private final static String TAG_E = "TagE";

    private final static String[] TAG_ARRAY = new String[]{TAG_A, TAG_B, TAG_C, TAG_D, TAG_E};

    private final static String OR = "||";

    private final static String ALL = "*";

    private final static String TAG_EXPRESSION_CONSUMER = ALL;

    private final static String TAG_PRODUCER = TAG_A;

    private String message(int i) {
        return "消息" + i + ": " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date());
    }

    @Test
    public void syncProducer() throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new
                DefaultMQProducer(PRODUCER_GROUP);
        // Specify name server addresses.
        producer.setNamesrvAddr(NAME_SERVER_ADDRESS);
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 100; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(TOPIC,
                    TAG_PRODUCER,
                    message(i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            //Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }

    @Test
    public void asyncProducer() throws MQClientException, InterruptedException {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCER_GROUP);
        // Specify name server addresses.
        producer.setNamesrvAddr(NAME_SERVER_ADDRESS);
        //Launch the instance.
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);

        int messageCount = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(messageCount);
        for (int i = 0; i < messageCount; i++) {
            try {
                final int index = i;
                Message msg = new Message(TOPIC,
                        TAG_PRODUCER,
                        "OrderID188",
                        message(i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                producer.send(msg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        countDownLatch.countDown();
                        System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable e) {
                        countDownLatch.countDown();
                        System.out.printf("%-10d Exception %s %n", index, e);
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        countDownLatch.await(5, TimeUnit.SECONDS);
        producer.shutdown();
    }


    @Test
    public void oneWayProducer() throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCER_GROUP);
        // Specify name server addresses.
        producer.setNamesrvAddr(NAME_SERVER_ADDRESS);
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 100; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(TOPIC,
                    TAG_PRODUCER,
                    message(i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            //Call send message to deliver message to one of brokers.
            producer.sendOneway(msg);
        }
        //Wait for sending to complete
        Thread.sleep(5000);
        producer.shutdown();
    }


    @Test
    public void orderedProducer() throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCER_GROUP);
        producer.setNamesrvAddr(NAME_SERVER_ADDRESS);
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 100; i++) {
            int orderId = i % 10;
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(TOPIC, TAG_ARRAY[i % TAG_ARRAY.length], "KEY" + i,
                    message(i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = (Integer) arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, orderId);

            log.info(sendResult.toString());
            log.info("");
        }
        //server shutdown
        producer.shutdown();
    }

    @Test
    public void consumer() throws MQClientException, InterruptedException {

        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP);

        // Specify name server addresses.
        consumer.setNamesrvAddr(NAME_SERVER_ADDRESS);

        // Subscribe one more more topics to consume.
        consumer.subscribe(TOPIC, TAG_EXPRESSION_CONSUMER);
        // Register callback to execute on arrival of messages fetched from brokers.
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                for (MessageExt messageExt : msgs) {
                    log.info("主题：" + messageExt.getTags() + "；消息：" + new String(messageExt.getBody(), StandardCharsets.UTF_8));
                }
                log.info("");
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        log.info("Consumer Started.");
        new Scanner(System.in).nextLine();
    }

    @Test
    public void orderedConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP);
        consumer.setNamesrvAddr(NAME_SERVER_ADDRESS);
        consumer.subscribe(TOPIC, TAG_EXPRESSION_CONSUMER);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.registerMessageListener(new MessageListenerOrderly() {
            AtomicLong consumeTimes = new AtomicLong(0);

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                context.setAutoCommit(false);
                this.consumeTimes.incrementAndGet();
                if ((this.consumeTimes.get() % 3) == 0) {
                    for (MessageExt messageExt : msgs) {
                        log.info("挂起；主题：" + messageExt.getTags() + "；消息：" + new String(messageExt.getBody(), StandardCharsets.UTF_8));
                    }
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                } else {
                    for (MessageExt messageExt : msgs) {
                        log.info("成功；主题：" + messageExt.getTags() + "；消息：" + new String(messageExt.getBody(), StandardCharsets.UTF_8));
                    }
                    return ConsumeOrderlyStatus.SUCCESS;
                }
                /*System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
                this.consumeTimes.incrementAndGet();
                if ((this.consumeTimes.get() % 2) == 0) {
                    return ConsumeOrderlyStatus.SUCCESS;
                } else if ((this.consumeTimes.get() % 3) == 0) {
                    return ConsumeOrderlyStatus.ROLLBACK;
                } else if ((this.consumeTimes.get() % 4) == 0) {
                    return ConsumeOrderlyStatus.COMMIT;
                } else if ((this.consumeTimes.get() % 5) == 0) {
                    context.setSuspendCurrentQueueTimeMillis(3000);

                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
                return ConsumeOrderlyStatus.SUCCESS;*/
            }
        });

        consumer.start();
        System.out.printf("Consumer Started.%n");
        new Scanner(System.in).nextLine();
    }

}
