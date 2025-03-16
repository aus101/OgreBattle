package ogrebattle.joker;

import java.security.SecureRandom;
import java.util.Random;

import ogrebattle.printer.Util;

public class Joker {
	protected final int CARDCOUNT;
	protected int countEarlyExit = 0;
	protected int[] tracker = new int[11];//hold counts for 0 to 9 matches each iteration and 10+ on last index
	public static final int LOOPS = 1_000_000;//iterations
	public static final int STOP = 6;//early exit on this many matches if calling iterate(int stop)
	private static final int PRECSISION = 4;//decimal places for results percentages
	//SecureRandom's nextFloat() is twice as fast as nextDouble()
	public static final float LOWER_LIMIT = (1/21f);//1 in 21 cards, duplicates not possible
	public static final float UPPER_LIMIT = (1/22f);//1 in 22 cards, for first draw only
	
	private static final Random r = new SecureRandom();//SecureRandom is many times slower but not glaringly biased or predictable
	
	public Joker() {
		CARDCOUNT = 60;//number of cards to draw and count matches for 1 cycle
	}
	
	public Joker(int cardCount) {
		CARDCOUNT = cardCount;
	}
	
	public static void main(String[] args) {
		Joker j = new Joker();
		int tally = 0;
		int totalCards;

		long start = System.nanoTime();
		for (int i=0; i<LOOPS; i++) {
			tally += j.iterateFaster();//Start drawing cards and return the matches
			//tally += j.iterate(STOP);//Stop drawing each loop if matches equal STOP
		}
		long end = System.nanoTime();
		if (j.getCountEarlyExit() > 0) {
			totalCards = j.getCountEarlyExit();
		} else {
			totalCards = LOOPS * j.getCardCount();
		}
		System.out.println((double) (end - start) / Util.NANOSECONDS_IN_1_SECOND + " seconds to execute for "
				+ LOOPS + " iterations at " + j.getCardCount() + " cards apiece");
		
		System.out.println(tally + " matches out of " + totalCards + " total cards");
		Util.percentPrintAlt(tally, totalCards, PRECSISION);
		System.out.println();
		j.printTrackerPercent();
	}
	
	/**
	 * Pull cards <code>cardCount</code> number of times<br>
	 * @param STOP Stop at this many matches found to only draw the minimum cards necessary<br>
	 * @return The number of matches<br>
	 */
	public int iterate(int STOP) {
		int tally = 0;
		float temp = 0;
		boolean lastFound = false;
		
		if (r.nextFloat() < UPPER_LIMIT) {//All 22 cards are possible on first draw
			tally++;
			if (STOP == 1) {
				//System.out.println("Found on first card");
				countEarlyExit++;
				return tally;
			}
			lastFound = true;//cannot match next pull
		}
		
		countEarlyExit += CARDCOUNT;//avoid incrementing
		for (int i=2; i<=CARDCOUNT; i++) {//start on second card, i=2 to simplify early exit code
			if (!lastFound) {
				temp = LOWER_LIMIT;
			} else {
				temp = 0;//cannot find a match this time since duplicates are impossible
				lastFound = false;//reset
			}
			if (r.nextFloat() < temp) {
				tally++;
				lastFound = true;
				if (tally == STOP) {
					System.out.println("Found early with " + i + " cards");
					countEarlyExit -= (CARDCOUNT - i);//subtract out cards not drawn
					break;//found the limit, early exit to next iteration
				}
			}
		}
		iterateTracker(tally);
		return tally;
	}
	
	/**
	 * Convenience method that calls <code>iterate(int STOP)</code> with -1 to avoid early exit<br>
	 * @return The number of matches<br>
	 */
	public int iterate() {
		return iterate(-1);
	}
	
	/**
	 * Pull cards <code>cardCount</code> number of times<br> with no early exit
	 * A modest 5.5% speed increase over <code>iterate</code> with same card count attained by:<br>
	 * No code checking for early exit possibility<br>
	 * No calling the RNG for the card after a match guaranteed not to be a duplicate<br>
	 * No temporary variable to hold the rate of finding a match<br>
	 * @return The number of matches<br>
	 */
	public int iterateFaster() {
		int tally = 0;
		boolean lastFound = true;
		
		if (r.nextFloat() < UPPER_LIMIT) {//All 22 cards are possible on first draw
			tally++;
		}
		for (int i=1; i<CARDCOUNT; i++) {//on second card: int i=0; i<CARDCOUNT-1; and int i=2; i<=CARDCOUNT; also work
			if (!lastFound) {
				if (r.nextFloat() < LOWER_LIMIT) {
					tally++;
					lastFound = true;//cannot match next pull
				}
			} else {//do not really draw the card guaranteed not to be a match to save time
				lastFound = false;//this way is not deterministic with iterate() due to not advancing the RNG
			}
		}
		//not trying to avoid overhead of a single call
		iterateTracker(tally);
		return tally;
	}
	
	private void iterateTracker(int tally) {
		if (tally <= 9) {
			tracker[tally]++;
		} else {//10 or more are shared on the last index
			tracker[10]++;
		}
	}
	
	public void printTracker() {
		for (int i=0; i<tracker.length-1; i++) {
			System.out.println(i + " found: " + tracker[i]);
		}
		System.out.println("10 or more found: " + tracker[10]);
	}
	
	public void printTrackerPercent() {
		for (int i=0; i<tracker.length-1; i++) {
			System.out.println(i + " found: " + Util.percentAlt(tracker[i], LOOPS, PRECSISION)+System.lineSeparator());
		}
		System.out.println("10 or more found: " + Util.percentAlt(tracker[10], LOOPS, PRECSISION));//last index
	}
	
	public int getCardCount() {
		return CARDCOUNT;
	}
	
	public int getCountEarlyExit() {
		return countEarlyExit;
	}
}
/*
48.995959 seconds to execute for 1000000 iterations at 60 cards apiece
2726742 matches out of 60000000 total cards
4.5446% (2726742)

0 found: 5.3709% (53709)

1 found: 16.8927% (168927)

2 found: 25.1984% (251984)

3 found: 23.8782% (238782)

4 found: 15.9510% (159510)

5 found: 8.1049% (81049)

6 found: 3.2340% (32340)

7 found: 1.0311% (10311)

8 found: 0.2655% (2655)

9 found: 0.0590% (590)

10 or more found: 0.0143% (143)

273068 out of 6000000
4.5511333333333335%
100000 iterations at 60 cards apiece

0 found: 5319
1 found: 16978
2 found: 25140
3 found: 23743
4 found: 16017
5 found: 8130
6 found: 3274
7 found: 1055
8 found: 277
9 found: 55
10 or more found: 12

271956 out of 6000000
4.5325999999999995%
100000 iterations at 60 cards apiece

0 found: 5407
1 found: 16845
2 found: 25501
3 found: 23806
4 found: 15984
5 found: 7859
6 found: 3179
7 found: 1056
8 found: 290
9 found: 58
10 or more found: 15
 */
