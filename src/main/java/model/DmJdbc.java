package model;

import utils.PropsHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author liuchenyu
 * @date 2020/11/12
 */
public enum DmJdbc {
    /**
     * singleton connection
     */
    INSTANCE;
    DmJdbc(){
        try {
            Class.forName("dm.jdbc.driver.DmDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection(){
        try {
            return DriverManager.getConnection(
                constants.Connection.DM_URL,
                constants.Connection.DM_USERNAME,
                constants.Connection.DM_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
