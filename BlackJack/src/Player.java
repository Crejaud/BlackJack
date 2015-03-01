
public abstract class Player
{
	private String myName;
	
	public Player() // Default constructor for a player
	{
		myName = "";
	}
	
	public Player(String name) // Value constructor for a player
	{
		myName = name;
	}
	
	public String getName() // Access name of a player
	{
		return myName;
	}
	
	public abstract boolean canPlay();
	public abstract String displayHand();
	public abstract int getSum();
	public abstract boolean testAce();
	public abstract void reset();
}
