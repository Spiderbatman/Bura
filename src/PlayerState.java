/**
 * Class representing a state of game from players point of view.
 */
import java.util.ArrayList;
import java.util.List;

public class PlayerState {
	private List<Integer> cardsInHand;
	private int trumpCard;
	private PlayerStateType stateType;
	private List<Integer> cardsOnBoard;
	private List<PlayerAction> actions;
	
	private static final int STATE_SIZE_IN_BYTES = 
			/* Cutting or playing */                                1 + 
			/* 20 (Deck size) bytes representing cards in hand */   Deck.SIZE + 
			/* 20 (Deck size) bytes representing cards on board */  Deck.SIZE + 
			/* 20 (Deck size) bytes representing trump card */      Deck.SIZE;
	
	/**
	 * 
	 * @param type - {@link PlayerStateType} representing if active player has to cut cards or play them.
	 * @param cardsInHand - {@link List} of cards in hand of active player.
	 * @param cardsOnBoard - {@link List} of cards on the board.
	 * @param trumpCard - {@link Integer} representation of trump card.
	 * @param actions - Possible {@link PlayerAction}s a player can take.
	 */
	public PlayerState(
		PlayerStateType type, 
		List<Integer> cardsInHand, 
		List<Integer> cardsOnBoard, 
		int trumpCard,
		List<PlayerAction> actions
	) {
		this.stateType = type;
		this.cardsInHand = cardsInHand;
		this.cardsOnBoard = cardsOnBoard;
		this.trumpCard = trumpCard;
		this.actions = new ArrayList<>(actions);
	}
	
	/**
	 * Get state encoded as {@link byte[]}
	 * 
	 * @return - {@link byte[]} representing a state.
	 */
	public Byte[] toBytes() {
		Byte[] state = new Byte[PlayerState.STATE_SIZE_IN_BYTES];
		int currentIndex = 0;
		// set state type
		state[currentIndex] = this.stateType == PlayerStateType.Ask ? (byte)1 : (byte)0;
		currentIndex++;
		
		// set hand
		for (int i = 0; i < this.cardsInHand.size(); i++) {
			state[currentIndex + this.cardsInHand.get(i)] = (byte)1;
		}
		currentIndex += Deck.SIZE;
		
		// set board
		for (int i = 0; i < this.cardsOnBoard.size(); i++) {
			state[currentIndex + this.cardsOnBoard.get(i)] = (byte)1;
		}
		currentIndex += Deck.SIZE;
		
		// set trump
		state[currentIndex + this.trumpCard] = (byte)1;
		
		return state;
	}
	
	public List<PlayerAction> getActions() {
		return this.actions;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("------State of the player-------");
		builder.append("Type: " + (this.stateType == PlayerStateType.Ask ? "Ask\n" : "Answer\n"));
		builder.append("Cards in hand:");
		for (int i = 0; i < this.cardsInHand.size(); i++) {
			if (i > 0) builder.append(",");
			builder.append(" " + Deck.getCardFromIndex(this.cardsInHand.get(i)));
		}
		return builder.toString();
	}
	
	public static enum PlayerStateType {
		Ask, Answer
	}
}
