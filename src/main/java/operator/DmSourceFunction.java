package operator;

import model.DmJdbc;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import utils.PropsHelper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * @author liuchenyu
 * @date 2020/11/12
 */
public class DmSourceFunction extends RichParallelSourceFunction<String> {
    private volatile boolean isRunning = true;
    private String sql;
    private int interval;
    public DmSourceFunction(String sql, int interval){
        this.sql=sql;
        this.interval=interval;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        DmJdbc dmJdbc =  DmJdbc.INSTANCE;
        Connection connection = dmJdbc.getConnection();
    }

    @Override
    public void run(SourceContext<String> sourceContext) throws SQLException {
        if(isRunning){
            try {
                Class.forName("dm.jdbc.driver.DmDriver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Connection connection = DriverManager.getConnection(
                "jdbc:dm://10.15.0.173:5236/CSSBASE",
                "CSSBASE",
                "1234567890");
            while (isRunning){
                synchronized (sourceContext.getCheckpointLock()){
                    System.out.println("begin sql...");
                    PreparedStatement ps = null;
                    try {
                        ps = connection.prepareStatement(sql);
                        LocalDateTime now = LocalDateTime.now();
                        ps.setTimestamp(1,Timestamp.valueOf(now.minusSeconds(interval)));
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
                            System.out.println(sb.toString());
                            sourceContext.collect(sb.toString());
                        }
                        ps.close();
                        Thread.sleep(5000);
                    } catch (SQLException | InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        connection.close();
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
    }

    @Override
    public void cancel() {
        isRunning = false;
    }

    @Override
    public void close(){

    }
}
