package model;

import constants.Const;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
                Const.DM_URL,
                Const.DM_USERNAME,
                Const.DM_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
