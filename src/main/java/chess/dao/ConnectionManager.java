package chess.dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

    private static final String FILE_LOCATION = "src/main/resources/mysql.properties";
    private static final Properties PROPERTIES = loadProperties();

    private static Properties loadProperties() {
        try (FileReader mysqlSettingFile = new FileReader(FILE_LOCATION)) {
            Properties properties = new Properties();
            properties.load(mysqlSettingFile);
            return properties;
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("mysql 설정 파일이 존재하지 않습니다.");
        } catch (IOException e) {
            throw new IllegalStateException("파일 입출력 오류가 발생했습니다.");
        }
    }

    public Connection createConnection() {
        try {
            String url = PROPERTIES.getProperty("URL");
            String username = PROPERTIES.getProperty("USERNAME");
            String password = PROPERTIES.getProperty("PASSWORD");
            return DriverManager.getConnection(url, username, password);
        } catch (final SQLException e) {
            System.err.println("DB 연결 오류:" + e.getMessage());
            throw new IllegalStateException("DB와 연결할 수 없습니다.", e);
        }
    }
}
