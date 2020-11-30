import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author liuchenyu
 * @date 2020/11/20
 */
public class MongoTest {
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
        MongoCollection<Document> flink = database.getCollection("flink");
        Gson gson = new GsonBuilder().create();
//        {"name":"sqljob"
        Map map = gson.fromJson("{\"name\":\"dmjob1\",\"nodeDataArray\":[{\"category\":\"Start\",\"text\":\"select * from s_log\",\"__gohashid\":105424,\"key\":-1,\"loc\":\"-200.5 -212\"},{\"category\":\"Operator\",\"text\":\"sum\",\"__gohashid\":105620,\"key\":-2,\"loc\":\"-192.5 -58\"},{\"category\":\"End\",\"text\":\"Insert into slog values\",\"__gohashid\":105770,\"key\":-4,\"loc\":\"117.5 -87\"}],\"linkDataArray\":[{\"__gohashid\":105839,\"from\":-1,\"to\":-2,\"points\":{\"__gohashid\":109263,\"s\":true,\"j\":[{\"x\":-200.5,\"y\":-175.25,\"s\":true},{\"x\":-200.5,\"y\":-165.25,\"s\":true},{\"x\":-200.5,\"y\":-125.4688621520996,\"s\":true},{\"x\":-192.5,\"y\":-125.4688621520996,\"s\":true},{\"x\":-192.5,\"y\":-85.68772430419922,\"s\":true},{\"x\":-192.5,\"y\":-75.68772430419922,\"s\":true}],\"Aa\":8,\"Ga\":null,\"Zg\":null}},{\"__gohashid\":105981,\"from\":-2,\"to\":-4,\"points\":{\"__gohashid\":109363,\"s\":true,\"j\":[{\"x\":-167.67842864990234,\"y\":-58,\"s\":true},{\"x\":-157.67842864990234,\"y\":-58,\"s\":true},{\"x\":-67.78461074829102,\"y\":-58,\"s\":true},{\"x\":-67.78461074829102,\"y\":-87,\"s\":true},{\"x\":22.109207153320312,\"y\":-87,\"s\":true},{\"x\":32.10920715332031,\"y\":-87,\"s\":true}],\"Aa\":8,\"Ga\":null,\"Zg\":null}}]}",Map.class);
//        flink.insertOne(new Document(map));
//        flink.find(new Document("name","dmjob"),new Document(map));
        flink.find(new Document("name","dmjob1")).forEach(
            (Consumer<? super Document>) System.out::println
        );

    }
}
