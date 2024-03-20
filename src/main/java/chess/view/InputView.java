package chess.view;

import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.util.Map;
import java.util.Scanner;

public class InputView {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Map<Character, File> FILE_INPUT = Map.of(
            'a', File.A, 'b', File.B, 'c', File.C, 'd', File.D,
            'e', File.E, 'f', File.F, 'g', File.G, 'h', File.H
    );
    private static final Map<Character, Rank> RANK_INPUT = Map.of(
            '1', Rank.ONE, '2', Rank.TWO, '3', Rank.THREE, '4', Rank.FOUR,
            '5', Rank.FIVE, '6', Rank.SIX, '7', Rank.SEVEN, '8', Rank.EIGHT
    );

    public GameCommand readCommand() {
        return GameCommand.from(SCANNER.next());
    }

    public Position readPosition() {
        String input = SCANNER.next();
        if (input.length() != 2) {
            throw new IllegalArgumentException("위치 형식이 올바르지 않습니다.");
        }
        File file = FILE_INPUT.get(input.charAt(0));
        Rank rank = RANK_INPUT.get(input.charAt(1));
        if (file == null || rank == null) {
            throw new IllegalArgumentException("위치 형식이 올바르지 않습니다.");
        }
        return new Position(file, rank);
    }
}