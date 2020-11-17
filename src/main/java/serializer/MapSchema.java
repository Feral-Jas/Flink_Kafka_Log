package serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

/**
 * @author liuchenyu
 * @date 2020/11/11
 */
public class MapSchema implements KafkaDeserializationSchema<Map> {
    private static Gson gson;
    public MapSchema(){
        gson = new GsonBuilder().create();
    }
    @Override
    public boolean isEndOfStream(Map map) {
        return false;
    }

    @Override
    public Map<String,Object> deserialize(ConsumerRecord<byte[], byte[]> consumerRecord) throws Exception {
        return gson.fromJson(new String(consumerRecord.value()),Map.class);
    }

    @Override
    public TypeInformation<Map> getProducedType() {
        return TypeExtractor.getForClass(Map.class);
    }
}