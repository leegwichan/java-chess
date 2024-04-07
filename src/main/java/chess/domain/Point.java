package chess.domain;

import java.util.Objects;

public class Point {

    public static final Point ZERO = new Point(0);

    private final double value;

    public Point(double value) {
        validate(value);
        this.value = value;
    }

    private void validate(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("점수로 음수를 가질 수 없습니다.");
        }
    }

    public Point add(Point other) {
        return new Point(this.value + other.value);
    }

    public double toDouble() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Point point = (Point) object;
        return Double.compare(value, point.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
