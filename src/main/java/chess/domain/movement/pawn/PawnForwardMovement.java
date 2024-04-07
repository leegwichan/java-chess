package chess.domain.movement.pawn;

import chess.domain.movement.MovementRule;
import chess.domain.position.Position;
import java.util.List;

public abstract class PawnForwardMovement implements MovementRule {

    @Override
    public final boolean isMovable(Position start, Position end, boolean isAttack) {
        if (isAttack) {
            return false;
        }

        int rankDifference = start.calculateRankDifference(end);
        int fileDifference = start.calculateFileDifference(end);
        return isMovablePath(rankDifference, fileDifference) && isSatisfyStart(start);
    }

    @Override
    public final List<Position> findPath(Position start, Position end, boolean isAttack) {
        if (!isMovable(start, end, isAttack)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
        return findPath(start, end);
    }

    protected abstract boolean isMovablePath(int rankDifference, int fileDifference);

    protected abstract boolean isSatisfyStart(Position start);

    protected abstract List<Position> findPath(Position start, Position end);
}
