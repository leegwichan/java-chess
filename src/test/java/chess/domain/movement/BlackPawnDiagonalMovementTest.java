package chess.domain.movement;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BlackPawnDiagonalMovementTest {

    @ParameterizedTest
    @CsvSource({"C, FIVE", "E, FIVE"})
    @DisplayName("이동 가능한지 확인한다.")
    void isMovableTest(File file, Rank rank) {
        Position start = new Position(File.D, Rank.SIX);
        Position end = new Position(file, rank);
        BlackPawnDiagonalMovement blackPawnDiagonalMovement = new BlackPawnDiagonalMovement();
        boolean isEnemyExistAtEnd = true;

        assertThat(blackPawnDiagonalMovement.isMovable(start, end, isEnemyExistAtEnd)).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"D, FIVE", "D, FOUR", "C, FOUR", "E, FOUR"})
    @DisplayName("이동 가능한지 확인한다.")
    void isMovableTest_false(File file, Rank rank) {
        Position start = new Position(File.D, Rank.SIX);
        Position end = new Position(file, rank);
        BlackPawnDiagonalMovement blackPawnDiagonalMovement = new BlackPawnDiagonalMovement();
        boolean isEnemyExistAtEnd = true;

        assertThat(blackPawnDiagonalMovement.isMovable(start, end, isEnemyExistAtEnd)).isFalse();
    }

    @Test
    @DisplayName("상대 말이 이동하고자 하는 곳에 없을 때, 이동이 불가능하다.")
    void isMovableTest_whenIsEnemyExistAtEnd_false() {
        Position start = new Position(File.D, Rank.SIX);
        Position end = new Position(File.C, Rank.FIVE);
        BlackPawnDiagonalMovement blackPawnDiagonalMovement = new BlackPawnDiagonalMovement();
        boolean isEnemyExistAtEnd = false;

        assertThat(blackPawnDiagonalMovement.isMovable(start, end, isEnemyExistAtEnd)).isFalse();
    }

    @Test
    @DisplayName("이동 경로를 알 수 있다.")
    void findPathTest() {
        Position start = new Position(File.D, Rank.SIX);
        Position end = new Position(File.C, Rank.FIVE);
        BlackPawnDiagonalMovement blackPawnDiagonalMovement = new BlackPawnDiagonalMovement();
        boolean isEnemyExistAtEnd = true;

        assertThat(blackPawnDiagonalMovement.findPath(start, end, isEnemyExistAtEnd))
                .containsExactly(end);
    }
}
