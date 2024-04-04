package chess.dao;

import chess.domain.Team;
import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.dto.PieceDto;
import chess.dto.PieceType;
import chess.dto.TeamType;

public class PieceEntity {

    private final Position position;
    private final PieceDto pieceDto;

    public PieceEntity(Position position, Piece piece) {
        this(position, PieceDto.from(piece));
    }

    public PieceEntity(Position position, PieceType pieceType, TeamType teamType) {
        this(position, new PieceDto(pieceType, teamType));
    }

    private PieceEntity(Position position, PieceDto pieceDto) {
        this.position = position;
        this.pieceDto = pieceDto;
    }

    public static PieceEntity createEmptyPiece(Position position) {
        return new PieceEntity(position, PieceDto.createEmptyPiece());
    }

    public Piece toPiece() {
        Team team = getTeamType().getTeam();
        return getPieceType().createPiece(team);
    }

    public boolean isExistPiece() {
        return pieceDto.isExist();
    }

    public Rank getRank() {
        return position.getRank();
    }

    public File getFile() {
        return position.getFile();
    }

    public Position getPosition() {
        return position;
    }

    public PieceType getPieceType() {
        return pieceDto.pieceType();
    }

    public TeamType getTeamType() {
        return pieceDto.teamType();
    }
}
