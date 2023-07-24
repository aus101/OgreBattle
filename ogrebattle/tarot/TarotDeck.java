package ogrebattle.tarot;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TarotDeck {
	public static final int DECK_SIZE = 22;
	//6 questions and 1 bonus/final card
	public static final int CARDS_DRAWN = 7;
	
	protected Tarot finalCard;
	protected List<Tarot> drawnCards;
	//only need 1 instance, reshuffled on each setup()
	protected static List<Tarot> cards = Arrays.asList(Tarot.values());
	
	protected static int foolCount = 0;
	protected static int devilCharotHermitCount = 0;
	protected static int totalHands = 0;
	protected static int validHands = 0;

	
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
		for(int i=0; i< handsToDraw; i++) {
			setup();
		}
	}
	
	protected void contains() {
		boolean fool = false;
		boolean dch = false;
		//deck of 7 ends up being faster to do 4 O(n) contains with a list versus make a set for O(1) contains
		if(drawnCards.contains(Tarot.Fool)) {
			foolCount++;
			fool = true;
		}
		
		//check if contains at least one
		if(drawnCards.contains(Tarot.Devil) ||
			drawnCards.contains(Tarot.Chariot) ||
				drawnCards.contains(Tarot.Hermit)) {
			devilCharotHermitCount++;
			dch = true;
		}
		
		if (fool && dch) {
			validHands++;
		}	
	}

	public Tarot getFinalCard() {
		return finalCard;
	}

	public static int getfoolCount() {
		return foolCount;
	}

	public static int getDevilCharotHermitCount() {
		return devilCharotHermitCount;
	}
	
	public static int getTotalHands() {
		return totalHands;
	}

	public static int getValidHands() {
		return validHands;
	}
}
