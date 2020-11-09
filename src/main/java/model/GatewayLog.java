package model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * @author liuchenyu
 * @date 2020/10/29
 */
public class GatewayLog {
    @SerializedName(value="@timestamp",alternate = "timestamp")
    public String timestamp;
    @SerializedName(value="@metadata",alternate = "metadata")
    public Map metadata;
    public Map host;
    public String hostname;
    public String architechture;
    public Map os;
    public String id;
    public Map log;
    public String message;
    public Map input;
    public Map agent;
    public Map ecs;
    public EsbMonitor esbMonitor;
}
