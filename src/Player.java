/**
 * A player interface that is responsible to provide actions given the state.
 */
public interface Player {
	public PlayerAction getAction(PlayerState state);
}
