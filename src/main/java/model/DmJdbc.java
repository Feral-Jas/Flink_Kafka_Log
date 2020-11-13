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
    private Properties dmProp;
    DmJdbc(){
        dmProp = PropsHelper.getProp("dm.properties");
        try {
            Class.forName("dm.jdbc.driver.DmDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection(){
        try {
            String DmUrl = dmProp.getProperty("url");
            String username = dmProp.getProperty("username");
            String password = dmProp.getProperty("password");
            return DriverManager.getConnection(DmUrl,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Properties getDmProp(){
        return dmProp;
    }

}
