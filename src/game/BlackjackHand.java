
package game;

/**
 * The BlackjackHand Class represents a hand of cards in the Blackjack game.  It is a subclass of
 * the Hand Class, and adds the getBlackjackHand method, which return the value of the blackjack
 * hand in the game.
 * 
 * @author:  MAbdurrahman
 * @date:  16 June 2017
 * @version:  1.0.0
 */
public class BlackjackHand extends Hand {
	
    /**
     * BlackjackHand Constructor - Creates an instance of the BlackjackHand
     */
    public BlackjackHand() {
        super();
        
    }//end of the BlackjackHand Constructor
    /**
     * getBlackjackValue Method - Gets the value of the blackjack hand for the game
     * @param Void
     * @return Int -  Returns the value of the blackjack hand in the form of an Integer
     */
    public int getBlackjackValue() {
        int value = 0;//The value computed for the hand
    	boolean ace = false;//If hand contains an ace, it will be set to true.
    	int cards = getCardCount();//number of cards in the hand
    	
    	for(int i = 0; i < cards; i++) {//This loop adds the value of the card to the hand
            Card card;
            int cardValue;
            card = getCard(i);
            cardValue = card.getRank(); //normal value

            if(cardValue > 10) {
                    cardValue = 10; //value for a Jack, Queen, or King
            }
            if(cardValue == 1) {
                    ace = true;
            }
            value = value + cardValue;
            
    	}//end for loop
    	
    	/*At this point value is the value of the hand, with counting any ace as 1.
    	 *If there is an ace, and if changing its value from 1 to 11 would leave the
    	 *score less than or equal to 21, then add the extra 10 to value.
    	 */
    	 
    	 if(ace == true && value + 10 <= 21)
    	 	value = value + 10;
    	 	
    	 return value;
    	  	
    }//end the getBlackjackValue Method
}//end of the BlackjackHand Class
