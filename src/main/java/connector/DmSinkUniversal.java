package connector;

import constants.Const;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @author liuchenyu
 * @date 2020/12/3
 */
public class DmSinkUniversal extends RichSinkFunction<Tuple1> {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private String sql;
    public DmSinkUniversal(String sql){
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
    public void close() throws Exception {
        if(preparedStatement != null){
            preparedStatement.close();
        }
        if(connection != null){
            connection.close();
        }
        super.close();
    }

    @Override
    public void invoke(Tuple1 tuple, Context context) throws Exception {
//        int arity = tuple.getArity();
//        for(int i=0;i<arity;i++){
//            preparedStatement.setObject(i,tuple.getField(i));
//        }
        preparedStatement.setObject(1,tuple.f0);
        preparedStatement.setObject(2,23);
        preparedStatement.setObject(3,60);
        preparedStatement.executeUpdate();
    }
}
