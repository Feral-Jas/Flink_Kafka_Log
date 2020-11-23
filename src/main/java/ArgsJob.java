import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import connector.DmSink;
import jdk.nashorn.internal.ir.ObjectNode;
import job.MultiAggBySec;
import job.ParseLog;
import model.DmJdbc;
import operator.BatchSourceFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liuchenyu
 * @date 2020/11/19
 */
public class ArgsJob {
    public static DataStream<String> ds;
    public static void main(String... args) throws Exception {
        ConnectionString connString = new ConnectionString(
            "mongodb://localhost:27017"
        );
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connString)
            .retryWrites(true)
            .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("flink");
        MongoCollection<Document> flink = database.getCollection("flink");
        Document first = flink.find(new Document("name", args[0])).first();
        List<Document> graphs =(ArrayList<Document>) first.get("nodeDataArray");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        Gson gson = new GsonBuilder().create();
        graphs.forEach(
            graph-> {
                System.out.println(graph.getString("text"));
                switch (graph.getString("category")){

                    case "Start":
                        ds = env.setParallelism(1).addSource(new BatchSourceFunction("select * from CSSBASE_CL.S_LOG t where t.TIMESTAMP >=? and t.TIMESTAMP <=?",2));
                        break;
                    case "Operator":

                        break;
                    case "End":
                        ds.addSink(DmSink.sink());
                        break;
                    default:break;
                }
            }
        );
        env.execute("dm");
    }

}

