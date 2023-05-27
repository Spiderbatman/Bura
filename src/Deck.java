import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	public static enum Suite {
		H, D, C, S
	}
	public static enum Type {
		A, T, K, Q, J
	}
	
	public static int SIZE = 20;
	public static int SUITE_COUNT = 4;
	public static int SAME_SUITE_CARD_COUNT = 5;
	
	/**
	 * Checks if {@code card} cuts {@code target} given the trump {@link Suite}.
	 * 
	 * @param card - {@link Card} checked if it cuts {@code target}.
	 * @param target - {@link Card} to cut.
	 * @param trump - Trump {@link Suite}.
	 * @return - {@code true} if {@code card} cuts {@code target}. {@code false} otherwise.
	 */
	public static boolean cuts(Card card, Card target, Suite trump) {
		if (card.suite == trump) {
			if (target.suite == trump) return card.value > target.value;
			else return true;
		} else {
			if (target.suite == trump) return false;
			if (target.suite != card.suite) return false;
			return card.value > target.value;
		}
	}
	
	/**
	 * Checks if {@code card} represented with {@code index} cuts {@code target} 
	 * given the trump {@link Suite} represented as {@link Integer}.
	 * 
	 * @param card - {@link Integer} representation of {@link Card} checked if it cuts {@code target}.
	 * @param target - {@link Integer} representation of {@link Card} to cut.
	 * @param trump - Trump {@link Suite} represented as {@link Integer} index.
	 * @return - {@code true} if {@code card} cuts {@code target}. {@code false} otherwise.
	 */
	public static boolean cutsIndex(int card, int target, Suite suite) {
		return cuts(getCardFromIndex(card), getCardFromIndex(target), suite);
	}
	
	/**
	 * Checks if {@link List} of cards represented as integers cuts {@code target} {@link List} of cards. 
	 * given the trump {@link Suite} represented as {@link Integer}.
	 * 
	 * @param cards - {@link Integer} {@link List} representation of {@link Card} checked
	 * if it cuts {@code targets}.
	 * @param target - {@link Integer} {@link List} representation of {@link Card}s to cut.
	 * @param trump - Trump {@link Suite} represented as {@link Integer} index.
	 * @return - {@code true} if {@code card} cuts {@code target}. {@code false} otherwise.
	 */
	public static boolean cutsIndex(List<Integer> cards, List<Integer> targets, Suite suite) {
		List<List<Integer>> permutations = Deck.getPermutations(new ArrayList<>(cards));
		for (int i = 0; i < permutations.size(); i++) {
			boolean cuts = true;
			for (int j = 0; j < permutations.get(i).size(); j++) {
				if (!cutsIndex(permutations.get(i).get(j), targets.get(j), suite)) {
					cuts = false;
					break;
				}
			}
			if (cuts) return true;
		}
		return false;
	}
	
	/**
	 * Get {@link Card} object from its {@link Integer} representation.
	 * 
	 * @param index - {@link Integer} representation of {@link Card}.
	 * @return - {@link Card} object corresponding to {@code index}.
	 */
	public static Card getCardFromIndex(int index) {
		if (index > Deck.SIZE) throw new IllegalArgumentException("Impossible card");
		return new Card(getSuiteFromIndex(index), getTypeFromIndex(index)); 
	}
	
	/**
	 * Get a shuffled list of {@link Integer}s, each representing {@link Card} objects. Basically 
	 * this function provides shuffled deck.
	 * 
	 * @return - {@Link List} of {@link Integer}s.
	 */
	public static List<Integer> getShuffledIndexDeck() {
		ArrayList<Integer> arr = new ArrayList<>();
		for (int i = 0; i < Deck.SIZE; i++) arr.add(i);
		Collections.shuffle(arr);
		return arr;
	}
	
	/**
	 * Get a shuffled list of {@link Card}s. Basically this function provides shuffled deck.
	 * 
	 * @return - {@Link List} of {@link Card}s.
	 */
	public static List<Card> getShuffledCardDeck() { 
		List<Integer> arr = getShuffledIndexDeck();
		ArrayList<Card> cards = new ArrayList<Deck.Card>();
		for (int i = 0; i < arr.size(); i++) {
			cards.add(getCardFromIndex(arr.get(i)));
		}
		return cards;
	}
	
	/**
	 * Return {@link Suite} of a card represented as an {@code Integer}.
	 * 
	 * @param index - {@link Integer} representation of the card.
	 * @return - {@link Suite} of a {@link Card} corresponding to index.
	 */
	public static Suite getSuiteFromIndex(int index) {
		switch (index / Deck.SAME_SUITE_CARD_COUNT) {
			case 0: return Suite.H;
			case 1: return Suite.D;
			case 2: return Suite.C;
			case 3: return Suite.S;
			default: throw new IllegalArgumentException("Impossible card");
		}
	}
	
	/**
	 * Return {@link Type} of a card represented as an {@code Integer}.
	 * 
	 * @param index - {@link Integer} representation of the card.
	 * @return - {@link Type} of a {@link Card} corresponding to index.
	 */
	public static Type getTypeFromIndex(int index) {
		switch(index % Deck.SAME_SUITE_CARD_COUNT) {
			case 0: return Type.A;
			case 1: return Type.T;
			case 2: return Type.K;
			case 3: return Type.Q;
			case 4: return Type.J;
			default: throw new IllegalArgumentException("Impossible card");
		}
	}
	
	private static <T> List<List<T>> getPermutations(List<T> list) {
		if (list.size() == 0) {
			ArrayList<T> emptyPerm = new ArrayList<>();
			List<List<T>> result = new ArrayList<>();
			result.add(emptyPerm);
			return result;
		}
		T elem = list.remove(0);
		
		List<List<T>> permutations = getPermutations(list);
		
		List<List<T>> result = new ArrayList<>();
		for (int i = 0; i < permutations.size(); i++) {
			for (int j = 0; j <= permutations.get(i).size(); j++) {
				List<T> currentPermutation = new ArrayList<>(permutations.get(i));
				currentPermutation.add(j, elem);
				result.add(currentPermutation);
			}
		}
		return result;
	}
	
	/**
	 * Class representing a single card.
	 *
	 */
	public static class Card {
		public Suite suite;
		public Type type;
		public int value;
		
		public Card(Suite suite, Type type) {
			this.suite = suite;
			this.type = type;
			switch (type) {
			case A: {
				this.value = 11;
				break;
			}
			case T: {
				this.value = 10;
				break;
			}
			case K: {
				this.value = 4;
				break;
			}
			case Q: {
				this.value = 3;
				break;
			}
			case J: {
				this.value = 2;
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + type);
			}
		}
		
		@Override
		public String toString() {
			return "" + getVisibleSuiteChar() + type;
		}
		
		private char getVisibleSuiteChar() {
			switch (this.suite) {
			case S: return '\u2660';
			case D: return '\u2666';
			case H: return '\u2665';
			case C: return '\u2663';
			default: throw new RuntimeException("Impossible");
			}
		}
	}
}
