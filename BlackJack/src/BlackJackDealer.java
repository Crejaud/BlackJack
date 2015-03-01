import java.util.ArrayList;


public class BlackJackDealer extends Player
{
	private ArrayList<Card> dealerHand;
	private boolean faceUp = true;
	public static int numOfDeals = 0;
	
	public BlackJackDealer()
	{
		super();
		dealerHand = null;
	}
	
	public BlackJackDealer(String name)
	{
		super(name);
		dealerHand = new ArrayList<Card>();
	}
	
	public boolean canPlay()
	{
		if(getSum() == 21)
			return false;
		else if(getSum() <= 16)
			return true;
		else
		{
			if(getSum() > 21)
				System.out.println(getName() + " BUSTED");
			return false;
		}
	}
	
	public void hit(Card c)
	{
		dealerHand.add(c);
		numOfDeals++;
		System.out.println(getName() + " was dealt a " + c);
		System.out.println(getName() + " now has : " + getSum());
	}
	
	public void dealtUp(Card c)
	{
		dealerHand.add(c);
	}
	
	public void dealtDown(Card c)
	{
		dealtUp(c);
		faceUp = false;
	}
	
	public String displayHand()
	{
		if(!faceUp)
			return "[" + dealerHand.get(0) + ", X]";
		else
			return "" + dealerHand + "= " + getSum();
	}
	
	public int getSum()
	{
		int sum = 0;
		
		for(int numCards = 0; numCards < dealerHand.size(); numCards++)
		{
			if(dealerHand.get(numCards).getRank() == 1)
			{
				if(testAce())
					sum += 1;
				if(!testAce())
					sum += 11;
			}			
			else if(dealerHand.get(numCards).getRank() == 11 || dealerHand.get(numCards).getRank() == 12 || dealerHand.get(numCards).getRank() == 13)
				sum += 10;
			else
				sum += dealerHand.get(numCards).getRank();
		}
		
		return sum;
	}
	
	public boolean testAce() //Tests ace to be 1 or 11. If 1, then true. If 11, then false.
	{
		int testSum = 0;
		for(int numCardsTest = 0; numCardsTest < dealerHand.size(); numCardsTest++)
		{
			if(dealerHand.get(numCardsTest).getRank() == 1)
				testSum += 11;	
			else if(dealerHand.get(numCardsTest).getRank() == 11 || dealerHand.get(numCardsTest).getRank() == 12 || dealerHand.get(numCardsTest).getRank() == 13)
				testSum += 10;
			else
				testSum += dealerHand.get(numCardsTest).getRank();
		}
		if(testSum > 21)
			return true;
		else
			return false;
	}
	
	public void turn()
	{
		faceUp = !faceUp;
	}
	
	public void reset()
	{
		dealerHand.clear();
	}
}
