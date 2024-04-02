package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GameStatusTest {


    @Test
    @DisplayName("현재 진행하는 팀이 아닐 경우, 예외를 던진다.")
    void validateTurnTest_whenDifferentTeam() {
        GameStatus gameStatus = new GameStatus(Team.WHITE);
        Team currentTeam = Team.BLACK;

        assertThatThrownBy(() -> gameStatus.validateTurn(currentTeam))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 팀의 차례가 아닙니다.");
    }

    @Test
    @DisplayName("현재 진행하는 팀이 맞을 경우, 아무 일도 일어나지 않는다.")
    void validateTurnTest_whenSameTeam() {
        GameStatus gameStatus = new GameStatus(Team.WHITE);
        Team currentTeam = Team.WHITE;

        assertThatCode(() -> gameStatus.validateTurn(currentTeam))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("현재 게임이 진행중인지 판단한다.")
    void isProgressGame() {
        GameStatus gameStatus = new GameStatus(Team.WHITE);

        assertThat(gameStatus.isProgressGame()).isTrue();
    }

    @Test
    @DisplayName("다음 턴으로 넘길 수 있다.")
    void nextTurnTest() {
        GameStatus gameStatus = new GameStatus(Team.WHITE);

        gameStatus.nextTurn();

        assertThat(gameStatus.getCurrentTurn()).isEqualTo(Team.BLACK);
    }

    @Test
    @DisplayName("게임 상태를 종료시킬 수 있다.")
    void endGameTest() {
        GameStatus gameStatus = new GameStatus(Team.WHITE);

        gameStatus.endGame();

        assertThat(gameStatus.isProgressGame()).isFalse();
    }
}
