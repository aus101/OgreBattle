package ogrebattle.joker;

import java.security.SecureRandom;
import java.util.Random;

import ogrebattle.printer.Util;

public class Joker {
	protected final int CARDCOUNT;
	protected int countEarlyExit = 0;
	protected int[] tracker = new int[11];//hold counts for 0 to 9 matches each iteration and 10+ on last index
	protected static final int TEN_OR_MORE = 10;//tracker index
	public static final int LOOPS = 100_000;//iterations
	public static final int STOP = 6;//early exit on this many matches if calling iterate(int stop)
	//SecureRandom's nextFloat() is twice as fast as nextDouble()
	public static final float LOWER_LIMIT = (1/21f);//1 in 21 cards, duplicates not possible
	public static final float UPPER_LIMIT = (1/22f);//1 in 22 cards, for first draw only
	
	private static final Random r = new SecureRandom();//SecureRandom is many times slower but not glaringly biased or predictable

	public Joker() {
		CARDCOUNT = 60;//default to 60 cards to draw and count matches for 1 cycle
	}
	
	public Joker(int cardCount) {
		CARDCOUNT = cardCount;//number of cards to draw and count matches for 1 cycle
	}
	
	public static void main(String[] args) {
		Joker j = new Joker();
		int tally = 0;
		long totalCards;

		long start = System.nanoTime();
		for (int i=0; i<LOOPS; i++) {
			tally += j.iterateFaster();//Start drawing cards and return the matches
		    //tally += j.iterate(STOP);//Stop drawing each loop if matches equal STOP
		}
		long end = System.nanoTime();
		if (j.getCountEarlyExit() > 0) {
			totalCards = j.getCountEarlyExit();
		} else {
			totalCards = (long) LOOPS * j.getCardCount();
			//totalCards = new BigDecimal(LOOPS).multiply(new BigDecimal(j.getCardCount())).longValueExact();
		}
		System.out.println(Util.nanoToMinutesSeconds(end - start) + " to execute for " + Util.numberSeparator(LOOPS) + " iterations at "
				+ Util.numberSeparator(j.getCardCount()) + " cards apiece = " + Util.numberSeparator(totalCards) + " total cards");
		String s;
		if (tally != 1) {
			s = " matches or 1 for every ";
		} else {
			s = " match or 1 for every ";
		} 
		System.out.println(Util.percentAlt(tally, totalCards, Util.PRECISION_PRINT, s) + " Joker/Liberation pulls");
		System.out.println(System.lineSeparator() + "Odds of the expected number of matches for iterations of "
				+ Util.numberSeparator(j.getCardCount()) + " cards:" + System.lineSeparator());
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
			if (STOP == 1) {
				//System.out.println("Found on first card");
				countEarlyExit++;
				iterateTracker(1);
				return 1;//can consolidate for only 1 point of return but harder to understand
			}
			tally++;//no need to use if exiting at 1 match
			lastFound = true;//cannot match next pull
		}
		
		countEarlyExit += CARDCOUNT;//avoid incrementing
		for (int i=2; i<=CARDCOUNT; i++) {//start on second card, i=2 to simplify early exit code
			if (!lastFound) {
				temp = LOWER_LIMIT;
			} else {
				temp = -1;//cannot find a match this time since duplicates are impossible
				lastFound = false;//reset
			}
			if (r.nextFloat() < temp) {
				tally++;
				lastFound = true;
				if (tally == STOP) {
					//System.out.println("Found early with " + i + " cards");
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
	 * A modest 4.4% faster over <code>iterate</code> with same card count attained by:<br>
	 * No code checking for early exit possibility<br>
	 * No calling the RNG for the card after a match guaranteed not to be a duplicate<br>
	 * No temporary variable to hold the rate of finding a match<br>
	 * @return The number of matches<br>
	 */
	public int iterateFaster() {
		int tally = 0;
		boolean lastFound = false;
		
		if (r.nextFloat() < UPPER_LIMIT) {//All 22 cards are possible on first draw
			tally++;
			lastFound = true;
		}
		//loop unrolling for 4 iterates per loop did not run faster
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
		//not a virtual method, not did not run faster with the code in-lined
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
			System.out.println(new StringBuilder().append(i).append(" found: ")
					.append(Util.percentAlt(tracker[i], LOOPS, Util.PRECISION_PRINT)).append(" iterations"));
		}
		StringBuilder s10 = new StringBuilder("10 or more found: ").append(Util.percentAlt(tracker[10], LOOPS, Util.PRECISION_PRINT));//last index
		if (tracker[TEN_OR_MORE] > 0) {
			s10.append(" iterations");
		}
		System.out.println(s10.toString());
	}
	
	public int getCardCount() {
		return CARDCOUNT;
	}
	
	public int getCountEarlyExit() {
		return countEarlyExit;
	}
}
/*
times with low tier i5 CPU on Windows 10 with Java 15
-
less than 1 second to execute for 1,000 iterations at 60 cards apiece = 60,000 total cards
4.6767% (2806) matches or 1 for every 21.4 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 60 cards:

0 found: 4.4% (44) or 1 in 22.7 iterations
1 found: 16.7% (167) or 1 in 6 iterations
2 found: 23.9% (239) or 1 in 4.2 iterations
3 found: 23.5% (235) or 1 in 4.3 iterations
4 found: 17.6% (176) or 1 in 5.7 iterations
5 found: 9.8% (98) or 1 in 10.2 iterations
6 found: 2.9% (29) or 1 in 34.5 iterations
7 found: 1% (10) or 1 in 100 iterations
8 found: 0.1% (1) or 1 in 1000 iterations
9 found: 0 iterations
10 or more found: 0.1% (1) or 1 in 1000 iterations
-
4 seconds to execute for 100,000 iterations at 60 cards apiece = 6,000,000 total cards
4.5415% (272492) matches or 1 for every 22 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 60 cards:

0 found: 5.45% (5450) or 1 in 18.3 iterations
1 found: 16.767% (16767) or 1 in 6 iterations
2 found: 25.312% (25312) or 1 in 4 iterations
3 found: 23.799% (23799) or 1 in 4.2 iterations
4 found: 15.951% (15951) or 1 in 6.3 iterations
5 found: 8.161% (8161) or 1 in 12.3 iterations
6 found: 3.241% (3241) or 1 in 30.9 iterations
7 found: 1.004% (1004) or 1 in 99.6 iterations
8 found: 0.232% (232) or 1 in 431 iterations
9 found: 0.069% (69) or 1 in 1449.3 iterations
10 or more found: 0.014% (14) or 1 in 7142.9 iterations
-
4 seconds to execute for 100,000 iterations at 60 cards apiece = 6,000,000 total cards
4.5363% (272178) matches or 1 for every 22 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 60 cards:

0 found: 5.489% (5489) or 1 in 18.2 iterations
1 found: 16.775% (16775) or 1 in 6 iterations
2 found: 25.302% (25302) or 1 in 4 iterations
3 found: 23.842% (23842) or 1 in 4.2 iterations
4 found: 15.907% (15907) or 1 in 6.3 iterations
5 found: 8.198% (8198) or 1 in 12.2 iterations
6 found: 3.171% (3171) or 1 in 31.5 iterations
7 found: 0.978% (978) or 1 in 102.2 iterations
8 found: 0.273% (273) or 1 in 366.3 iterations
9 found: 0.052% (52) or 1 in 1923.1 iterations
10 or more found: 0.013% (13) or 1 in 7692.3 iterations
-
47 seconds to execute for 1,000,000 iterations at 60 cards apiece = 60,000,000 total cards
4.5407% (2724435) matches or 1 for every 22 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 60 cards:

0 found: 5.3638% (53638) or 1 in 18.6 iterations
1 found: 16.9084% (169084) or 1 in 5.9 iterations
2 found: 25.2495% (252495) or 1 in 4 iterations
3 found: 23.8755% (238755) or 1 in 4.2 iterations
4 found: 15.9483% (159483) or 1 in 6.3 iterations
5 found: 8.0939% (80939) or 1 in 12.4 iterations
6 found: 3.1981% (31981) or 1 in 31.3 iterations
7 found: 1.0239% (10239) or 1 in 97.7 iterations
8 found: 0.2708% (2708) or 1 in 369.3 iterations
9 found: 0.0556% (556) or 1 in 1798.6 iterations
10 or more found: 0.0122% (122) or 1 in 8196.7 iterations
-
7 minutes, 59 seconds to execute for 10,000,000 iterations at 60 cards apiece = 600,000,000 total cards
4.5459% (27275524) matches or 1 for every 22 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 60 cards:

0 found: 5.3664% (536635) or 1 in 18.6 iterations
1 found: 16.8659% (1686594) or 1 in 5.9 iterations
2 found: 25.2111% (2521108) or 1 in 4 iterations
3 found: 23.8493% (2384933) or 1 in 4.2 iterations
4 found: 16.0025% (1600247) or 1 in 6.2 iterations
5 found: 8.1131% (811313) or 1 in 12.3 iterations
6 found: 3.2215% (322146) or 1 in 31 iterations
7 found: 1.032% (103199) or 1 in 96.9 iterations
8 found: 0.2675% (26748) or 1 in 373.9 iterations
9 found: 0.0586% (5855) or 1 in 1707.9 iterations
10 or more found: 0.0122% (1222) or 1 in 8183.3 iterations
-
78 minutes, 51 seconds to execute for 100,000,000 iterations at 60 cards apiece = 6,000,000,000 total cards
4.5455% (272728032) matches or 1 for every 22 Joker/Liberation pulls

Odds of the expected number of matches for iterations of 60 cards:

0 found: 5.3665% (5366472) or 1 in 18.6 iterations
1 found: 16.8731% (16873106) or 1 in 5.9 iterations
2 found: 25.2233% (25223253) or 1 in 4 iterations
3 found: 23.8373% (23837324) or 1 in 4.2 iterations
4 found: 15.9986% (15998574) or 1 in 6.3 iterations
5 found: 8.1085% (8108499) or 1 in 12.3 iterations
6 found: 3.2187% (3218688) or 1 in 31.1 iterations
7 found: 1.0317% (1031650) or 1 in 96.9 iterations
8 found: 0.2708% (270833) or 1 in 369.2 iterations
9 found: 0.0589% (58862) or 1 in 1698.9 iterations
10 or more found: 0.0127% (12739) or 1 in 7849.9 iterations

 */
