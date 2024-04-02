package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.piece.King;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BoardTest {

    @Test
    @DisplayName("특정 위치에 어떤 말이 있는지 알려준다.")
    void findTest_whenPieceExist() {
        Position position = new Position(File.D, Rank.TWO);
        King king = new King(Team.WHITE);
        Map<Position, Piece> map = Map.of(position, king);
        Board board = new Board(map);

        assertThat(board.findPieceAt(position)).isEqualTo(Optional.of(king));
    }

    @Test
    @DisplayName("특정 위치에 말이 없는지 알려준다.")
    void findTest_whenPieceNotExist() {
        Position position = new Position(File.D, Rank.TWO);
        King king = new King(Team.WHITE);
        Map<Position, Piece> map = Map.of(position, king);
        Board board = new Board(map);

        assertThat(board.findPieceAt(position)).isEqualTo(Optional.of(king));
    }


    @Nested
    @DisplayName("상대편 왕 특정 위치의 존재하는지 테스트")
    class OpponentKingTest {

        @Test
        @DisplayName("해당 위치가 비어있을 경우, false를 반환한다.")
        void isOpponentKingExistAtTest_whenNotExist() {
            Board board = new Board(Map.of());
            Position notExistPosition = new Position(File.D, Rank.TWO);

            assertThat(board.isOpponentKingExist(notExistPosition, Team.BLACK)).isFalse();
        }

        @Test
        @DisplayName("해당 위치가 같은 팀 말이 있을 경우, false를 반환한다.")
        void isOpponentKingExistAtTest_whenOurTeamExist() {
            Position position = new Position(File.E, Rank.TWO);
            King king = new King(Team.BLACK);
            Map<Position, Piece> map = Map.of(position, king);
            Board board = new Board(map);

            assertThat(board.isOpponentKingExist(position, Team.BLACK)).isFalse();
        }
        @Test
        @DisplayName("해당 위치가 왕이 아닌 다른 말이 있을 경우, false를 반환한다.")
        void isOpponentKingExistAtTest_whenOtherPieceExist() {
            Position position = new Position(File.E, Rank.TWO);
            Pawn pawn = new Pawn(Team.WHITE);
            Map<Position, Piece> map = Map.of(position, pawn);
            Board board = new Board(map);

            assertThat(board.isOpponentKingExist(position, Team.BLACK)).isFalse();
        }

        @Test
        @DisplayName("해당 위치가 상대편 왕이 있을 경우, true를 반환한다.")
        void isOpponentKingExistAtTest_whenOpponentKingExist() {
            Position position = new Position(File.E, Rank.TWO);
            King king = new King(Team.WHITE);
            Map<Position, Piece> map = Map.of(position, king);
            Board board = new Board(map);

            assertThat(board.isOpponentKingExist(position, Team.BLACK)).isTrue();
        }
    }

    /*
     * ........ 8
     * ........ 7
     * ........ 6
     * ........ 5
     * ....k... 4
     * ........ 3
     * ....q..R 2
     * ........ 1
     * abcdefgh
     * */
    @Nested
    @DisplayName("말 이동 테스트")
    class MovingTest {

        private static final Position START_KING = new Position(File.E, Rank.FOUR);
        private static final King KING = new King(Team.WHITE);
        private static final Position START_QUEEN = new Position(File.E, Rank.TWO);
        private static final Queen QUEEN = new Queen(Team.WHITE);
        private static final Rook ENEMY_ROOK = new Rook(Team.BLACK);
        private static final Position START_ENEMY_ROOK = new Position(File.H, Rank.TWO);
        private static final Map<Position, Piece> MAP = Map.of(
                START_KING, KING, START_QUEEN, QUEEN, START_ENEMY_ROOK, ENEMY_ROOK);
        private Board board;

        @BeforeEach
        void beforeEach() {
            board = new Board(MAP);
        }

        @Test
        @DisplayName("시작 위치에 있는 말을 도착 위치로 움직인다.")
        void moveTest() {
            Position possibleEnd = new Position(File.E, Rank.THREE);

            board.move(START_KING, possibleEnd);

            assertAll(
                    () -> assertThat(board.findPieceAt(possibleEnd)).isEqualTo(Optional.of(KING)),
                    () -> assertThat(board.findPieceAt(START_KING)).isEmpty()
            );
        }

        @Test
        @DisplayName("시작 위치에 말이 없을 경우, 예외가 발생한다.")
        void moveTest_whenPieceNotExist_throwException() {
            Position emptyPosition = new Position(File.F, Rank.EIGHT);
            Position end = new Position(File.F, Rank.TWO);

            assertThatThrownBy(() -> board.move(emptyPosition, end))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 위치에 말이 없습니다.");
        }

        @Test
        @DisplayName("말의 이동 반경을 벗어날 경우, 예외가 발생한다.")
        void moveTest_whenOutOfMovement_throwException() {
            Position outOfMovement = new Position(File.F, Rank.EIGHT);

            assertThatThrownBy(() -> board.move(START_KING, outOfMovement))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("불가능한 경로입니다.");
        }

        @Test
        @DisplayName("이동 경로 위에 다른 말이 존재할 경우, 예외가 발생한다.")
        void moveTest_whenBlocked_throwException() {
            Position blockedPosition = new Position(File.E, Rank.FIVE);

            assertThatThrownBy(() -> board.move(START_QUEEN, blockedPosition))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("다른 말이 있어 이동 불가능합니다.");
        }

        @Test
        @DisplayName("도착할 곳에 같은 팀이 있을 경우, 예외가 발생한다.")
        void moveTest_whenExistSameTeamAtTheEnd_throwException() {
            assertThatThrownBy(() -> board.move(START_QUEEN, START_KING))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("도착 지점에 같은 팀 말이 있어 이동이 불가능합니다.");
        }

        @Test
        @DisplayName("도착할 곳에 다른 팀이 있을 경우, 해당 말을 잡아먹는다.")
        void moveTest_whenExistSameTeamAtTheEnd() {
            board.move(START_QUEEN, START_ENEMY_ROOK);

            assertAll(
                    () -> assertThat(board.findPieceAt(START_QUEEN)).isEmpty(),
                    () -> assertThat(board.findPieceAt(START_ENEMY_ROOK)).isEqualTo(Optional.of(QUEEN))
            );
        }
    }

    @Nested
    @DisplayName("보드판에 있는 기물 점수를 계산할 수 있다.")
    class CalculatingPointTest {

        /*
         * ........ 8
         * ........ 7
         * ........ 6
         * ........ 5
         * ........ 4
         * ........ 3
         * ........ 2
         * r..q.... 1
         * abcdefgh
         * */
        @Test
        @DisplayName("팀 별로 각 기물의 점수를 더하여 계산한다.")
        void calculatePointTest_whenNotExistPawn_addAll() {
            Board board = new Board(Map.of(
                    new Position(File.A, Rank.ONE), new Rook(Team.WHITE),
                    new Position(File.D, Rank.ONE), new Queen(Team.WHITE)));

            assertThat(board.calculateTotalPoints()).containsOnly(
                    Map.entry(Team.WHITE, new Point(5.0 + 9.0)),
                    Map.entry(Team.BLACK, Point.ZERO));
        }

        /*
         * ........ 8
         * ....P... 7
         * ........ 6
         * ........ 5
         * ........ 4
         * ........ 3
         * ....pp.. 2
         * ........ 1
         * abcdefgh
         * */
        @Test
        @DisplayName("각 폰은 한 파일에 같이 없을 경우, 높은 점수로 계산한다.")
        void calculatePointTest_whenExistPawnSameTeamAndDifferentFile_addHighPoint() {
            Board board = new Board(Map.of(
                    new Position(File.E, Rank.TWO), new Pawn(Team.WHITE),
                    new Position(File.F, Rank.TWO), new Pawn(Team.WHITE),
                    new Position(File.E, Rank.SEVEN), new Pawn(Team.BLACK)));

            assertThat(board.calculateTotalPoints()).containsOnly(
                    Map.entry(Team.WHITE, new Point(1.0 + 1.0)),
                    Map.entry(Team.BLACK, new Point(1.0)));
        }

        /*
         * ........ 8
         * ........ 7
         * ........ 6
         * ........ 5
         * ........ 4
         * ....p... 3
         * ....p... 2
         * ........ 1
         * abcdefgh
         * */
        @Test
        @DisplayName("각 폰은 한 줄에 같이 있을 경우, 낮은 점수로 계산한다.")
        void calculatePointTest_whenExistPawnSameTeamAndFile_addLowPoint() {
            Board board = new Board(Map.of(
                    new Position(File.E, Rank.TWO), new Pawn(Team.WHITE),
                    new Position(File.E, Rank.THREE), new Pawn(Team.WHITE)));

            assertThat(board.calculateTotalPoints()).containsOnly(
                    Map.entry(Team.WHITE, new Point(0.5 + 0.5)),
                    Map.entry(Team.BLACK, Point.ZERO));
        }
    }
}
