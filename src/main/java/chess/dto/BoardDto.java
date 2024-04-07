package chess.dto;

import chess.domain.ChessGame;
import chess.domain.position.Position;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class BoardDto {

    private final Map<Position, PieceDto> board;

    private BoardDto(Map<Position, PieceDto> board) {
        this.board = board;
    }

    public static BoardDto from(ChessGame chessGame) {
        Map<Position, PieceDto> board = Position.ALL_POSITIONS.stream()
                .map(position -> toResultEntry(chessGame, position))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        return new BoardDto(board);
    }

    private static Entry<Position, PieceDto> toResultEntry(ChessGame chessGame, Position position) {
        PieceDto pieceDto = chessGame.findPieceAt(position)
                .map(PieceDto::from)
                .orElse(PieceDto.createEmptyPiece());
        return Map.entry(position, pieceDto);
    }

    public PieceDto getPieceDto(Position position) {
        return board.get(position);
    }
}
