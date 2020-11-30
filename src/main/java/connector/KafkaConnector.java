package connector;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.formats.json.JsonNodeDeserializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;
import serializer.MapSchema;
import serializer.Tuple2Schema;
import utils.PropsHelper;

import java.util.Map;
import java.util.Properties;

/**
 * @author liuchenyu
 * @date 2020/11/4
 */
public class KafkaConnector {

    public static Properties readProp(){
        Properties kafkaProp = new Properties();
        kafkaProp.setProperty("bootstrap.servers","localhost:9092");
        kafkaProp.setProperty("group.id","test");
        return kafkaProp;
    }
    public static FlinkKafkaConsumer<String> consumer(String topicName){
        return new FlinkKafkaConsumer<>(
            topicName,
            new SimpleStringSchema(),
            readProp()
        );
    }
    public static FlinkKafkaConsumer<Map> mapConsumer(String topicName){
        return new FlinkKafkaConsumer<>(
            topicName,
            new MapSchema(),
            readProp()
        );
    }
    public static FlinkKafkaConsumer<ObjectNode> JsonNodeConsumer(String topicName){
        return new FlinkKafkaConsumer<>(
            topicName,
            new JsonNodeDeserializationSchema(),
            readProp()
        );
    }
    public static FlinkKafkaProducer<String> producer(String topicName){
        return new FlinkKafkaProducer<>(
            topicName,
            new SimpleStringSchema(),
            readProp()
        );
    }
}
