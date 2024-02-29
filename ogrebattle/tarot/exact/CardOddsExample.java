package ogrebattle.tarot.exact;

import java.util.Arrays;

import ogrebattle.printer.Util;
import ogrebattle.tarot.pojo.Tarot;

/**
 * Exact odds of hand combinations using BigDecimal to prevent floating point error.<br>
 * Compare to simulated odds in <code>ogrebattle.tarot.simulate.OddsExample.java<code>.<br>
 */
public class CardOddsExample {
	private static final int COMBIN = 170544;//COMBIN(22,7)
	private AllPossibleHands sevenCards = new AllPossibleHands(7, true);//do not sort hands so runs faster
	private AllPossibleHands sixCards = new AllPossibleHands(6, true);//do not sort hands so runs faster
	
	public static void main(String[] args) {
		CardOddsExample e = new CardOddsExample();
		
		e.printRandomHandOf6();
		//repeated to show randomness versus same hand or iterated hand, unless 1 in 38760 of same hand twice
		e.printRandomHandOf6();
		
		System.out.println("Odds of 1 specific card in opening hand of 7:");
		Util.percentPrint(e.sevenCards.countContains(Tarot.Fool), COMBIN, 4);
		System.out.println();
		
		System.out.println("Odds of at least 1 of 3 specific cards in opening hand of 7:");
		Util.percentPrint(e.sevenCards.countContainsAny(Tarot.Devil, Tarot.Chariot, Tarot.Hermit), COMBIN, 4);
		System.out.println();
		
		System.out.println("Odds of Fool and at least 1 of 3 other specific cards in opening hand of 7:");
		Util.percentPrint(e.sevenCards.countContainsAndContainsAny(Tarot.Fool,
				Tarot.Devil, Tarot.Chariot, Tarot.Hermit), COMBIN, 4);
		System.out.println();
		
		System.out.println("2 out of 3 or all 3 specific cards in opening hand of 7:");
		Util.percentPrint(e.sevenCards.countContainsAndContainsAny(Tarot.Devil,
				Tarot.Chariot, Tarot.Hermit), COMBIN, 4);
		System.out.println();
		
		System.out.println("2 out of 2 specific cards in opening hand of 7:");
		Util.percentPrint(e.sevenCards.countContainsAll(Tarot.Devil, Tarot.Chariot), COMBIN, 4);
		System.out.println();
		
		System.out.println("at least 2 out of 3 specific cards in opening hand of 7:");
		Util.percentPrint(e.sevenCards.countContainsAtLeastXOutOfThese(2, Tarot.Devil, Tarot.Chariot, Tarot.Hermit), COMBIN, 4);
		System.out.println();
		
		System.out.println("contains one specific card and at least 2 out of 3 other specific cards in opening hand of 7:");
		Util.percentPrint(e.sevenCards.countContainsAndContainsAtLeastXOutOfThese(Tarot.Fool, 2, Arrays.asList(Tarot.Devil, Tarot.Chariot, Tarot.Hermit)), COMBIN, 4);
	}
	
	/**
	 * Ordered by normal Tarot card ordering so must randomize order in deep copy if dealing as individual cards
	 */
	public void printRandomHandOf6() {
		System.out.println(sixCards.returnRandomHandInList());
	}
	
	/**
	 * Ordered by normal Tarot card ordering so must randomize order in deep copy if dealing as individual cards
	 */
	public void printRandomHandsOf6(int hands) {
		for(int i=0; i<hands; i++)
			System.out.println(sixCards.returnRandomHandInList());
	}
	
	/**
	 * Ordered by normal Tarot card ordering so must randomize order in deep copy if dealing as individual cards
	 */
	public void printRandomHandOf7() {
		System.out.println(sevenCards.returnRandomHandInList());
	}
	
	/**
	 * Ordered by normal Tarot card ordering so must randomize order in deep copy if dealing as individual cards
	 */
	public void printRandomHandsOf7(int hands) {
		for(int i=0; i<hands; i++)
			System.out.println(sevenCards.returnRandomHandInList());
	}
}
/*
[Priestess, Lovers, Hermit, Fortune, Devil, Star]
[Magician, Justice, Devil, Star, Moon, Fool]
Odds of 1 specific card in opening hand of 7:
54264 / 170544
31.8182%

Odds of at least 1 of 3 specific cards in opening hand of 7:
120156 / 170544
70.4545%

Odds of Fool and at least 1 of 3 other specific cards in opening hand of 7:
35700 / 170544
20.9330%

2 out of 3 or all 3 specific cards in opening hand of 7:
27132 / 170544
15.9091%

2 out of 2 specific cards in opening hand of 7:
15504 / 170544
9.0909%

at least 2 out of 3 specific cards in opening hand of 7:
38760 / 170544
22.7273%

contains one specific card and at least 2 out of 3 other specific cards in opening hand of 7:
9996 / 170544
5.8612%
*/
