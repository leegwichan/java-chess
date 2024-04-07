package chess.domain.movement.pawn;

import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.util.List;

public final class WhitePawnFirstMovement extends PawnForwardMovement {

    private static final Rank WHITE_PAWN_INIT_RANK = Rank.TWO;
    private static final int TO_NORTH_TWO_BLOCK = 2;
    private static final int SAME_FILE = 0;

    @Override
    protected boolean isMovablePath(int rankDifference, int fileDifference) {
        return (rankDifference == TO_NORTH_TWO_BLOCK && fileDifference == SAME_FILE);
    }

    @Override
    protected boolean isSatisfyStart(Position start) {
        return start.isSameRank(WHITE_PAWN_INIT_RANK);
    }

    @Override
    protected List<Position> findPath(Position start, Position end) {
        Position middle = start.moveToNorth();
        return List.of(middle, end);
    }
}
