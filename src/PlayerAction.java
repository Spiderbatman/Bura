import java.util.ArrayList;
import java.util.List;

public class PlayerAction {
	private List<Integer> cardIndices;
	
	public PlayerAction(List<Integer> cardIndices) {
		this.cardIndices = new ArrayList<>(cardIndices);
	}
	
	/**
	 * Returns indices of cards played during an action.
	 * @return
	 */
	public List<Integer> getCardIndeces() {
		return this.cardIndices;
	}
}
