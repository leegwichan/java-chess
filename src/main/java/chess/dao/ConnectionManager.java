package chess.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String SERVER = "localhost:13306";
    private static final String DATABASE = "chess";
    private static final String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String URL = "jdbc:mysql://" + SERVER + "/" + DATABASE + OPTION;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private final Connection singleConnection;

    public ConnectionManager() {
        singleConnection = createConnection();
    }

    private static Connection createConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (final SQLException e) {
            System.err.println("DB 연결 오류:" + e.getMessage());
            throw new IllegalStateException("DB와 연결할 수 없습니다.", e);
        }
    }

    public Connection getConnection() {
        return singleConnection;
    }

    public void closeConnection() {
        try {
            singleConnection.close();
        } catch (SQLException ignore) {
        }
    }
}
