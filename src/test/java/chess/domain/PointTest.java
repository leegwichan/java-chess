package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PointTest {

    @Test
    @DisplayName("점수로 음수를 가질 경우, 예외를 던진다.")
    void validateTest_whenPointsNegative_throwException() {
        double negativeValue = -0.001;

        assertThatThrownBy(() -> new Point(negativeValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("점수로 음수를 가질 수 없습니다.");
    }

    @ParameterizedTest
    @CsvSource({"1.5, 0, 1.5", "1, 1, 2", "0.5, 0.5, 1"})
    @DisplayName("두 점수를 더할 수 있다.")
    void addTest(double value1, double value2, double expected) {
        Point point1 = new Point(value1);
        Point point2 = new Point(value2);

        Point actual = point1.add(point2);

        assertThat(actual.toDouble()).isEqualTo(expected, offset(0.001));
    }

    @Test
    @DisplayName("점수를 여러번 더할 수 있다.")
    void addTest_whenAddRepeat() {
        Point added = new Point(0.5);
        int count = 100;

        Point actual = Point.ZERO;
        for (int i = 0; i < count; i++) {
            actual = actual.add(added);
        }

        assertThat(actual.toDouble()).isEqualTo(0.5 * 100, offset(0.001));
    }
}
