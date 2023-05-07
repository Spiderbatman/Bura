import java.util.concurrent.ThreadLocalRandom;

/**
 * An implementation of {@link Player} interface, that acts randomly.
 */
public class RandomPlayer implements Player {
	@Override
	public PlayerAction getAction(PlayerState state) {
		return state.getActions().get(ThreadLocalRandom.current().nextInt(0, state.getActions().size()));
	}
}
