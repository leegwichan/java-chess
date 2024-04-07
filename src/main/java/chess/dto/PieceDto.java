package chess.dto;

import chess.domain.piece.Piece;

public record PieceDto(PieceType pieceType, TeamType teamType) {

    public static PieceDto from(Piece piece) {
        PieceType pieceType = PieceType.from(piece);
        TeamType teamType = TeamType.from(piece.getTeam());

        return new PieceDto(pieceType, teamType);
    }

    public static PieceDto createEmptyPiece() {
        return new PieceDto(PieceType.getEmptyType(), TeamType.getEmptyType());
    }

    public boolean isBlack() {
        return teamType.isBlackTeam();
    }

    public boolean isExist() {
        return !pieceType.isEmpty();
    }
}
