package utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author liuchenyu
 * @date 2020/10/25
 */

public final class PropsHelper {
    public static Properties getProp(String fileName) {
        Properties props = new Properties();
        try {
            props.load(new InputStreamReader(ClassLoader.getSystemResourceAsStream(fileName),"utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
}
