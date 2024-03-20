package chess.domain.position;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ColumnTest {

    @ParameterizedTest
    @CsvSource({"G, H", "A, B"})
    @DisplayName("다음 동쪽 위치를 알 수 있다.")
    void toEastTest(Column before, Column after) {
        assertThat(before.toEast()).isEqualTo(after);
    }

    @Test
    @DisplayName("동쪽으로 갈 수 없으면, 예외가 발생한다.")
    void toEastTest_whenCant() {
        assertThatThrownBy(Column.H::toEast)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("동쪽으로 이동할 수 없습니다.");
    }

    @ParameterizedTest
    @CsvSource({"H, G", "B, A"})
    @DisplayName("다음 서쪽 위치를 알 수 있다.")
    void toWestTest(Column before, Column after) {
        assertThat(before.toWest()).isEqualTo(after);
    }

    @Test
    @DisplayName("서쪽으로 갈 수 없으면, 예외가 발생한다.")
    void toWestTest_whenCant() {
        assertThatThrownBy(Column.A::toWest)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("서쪽으로 이동할 수 없습니다.");
    }
}
