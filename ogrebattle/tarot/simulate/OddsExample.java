package ogrebattle.tarot.simulate;

/**
 * This random hands approach is inferior to the <code>ogrebattle.tarot.exact</code> approach that finds
 * exact odds to back up the calculations given below that the simulation here merely converges to.<br>
 * @param args the number of hands to draw, default to 1 million if no args passed<br>
 * <br>
 * EXACT ODDS:<br>
 * <br>
 * Desired Tarot pulled: 54264 / 170544 = COMBIN(21,6) / COMBIN(22,7)<br>
 * = HYPGEOM.DIST(1,7,1,22,0)<br>
 * = 1 - (21/22)*(20/21)*(19/20)*(18/19)*(17/18)*(16/17)*(15/16)<br>
 * = 31.81%, 81 repeating of course<br>
 * <br>
 * At least 1 of 3 desired Tarot pulled: 120156 / 170544 = SUM(COMBIN(21,6), COMBIN(20,6), COMBIN(19,6)) / COMBIN(22,7)<br>
 * = 1 - COMBIN(19,7) / COMBIN(22,7)<br>
 * = 1 - HYPGEOM.DIST(0,7,3,22,0)<br>
 * = SUM(HYPGEOM.DIST(1,7,3,22,0), HYPGEOM.DIST(2,7,3,22,0), HYPGEOM.DIST(3,7,3,22,0))<br>
 * = 70.45%, 45 repeating of course<br>
 * <br>
 * Fool AND (at least 1 of 3 other cards) from the same deck: 35700 / 170544, rough with COMBIN<br>
 * 	= SUM(HYPGEOM.DIST(1,7,3,22,0) * HYPGEOM.DIST(1,6,1,19,0),<br>
 *  HYPGEOM.DIST(2,7,3,22,0) * HYPGEOM.DIST(1,5,1,19,0),<br>
 *  HYPGEOM.DIST(3,7,3,22,0) * HYPGEOM.DIST(1,4,1,19,0))<br>
 *  = 20.93301435406699% to the spreadsheet's precision limit<br>
 *  <br>
 *  At least 1 of 2 desired Tarot pulled: 15504 / 170544 = SUM(COMBIN(21,6), COMBIN(20,6)) / COMBIN(22,7)<br>
 *  = 1 - COMBIN(20,7) / COMBIN(22,7)<br>
 *  = 1 - HYPGEOM.DIST(0,7,2,22,0)<br>
 *  = 54.54%, 54 repeating of course<br>
 *  <br>
 *  Fool AND (at least 1 of 2 other cards) from the same deck: 27132 / 170544, rough with COMBIN<br>
 *  = HYPGEOM.DIST(1,7,2,22,0) * HYPGEOM.DIST(1,6,1,20,0),<br>
 *  HYPGEOM.DIST(2,7,2,22,0) * HYPGEOM.DIST(1,5,1,20,0),<br>
 *  = 15.90%, 90 repeating of course<br>
 *  <br>
 *  Fool + Devil: 15504 / 170544 = COMBIN(20,5) / COMBIN(22,7)<br>
 *  = HYPGEOM.DIST(2,7,2,22,0)<br>
 *  = 9.09%, 09 repeating of course<br>
 *  <br>
 *  Notice the convergence as hands increase, with diminished returns, toward the true values.<br>
 *  One limitation to this approach is difficulty in noticing Ianuki and Ice Cloud have multiple optimal solutions.<br>
 */
public class OddsExample {
	//default 1 million should run in less than 1 second a modern computer, 10 million in less than 2 seconds
	private static int HANDS = 1_000_000;
	private final static int NANOSECONDS_IN_1_SECOND = 1_000_000_000;
	
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
		System.out.println((double)(end - start)/NANOSECONDS_IN_1_SECOND  + " seconds to execute for " + HANDS + " hands" + System.lineSeparator());
		
		System.out.println(TarotDeck.getfoolCount() + ": " + printPercent(TarotDeck.getfoolCount())
			+ " hands with Fool");
		System.out.println(TarotDeck.getDevilCharotHermitCount() + ": " + printPercent(TarotDeck.getDevilCharotHermitCount())
			+ " hands with Devil, Chariot and/or Hermit");
		System.out.println(TarotDeck.getValidHands() + ": " + printPercent(TarotDeck.getValidHands())
		 + " hands with Fool and at least 1 of the 3 damage cards");
	}
	
	public static String printPercent(int successes) {
		return String.valueOf((double) 100 * successes / TarotDeck.getTotalHands()) + '%';
	}
}
/*
 * Example output:
 * 
 * 1 million:
 * 318751: 31.8751%
 * 704796: 70.4796%
 * 209556: 20.9556%
 * 
 * 10 million:
 * 3183461: 31.83461%
 * 7044252: 70.44252%
 * 2093905: 20.93905%
 * 
 * 100 million:
 * 31816471: 31.816471%
 * 70454716: 70.454716%
 * 20930914: 20.930914%
 * 
 * 1 billion
 * 318199858: 31.8199858%
 * 704540543: 70.4540543%
 * 209341835: 20.9341835%
 * 
 * versus exact odds
 * 31.81%
 * 70.45%
 * 20.93301435406699% to precision limit
 */
