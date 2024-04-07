package chess.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PieceTest {

    @ParameterizedTest
    @CsvSource({"BLACK, true", "WHITE, false"})
    @DisplayName("기물이 해당 팀인지 확인한다.")
    void isSameTeamTest_whenOnePiece(Team pieceTeam, boolean expected) {
        Piece piece = new Pawn(pieceTeam);
        Team team = Team.BLACK;

        assertThat(piece.isSameTeam(team)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"BLACK, true", "WHITE, false"})
    @DisplayName("두 기물이 같은 팀인지 확인한다.")
    void isSameTeamTest_whenTwoPieces(Team team, boolean expected) {
        Piece one = new Pawn(Team.BLACK);
        Piece other = new Pawn(team);

        assertThat(one.isSameTeam(other)).isEqualTo(expected);
    }
}
