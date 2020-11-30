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
    public String getIp() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        Pattern pattern = Pattern.compile("/\\b(?:(?:2(?:[0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9])\\.){3}(?:(?:2([0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9]))\\b");
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            for (InterfaceAddress item : networkInterface.getInterfaceAddresses()) {
                    String address = item.getAddress().toString();
                    Matcher matcher = pattern.matcher(address);
                    boolean res = matcher.find();
                    if (res == true && !("/127.0.0.1".equals(address))) {
                        return address.substring(1);
                    }
            }
        }
        return null;
    }

    public static void main(String[] args) throws SocketException {
        System.out.println(new IPTest().getIp());
    }
}
