package utils;

import org.apache.flink.util.OutputTag;

/**
 * @author liuchenyu
 * @date 2020/11/4
 */
public class SideoutHelper {
    public static final OutputTag<String> dataAllTag = new OutputTag<String>("dataAll"){};
}
