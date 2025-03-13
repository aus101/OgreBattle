package ogrebattle.tarot.exact;

import java.util.Arrays;

import ogrebattle.printer.Util;
import ogrebattle.tarot.pojo.Tarot;

/**
 * Exact odds of hand combinations using BigDecimal for division to prevent floating point error.<br>
 * As these odds are exact, they are deterministic.<br>
 * Compare to simulated odds in <code>ogrebattle.tarot.simulate.CardOddsExample.java<code>.<br>
 */
public class CardOddsExample {
	private static int PRECISION_PRINT = 4;
	private static AllPossibleHands sevenCards = new AllPossibleHands(7, true);//do not sort hands so runs faster
	private static AllPossibleHands sixCards = new AllPossibleHands(6, true);//do not sort hands so runs faster
	
	//all static not desirable but no real reason to change
	public static void main(String[] args) {
		System.out.println("Random hand of 6 with game's Tarot ordering:");
		Util.printRandomHand(sixCards);
		System.out.println();
		//repeated to show randomness versus same hand or iterated hand, unless 1 in 38760 of same hand twice
		System.out.println("Another random hand of 6 with game's Tarot ordering:");
		Util.printRandomHand(sixCards);
		System.out.println();
		System.out.println("Odds of 1 specific card in opening hand of 7:");
		Util.percentPrint(sevenCards.countContains(Tarot.Fool), sevenCards.combinations(), PRECISION_PRINT);
		System.out.println();
		
		System.out.println("Odds of at least 1 of 3 specific cards in opening hand of 7:");
		Util.percentPrint(sevenCards.countContainsAny(Tarot.Devil, Tarot.Chariot, Tarot.Hermit),
				sevenCards.combinations(), PRECISION_PRINT);
		System.out.println();
		
		System.out.println("Odds of Fool and at least 1 of 3 other specific cards in opening hand of 7:");
		Util.percentPrint(sevenCards.countContainsAndContainsAny(Tarot.Fool,
				Tarot.Devil, Tarot.Chariot, Tarot.Hermit), sevenCards.combinations(), PRECISION_PRINT);
		System.out.println();
		
		System.out.println("Odds of 2 out of 3 or all 3 specific cards in opening hand of 7:");
		Util.percentPrint(sevenCards.countContainsAndContainsAny(Tarot.Devil,
				Tarot.Chariot, Tarot.Hermit), sevenCards.combinations(), PRECISION_PRINT);
		System.out.println();
		
		System.out.println("Odds of 2 out of 2 specific cards in opening hand of 7:");
		Util.percentPrint(sevenCards.countContainsAll(Tarot.Devil, Tarot.Chariot), sevenCards.combinations(), PRECISION_PRINT);
		System.out.println();
		
		System.out.println("Odds of at least 2 out of 3 specific cards in opening hand of 7:");
		Util.percentPrint(sevenCards.countContainsAtLeastXOutOfThese(2, Tarot.Devil, Tarot.Chariot, Tarot.Hermit),
				sevenCards.combinations(), PRECISION_PRINT);
		System.out.println();
		
		System.out.println("Odds of containing one specific card and at least 2 out of 3 other specific cards in opening hand of 7:");
		Util.percentPrint(sevenCards.countContainsAndContainsAtLeastXOutOfThese(Tarot.Fool, 2,
				Arrays.asList(Tarot.Devil, Tarot.Chariot, Tarot.Hermit)), sevenCards.combinations(), PRECISION_PRINT);
		
		System.out.println();
		System.out.println("Odds of containing one specific card NONE of 3 other specific cards in opening hand of 7:");
		Util.percentPrint(sevenCards.countContainsAndContainsAtLeastXOutOfThese(Tarot.Fool, 0,
				Arrays.asList(Tarot.Devil, Tarot.Chariot, Tarot.Hermit)), sevenCards.combinations(), PRECISION_PRINT);
		
		System.out.println();
		System.out.println("Odds of containing one specific card NONE of 4 other specific cards in opening hand of 7:");
		Util.percentPrint(sevenCards.countContainsAndContainsAtLeastXOutOfThese(Tarot.Fool, 0,
				Arrays.asList(Tarot.Devil, Tarot.Chariot, Tarot.Hermit, Tarot.Tower)), sevenCards.combinations(), PRECISION_PRINT);
	}
}
/*
Random hand of 6 with game's Tarot ordering:
[Empress, Hierophant, Tower, Sun, Fool, World]

Another random hand of 6 with game's Tarot ordering:
[Magician, Priestess, Empress, Emperor, Lovers, Temperance]

Odds of 1 specific card in opening hand of 7:
54264 / 170544
31.8182%

Odds of at least 1 of 3 specific cards in opening hand of 7:
120156 / 170544
70.4545%

Odds of Fool and at least 1 of 3 other specific cards in opening hand of 7:
35700 / 170544
20.9330%

Odds of 2 out of 3 or all 3 specific cards in opening hand of 7:
27132 / 170544
15.9091%

Odds of 2 out of 2 specific cards in opening hand of 7:
15504 / 170544
9.0909%

Odds of at least 2 out of 3 specific cards in opening hand of 7:
38760 / 170544
22.7273%

Odds of containing one specific card and at least 2 out of 3 other specific cards in opening hand of 7:
9996 / 170544
5.8612%
*/
