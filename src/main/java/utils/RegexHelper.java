package utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.EsbMonitor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuchenyu
 * @date 2020/10/29
 */
public class RegexHelper {
    final static String TIMESTAMP_RGX = "(?<timestamp>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})";
    final static String THREAD_RGX = "\\[(?<thread>[^]]+)\\]";
    final static String LEVEL_RGX = "(?<level>INFO|ERROR|WARN|TRACE|DEBUG|FATAL)";
    final static String CLASS_RGX = "(?<class>.*)";
    final static String LINE_RGX = "(?<line>[0-9]+)";
    final static String TEXT_RGX = "(?<text>.*)";
    final static String[] ATTR_DICT =
        {
        "uuid","sysId","callSysId","serverIp","remoteIp",
            "pubItemName","parentPubItemName","sessionId",
            "orderNum","monitorId","startTime","duration",
            "status","resultCode","resultDesc","dataSizeIn",
            "dataSizeOut","gatewayCode","tokenId","clientId",
            "serverId","tracedId","parentId","spanId","timestamp"
    };
    private static String CUSTOM_RGX="";
    static {
        for(int i=0;i<ATTR_DICT.length-2;i++){
            CUSTOM_RGX+="(?<"+ATTR_DICT[i]+">+|$)";
        }
        CUSTOM_RGX+="(?<timestamp>.*)";
    }
    public static EsbMonitor parseRegex(String message) throws NoSuchFieldException, IllegalAccessException {

        Pattern pattern = Pattern.compile(TIMESTAMP_RGX +" "+ THREAD_RGX +" "+ LEVEL_RGX +" "+CLASS_RGX+" "+LINE_RGX+" - "+TEXT_RGX);
        Matcher matcher = pattern.matcher(message);
        matcher.find();
        String[] texts = matcher.group("text").split("\\|");
        EsbMonitor esbMonitor = new EsbMonitor();
        for(int i=0;i<ATTR_DICT.length-1;i++){
            if(ATTR_DICT[i]=="dataSizeIn"){
                esbMonitor.dataSizeIn = Long.parseLong(texts[i]);
            }else if(ATTR_DICT[i]=="dataSizeOut"){
                esbMonitor.dataSizeOut = Long.parseLong(texts[i]);
            }else{
                esbMonitor.getClass().getField(ATTR_DICT[i]).set(esbMonitor,texts[i]);
            }
        }
        return esbMonitor;
    }
    public static Map mapRegex(String message){
        Pattern pattern = Pattern.compile(TIMESTAMP_RGX +" "+ THREAD_RGX +" "+ LEVEL_RGX +" "+CLASS_RGX+" "+LINE_RGX+" - "+TEXT_RGX);
        Matcher matcher = pattern.matcher(message);
        matcher.find();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(matcher.group("text"),Map.class);
    }
}
