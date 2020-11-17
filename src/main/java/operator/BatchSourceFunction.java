package operator;

import model.DmJdbc;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;
import utils.PropsHelper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * @author liuchenyu
 * @date 2020/11/12
 */
public class BatchSourceFunction implements ParallelSourceFunction<String> {

    private volatile boolean isRunning = true;
    @Override
    public void run(SourceContext<String> sourceContext) throws SQLException {
//        DmJdbc dmInstance = DmJdbc.INSTANCE;
//        Connection connection = dmInstance.getConnection();
        Properties dmProp = PropsHelper.getProp("dm.properties");
        try {
            Class.forName("dm.jdbc.driver.DmDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String dmUrl = dmProp.getProperty("url");
        String username = dmProp.getProperty("username");
        String password = dmProp.getProperty("password");
        Connection connection = DriverManager.getConnection(
            dmUrl,
            username,
            password);
//        Connection connection = DriverManager.getConnection(
//            "jdbc:dm://10.15.0.173:5236/CSSBASE",
//            "CSSBASE",
//            "1234567890");
        while (true){
            synchronized (sourceContext.getCheckpointLock()){
                System.out.println("begin sql...");
                PreparedStatement ps = null;
                try {
                    ps = connection.prepareStatement("select * from CSSBASE_CL.S_LOG t where t.TIMESTAMP >=? and t.TIMESTAMP <=?");
                    LocalDateTime now = LocalDateTime.now();
                    ps.setTimestamp(1,Timestamp.valueOf(now.minusSeconds(1)));
                    ps.setTimestamp(2,Timestamp.valueOf(now));
                    ResultSet resultSet = ps.executeQuery();
                    while(resultSet.next()){
                        String uuid = resultSet.getString(1);
                        StringBuilder sb = new StringBuilder();
                        sb.append(uuid);
                        sb.append(",");
                        sb.append(resultSet.getString(2));
                        sb.append(",");
                        sb.append(resultSet.getString(3));
                        sb.append(",");
                        Timestamp resTime = resultSet.getTimestamp(4);
                        sb.append(resTime);
                        sourceContext.collect(sb.toString());

                    }
                    ps.close();
                    Thread.sleep(2000);
                } catch (SQLException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
//        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
//        service.scheduleAtFixedRate(
//            () -> {
//                    if (isRunning){
//                        synchronized (sourceContext.getCheckpointLock()){
//                            System.out.println("begin sql...");
//                            PreparedStatement ps = null;
//                            try {
//                                ps = connection.prepareStatement("select * from CSSBASE_CL.S_LOG t where t.TIMESTAMP >=? and t.TIMESTAMP <=?");
//                                LocalDateTime now = LocalDateTime.now();
//                                ps.setTimestamp(1,Timestamp.valueOf(now.minusSeconds(1)));
//                                ps.setTimestamp(2,Timestamp.valueOf(now));
//                                ResultSet resultSet = ps.executeQuery();
//                                while(resultSet.next()){
//                                    String uuid = resultSet.getString(1);
//                                    StringBuilder sb = new StringBuilder();
//                                    sb.append(uuid);
//                                    sb.append(",");
//                                    sb.append(resultSet.getString(2));
//                                    sb.append(",");
//                                    sb.append(resultSet.getString(3));
//                                    sb.append(",");
//                                    Timestamp resTime = resultSet.getTimestamp(4);
//                                    sb.append(resTime);
//                                    sourceContext.collect(sb.toString());
//
//                                }
//                                ps.close();
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//            },0,2,TimeUnit.SECONDS
//        );
    }

    @Override
    public void cancel() {
        isRunning = false;
    }


}
