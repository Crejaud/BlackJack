import java.util.ArrayList;
import java.util.Scanner;

public class BlackJackPlayer extends Player
{
	private ArrayList<Card> myHand;
	private ArrayList<Card> mySplitOne, mySplitTwo; //The ArrayList for when someone splits
	private double myBankroll;
	public static int numOfDeals = 0;
	static Scanner scan = new Scanner(System.in);
	
	public BlackJackPlayer() //Default constructor
	{
		super();
		myHand = null;
		mySplitOne = null;
		mySplitTwo = null;
		myBankroll = 0;
	}
	
	public BlackJackPlayer(String name, double bankroll) //Value constructor
	{
		super(name);
		myHand = new ArrayList<Card>();
		mySplitOne = null;
		mySplitTwo = null;
		myBankroll = bankroll;
	}
	
	public String play() //Asks hit, stand, split, etc
	{
		String choice = "";
		boolean real = false;
		if(numOfDeals == 0)
		{
			while(!real)
			{
				if(testSplit())
				{
					System.out.print("Hit, Stand, Split, Double, or Surrender : ");
					choice = scan.next();
					
					if(choice.toLowerCase().equals("hit") || choice.toLowerCase().equals("stand") || choice.toLowerCase().equals("split") || choice.toLowerCase().equals("double") || choice.toLowerCase().equals("surrender"))
						real = true;
					else
					{
						System.out.println("Error : Type hit, stand, split, double, or surrender.");
						real = false;
					}
				}
				else
				{
					System.out.print("Hit, Stand, Double, or Surrender : ");
					choice = scan.next();
					
					if(choice.toLowerCase().equals("hit") || choice.toLowerCase().equals("stand") || choice.toLowerCase().equals("double") || choice.toLowerCase().equals("surrender"))
						real = true;
					else
					{
						System.out.println("Error : Type hit, stand, double, or surrender.");
						real = false;
					}
				}
			}	
		}
		
		boolean real2 = false;
		
		if(numOfDeals != 0)
		{
			while(!real2)
			{
				System.out.print("Hit or Stand : ");
				choice = scan.next();
				
				if(choice.toLowerCase().equals("hit") || choice.toLowerCase().equals("stand"))
				{
					real2 = true;
					return choice;
				}
				else
				{
					System.out.println("Error : Type hit or stand.");
					real2 = false;
				}
			}	
		}
		
		return choice;
	}
	
	public boolean canPlay() //True or False if player is aloud to hit, stand, split, etc
	{
		if(getSum() == 21)
		{
			numOfDeals = 0;
			return false;
		}
		else if(getSum() >= 0 && getSum() < 21)
			return true;
		else
		{
			numOfDeals = 0;
			System.out.println("BUSTED : " + getSum());
			return false;
		}
	}
	
	public boolean canPlay(int whichSplit) //True or False if player is aloud to hit, stand, double, etc for after the player splits and has two hands
	{
		if(getSplitSum(whichSplit) == 21)
		{
			System.out.println("BLACKJACK!");
			return false;
		}
		else if(getSplitSum(whichSplit) >= 0 && getSplitSum(whichSplit) < 21)
			return true;
		else
		{
			System.out.println("BUSTED : " + getSplitSum(whichSplit));
			return false;
		}
	}
	
	public void hit(Card c) //Regular hit method
	{
		dealt(c);
		System.out.println("You were dealt a " + c);
		System.out.println("You now have : " + getSum());
		numOfDeals++;
	}
	
	public void hit(Card c, int whichSplit) //Hit method for after player splits
	{
		if(whichSplit == 0)
			mySplitOne.add(c);
		if(whichSplit == 1)
			mySplitTwo.add(c);
		System.out.println("You were dealt a " + c);
		System.out.println("You now have : " + getSplitSum(whichSplit));
		
		numOfDeals++;
	}
	
	public void split(Card c1, Card c2)
	{
		mySplitOne = new ArrayList<Card>();
		mySplitTwo = new ArrayList<Card>();
		
		mySplitOne.add(myHand.get(0));
		mySplitOne.add(c1);
		mySplitTwo.add(myHand.get(1));
		mySplitTwo.add(c2);
		
		System.out.println("First hand : " + mySplitOne);
		System.out.println("Second hand : " + mySplitTwo);
		numOfDeals++;
	}
	
	public void DOUBLE(Card c)
	{
		dealt(c);
		System.out.println("You were dealt a " + c);
		System.out.println("You now have : " + getSum());
		numOfDeals = 0;
	}
	
	public void surrender()
	{
		numOfDeals = 0;
	}
	
	public void changeBank(double bet) //This is used when distributing money
	{
		myBankroll += bet;
	}
	
	public double getBank() //Accessor for player's bankroll
	{
		return myBankroll;
	}
	
	public int getSum() //Gets score of the hand
	{
		int sum = 0;
		for(int numCards = 0; numCards < myHand.size(); numCards++)
		{
			if(myHand.get(numCards).getRank() == 1)
			{
				if(testAce())
					sum += 1;
				if(!testAce())
					sum += 11;
			}			
			else if(myHand.get(numCards).getRank() == 11 || myHand.get(numCards).getRank() == 12 || myHand.get(numCards).getRank() == 13)
				sum += 10;
			else
				sum += myHand.get(numCards).getRank();
		}
		return sum;
	}
		
	public int getSplitSum(int whichSplit) //Gets score of a split hand
	{
		int sum = 0;
		if(whichSplit == 0)
		{
			for(int numCards = 0; numCards < mySplitOne.size(); numCards++)
			{
				if(mySplitOne.get(numCards).getRank() == 1)
				{
					if(testAce())
						sum += 1;
					if(!testAce())
						sum += 11;
				}			
				else if(mySplitOne.get(numCards).getRank() == 11 || mySplitOne.get(numCards).getRank() == 12 || mySplitOne.get(numCards).getRank() == 13)
					sum += 10;
				else
					sum += mySplitOne.get(numCards).getRank();
			}
		}
		if(whichSplit == 1)
		{
			for(int numCards = 0; numCards < mySplitTwo.size(); numCards++)
			{
				if(mySplitTwo.get(numCards).getRank() == 1)
				{
					if(testAce())
						sum += 1;
					if(!testAce())
						sum += 11;
				}			
				else if(mySplitTwo.get(numCards).getRank() == 11 || mySplitTwo.get(numCards).getRank() == 12 || mySplitTwo.get(numCards).getRank() == 13)
					sum += 10;
				else
					sum += mySplitTwo.get(numCards).getRank();
			}
		}
		return sum;
	}
	
	public void dealt(Card card) //Receives dealt card and puts it in player's hand
	{
		myHand.add(card);			
	}
	
	public String displayHand() //Displays hand
	{
		return myHand + "";
	}
	
	public boolean testSplit() //True or False if player is aloud to split.
	{
		int[] test = new int[2];
		
		for(int x = 0; x < 2; x++)
		{
			if(myHand.get(x).getRank() == 11 || myHand.get(x).getRank() == 12 || myHand.get(x).getRank() == 13)
				test[x] = 10;
			else
				test[x] = myHand.get(x).getRank();
		}
			
		if(test[0] == test[1])
			return true;
		else
			return false;
	}
	
	public boolean testAce() //Tests ace to be 1 or 11. If 1, then true. If 11, then false.
	{
		int testSum = 0;
		for(int numCardsTest = 0; numCardsTest < myHand.size(); numCardsTest++)
		{
			if(myHand.get(numCardsTest).getRank() == 1)
				testSum += 11;
			else if(myHand.get(numCardsTest).getRank() == 11 || myHand.get(numCardsTest).getRank() == 12 || myHand.get(numCardsTest).getRank() == 13)
				testSum += 10;
			else
				testSum += myHand.get(numCardsTest).getRank();
		}
		if(testSum > 21)
			return true;
		else
			return false;
	}
	
	public void reset() //Clears out hand of player
	{
		myHand.clear();
	}
	
	public void resetNumOfDeals() //Resets a counter to assure correct output
	{
		numOfDeals = 0;
	}
}
