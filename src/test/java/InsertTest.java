import model.DmJdbc;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author liuchenyu
 * @date 2020/11/13
 */
public class InsertTest {
    public static void main(String[] args) {
        DmJdbc dmJdbc = DmJdbc.INSTANCE;
        Connection connection = dmJdbc.getConnection();
        while(true){
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO CSSBASE_CL.S_LOG VALUES (?,?,?,?)");
                ps.setString(1, String.valueOf(UUID.randomUUID()));
                ps.setString(2,"login.action");
                ps.setString(3,"sysadmin");
                ps.setTimestamp(4,Timestamp.valueOf(LocalDateTime.now()));
                ps.execute();
                ps.close();
                Thread.sleep(2000);
            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
