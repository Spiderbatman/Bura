/**
 * Main logic of game.
 */
import java.util.ArrayList;
import java.util.List;

public class Bura {
	List<Integer> deck;
	Player playerA;
	Player playerB;
	int trumpCard;

	private static final int CARDS_IN_HAND = 3;
	
	public Bura(Player playerA, Player playerB) {
		this.playerA = playerA;
		this.playerB = playerB;
		deck = Deck.getShuffledIndexDeck(); 
		trumpCard = deck.get(deck.size() - 1);
	}
	
	/**
	 * Simulation of a game.
	 */
	public void play() {
		System.out.println("\nStarting the game.\n");
		System.out.println("Trump is " + Deck.getCardFromIndex(trumpCard)+ "\n");
		List<Integer> aCards = new ArrayList<>();
		List<Integer> bCards = new ArrayList<>();
		int aSum = 0, bSum = 0;
		
		while (true) {
			if (deck.size() == 0) break;
			
			PlayerState state = new PlayerState(
				PlayerState.PlayerStateType.Ask, 
				aCards, 
				new ArrayList<>(), 
				trumpCard, 
				Bura.getActions(aCards)
			);
			
			PlayerAction actoinA = this.playerA.getAction(state);
//			PlayerAction actionB = this.playerB.getAction(b);
			
			if (aCards.size() < Bura.CARDS_IN_HAND) {
				
			}
		}
		System.out.println("\nEnd of the game.\n");
	}
	
	/**
	 * Returns the number of valid actions player can take, while holding {@code cards} in hand.
	 * 
	 * @param cards - {@link Card}s in players hand represented as {@Code Integer}s.
	 * @return - Number of valid actions a player can take.
	 */
	private static List<PlayerAction> getActions(List<Integer> cards) {
		ArrayList<PlayerAction> allActions = new ArrayList<>();
		for (int i = 0; i < Bura.CARDS_IN_HAND; i++) {
			List<Integer> currentAction = new ArrayList<>();
			currentAction.add(i);
			allActions.add(new PlayerAction(currentAction));
		}
		
		for (int i = 0; i < Bura.CARDS_IN_HAND; i++) {
			for (int j = i + 1; j < Bura.CARDS_IN_HAND; j++) {
				if (Deck.getSuiteFromIndex(cards.get(i)).equals(Deck.getSuiteFromIndex(cards.get(j)))) {
					List<Integer> currentAction = new ArrayList<>();
					currentAction.add(i);
					currentAction.add(j);
					allActions.add(new PlayerAction(currentAction));
				}
			}
		}
		
		if (Deck.getSuiteFromIndex(cards.get(0)).equals(Deck.getSuiteFromIndex(cards.get(1))) && 
				Deck.getSuiteFromIndex(cards.get(1)).equals(Deck.getSuiteFromIndex(cards.get(2)))) {
			List<Integer> currentAction = new ArrayList<>();
			currentAction.add(0);
			currentAction.add(1);
			currentAction.add(2);
			allActions.add(new PlayerAction(currentAction));
		}
		
		if (Deck.getTypeFromIndex(cards.get(0)).equals(Deck.getTypeFromIndex(cards.get(1))) && 
				Deck.getTypeFromIndex(cards.get(1)).equals(Deck.getTypeFromIndex(cards.get(2)))) {
			List<Integer> currentAction = new ArrayList<>();
			currentAction.add(0);
			currentAction.add(1);
			currentAction.add(2);
			allActions.add(new PlayerAction(currentAction));
		}
		
		return allActions;
	}
	
	private void changeTurn() {
		Player tmp = this.playerA;
		this.playerA = this.playerB;
		this.playerB = tmp;
	}
	
}
