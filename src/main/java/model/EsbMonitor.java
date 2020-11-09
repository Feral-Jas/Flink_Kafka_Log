package model;

import java.io.Serializable;

/**
 * @author liuchenyu
 * @date 2020/10/29
 */
public class EsbMonitor implements Serializable {
    /**
     * identifier field
     */
    public String uuid;

    /**
     * 系统ID
     */
    public String sysId;
    /**
     * 系统ID
     */
    public String callSysId;
    /**
     * 服务器IP
     */
    public String serverIp;
    /**
     * 请求者IP
     */
    public String remoteIp;
    /**
     * 发布服务项ID
     */
    public String pubItemName;
    /**
     * 父发布服务项ID
     */
    public String parentPubItemName;
    /**
     * 会话ID
     */
    public String sessionId;
    /**
     * 会话序号
     */
    public String orderNum;
    /**
     * 监控点ID
     */
    public String monitorId;
    /**
     * 监控开始时间
     */
    public String startTime;
    /**
     * 距前一监控点时长（ms）
     */
    public String duration;
    /**
     * 操作状态
     */
    public String status;
    /**
     * 返回代码
     */
    public String resultCode;
    /**
     * 返回描述
     */
    public String resultDesc;
    /**
     * 传入数据大小
     */
    public long dataSizeIn;
    /**
     * 返回数据大小
     */
    public long dataSizeOut;
    /**
     * 网关编码
     */
    public String gatewayCode;
    /**
     * 链路ID
     */
    public String tokenId;
    /**
     * 客户端ID
     */
    public String clientId;
    /**
     * 服务器ID
     */
    public String serverId;
    /**
     * 链路ID
     */
    public String tracedId;
    /**
     * 父节点ID
     */
    public String parentId;
    /**
     * 步骤ID
     */
    public String spanId;
    /**
     * 时间戳
     */
    public String timestamp;
    /**
     * 持续时间比例
     */
    public String durationRatio;


    public EsbMonitor() {

    }

}
