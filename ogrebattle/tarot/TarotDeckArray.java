package ogrebattle.tarot;

import java.util.Random;

/**
 * Only necessary to shuffle the first 7 cards in the deck.<br>
 * Can cut execution time down by sticking to more cumbersome arrays.<br>
 */
public class TarotDeckArray extends TarotDeck {
	//as opposed to List<Tarot> drawnCards;
	private Tarot[] drawnCards;
	//as opposed to List<Tarot> cards, re-shuffled on each setup()
	private Tarot[] arrayCards = Tarot.values();
	//No need for SecureRandom here, instance level Random slows down throughput
	private static Random r = new Random();
	
	@Override
	public void setup() {
		totalHands++;
		//Fisher-Yates shuffle with array but only shuffle 7 cards
		//shuffle from back to front of the deck
	    for (int i=arrayCards.length-1; i>(DECK_SIZE-CARDS_DRAWN); i--) {
	      int card = r.nextInt(i+1);
	      //swap them
	      Tarot temp = arrayCards[card];
	      arrayCards[card] = arrayCards[i];
	      arrayCards[i] = temp;
	    }
	    
	    drawnCards = new Tarot[CARDS_DRAWN];
	    //loop unrolling helps performance a little, choosing not to unroll other loops
	    drawnCards[6] = arrayCards[21];
	    drawnCards[5] = arrayCards[20];
	    drawnCards[4] = arrayCards[19];
	    drawnCards[3] = arrayCards[18];
	    drawnCards[2] = arrayCards[17];
	    drawnCards[1] = arrayCards[16];
	    drawnCards[0] = arrayCards[15];
	    
	    //alternative 1, add the -1 to the loop end condition to not draw an extra card
//	    for (int i=arrayCards.length-1; i>(DECK_SIZE-CARDS_DRAWN-1); i--) {
//	    	drawnCards[i-(DECK_SIZE-CARDS_DRAWN)] = arrayCards[i];
//	    }

	    //alternative 2
//	    System.arraycopy(arrayCards, (DECK_SIZE-CARDS_DRAWN), drawnCards, 0, drawnCards.length);
	    
		//store because final card chosen from the deck alters the Opinion Leader's starting stats
		//chosen card is an optical illusion, each one is the same card
		finalCard = drawnCards[0];
		
		contains();
	}
	
	/**
	 * Check if cards are present using the array instead of the list
	 */
	@Override
	protected void contains() {
		boolean fool = false;
		boolean dch = false;
		//deck of 7 ends up being faster to do 4 O(n) contains with a list versus make a set for O(1) contains
		for(int i=0; i<CARDS_DRAWN; i++) {
			if(drawnCards[i] == Tarot.Fool) {
				foolCount++;
				fool = true;
				break;
			}
		}
		
		//check if contains at least one
		for(int i=0; i<CARDS_DRAWN; i++) {
			if(((drawnCards[i] == Tarot.Devil) ||
				drawnCards[i] == Tarot.Chariot) ||
					drawnCards[i] == Tarot.Hermit) {
						devilCharotHermitCount++;
						dch = true;
						break;
					}
				}
				
		if (fool && dch) {
			validHands++;
		}
	}
}
