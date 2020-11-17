package serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

/**
 * @author liuchenyu
 * @date 2020/11/11
 */
public class Tuple2Schema implements KafkaDeserializationSchema<Tuple2<String,Double>> {
    private static Gson gson;
    public Tuple2Schema(){
        gson = new GsonBuilder().create();
    }
    @Override
    public boolean isEndOfStream(Tuple2<String, Double> stringIntegerTuple2) {
        return false;
    }

    @Override
    public Tuple2<String, Double> deserialize(ConsumerRecord<byte[], byte[]> consumerRecord) throws Exception {
        Map map = gson.fromJson(new String(consumerRecord.value()), Map.class);
        String name = map.get("name").toString();
        Double age = (Double) map.get("age");
        return new Tuple2<>(name,age);
    }

    @Override
    public TypeInformation<Tuple2<String,Double>> getProducedType() {
        return Types.TUPLE(Types.STRING,Types.DOUBLE);
    }
}
