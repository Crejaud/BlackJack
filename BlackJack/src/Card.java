
public class Card 
{
	private int myRank;
	private String mySuit;
	private boolean faceUp = true;
	
	public Card(int r, String s) //Value constructor
	{
		myRank = r;
		mySuit = s;
	}
	
	public String toString()
	{
		return changeRank() + mySuit;
	}
	
	public int getRank() //Accessor for myRank
	{
		return myRank;
	}
	
	public String mySuit() //Accessor for mySuit
	{
		return mySuit;
	}
	
	public String changeRank() //Gets value of the rank
	{
		String rank = Integer.toString(myRank);
		if(myRank == 1)
			rank = "A";
		if(myRank == 11)
			rank = "J";
		if(myRank == 12)
			rank = "Q";
		if(myRank == 13)
			rank = "K";
			
		return rank;
	}
	
	public boolean GreaterThanEqual(Card c) //True or False if card called is greater than object sent in parameters
	{
		int rank = c.getRank();
		if(myRank >= rank)
			return true;
		else
			return false;
	}
	
	public boolean isFaceUp() //True or False if card is faced up
	{
		if(faceUp)
			return true;
		else
			return false;
	}
	
	public void turn() //Turns the card face up or face down
	{
		faceUp = !faceUp;
	}
}
