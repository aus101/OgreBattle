package ogrebattle.tarot.simulate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ogrebattle.tarot.Tarot;

public class TarotDeck {
	public static final int DECK_SIZE = Tarot.values().length;
	//6 questions and 1 bonus/final card
	public static final int CARDS_DRAWN = 7;
	
	protected Tarot finalCard;
	protected List<Tarot> drawnCards;
	//only need 1 instance, re-shuffled on each setup()
	protected static List<Tarot> cards = Arrays.asList(Tarot.values());
	protected static final Tarot[] checking2 = {Tarot.Devil, Tarot.Chariot};
	protected static final Tarot[] checking3 = {Tarot.Devil, Tarot.Chariot, Tarot.Hermit};
	
	protected static int foolCount = 0;
	protected static int anyOfThree = 0;//i.e. at least one of three
	protected static int totalHands = 0;
	protected static int validHands = 0;
	
	/**
	 * Generate deck of 7 Tarot cards using lists
	 */
	public void setup() {
		totalHands++;
		Collections.shuffle(cards);
		//no need to use RNG to pull a random card when deck is already shuffled
		drawnCards = cards.subList(0, CARDS_DRAWN);
		//store because final card chosen from the deck alters the Opinion Leader's starting stats
		//chosen card is an optical illusion, each one is the same card
		finalCard = drawnCards.get(CARDS_DRAWN-1);
		
		contains();
	}
		
	public void setup(int handsToDraw) {
		for(int i=0; i< handsToDraw; i++)
			setup();
	}
	
	/**
	 * 
	 * Check if desired cards are present
	 */
	protected void contains() {
		boolean fool = false;
		boolean dch = false;
		//deck of 7 ends up being much faster to do 4 O(n) against a list versus any set construction for O(1) contains
		if(drawnCards.contains(Tarot.Fool)) {
			foolCount++;
			fool = true;
		}
		
		//check if contains at least one
		if(drawnCards.contains(checking3[0]) ||
			drawnCards.contains(checking3[1]) ||
				drawnCards.contains(checking3[2])) {
			anyOfThree++;
			dch = true;
		}
		
		if (fool && dch) {
			validHands++;
		}	
	}

	public static int getfoolCount() {
		return foolCount;
	}

	public static int getDevilCharotHermitCount() {
		return anyOfThree;
	}
	
	public static int getTotalHands() {
		return totalHands;
	}

	public static int getValidHands() {
		return validHands;
	}
}
