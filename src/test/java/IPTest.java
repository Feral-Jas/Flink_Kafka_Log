import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple10;
import org.apache.flink.api.java.tuple.Tuple3;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuchenyu
 * @date 2020/11/30
 */
public class IPTest {
    public static String getIp()  {
        Enumeration<NetworkInterface> networkInterfaces = null;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Pattern pattern = Pattern.compile(
            "/\\b(?:(?:2(?:[0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9])\\.){3}(?:(?:2([0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9]))\\b"
        );
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            for (InterfaceAddress item : networkInterface.getInterfaceAddresses()) {
                    String address = item.getAddress().toString();
                    Matcher matcher = pattern.matcher(address);
                    if (matcher.find() && item.getAddress().isSiteLocalAddress()) {
                        return address.substring(1);
                    }
            }
        }
        return null;
    }

    public static void main(String[] args) throws SocketException {
        System.out.println(IPTest.getIp());
    }

}
