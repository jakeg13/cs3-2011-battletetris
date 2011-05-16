
public interface TetrisAI {

	public void think(); // usually determines the goal state
	public void actuate(); // usually determines how to get to the goal state step by step.
}
