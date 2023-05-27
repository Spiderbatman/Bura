import java.util.ArrayList;
import java.util.List;

public class PlayerAction {
	private List<Integer> cardIndices;
	private ActionType type;
	
	public static PlayerAction createPlayAction(List<Integer> cardIndices) {
		return new PlayerAction(cardIndices); 
	}
	
	public static PlayerAction createPointIncreaseAction() {
		return new PlayerAction(ActionType.POINT_INCREASE); 
	}
	
	public static PlayerAction createPointIncreaseAcceptAction() {
		return new PlayerAction(ActionType.POINT_INCREASE_ACCEPT);
	}
	
	public static PlayerAction createPointIncreaseRefuseAction() {
		return new PlayerAction(ActionType.POINT_INCREASE_REFUSE);
	}
	
	public static PlayerAction createEndGameAction() {
		return new PlayerAction(ActionType.END_GAME);
	}
	
	private PlayerAction(ActionType type) {
		if (type == ActionType.PLAY) {
			throw new IllegalArgumentException("Play action needs card list to play.");
		}
		this.type = type;
	}
	
	private PlayerAction(List<Integer> cardIndices) {
		this.type = ActionType.PLAY;
		this.cardIndices = new ArrayList<>(cardIndices);
	}
	
	/**
	 * Returns indices of cards played during an action.
	 * @return
	 */
	public List<Integer> getCardIndeces() {
		return this.cardIndices;
	}
	
	/**
	 * Returns the type of this action.
	 * @return - {@link ActionType} of this action.
	 */
	public ActionType getType() {
		return this.type;
	}
	
	public enum ActionType {
		PLAY, POINT_INCREASE, POINT_INCREASE_ACCEPT, POINT_INCREASE_REFUSE, END_GAME
	}
}
