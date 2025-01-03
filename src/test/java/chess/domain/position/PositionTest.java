package chess.domain.position;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PositionTest {

    @Test
    @DisplayName("같은 행, 같은 열을 가르키면 같은 객체라고 판단한다.")
    void equalsTest() {
        Position one = new Position(File.A, Rank.SIX);
        Position another = new Position(File.A, Rank.SIX);
        Position different = new Position(File.C, Rank.SIX);

        assertThat(one)
                .isEqualTo(another)
                .isNotEqualTo(different)
                .hasSameHashCodeAs(another);
    }

    @Test
    @DisplayName("동, 서, 남, 북으로 이동한 위치를 알 수 있다.")
    void moveTest() {
        Position position = new Position(File.D, Rank.FOUR);

        assertAll(
                () -> assertThat(position.moveToEast()).isEqualTo(new Position(File.E, Rank.FOUR)),
                () -> assertThat(position.moveToWest()).isEqualTo(new Position(File.C, Rank.FOUR)),
                () -> assertThat(position.moveToSouth()).isEqualTo(new Position(File.D, Rank.THREE)),
                () -> assertThat(position.moveToNorth()).isEqualTo(new Position(File.D, Rank.FIVE))
        );
    }
}
