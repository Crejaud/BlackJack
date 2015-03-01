import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

// Made by Corentin (Corey) Rejaud
// This is the class is the driver
// BlackJack Game with multiple features

public class BlackJack
{
	private BlackJackDealer myDealer;
	private ArrayList<BlackJackPlayer> myPlayers;
	private Shoe myShoe;
	private TextScroll text; //Used to call scrolling text method
	static Scanner scan = new Scanner(System.in);
	static double[] bet; //Array to hold bets
	static boolean[] blackjack; //True of False if someone got blackjack
	static boolean[] surrender; //True or False if someone surrendered
	static boolean[] split; //True or False if someone split
	static boolean exitGame; //True or False if game will exit
	
	public BlackJack() throws Exception //Necessary for scrolling text
	{
		text = new TextScroll();
		
		String choice;
		boolean valid = false; // A boolean to assure that user is saying yes or no
		text.scrollLn("Welcome to BlackJack");
		
		while(!valid)
		{
			text.scroll("Would you like to know the rules [Yes, No] : ");
			choice = scan.next();
			
			if(choice.toLowerCase().equals("yes"))
			{
				tutorial();
				valid = true;
			}
			else if(choice.toLowerCase().equals("no"))
			{
				valid = true;
			}
			else
			{
				text.scrollLn("It's a yes or no question.");
				valid = false;
			}
		}
		
		valid = false; // A boolean to assure that # of players is 1-4
		int playerCounter = 1; // Used to assign names, bankrolls, etc for each player
		String name;
		double bankroll = 0;
		int numPlayers = 0;
		
		myShoe = new Shoe(); //Holds the shoe
		myPlayers = new ArrayList<BlackJackPlayer>(); //Holds players
		
		// This while loop asks # of players and assures it's 1-4
		while(!valid)
		{
			text.scroll("# of Players [1-4] : ");
			numPlayers = scan.nextInt();
		
			if(numPlayers > 4 || numPlayers < 1)
			{
				text.scrollLn("Error : Must be from 1 to 4.");
				valid = false;
			}
			else
				valid = true;
		}
		
		while(numPlayers >= playerCounter) //Assigns names, bankrolls, etc for each player
		{
			text.scroll("Player " + playerCounter + "'s name : ");
			name = scan.next();
			
			valid = false; // A boolean to assure that bankroll is a valid number
			
			while(!valid)
			{
				text.scroll("Player " + playerCounter + "'s bankroll : ");
				bankroll = scan.nextDouble();
				
				if(bankroll > 0) //valid if greater than 0. Not valid if less than or equal to 0
					valid = true;
				else
				{
					text.scrollLn("Error : not a valid number");
					valid = false;
				}
			}
			
			myPlayers.add(new BlackJackPlayer(name, bankroll)); //Constructs a BlackJackPlayer and adds it to the ArrayList : myPlayers
			
			playerCounter++; //increments playerCounter to get information for next player
		}
		
		//Assign dealer's name and construct a dealer
		text.scroll("Dealer's name : "); //text.scroll makes the text scroll and look cool
		name = scan.next();
		
		myDealer = new BlackJackDealer(name); //Created the dealer with a name
		
		play(); //Starts the round
	}
	
	public void play() throws Exception
	{
		exitGame = false;
		bet = new double[myPlayers.size()]; //Array to hold bets
		blackjack = new boolean[myPlayers.size()]; //True of False if someone got blackjack
		surrender = new boolean[myPlayers.size()]; //True or False if someone surrendered
		split = new boolean[myPlayers.size()]; //True or False if someone split
		
		myShoe.cutSpot(); //Finds cutSpot before game starts
		myShoe.shuffle(); //Shuffles before game starts
		
		while(!exitGame) //Start of the game
		{
			
			resetToDefault(); //Resets everything to default to start new round			
			setBets(); //Sets bets for each player
			checkCutSpot(); //Checks cutSpot to see if need shuffle and new cutSpot			
			resetCards(); //Resets cards of players and dealer for the new round
			dealCards(); //Deals cards to players and dealer			
			display(); //Display the available information
			userCommand(); //Deals with hits, stands, etc. Auto for dealer			
			distributeMoney(); //Distributes the money of the bets correctly
			checkBanks(); //Checks to make sure all players have sufficient bankrolls (Greater than 0)
			restart(); //Asks player if he/she wants to start another round
		}
	}
	
	public void tutorial() throws Exception
	{
		//Scrolling of text
		text.scrollLn("The objective is to beat the dealer and nothing else. \n" +
				"The dealer will deal one card to everyone face up, even himself. \n" +
				"Once that is done, he deals them again. When he deals \n" +
				"himself, he will keep it faced down to prevent you from guessing \n" +
				"his 'score'. The dealer will go around to every player and ask for \n" +
				"a command. The commands are : hit, stand, split, double, and surrender. \n" +
				"Hit allows you to accept another card and you are able to keep \n" +
				"hitting until you surpass 21. The objective of the game is to be as \n" +
				"close as you can to 21, but not go over it or you lose, since the \n" +
				"higher score you get, the greater chance you'll have of winning money. \n" +
				"Stand means you skip your turn. Split is only aloud if you have \n" +
				"two cards of the same value. Double is similar to hit, but you can \n" +
				"only do it once and it ends your turn and in addition to that, it \n" +
				"doubles your bet. Surrender is similar to stand, but you can \n" +
				"only use it on your first turn. It halves your bet.");
	}
	
	public void resetToDefault()
	{
		//Resetting to default
		Arrays.fill(surrender, false);
		Arrays.fill(blackjack, false);
		Arrays.fill(bet, 0);
		Arrays.fill(split, false);
	}
	
	public void setBets() throws Exception
	{
		for(int playerCounter = 0; playerCounter < myPlayers.size(); playerCounter++)
		{
			boolean valid = false; //Checks to see if answer is valid
			while(!valid)
			{
				text.scrollLn(myPlayers.get(playerCounter).getName() + "'s bankroll : " + myPlayers.get(playerCounter).getBank()); //Shows the player their current bankroll
				text.scroll(myPlayers.get(playerCounter).getName() + "'s bet : "); //Gets name and asks for bet
				bet[playerCounter] = scan.nextDouble();
				
				if(bet[playerCounter] > myPlayers.get(playerCounter).getBank() || bet[playerCounter] <= 0) //Checks to see if bet is less than or equal to bankroll
				{
					text.scrollLn("Error : not a valid bet.");
					valid = false;
				}
				else
					valid = true;
			}
		}
	}
	
	public void checkCutSpot()
	{
		if(myShoe.getSpot() >= myShoe.Size()) //Checks cutSpot
		{
			myShoe.cutSpot();
			myShoe.resetShoe();
			myShoe.shuffle();
		}
	}
	
	public void resetCards()
	{
		for(int playerCounter = 0; playerCounter < myPlayers.size(); playerCounter++) //Reset the cards of each player
			myPlayers.get(playerCounter).reset();
		
		myDealer.reset(); //Resets the cards of dealer
	}
	
	public void dealCards()
	{
		int numOfDeals = 0; //Counter for # of deals
		while(numOfDeals < 2) //While-Loop to deal cards twice
		{
			
			for(int playerCounter = 0; playerCounter < myPlayers.size(); playerCounter++) //This for-loop deals the cards to each player
				myPlayers.get(playerCounter).dealt(myShoe.deal()); //Deals card
			
			//Deals a card to Dealer
			if(numOfDeals == 0)
				myDealer.dealtUp(myShoe.deal());
			if(numOfDeals == 1)
				myDealer.dealtDown(myShoe.deal());
			
			numOfDeals++;
		}
	}
	
	public void display() throws Exception
	{
		for(int playerCounter = 0; playerCounter < myPlayers.size(); playerCounter++) //For-loop to display info about each player
		{
			text.scrollLn(myPlayers.get(playerCounter).getName() + "'s hand is : " + myPlayers.get(playerCounter).displayHand() + " = " + myPlayers.get(playerCounter).getSum()); //Displays the hand of specific player
			if(myPlayers.get(playerCounter).getSum() == 21) //Checks if player got BlackJack
			{
				text.scrollLn("BLACKJACK!");
				blackjack[playerCounter] = true;
			}
			else
				blackjack[playerCounter] = false;
			text.scrollLn(myPlayers.get(playerCounter).getName() + "'s bankroll is : " + myPlayers.get(playerCounter).getBank()); //Displays the bankroll of specific player
		}
		
		text.scrollLn(myDealer.getName() + "'s hand is : " + myDealer.displayHand()); //Displays hand of the dealer
	}
	
	public void userCommand() throws Exception
	{
		boolean real; //A boolean that assures if player is aloud to play after choosing a command
		String commandContainer = ""; //Contains the user-command
		
		for(int playerCounter = 0; playerCounter < myPlayers.size(); playerCounter++) //This for-loop asks for hit, stand, split, surrender for each player
		{
			myPlayers.get(playerCounter).resetNumOfDeals();
			
			real = false;
			
			while(!real)
			{
				if(!split[playerCounter]) //If player did not use split --> continue
				{
					if(myPlayers.get(playerCounter).canPlay()) //Checks to make sure player is not 21 or over to choose a command
					{
						text.scroll(myPlayers.get(playerCounter).getName() + " - ");
						commandContainer = myPlayers.get(playerCounter).play();
						
						if(commandContainer.toLowerCase().equals("hit"))
						{
							myPlayers.get(playerCounter).hit(myShoe.deal());
							if(myPlayers.get(playerCounter).canPlay())
								real = false;
							else
								real = true;
						}
						if(commandContainer.toLowerCase().equals("stand"))
							real = true;
						if(commandContainer.toLowerCase().equals("split"))
						{
							myPlayers.get(playerCounter).split(myShoe.deal(), myShoe.deal());
							split[playerCounter] = true;
							real = false;
						}
						if(commandContainer.toLowerCase().equals("double"))
						{
							myPlayers.get(playerCounter).DOUBLE(myShoe.deal());
							bet[playerCounter] *= 2;
							real = true;
						}
						if(commandContainer.toLowerCase().equals("surrender"))
						{
							myPlayers.get(playerCounter).surrender();
							surrender[playerCounter] = true;
							real = true;
						}
					}
				}
				
				if(split[playerCounter])
				{
					int whichSplit; //A variable to determine which split the actions are focusing on
					boolean real2;
					for(whichSplit = 0; whichSplit < 2; whichSplit++)
					{
						real2 = false;
						while(!real2)
						{
							if(myPlayers.get(playerCounter).canPlay(whichSplit)) //Checks to make sure player is not 21 or over to choose a command
							{
								text.scroll(myPlayers.get(playerCounter).getName() + "'s hand " + (whichSplit + 1) + " - ");
								commandContainer = myPlayers.get(playerCounter).play();
								if(commandContainer.toLowerCase().equals("hit"))
									myPlayers.get(playerCounter).hit(myShoe.deal(), whichSplit);
								if(commandContainer.toLowerCase().equals("stand"))
									real2 = true;
								if(myPlayers.get(playerCounter).getSplitSum(whichSplit) >= 21)
									real2 = true;
							}
							else //If player is not aloud to hit again, get out of while-loop
								real2 = true;
						}
					}
					real = true;
				}
				if(blackjack[playerCounter]) //If you have blackjack, skip your turn
					real = true;
			}
			
		}
		
		real = false;
		while(!real) //This while-loop uses hit or stand automatically for the dealer when necessary
		{
			if(myDealer.canPlay())
			{
				myDealer.hit(myShoe.deal());
				real = false;
			}
			else
				real = true;
		}
		text.scrollLn(myDealer.getName() + " has : " + myDealer.getSum()); //Shows sum of the dealer
	}
	
	public void distributeMoney() throws Exception
	{
		for(int playerCounter = 0; playerCounter < myPlayers.size(); playerCounter++) //This for-loop distributes money from the bets correctly
		{
			if(!split[playerCounter]) //If not split, run this for-loop
			{
				if(myPlayers.get(playerCounter).getSum() == myDealer.getSum() && blackjack[playerCounter] == false && surrender[playerCounter] == false && myPlayers.get(playerCounter).getSum() <= 21)
				{
					text.scrollLn(myPlayers.get(playerCounter).getName() + " tied with the house : lost nothing");
					bet[playerCounter] = 0;
				}
				if(myPlayers.get(playerCounter).getSum() < myDealer.getSum() &&  myPlayers.get(playerCounter).getSum() < 21 && surrender[playerCounter] == false && myDealer.getSum() <= 21|| myPlayers.get(playerCounter).getSum() > 21)
				{
					text.scrollLn(myPlayers.get(playerCounter).getName() + " lost " + bet[playerCounter]);
					bet[playerCounter] *= -1;
				}
				if(myPlayers.get(playerCounter).getSum() > myDealer.getSum() && myPlayers.get(playerCounter).getSum() <= 21 && blackjack[playerCounter] == false && surrender[playerCounter] == false || myDealer.getSum() > 21 && myPlayers.get(playerCounter).getSum() <= 21 && blackjack[playerCounter] == false && surrender[playerCounter] == false )
				{	
					text.scrollLn(myPlayers.get(playerCounter).getName() + " beat " + myDealer.getName() + " regularly: won " + bet[playerCounter]);
					bet[playerCounter] *= 1;
				}
				if(myPlayers.get(playerCounter).getSum() == 21 && blackjack[playerCounter] == true && surrender[playerCounter] == false)
				{
					bet[playerCounter] *= 1.5;
					text.scrollLn(myPlayers.get(playerCounter).getName() + " got BlackJack : won " + bet[playerCounter]);
				}
				if(surrender[playerCounter] == true)
				{
					bet[playerCounter] *= -0.5;
					text.scrollLn(myPlayers.get(playerCounter).getName() + " surrendered : lost " + Math.abs(bet[playerCounter]));
				}
				
				myPlayers.get(playerCounter).changeBank(bet[playerCounter]);
			}
			
			if(split[playerCounter])
			{
				int whichSplit; //A variable to determine which split the actions are focusing on
				double temp; //Temporary container for the bets				
				temp = bet[playerCounter];
				
				for(whichSplit = 0; whichSplit < 2; whichSplit++)
				{
					bet[playerCounter] = temp;
					
					if(myPlayers.get(playerCounter).getSplitSum(whichSplit) == myDealer.getSum() && blackjack[playerCounter] == false && surrender[playerCounter] == false && myPlayers.get(playerCounter).getSplitSum(whichSplit) <= 21)
					{
						text.scrollLn(myPlayers.get(playerCounter).getName() + " tied with the house : lost nothing");
						bet[playerCounter] = 0;
					}
					if(myPlayers.get(playerCounter).getSplitSum(whichSplit) < myDealer.getSum() &&  myPlayers.get(playerCounter).getSplitSum(whichSplit) < 21 && surrender[playerCounter] == false && myDealer.getSum() <= 21|| myPlayers.get(playerCounter).getSplitSum(whichSplit) > 21)
					{
						bet[playerCounter] = temp;
						text.scrollLn(myPlayers.get(playerCounter).getName() + " lost " + bet[playerCounter]);
						bet[playerCounter] *= -1;
					}
					if(myPlayers.get(playerCounter).getSplitSum(whichSplit) > myDealer.getSum() && myPlayers.get(playerCounter).getSplitSum(whichSplit) <= 21 && blackjack[playerCounter] == false && surrender[playerCounter] == false || myDealer.getSum() > 21 && myPlayers.get(playerCounter).getSplitSum(whichSplit) <= 21 && blackjack[playerCounter] == false && surrender[playerCounter] == false)
					{	
						bet[playerCounter] = temp;
						text.scrollLn(myPlayers.get(playerCounter).getName() + " beat " + myDealer.getName() + " regularly: won " + bet[playerCounter]);
						bet[playerCounter] *= 1;
					}
					if(myPlayers.get(playerCounter).getSplitSum(whichSplit) == 21 && blackjack[playerCounter] == true && surrender[playerCounter] == false)
					{
						bet[playerCounter] = temp;
						bet[playerCounter] *= 1.5;
						text.scrollLn(myPlayers.get(playerCounter).getName() + " got BlackJack : won " + bet[playerCounter]);
					}
					if(surrender[playerCounter] == true)
					{
						bet[playerCounter] = temp;
						bet[playerCounter] *= -0.5;
						text.scrollLn(myPlayers.get(playerCounter).getName() + " surrendered : lost " + Math.abs(bet[playerCounter]));
					}
					myPlayers.get(playerCounter).changeBank(bet[playerCounter]);
				}
			}
		}
	}
	
	public void checkBanks() throws Exception
	{
		for(int playerCounter = 0; playerCounter < myPlayers.size(); playerCounter++) //This for-loop checks to see if people still have money to continue the game
		{
			if(myPlayers.get(playerCounter).getBank() <= 0)
			{
				text.scrollLn(myPlayers.get(playerCounter).getName() + " lost all of his/her money");
				text.scrollLn("Start a new game");
				System.exit(0);
			}
			else
				exitGame = false;			
		}
	}
	
	public void restart() throws Exception
	{
		String replay = ""; //Answer for wanting to replay or not
		boolean valid = false; //Checks to see if answer is valid
		
		while(!valid)
		{
			text.scroll("Next round [Yes/No] : "); //Question to ask if players want to continue playing
			replay = scan.next();
			
			if(replay.toLowerCase().equals("yes"))
			{
				exitGame = false;
				valid = true;
			}
			else if(replay.toLowerCase().equals("no"))
			{
				exitGame = true;
				valid = true;
			}
			else
			{
				text.scrollLn("It's a yes or no question.");
				valid = false;
			}
		}
	}
}
