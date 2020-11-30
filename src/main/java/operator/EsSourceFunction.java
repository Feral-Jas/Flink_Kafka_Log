package operator;

import constants.Const;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author liuchenyu
 * @date 2020/11/24
 */
public class EsSourceFunction extends RichParallelSourceFunction<Tuple2<LocalDateTime,String>> {
    private volatile boolean isRunning = true;
    private RestHighLevelClient client;
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        this.client = new RestHighLevelClient(
            RestClient.builder(Const.ES_HOST));
    }

    @Override
    public void run(SourceContext<Tuple2<LocalDateTime,String>> sourceContext) throws Exception {
        this.client = new RestHighLevelClient(
            RestClient.builder(Const.ES_HOST));
            SearchRequest searchRequest = new SearchRequest("microservice");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(
                QueryBuilders.rangeQuery("start_time")
                    .gte(LocalDateTime.now().minusSeconds(10L))
                    .lte(LocalDateTime.now())
            );
            searchRequest.source(searchSourceBuilder);
        while(isRunning){
            SearchResponse searchResponse = this.client.search(
                searchRequest,
                RequestOptions.DEFAULT
            );
            for (SearchHit hit : searchResponse.getHits()) {
                Tuple2<LocalDateTime,String> res = new Tuple2<>(
                    LocalDateTime.parse(hit.getSourceAsMap().get("start_time").toString()),
                    hit.getSourceAsMap().get("server_ip").toString()
                );
                System.out.println(res.f1);
                sourceContext.collect(res);
            }
            Thread.sleep(2000L);
        }
    }

    @Override
    public void cancel() {
        this.isRunning = false;
        try {
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        super.close();
    }
}
