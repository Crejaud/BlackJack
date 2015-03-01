
public class TextScroll //A class to make the text look cool. That is all.
{
	private String myPhrase;
	private int Index;
	private int phraseLength;
	
	public TextScroll()
	{
		reset();
	}
	
	public void scroll(String phrase) throws Exception //For System.out.print()
	{
		myPhrase = phrase;
		phraseLength = myPhrase.length();
        char[] array = new char[phraseLength];
        for(Index = 0; Index < phraseLength; Index++) //For-loop which extracts the characters of the phrase and puts them in an array
        	array[Index] = myPhrase.charAt(Index);
        Index = 0;
        while(Index != phraseLength)
        {  
            System.out.print(array[Index]); //Output one character after each quick pause to simulate scrolling text
            Thread.sleep(35); //The quick pause
            Index++; //Goes to next character
        }
        
        reset(); //Resets all instance variables to default values
	}
	public void scrollLn(String phrase) throws Exception //For System.out.println()
	{
		scroll(phrase);
		System.out.println(); //Skip a line
	}
	public void reset()
	{
		myPhrase = "";
		Index = 0;
		phraseLength = 0;
	}
}
