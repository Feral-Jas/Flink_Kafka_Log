import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author liuchenyu
 * @date 2020/11/24
 */
public class ElasticTest {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("10.10.133.53", 9200, "http")));
        SearchRequest searchRequest = new SearchRequest("microservice");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(
            QueryBuilders.rangeQuery("start_time")
                .gte(LocalDateTime.now().minusSeconds(10L))
                .lte(LocalDateTime.now())
        );
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        searchResponse.getHits().forEach(
            item-> System.out.println(item.getSourceAsMap().get("server_ip"))
        );
        client.close();
    }
}
