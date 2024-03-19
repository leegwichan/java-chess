package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.King;
import chess.domain.piece.Piece;
import chess.domain.piece.Team;
import chess.domain.position.Column;
import chess.domain.position.Position;
import chess.domain.position.Row;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardTest {

    @Test
    @DisplayName("특정 위치에 어떤 말이 있는지 알려준다.")
    void findTest_whenPieceExist() {
        Position position = new Position(Row.TWO, Column.D);
        King king = new King(Team.WHITE);
        Map<Position, Piece> map = Map.of(position, king);
        Board board = new Board(map);

        assertThat(board.find(position)).isEqualTo(Optional.of(king));
    }

    @Test
    @DisplayName("특정 위치에 어떤 말이 있는지 알려준다.")
    void findTest_whenPieceNotExist() {
        Position position = new Position(Row.TWO, Column.E);
        King king = new King(Team.WHITE);
        Map<Position, Piece> map = Map.of(position, king);
        Position notExistPosition = new Position(Row.TWO, Column.D);
        Board board = new Board(map);

        assertThat(board.find(notExistPosition)).isEqualTo(Optional.empty());
    }
}