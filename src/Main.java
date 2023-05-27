import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String args[]) {
		for (int i = 0; i < Deck.SUITE_COUNT; i++) {
			for (int j = 0; j < Deck.SAME_SUITE_CARD_COUNT; j++) {
				System.out.print(Deck.getCardFromIndex(i * Deck.SAME_SUITE_CARD_COUNT + j) + " ");
			}
			System.out.println();
		}
		
		Bura bura = new Bura(new RandomPlayer("PLAYER_A"), new RandomPlayer("PLAYER_B"));
		bura.play();
	}
}
