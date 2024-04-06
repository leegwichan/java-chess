package chess.dao;

import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.dto.PieceType;
import chess.dto.TeamType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MysqlPieceRepository implements PieceRepository {

    private static final Map<File, String> FILE_TO_STRING = Map.of(
            File.A, "A", File.B, "B", File.C, "C", File.D, "D",
            File.E, "E", File.F, "F", File.G, "G", File.H, "H");
    private static final Map<String, File> STRING_TO_FILE = Map.of(
            "A", File.A, "B", File.B, "C", File.C, "D", File.D,
            "E", File.E, "F", File.F, "G", File.G, "H", File.H);
    private static final Map<Rank, String> RANK_TO_STRING = Map.of(
            Rank.ONE, "1", Rank.TWO, "2", Rank.THREE, "3", Rank.FOUR, "4",
            Rank.FIVE, "5", Rank.SIX, "6", Rank.SEVEN, "7", Rank.EIGHT, "8");
    private static final Map<String, Rank> STRING_TO_RANK = Map.of(
            "1", Rank.ONE, "2", Rank.TWO, "3", Rank.THREE, "4", Rank.FOUR,
            "5", Rank.FIVE, "6", Rank.SIX, "7", Rank.SEVEN, "8", Rank.EIGHT);
    private static final Map<PieceType, String> PIECE_TYPE_TO_STRING = Map.of(
            PieceType.KING, "KING", PieceType.QUEEN, "QUEEN", PieceType.ROOK, "ROOK", PieceType.BISHOP, "BISHOP",
            PieceType.KNIGHT, "KNIGHT", PieceType.PAWN, "PAWN", PieceType.EMPTY, "EMPTY");
    private static final Map<String, PieceType> STRING_TO_PIECE_TYPE = Map.of(
            "KING", PieceType.KING, "QUEEN", PieceType.QUEEN, "ROOK", PieceType.ROOK, "BISHOP", PieceType.BISHOP,
            "KNIGHT", PieceType.KNIGHT, "PAWN", PieceType.PAWN, "EMPTY", PieceType.EMPTY);
    private static final Map<TeamType, String> TEAM_TYPE_TO_STRING = Map.of(
            TeamType.WHITE, "WHITE", TeamType.BLACK, "BLACK", TeamType.EMPTY, "EMPTY");
    private static final Map<String, TeamType> STRING_TO_TEAM_TYPE = Map.of(
            "WHITE", TeamType.WHITE, "BLACK", TeamType.BLACK, "EMPTY", TeamType.EMPTY);

    private final ConnectionManager connectionManager;

    public MysqlPieceRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public PieceEntities findAll() {
        Connection connection = connectionManager.getConnection();
        String query = "SELECT board_file, board_rank, type, team FROM pieces";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return toPieceEntities(resultSet);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private PieceEntities toPieceEntities(ResultSet resultSet) throws SQLException {
        List<PieceEntity> pieceEntities = new ArrayList<>();
        while (resultSet.next()) {
            PieceEntity pieceEntity = toPieceEntity(resultSet);
            pieceEntities.add(pieceEntity);
        }
        return new PieceEntities(pieceEntities);
    }

    private PieceEntity toPieceEntity(ResultSet resultSet) throws SQLException {
        String fileString = resultSet.getString("board_file");
        String rankString = resultSet.getString("board_rank");
        String pieceString = resultSet.getString("type");
        String teamString = resultSet.getString("team");

        File file = STRING_TO_FILE.get(fileString);
        Rank rank = STRING_TO_RANK.get(rankString);
        Position position = new Position(file, rank);
        PieceType pieceType = STRING_TO_PIECE_TYPE.get(pieceString);
        TeamType teamType = STRING_TO_TEAM_TYPE.get(teamString);

        return new PieceEntity(position, pieceType, teamType);
    }

    @Override
    public PieceEntities saveAll(PieceEntities pieces) {
        Connection connection = connectionManager.getConnection();
        String query = "INSERT INTO pieces(board_file, board_rank, type, team) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            addBatch(pieces, preparedStatement);
            preparedStatement.executeBatch();
            return pieces;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void addBatch(PieceEntities pieces, PreparedStatement preparedStatement) throws SQLException {
        for (PieceEntity entity : pieces.getPieceEntities()) {
            setPreparedStatementForInsert(entity, preparedStatement);
            preparedStatement.addBatch();
        }
    }

    private void setPreparedStatementForInsert(PieceEntity entity, PreparedStatement preparedStatement)
            throws SQLException {
        String fileString = FILE_TO_STRING.get(entity.getFile());
        String rankString = RANK_TO_STRING.get(entity.getRank());
        String pieceString = PIECE_TYPE_TO_STRING.get(entity.getPieceType());
        String teamString = TEAM_TYPE_TO_STRING.get(entity.getTeamType());

        preparedStatement.setString(1, fileString);
        preparedStatement.setString(2, rankString);
        preparedStatement.setString(3, pieceString);
        preparedStatement.setString(4, teamString);
    }

    @Override
    public void update(PieceEntity piece) {
        Connection connection = connectionManager.getConnection();
        String query = "UPDATE pieces SET type = ?, team = ? WHERE board_file = ? AND board_rank = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setPreparedStatementForUpdate(piece, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void setPreparedStatementForUpdate(PieceEntity entity, PreparedStatement preparedStatement)
            throws SQLException {
        String pieceString = PIECE_TYPE_TO_STRING.get(entity.getPieceType());
        String teamString = TEAM_TYPE_TO_STRING.get(entity.getTeamType());
        String fileString = FILE_TO_STRING.get(entity.getFile());
        String rankString = RANK_TO_STRING.get(entity.getRank());

        preparedStatement.setString(1, pieceString);
        preparedStatement.setString(2, teamString);
        preparedStatement.setString(3, fileString);
        preparedStatement.setString(4, rankString);
    }

    @Override
    public void deleteAll() {
        Connection connection = connectionManager.getConnection();
        String query = "DELETE FROM pieces";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
