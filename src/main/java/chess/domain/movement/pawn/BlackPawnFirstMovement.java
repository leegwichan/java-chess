package chess.domain.movement.pawn;

import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.util.List;

public final class BlackPawnFirstMovement extends PawnForwardMovement {

    private static final Rank BLACK_PAWN_INIT_RANK = Rank.SEVEN;
    private static final int TO_SOUTH_TWO_BLOCK = -2;
    private static final int SAME_FILE = 0;

    @Override
    protected boolean isMovablePath(int rankDifference, int fileDifference) {
        return (rankDifference == TO_SOUTH_TWO_BLOCK && fileDifference == SAME_FILE);
    }

    @Override
    protected boolean isSatisfyStart(Position start) {
        return start.isSameRank(BLACK_PAWN_INIT_RANK);
    }

    @Override
    protected List<Position> findPath(Position start, Position end) {
        Position middle = start.moveToSouth();
        return List.of(middle, end);
    }
}
