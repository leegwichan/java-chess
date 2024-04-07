package chess.domain.movement.pawn;

import chess.domain.position.Position;
import java.util.List;

public final class WhitePawnDefaultMovement extends PawnForwardMovement {

    private static final int TO_NORTH_ONE_BLOCK = 1;
    private static final int SAME_FILE = 0;

    @Override
    protected boolean isMovablePath(int rankDifference, int fileDifference) {
        return (rankDifference == TO_NORTH_ONE_BLOCK && fileDifference == SAME_FILE);
    }

    @Override
    protected boolean isSatisfyStart(Position start) {
        return true;
    }

    @Override
    protected List<Position> findPath(Position start, Position end) {
        return List.of(end);
    }
}
