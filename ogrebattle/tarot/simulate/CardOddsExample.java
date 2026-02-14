package ogrebattle.tarot.simulate;

import ogrebattle.printer.Util;

/**
 * This random hands approach is inferior to the <code>ogrebattle.tarot.exact</code> approach that finds
 * exact odds to back up the calculations given below that the simulation here merely converges to.<br>
 * The exception where simulation is useful is for drawing more than 7 cards in one setting, such as many Joker pulls,
 * that brute-forcing becomes impractical.<br>
 * In such a case, adjust <code>public static final int CARDS_DRAWN = 7;</code> in TarotDeck.java and
 * <code>setup()</code> in TarotDeckArray.java, if using that class<br>
 * @param args the number of hands to draw, default to 1 million if no args passed<br>
 * <br>
 * EXACT ODDS:<br>
 * <br>The subtraction of occurrences in the 1 AND (1 of X) calculations is necessary to remove<br>
 * double-counted occurrences, as it were. Odds here with the hypergeometric distribution are<br>
 * generally easier to calculate than with combinatorics. With more complex hands, such as in<br>
 * poker, combinatorics is the most practical approach for exact odds.<br>
 * <br>
 * Desired Tarot pulled in the first 6 cards: 20349 / 74613<br>
 * = COMBIN(21,5) / COMBIN(22,6)<br>
 * = HYPGEOM.DIST(1,6,1,22,0)<br>
 * = 1 - (21/22)*(20/21)*(19/20)*(18/19)*(17/18)*(16/17)<br>
 * = 27.27%, 27 repeating of course:  31x less than odds of at least 1 of 3<br>
 * <br>
 * All further odds account for 7 cards, including the bonus card:<br><br>
 * Desired Tarot pulled: 54264 / 170544<br>
 * = COMBIN(21,6) / COMBIN(22,7)<br>
 * = COMBIN(21,15) / COMBIN(22,7)<br>
 * = HYPGEOM.DIST(1,7,1,22,0)<br>
 * = 1 - (21/22)*(20/21)*(19/20)*(18/19)*(17/18)*(16/17)*(15/16)<br>
 * = 31.81%, 81 repeating of course<br>
 *  <br>
 *  At least 1 of 2 desired Tarot cards pulled: 93024 / 170544<br>
 *  = SUM(COMBIN(21,6), COMBIN(20,6)) / COMBIN(22,7)<br>
 *  = 1 - COMBIN(20,7) / COMBIN(22,7)<br>
 *  = 1 - HYPGEOM.DIST(0,7,2,22,0)<br>
 *  = 54.54%, 54 repeating of course<br>
 *  <br>
 *  Desired card AND (at least 1 of 2 other desired Tarot cards): 27132 / 170544<br>
 *  = COMBIN(19,6) / COMBIN(22,7), = 0.5 * COMBIN(21,6) / COMBIN(22,7)<br>
 *  = 0.5 * SUM(COMBIN(20,6), COMBIN(20,5)) / COMBIN(22,7)<br>
 *  = SUM(2 * COMBIN(20,5), -COMBIN(19,4)) / COMBIN(22,7)<br>
 *  = SUM(HYPGEOM.DIST(1,6,1,20,0) * HYPGEOM.DIST(1,7,2,22,0),
 *  HYPGEOM.DIST(1,5,1,20,0) * HYPGEOM.DIST(2,7,2,22,0))<br>
 *  = 15.90%, 90 repeating of course<br>
 *  <br>
 * At least 1 of 3 desired Tarot cards pulled: 120156 / 170544<br>
 * = SUM(COMBIN(21,6), COMBIN(20,6), COMBIN(19,6)) / COMBIN(22,7)<br>
 * = SUM(2 * COMBIN(21,6), COMBIN(19,5)) / COMBIN(22,7)<br>
 * = SUM(COMBIN(20,6), 3 * COMBIN(19,6)) / COMBIN(22,7)<br>
 * = 1 - COMBIN(19,7) / COMBIN(22,7)<br>
 * = 1 - HYPGEOM.DIST(0,7,3,22,0)<br>
 * = SUM(HYPGEOM.DIST(1,7,3,22,0), HYPGEOM.DIST(2,7,3,22,0), HYPGEOM.DIST(3,7,3,22,0))<br>
 * = 70.45%, 45 repeating of course<br>
 * <br>
 * Desired card AND (at least 1 of 3 other cards): 35700 / 170544<br>
 *  = SUM(COMBIN(19,6), COMBIN(19,5), -COMBIN(18,4)) / COMBIN(22,7)<br>
 *  = SUM(COMBIN(18,6), 2 * COMBIN(18,5)) / COMBIN(22,7)<br>
 *  = SUM(3 * COMBIN(20,5), 3 * -COMBIN(19,4), COMBIN(18, 3)) / COMBIN(22,7)
 * 	= SUM(HYPGEOM.DIST(1,7,3,22,0) * HYPGEOM.DIST(1,6,1,19,0),
 *  HYPGEOM.DIST(2,7,3,22,0) * HYPGEOM.DIST(1,5,1,19,0),
 *  HYPGEOM.DIST(3,7,3,22,0) * HYPGEOM.DIST(1,4,1,19,0))<br>
 *  ≈ 20.93%<br>
 *  <br>
 *  Desired card AND (at least 2 of 3 other cards): 9996 / 170544<br>
 *  = SUM(COMBIN(20,5), -COMBIN(19,4), 2 * -COMBIN(18,3)) / COMBIN(22,7)<br>
 *  = SUM(COMBIN(19,4),  2 * COMBIN(18,4)) / COMBIN(22,7)<br>
 *  = SUM(HYPGEOM.DIST(2,7,3,22,0) * HYPGEOM.DIST(1,5,1,19,0),
 *    HYPGEOM.DIST(3,7,3,22,0) * HYPGEOM.DIST(1,4,1,19,0))<br>
 *  ≈ 5.86%<br>
 *  <br>
 *  2 desired cards: 15504 / 170544<br>
 *  = COMBIN(20,5) / COMBIN(22,7)<br>
 *  = COMBIN(20,15) / COMBIN(22,7)
 *  = HYPGEOM.DIST(2,7,2,22,0)<br>
 *  = 9.09%, 09 repeating of course: 6x less than odds of at least 1 of 2 <br>
 *  <br>
 *  3 desired cards: 3876 / 170544<br>
 *  = COMBIN(19,4) / COMBIN(22,7)<br>
 *  = COMBIN(19,15) / COMBIN(22,7)<br>
 *  = HYPGEOM.DIST(3,7,3,22,0)<br>
 *  = 2.27%, 27 repeating of course<br>
 *  <br>
 *  At least 2 out of 3 desired cards: 38760 / 170544<br>
 *  = SUM(3 * COMBIN(20,5), 2 * -COMBIN(19,4)) / COMBIN(22,7)<br>
 *  = SUM(3 * COMBIN(19,5), COMBIN(19,4)) / COMBIN(22,7)<br>
 *  = SUM(HYPGEOM.DIST(2,7,3,22,0), HYPGEOM.DIST(3,7,3,22,0))<br>
 *  = 22.27%, 27 repeating of course: 10x greater than previous odds
 *  <br>
 *  Tower in first 6 cards and 1 of 5 specific cards as the 7th bonus card: 101745 / 1566873<br>
 *  = COMBIN(21,5) / COMBIN(22/6) * COMBIN(5,1) / COMBIN(21,1) = COMBIN(21,5) / COMBIN(22/6) * (5/21)<br>
 *  = HYPGEOM.DIST(1,6,1,22,0) * HYPGEOM.DIST(1,1,5,21,0)<br>
 *  = 6.49%<br>
 *  <br>
 *  4 desired cards: 816 / 170544<br>
 *  = COMBIN(19,4) / COMBIN(22,7)<br>
 *  = HYPGEOM.DIST(4,7,4,22,0)<br>
 *  ≈ 0.4785%<br> 
 *  <br>
 *  Notice the convergence as hands increase, with diminished returns, toward the true values.<br>
 *  One limitation to this approach is difficulty in noticing Ianuki and Ice Cloud have multiple optimal solutions.<br>
 */
public class CardOddsExample {
	//default 1 million should run in less than 1 second a modern computer, 10 million in less than 2 seconds
	private static int HANDS = 1_000_000;
	
	public static void main(String[] args) {
		if (args.length > 0) {
			int parsedHands = Integer.parseInt(args[0]);
			if (parsedHands > 0)
				HANDS = parsedHands;
		}
		
		//TarotDeck is slower but code is easier to understand
		//TarotDeck d = new TarotDeck();
		TarotDeck d = new TarotDeckArray();
		
		long start = System.nanoTime();
		d.setup(HANDS);
		long end = System.nanoTime();
		System.out.println((double) (end - start) / Util.NANOSECONDS_IN_1_SECOND + " seconds to execute for " + HANDS + " hands" + System.lineSeparator());
		
		System.out.println(TarotDeck.getfoolCount() + ": " + Util.percent(TarotDeck.getfoolCount(), TarotDeck.getTotalHands())
			+ " hands with Fool");
		System.out.println(TarotDeck.getDevilCharotHermitCount() + ": " + Util.percent(TarotDeck.getDevilCharotHermitCount(), TarotDeck.getTotalHands())
			+ " hands with Devil, Chariot and/or Hermit");
		System.out.println(TarotDeck.getValidHands() + ": " + Util.percent(TarotDeck.getValidHands(), TarotDeck.getTotalHands())
		 + " hands with Fool and at least 1 of the 3 damage cards");
		System.out.println(TarotDeck.getInvalidHands() + ": " + Util.percent(TarotDeck.getInvalidHands(),  TarotDeck.getTotalHands())
		 + " hands with Fool and NONE of the 3 damage cards");
	}
}
/*
 * Example output:
 * 
 * 1 million:
 * 318751: 31.8751% hands with Fool
 * 704796: 70.4796% hands with Devil, Chariot and/or Hermit
 * 209556: 20.9556% hands with Devil, Chariot and/or Hermit
 * 
 * 10 million:
 * 3183461: 31.83461% hands with Fool
 * 7044252: 70.44252% hands with Devil, Chariot and/or Hermit
 * 2093905: 20.93905% hands with Fool and at least 1 of the 3 damage cards
 * 
 * 100 million:
 * 31816471: 31.816471% hands with Fool
 * 70454716: 70.454716% hands with Devil, Chariot and/or Hermit
 * 20930914: 20.930914% hands with Fool and at least 1 of the 3 damage cards
 * 
 * 1 billion
 * 318199858: 31.8199858% hands with Fool
 * 704540543: 70.4540543% hands with Devil, Chariot and/or Hermit
 * 209341835: 20.9341835% hands with Fool and at least 1 of the 3 damage cards
 * 
 * Versus exact odds to four decimal places:
 * 31.8181%
 * 70.4545%
 * 20.9330%
 */
