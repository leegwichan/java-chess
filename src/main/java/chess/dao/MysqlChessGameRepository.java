package chess.dao;

import chess.dto.TurnType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class MysqlChessGameRepository implements ChessGameRepository {

    private static final Map<TurnType, String> TURN_TO_STING = Map.of(
            TurnType.BLACK, "BLACK", TurnType.WHITE, "WHITE");
    private static final Map<String, TurnType> STRING_TO_TURN = Map.of(
            "BLACK", TurnType.BLACK, "WHITE", TurnType.WHITE);

    private final ConnectionManager connectionManager;

    public MysqlChessGameRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public boolean isExistGame() {
        Connection connection = connectionManager.getConnection();
        String query = "SELECT EXISTS (SELECT 1 FROM chess_game);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(1);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public TurnType find() {
        Connection connection = connectionManager.getConnection();
        String query = "SELECT turn FROM chess_game";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String turn = resultSet.getString("turn");
            return STRING_TO_TURN.get(turn);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void update(TurnType turn) {
        deleteAll();
        save(turn);
    }

    private void save(TurnType team) {
        Connection connection = connectionManager.getConnection();
        String query = "INSERT INTO chess_game (turn) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, TURN_TO_STING.get(team));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void deleteAll() {
        Connection connection = connectionManager.getConnection();
        String query = "DELETE FROM chess_game;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
