package chess.domain.movement;

import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.util.List;

public final class BlackPawnFirstMovement implements MovementRule {

    private static final Rank BLACK_PAWN_INIT_RANK = Rank.SEVEN;
    private static final int TO_SOUTH_TWO_BLOCK = -2;
    private static final int SAME_FILE = 0;

    @Override
    public boolean isMovable(Position start, Position end, boolean isAttack) {
        int rankDifference = start.calculateRankDifference(end);
        int fileDifference = start.calculateFileDifference(end);

        boolean isEmptyAtEnd = !isAttack;
        boolean isExistInitPosition = start.isSameRank(BLACK_PAWN_INIT_RANK);
        boolean isMatchDifference = (rankDifference == TO_SOUTH_TWO_BLOCK && fileDifference == SAME_FILE);

        return isEmptyAtEnd && isExistInitPosition && isMatchDifference;
    }

    @Override
    public List<Position> findPath(Position start, Position end, boolean isAttack) {
        if (!isMovable(start, end, isAttack)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }

        Position middle = start.moveToSouth();
        return List.of(middle, end);
    }
}
