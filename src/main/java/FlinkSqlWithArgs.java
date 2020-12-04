
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.function.Consumer;

/**
 * @author liuchenyu
 * @date 2020/11/27
 */
public class FlinkSqlWithArgs {
    public static void main(String[] args) {
        ConnectionString connString = new ConnectionString(
            "mongodb://localhost:27017"
        );
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connString)
            .retryWrites(true)
            .build();


        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("flink");
        MongoCollection<Document> sqljobs = database.getCollection("sqljobs");
        Document matchJob = sqljobs.find(new Document("_id", new ObjectId(args[0]))).first();
        assert matchJob != null;

        System.out.println(matchJob.getString("source"));
        System.out.println(matchJob.getString("sink"));
        System.out.println(matchJob.getString("sql"));






        EnvironmentSettings environmentSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        TableEnvironment tableEnv = TableEnvironment.create(environmentSettings);
        tableEnv.executeSql(matchJob.getString("source"));
        tableEnv.executeSql(matchJob.getString("sink"));
        tableEnv.executeSql(matchJob.getString("sql"));
    }
}
