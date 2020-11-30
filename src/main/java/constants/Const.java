package constants;

import org.apache.http.HttpHost;

/**
 * @author liuchenyu
 * @date 2020/11/20
 */
public class Const {
    public static final String DM_URL = "jdbc:dm://10.10.133.220:5236/CSSBASE_FLINK";
    public static final String DM_USERNAME = "CSSBASE";
    public static final String DM_PASSWORD = "1234567890";
    public static final String MONGODB_URL = "mongodb://localhost:27017";
    public static final HttpHost ES_HOST = new HttpHost("10.10.133.53", 9200, "http");
}
