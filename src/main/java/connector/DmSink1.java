package connector;

import constants.Const;
import org.apache.flink.api.java.tuple.Tuple7;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author liuchenyu
 * @date 2020/11/13
 * sdept,spost,role,suser,cert : type count
 */
public class DmSink1 extends RichSinkFunction<Tuple7<String,LocalDateTime,Integer,Integer,Integer,Integer,Integer>> {
    private static final long serialVersionUID = 1L;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private String sql;
    public DmSink1(String sql){
        this.sql = sql;
    }
    @Override
    public void open(Configuration parameters) throws Exception {
        try {
            Class.forName("dm.jdbc.driver.DmDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection(
            Const.DM_URL,
            Const.DM_USERNAME,
            Const.DM_PASSWORD);
        preparedStatement = connection.prepareStatement(sql);
        super.open(parameters);
    }

    @Override
    public void invoke(Tuple7<String, LocalDateTime, Integer, Integer, Integer, Integer, Integer> value, Context context) throws Exception {
        preparedStatement.setString(1, value.f0);
        preparedStatement.setTimestamp(2, Timestamp.valueOf(value.f1));
        preparedStatement.setInt(3, value.f2);
        preparedStatement.setInt(4, value.f3);
        preparedStatement.setInt(5, value.f4);
        preparedStatement.setInt(6, value.f5);
        preparedStatement.setInt(7, value.f6);
        preparedStatement.executeUpdate();
    }

    @Override
    public void close() throws Exception {
        if(preparedStatement != null){
            preparedStatement.close();
        }
        if(connection != null){
            connection.close();
        }
        super.close();
    }

}
