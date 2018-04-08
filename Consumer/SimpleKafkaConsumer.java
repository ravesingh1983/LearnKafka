import java.util.*;
import java.io.*;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class SimpleKafkaConsumer{

    public static void main(String[] args) throws Exception{

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093");
        props.put("group.id", "newgrp");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = null;

        try {
            consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Arrays.asList("3partReload"));
            //Start below 2 lines to reset the offsets
            //consumer.poll(0);
            //consumer.seekToBeginning(consumer.assignment());

            while (true){
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records){
                    System.out.println("Data= " + String.valueOf(record.value() + " Partition = " + record.partition() + " Offset " + record.offset()));
                }
                consumer.commitAsync();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }catch(Error ex){
            ex.printStackTrace();
        }
        finally{
            consumer.commitSync();
            consumer.close();
        }
    }
}
