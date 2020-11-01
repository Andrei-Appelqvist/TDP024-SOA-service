// # Start the ZooKeeper service
// $ bin/zookeeper-server-start.sh config/zookeeper.properties
//
//
// # Start the Kafka broker service
// $ bin/kafka-server-start.sh config/server.properties
//
//
//
// bin/kafka-topics.sh --create --topic access-events --bootstrap-server localhost:9092
//
// bin/kafka-topics.sh --create --topic error-events --bootstrap-server localhost:9092
//
// $ bin/kafka-console-consumer.sh --topic error-events --from-beginning --bootstrap-server localhost:9092

package se.liu.ida.tdp024.account.util.logger;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class KafkaObject {

  public void sendToKafka(String channel , String message) {
    // SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // Date date = new Date(System.currentTimeMillis());
    // String datetime = formatter.format(date);
    //
    // Properties props = new Properties();
    // props.put("bootstrap.servers", "localhost:9092");
    // props.put("transactional.id", "my-transactional-id");
    // Producer<String, String> producer = new KafkaProducer<>(props, new StringSerializer(), new StringSerializer());
    // producer.initTransactions();
    // try{
    //
    //   producer.beginTransaction();
    //   producer.send(new ProducerRecord<>(channel, "Can't find","["+datetime+"]"+message));
    // } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
    //   // We can't recover from these exceptions, so our only option is to close the producer and exit.
    //   ;
    //   producer.close();
    // } catch (final KafkaException e) {
    //   // For all other exceptions, just abort the transaction and try again.
    //
    //   producer.abortTransaction();
    // }
    // producer.close();
  }
}
