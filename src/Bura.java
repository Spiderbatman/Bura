
/**
 * Main logic of game.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bura {
	List<Integer> deck;
	Player playerA;
	Player playerB;
	List<Integer> aCards;
	List<Integer> bCards;
	int trumpCard;
	int aSum = 0, bSum = 0; // Sum of cards taken by each player.

	private static final int CARDS_IN_HAND = 3;

	public Bura(Player playerA, Player playerB) {
		this.playerA = playerA;
		this.playerB = playerB;
		this.aCards = new ArrayList<>();
		this.bCards = new ArrayList<>();
		this.aSum = this.bSum = 0;
		
		deck = Deck.getShuffledIndexDeck();
		trumpCard = deck.get(deck.size() - 1);
	}

	/**
	 * Simulation of a game.
	 */
	public void play() {
		System.out.println("\nStarting the game.\n");
		System.out.println("Trump is " + Deck.getCardFromIndex(trumpCard) + "\n");
		int currentPoint = 1;
		boolean playerEndedGame = false;
		int turnCount = 1;
		while (true) {
			System.out.print(turnCount + ". ");
			turnCount++;
			// Take cards from deck.
			while (deck.size() > 0 && aCards.size() < CARDS_IN_HAND) {
				aCards.add(deck.remove(0));
				bCards.add(deck.remove(0));
			}
			if (aCards.size() != bCards.size()) {
				throw new RuntimeException("Illegal state, players have different number of cards.");
			}

			PlayerState state = new PlayerState(
					PlayerState.PlayerStateType.ASK, 
					aCards, 
					new ArrayList<>(), 
					trumpCard,
					Bura.getActions(aCards));
			PlayerAction actionA = this.playerA.getAction(state);

			if (actionA.getType() == PlayerAction.ActionType.END_GAME) {
				System.out.println(playerA.getName() + " Ended the game");
				playerEndedGame = true;
				break;
			}
			
			// TODO: Add point increase
			PlayerState stateB = new PlayerState(
					PlayerState.PlayerStateType.ANSWER,
					bCards,
					aCards.stream()
						.filter(elem -> actionA.getCardIndeces().contains(aCards.indexOf(elem))).toList(),
					trumpCard,
					Bura.getAllActionsOfSize(bCards, actionA.getCardIndeces().size()));
			PlayerAction actionB = this.playerB.getAction(stateB);
			
			
			List<Integer> aPlayedCards = aCards.stream()
					.filter(elem -> actionA.getCardIndeces().contains(aCards.indexOf(elem))).toList();
			List<Integer> bPlayedCards = bCards.stream()
					.filter(elem -> actionB.getCardIndeces().contains(bCards.indexOf(elem))).toList();
			
			System.out.print(playerA.getName() + " - ");
			for (int i = 0; i < aPlayedCards.size(); i++) {
				System.out.print(Deck.getCardFromIndex(aPlayedCards.get(i)) + " ");
			}
			System.out.print(playerB.getName() + " - ");
			for (int i = 0; i < bPlayedCards.size(); i++) {
				System.out.print(Deck.getCardFromIndex(bPlayedCards.get(i)) + " ");
			}
			System.out.flush();
			
			ArrayList<Integer> aNewHand = new ArrayList<>();
			for (int i = 0; i < aCards.size(); i++) {
				if (!aPlayedCards.contains(aCards.get(i))) aNewHand.add(aCards.get(i));
			}
			aCards = aNewHand;
			ArrayList<Integer> bNewHand = new ArrayList<>();
			for (int i = 0; i < bCards.size(); i++) {
				if (!bPlayedCards.contains(bCards.get(i))) bNewHand.add(bCards.get(i));
			}
			bCards = bNewHand;
			
			int playedCardsSum = 0;
			for (int i = 0; i < aPlayedCards.size(); i++) {
				playedCardsSum += Deck.getCardFromIndex(aPlayedCards.get(i)).value;
			}
			for (int i = 0; i < bPlayedCards.size(); i++) {
				playedCardsSum += Deck.getCardFromIndex(bPlayedCards.get(i)).value;
			}
			
			if (Deck.cutsIndex(bPlayedCards, aPlayedCards, Deck.getSuiteFromIndex(trumpCard))) {
				bSum += playedCardsSum;
				changeTurn();
			} else {
				aSum += playedCardsSum;
			}
			
			System.out.println(aSum + " " + bSum);
			System.out.flush();
			
			if (aCards.size() == 0) {
				break;
			}
		}
		if (playerEndedGame) {
			if (aSum > 30) {
				System.out.println(playerA.getName() + " Won the game with " + aSum + " points.");
			} else {
				System.out.println(playerB.getName() + " Won the game, because of wrong end game call.");
			}
			System.out.println(playerA);
		} else {
			if (aSum > bSum) {
				System.out.println(playerA.getName() + " Won the game with most points - " + aSum);
			} else if (aSum < bSum) {
				System.out.println(playerB.getName() + " Won the game with most points - " + bSum);
			} else {
				System.out.println("Game ended with a draw.");
			}
		}
		System.out.println();
		
		System.out.println("\nEnd of the game.\n");
	}

	/**
	 * Returns the number of valid actions player can take, while holding
	 * {@code cards} in hand.
	 * 
	 * @param cards - {@link Card}s in players hand represented as {@Code Integer}s.
	 * @return - Number of valid actions a player can take.
	 */
	private static List<PlayerAction> getActions(List<Integer> cards) {
		ArrayList<PlayerAction> allActions = new ArrayList<>();

		for (int i = 0; i < cards.size(); i++) {
			// play one card
			List<Integer> currentSingleCardAction = new ArrayList<>();
			currentSingleCardAction.add(i);
			allActions.add(PlayerAction.createPlayAction(currentSingleCardAction));

			for (int j = i + 1; j < cards.size(); j++) {
				if (Deck.getSuiteFromIndex(cards.get(i)).equals(Deck.getSuiteFromIndex(cards.get(j)))) {
					// play 2 cards if they are same suite
					List<Integer> currentAction = new ArrayList<>();
					currentAction.add(i);
					currentAction.add(j);
					allActions.add(PlayerAction.createPlayAction(currentAction));
				}
			}
		}

		// normal play of 3 cards
		if (areAllCardsSameSuite(cards)) {
			List<Integer> currentAction = new ArrayList<>();
			currentAction.add(0);
			currentAction.add(1);
			currentAction.add(2);
			allActions.add(PlayerAction.createPlayAction(currentAction));
		}

		// maliutka
		if (areAllCardsSameType(cards)) {
			List<Integer> currentAction = new ArrayList<>();
			currentAction.add(0);
			currentAction.add(1);
			currentAction.add(2);
			allActions.add(PlayerAction.createPlayAction(currentAction));
		}

		return allActions;
	}
	
	private static List<PlayerAction> getAllActionsOfSize(List<Integer> cards, int size) {
		ArrayList<PlayerAction> allActions = new ArrayList<>();
		if (size == 1) {
			for (int i = 0; i < cards.size(); i++) {
				List<Integer> currentAction = new ArrayList<>();
				currentAction.add(i);
				allActions.add(PlayerAction.createPlayAction(currentAction));
			}
		} else if (size == 2) {
			for (int i = 0; i < cards.size(); i++) {
				for (int j = i + 1; j < cards.size(); j++) {
					List<Integer> currentAction = new ArrayList<>();
					currentAction.add(i);
					currentAction.add(j);
					allActions.add(PlayerAction.createPlayAction(currentAction));
				}
			}
		} else {
			List<Integer> currentAction = new ArrayList<>();
			for (int i = 0; i < cards.size(); i++) {
				currentAction.add(i);
			}
			allActions.add(PlayerAction.createPlayAction(currentAction));
		}
		return allActions;
	}

	private void changeTurn() {
		Player tmp = this.playerA;
		this.playerA = this.playerB;
		this.playerB = tmp;
		int tmpSum = this.aSum;
		this.aSum = this.bSum;
		this.bSum = tmpSum;
		List<Integer> tmpCards = this.aCards;
		this.aCards = this.bCards;
		this.bCards = tmpCards;
	}

	private static boolean areAllCardsSameType(List<Integer> cards) {
		for (int i = 1; i < cards.size(); i++) {
			if (!Deck.getTypeFromIndex(cards.get(i)).equals(Deck.getTypeFromIndex(cards.get(0)))) {
				return false;
			}
		}
		return true;
	}

	private static boolean areAllCardsSameSuite(List<Integer> cards) {
		for (int i = 1; i < cards.size(); i++) {
			if (!Deck.getSuiteFromIndex(cards.get(i)).equals(Deck.getSuiteFromIndex(cards.get(0)))) {
				return false;
			}
		}
		return true;
	}
}
