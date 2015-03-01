import java.util.ArrayList;
import java.util.Random;

public class Shoe
{
	private int myCutSpot;
	static Random rand = new Random();
	private ArrayList<Card> myCards;
	
	public Shoe()
	{
		myCards = new ArrayList<Card>();
		//Makes 6 decks to construct a Shoe
		resetShoe();
	}
	
	public void shuffle() //Simulates bridge shuffle
	{
		if(Size() == 312)
		{
			ArrayList<Card> test1 = new ArrayList<Card>(); //Holds first portion of deck
			ArrayList<Card> test2 = new ArrayList<Card>(); //Holds second portion of deck
			
			for(int numShuffles = 8; numShuffles > 0 ; numShuffles--) //numShuffles is how many times it will simulate a bridge shuffle
			{
				int splitSpot = rand.nextInt(80) + 116; //splitSpot is the point at which the deck is split and then shuffled.
				
				for(int x = 0; x <= splitSpot; x++)
					test1.add(myCards.remove(0)); //For-loop to add first portion to test1 and at the same time removing those cards from actual deck
				
				for(int x = 0; x <= 310-splitSpot; x++)
					test2.add(myCards.remove(0)); //For-loop to add second portion to test1 and at the same time removing those cards from actual deck
				
				for(int x = 0; x <= 311; x++) //The for-loop that shuffles
				{
					if (test1.isEmpty() && test2.isEmpty()) //Tests if the two holders are empty -> breaks out of for-loop
						x = 80085 + 9001;
					if (x % 2 == 0 && !test2.isEmpty()) //If counter (x) is even and test2 is not empty: send card from test2 to deck
						myCards.add(test2.remove(0));
					if (x % 2 == 1 && !test1.isEmpty()) //If counter (x) is odd and test1 is not empty: send card from test1 to deck
						myCards.add(test1.remove(0));
					if (test1.isEmpty() && !test2.isEmpty()) //If test1 is empty and test2 is not empty: send card from test2 to deck
						myCards.add(test2.remove(0));
					if (test2.isEmpty() && !test1.isEmpty()) //If test2 is empty and test1 is not empty: send card from test1 to deck
						myCards.add(test1.remove(0));
				}
			}
		}
	}
	
	public boolean IsEmpty() //True or False if shoe is empty
	{
		if(myCards.isEmpty())
			return true;
		else
			return false;
	}
	
	public int Size() //Accessor for the size of the shoe
	{
		return myCards.size();
	}
	
	public Card deal() //Deals one card
	{
		if(!IsEmpty())
			return myCards.remove(0);
		else
			return null;
	}
	
	public void cutSpot() //Initializes myCutSpot
	{
		myCutSpot = rand.nextInt(30) + 20; //Random number between 20-50
	}
	
	public int getSpot() //Accessor for myCutSpot
	{
		return myCutSpot;
	}
	
	public void resetShoe() //Resets shoe to before it was shuffled in order
	{
		myCards.clear();
		for(int y = 1; y <= 6; y++)
		{
			for(int x = 1; x<=13; x++)
				myCards.add(new Card(x,"h"));
			for(int x = 1; x<=13; x++)
				myCards.add(new Card(x,"d"));
			for(int x = 1; x<=13; x++)
				myCards.add(new Card(x,"c"));
			for(int x = 1; x<=13; x++)
				myCards.add(new Card(x,"s"));
		}
	}
}
