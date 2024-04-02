package chess.dto;

import chess.domain.Team;
import chess.domain.piece.Bishop;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import java.util.Arrays;
import java.util.function.Function;

public enum PieceType {

    KING(King.class, King::new),
    QUEEN(Queen.class, Queen::new),
    ROOK(Rook.class, Rook::new),
    BISHOP(Bishop.class, Bishop::new),
    KNIGHT(Knight.class, Knight::new),
    PAWN(Pawn.class, Pawn::new),
    EMPTY(null, null),
    ;

    private final Class<? extends Piece> category;
    private final Function<Team, Piece> pieceFunction;

    PieceType(Class<? extends Piece> category, Function<Team, Piece> pieceFunction) {
        this.category = category;
        this.pieceFunction = pieceFunction;
    }

    public static PieceType from(Piece piece) {
        return Arrays.stream(PieceType.values())
                .filter(pieceType -> pieceType.category == piece.getClass())
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 기물이 존재하지 않습니다."));
    }

    public static PieceType getEmptyType() {
        return EMPTY;
    }

    public Piece createPiece(Team team) {
        if (isEmpty()) {
            throw new IllegalStateException("빈 객체는 말을 만들 수 없습니다.");
        }
        return pieceFunction.apply(team);
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
