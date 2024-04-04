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
import chess.dto.ProgressStatus;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ChessGameTest {

    @Test
    @DisplayName("특정 위치에 어떤 말이 있는지 알려준다.")
    void findTest_whenPieceExist() {
        Position position = new Position(File.D, Rank.TWO);
        King king = new King(Team.WHITE);
        Map<Position, Piece> map = Map.of(position, king);
        ChessGame chessGame = new ChessGame(map);

        assertThat(chessGame.findPieceAt(position)).isEqualTo(Optional.of(king));
    }

    @Test
    @DisplayName("특정 위치에 어떤 말이 있는지 알려준다.")
    void findTest_whenPieceNotExist() {
        Position position = new Position(File.E, Rank.TWO);
        King king = new King(Team.WHITE);
        Map<Position, Piece> map = Map.of(position, king);
        Position notExistPosition = new Position(File.D, Rank.TWO);
        ChessGame chessGame = new ChessGame(map);

        assertThat(chessGame.findPieceAt(notExistPosition)).isEmpty();
    }

    /*
     * ........ 8
     * ........ 7
     * ........ 6
     * ........ 5
     * ....k... 4
     * ........ 3
     * K...q..R 2
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
        private static final King ENEMY_KING = new King(Team.BLACK);
        private static final Position START_ENEMY_KING = new Position(File.A, Rank.TWO);
        private static final Map<Position, Piece> MAP = Map.of(
                START_KING, KING, START_QUEEN, QUEEN, START_ENEMY_ROOK, ENEMY_ROOK, START_ENEMY_KING, ENEMY_KING);
        private ChessGame chessGame;

        @BeforeEach
        void beforeEach() {
            chessGame = new ChessGame(MAP);
        }

        @Test
        @DisplayName("시작 위치에 있는 말을 도착 위치로 움직인다.")
        void moveTest() {
            Position possibleEnd = new Position(File.E, Rank.THREE);

            chessGame.move(START_KING, possibleEnd);

            assertAll(
                    () -> assertThat(chessGame.findPieceAt(possibleEnd)).isEqualTo(Optional.of(KING)),
                    () -> assertThat(chessGame.findPieceAt(START_KING)).isEmpty()
            );
        }

        @Test
        @DisplayName("시작 위치에 말이 없을 경우, 예외가 발생한다.")
        void moveTest_whenPieceNotExist_throwException() {
            Position emptyPosition = new Position(File.F, Rank.EIGHT);
            Position end = new Position(File.F, Rank.TWO);

            assertThatThrownBy(() -> chessGame.move(emptyPosition, end))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 위치에 말이 없습니다.");
        }

        @Test
        @DisplayName("말의 이동 반경을 벗어날 경우, 예외가 발생한다.")
        void moveTest_whenOutOfMovement_throwException() {
            Position outOfMovement = new Position(File.F, Rank.EIGHT);

            assertThatThrownBy(() -> chessGame.move(START_KING, outOfMovement))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("불가능한 경로입니다.");
        }

        @Test
        @DisplayName("이동 경로 위에 다른 말이 존재할 경우, 예외가 발생한다.")
        void moveTest_whenBlocked_throwException() {
            Position blockedPosition = new Position(File.E, Rank.FIVE);

            assertThatThrownBy(() -> chessGame.move(START_QUEEN, blockedPosition))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("다른 말이 있어 이동 불가능합니다.");
        }

        @Test
        @DisplayName("도착할 곳에 같은 팀이 있을 경우, 예외가 발생한다.")
        void moveTest_whenExistSameTeamAtTheEnd_throwException() {
            assertThatThrownBy(() -> chessGame.move(START_QUEEN, START_KING))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("도착 지점에 같은 팀 말이 있어 이동이 불가능합니다.");
        }

        @Test
        @DisplayName("도착할 곳에 다른 팀이 있을 경우, 해당 말을 잡아먹는다.")
        void moveTest_whenExistSameTeamAtTheEnd() {
            chessGame.move(START_QUEEN, START_ENEMY_ROOK);

            assertAll(
                    () -> assertThat(chessGame.findPieceAt(START_QUEEN)).isEmpty(),
                    () -> assertThat(chessGame.findPieceAt(START_ENEMY_ROOK)).isEqualTo(Optional.of(QUEEN))
            );
        }

        @Test
        @DisplayName("도착할 곳에 다른 팀이 있을 경우, 해당 말을 잡아먹는다.")
        void moveTest_whenNotInTurn_throwException() {
            Position legalPosition = new Position(File.D, Rank.EIGHT);

            assertThatThrownBy(() -> chessGame.move(START_ENEMY_ROOK, legalPosition))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("해당 팀의 차례가 아닙니다.");
        }

        @Test
        @DisplayName("상대편 왕을 잡을 경우, 해당 팀이 이긴다.")
        void moveTest_whenCaptureKing_winGame() {
            chessGame.move(START_QUEEN, START_ENEMY_KING);

            assertThat(chessGame.findStatus()).isEqualTo(ProgressStatus.WHITE_WIN);
        }

        @Test
        @DisplayName("상대편 왕을 잡지 않은 경우, 게임이 진행된다.")
        void moveTest_whenNotCaptureKing_progressGame() {
            Position possiblePosition = new Position(File.E, Rank.THREE);

            chessGame.move(START_QUEEN, possiblePosition);

            assertThat(chessGame.findStatus()).isEqualTo(ProgressStatus.PROGRESS);
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
            ChessGame chessGame = new ChessGame(Map.of(
                    new Position(File.A, Rank.ONE), new Rook(Team.WHITE),
                    new Position(File.D, Rank.ONE), new Queen(Team.WHITE)));

            assertThat(chessGame.calculateTotalPoints()).containsOnly(
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
            ChessGame chessGame = new ChessGame(Map.of(
                    new Position(File.E, Rank.TWO), new Pawn(Team.WHITE),
                    new Position(File.F, Rank.TWO), new Pawn(Team.WHITE),
                    new Position(File.E, Rank.SEVEN), new Pawn(Team.BLACK)));

            assertThat(chessGame.calculateTotalPoints()).containsOnly(
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
            ChessGame chessGame = new ChessGame(Map.of(
                    new Position(File.E, Rank.TWO), new Pawn(Team.WHITE),
                    new Position(File.E, Rank.THREE), new Pawn(Team.WHITE)));

            assertThat(chessGame.calculateTotalPoints()).containsOnly(
                    Map.entry(Team.WHITE, new Point(0.5 + 0.5)),
                    Map.entry(Team.BLACK, Point.ZERO));
        }
    }
}
