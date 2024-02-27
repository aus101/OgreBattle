package ogrebattle.tarot.simulate;

import java.util.Random;

import ogrebattle.tarot.pojo.Tarot;

/**
 * Only necessary to shuffle the first 7 cards in the deck.<br>
 * Can cut execution time down by sticking to more cumbersome arrays.<br>
 */
public class TarotDeckArray extends TarotDeck {
	//No need for slow SecureRandom here, instance level Random is also slower
	private Random r = new Random();
	//as opposed to List<Tarot> drawnCards;
	private Tarot[] drawnCards;
	//as opposed to List<Tarot> cards, re-shuffled on each setup()
	private static Tarot[] arrayCards = Tarot.values();
	
	/**
	 * Generate deck of 7 Tarot cards using arrays and only shuffling 7 cards
	 */
	@Override
	public void setup() {
		totalHands++;
		//Fisher-Yates shuffle with array but only shuffle 7 cards
		//shuffles from back to front of the deck
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

	    //alternative A
//	    System.arraycopy(arrayCards, (DECK_SIZE-CARDS_DRAWN), drawnCards, 0, drawnCards.length);
	    
	    //alternative B, need the -1 to the loop end condition to not draw an extra card
//	    for (int i=arrayCards.length-1; i>(DECK_SIZE-CARDS_DRAWN-1); i--) {
//	    	drawnCards[i-(DECK_SIZE-CARDS_DRAWN)] = arrayCards[i];
//	    }
 
		//store because final card chosen from the deck alters the Opinion Leader's starting stats
		//chosen card is an optical illusion in SNES/SFC, each one is the same card
		finalCard = drawnCards[0];
		
		contains();
	}
	
	/**
	 * Check if desired cards are present using the array instead of the list. Not robust, only checks for Fool and
	 * Fool + 1 of Devil/Chariot/Hermit but code can easily be adjusted and expanded upon.<br>
	 * Is robust in the sense of working with any number of drawn cards, not just 6 or 7.<br>
	 */
	@Override
	protected void contains() {
		boolean fool = false;
		boolean dch = false;
		//deck of 7 ends up being much faster to do 4 O(n) against a list versus any set construction for O(1) contains
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
						anyOfThree++;
						dch = true;
						break;
					}
				}
				
		if (fool && dch) {
			validHands++;
		}
	}
}
