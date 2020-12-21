package connector;

import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuchenyu
 * @date 2020/10/22
 */


public class ElasticSink {
    private static List<HttpHost> httpHosts = new ArrayList<>();

    public static ElasticsearchSink<String> builder0(String indexName) {
        httpHosts.add(new HttpHost("10.10.133.53", 9200, "http"));
        ElasticsearchSink.Builder<String> esSinkBuilder = new ElasticsearchSink.Builder<>(
            httpHosts,
            new ElasticsearchSinkFunction<String>() {
                public IndexRequest createIndexRequest(String element) {
                    return Requests.indexRequest()
                        .index(indexName+"_"+LocalDate.now().toString())
                        .type("test")
                        .source(element, XContentType.JSON);
                }
                @Override
                public void process(String element, RuntimeContext ctx, RequestIndexer indexer) {
                    indexer.add(createIndexRequest(element));
                }
            }
        );
        esSinkBuilder.setBulkFlushMaxActions(1);
        return esSinkBuilder.build();
    }
}
