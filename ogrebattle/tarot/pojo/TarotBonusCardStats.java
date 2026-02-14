package ogrebattle.tarot.pojo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ogrebattle.printer.Util;

/**
 * The 7th bonus card does not affect the Lord type but instead alters the Lord / Opinion Leader's starting stats.<br>
 * Each stat's number is in a range from 0 to the number, inclusive.<br>
 * For instance, Priestess modifies starting HP by +0, +1, +2 or +3. Starting STR is modified by +0, -1 or -2.<br>
 * Note that PSX further gives +1 to STR, AGI and INT to every unit leader, including the Lord / Opinion Leader.<br>
 * There is no disputing these values. They can be found by searching any release for "FC F8 FE 08" for the start<br>
 * of the 154 byte (7x22) table, i.e. Magician. Negative values are represented in 2's compliment so that FC = -4.<br>
 * <br>Values generated from <code>DEC.java</code><br>
 */
public enum TarotBonusCardStats {
	//RIP http://www.carbuncle.jp/og5/
	//Values match https://gcgx.games/ogrebattle/making.html
	//HP, STR, AGI, INT, CHA, ALI, LUK
	Magician  (new int[]{-4,-8,-2,8,3,-8,5}),
	Priestess (new int[]{3,-2,1,-3,4,5,-3}),
	Empress   (new int[]{5,2,3,4,7,3,7}),
	
	Emperor   (new int[]{7,5,4,2,7,4,3}),//Carbuncle had wrong {7,5,4,2,7,5,3})
	Hierophant(new int[]{3,-5,-3,4,2,-5,-3}),
	Lovers    (new int[]{4,2,-3,-4,3,7,4}),
	
	Chariot   (new int[]{7,3,7,-5,6,7,-5}), 
	Strength  (new int[]{4,7,3,-7,2,3,-7}),
	Hermit    (new int[]{-4,-2,-3,7,5,7,2}),
	
	Fortune   (new int[]{-4,-5,7,-2,4,6,-1}),
	Justice   (new int[]{2,2,3,4,2,7,1}),
	HangedMan (new int[]{-3,-7,-4,5,6,7,-1}),
	
	Death     (new int[]{-5,-3,-4,-1,2,-7,7}),
	Temperance(new int[]{1,1,1,-1,1,-1,-1}),
	Devil     (new int[]{3,7,1,-5,4,-7,-2}),//zombero had wrong {3,7,1,-1,4,-7,-2}
    
	Tower     (new int[]{-4,3,-7,5,5,-1,-2}),
	Star      (new int[]{2,4,7,1,2,3,7}),
	Moon      (new int[]{-3,-2,-4,5,2,6,7}),
    
	Sun       (new int[]{4,3,-4,5,3,7,1}),
	Judgment  (new int[]{-1,-2,-3,-1,7,2,-1}),
	Fool      (new int[]{-4,-3,-2,-4,3,-3,-2}),//Carbuncle had wrong {4,-3,-2,-4,3,-3,-2}
	World     (new int[]{-4,7,-7,7,1,-7,-7});

	public static List<String> STATS = Collections.unmodifiableList(
			Arrays.asList("HP", "STR", "AGI", "INT", "CHA", "ALI", "LUK"));
	
	private int[] values = new int[7];
	
	TarotBonusCardStats(int[] values) {
        this.values = values;
    }
    
    public int[] getValues() {
    	return values;
    }
    
    public static int[] getValues(TarotBonusCardStats tarot) {
    	return tarot.values;
    }
    
    public static int[] getValues(Tarot tarot) {
    	return TarotBonusCardStats.valueOf(tarot.toString()).getValues();
    }

	public static void main(String[] args) {
//      Example with output showing with and without the + sign on positive values
//		Util.printArray(Priestess.getValues());
//		Util.printTarotBonusCardStats(Tarot.Priestess);//use enum from Tarot class
//		Util.printTarotBonusCardStatsPlus(World);//use enum from this class
/*		[3,-2,1,-3,4,5,-3]
				Priestess
				HP:   3
				STR: -2
				AGI:  1
				INT: -3
				CHA:  4
				ALI:  5
				LUK: -3

				World
				HP:  -4
				STR: +7
				AGI: -7
				INT: +7
				CHA: +1
				ALI: -7
				LUK: -7
*/				
		Util.printAllTarotBonusCardStatsPlus();
	}
}
/*
Magician
HP : -4
STR: -8
AGI: -2
INT: +8
CHA: +3
ALI: -8
LUK: +5

Priestess
HP : +3
STR: -2
AGI: +1
INT: -3
CHA: +4
ALI: +5
LUK: -3

Empress
HP : +5
STR: +2
AGI: +3
INT: +4
CHA: +7
ALI: +3
LUK: +7

Emperor
HP : +7
STR: +5
AGI: +4
INT: +2
CHA: +7
ALI: +4
LUK: +3

Hierophant
HP : +3
STR: -5
AGI: -3
INT: +4
CHA: +2
ALI: -5
LUK: -3

Lovers
HP : +4
STR: +2
AGI: -3
INT: -4
CHA: +3
ALI: +7
LUK: +4

Chariot
HP : +7
STR: +3
AGI: +7
INT: -5
CHA: +6
ALI: +7
LUK: -5

Strength
HP : +4
STR: +7
AGI: +3
INT: -7
CHA: +2
ALI: +3
LUK: -7

Hermit
HP : -4
STR: -2
AGI: -3
INT: +7
CHA: +5
ALI: +7
LUK: +2

Fortune
HP : -4
STR: -5
AGI: +7
INT: -2
CHA: +4
ALI: +6
LUK: -1

Justice
HP : +2
STR: +2
AGI: +3
INT: +4
CHA: +2
ALI: +7
LUK: +1

HangedMan
HP : -3
STR: -7
AGI: -4
INT: +5
CHA: +6
ALI: +7
LUK: -1

Death
HP : -5
STR: -3
AGI: -4
INT: -1
CHA: +2
ALI: -7
LUK: +7

Temperance
HP : +1
STR: +1
AGI: +1
INT: -1
CHA: +1
ALI: -1
LUK: -1

Devil
HP : +3
STR: +7
AGI: +1
INT: -5
CHA: +4
ALI: -7
LUK: -2

Tower
HP : -4
STR: +3
AGI: -7
INT: +5
CHA: +5
ALI: -1
LUK: -2

Star
HP : +2
STR: +4
AGI: +7
INT: +1
CHA: +2
ALI: +3
LUK: +7

Moon
HP : -3
STR: -2
AGI: -4
INT: +5
CHA: +2
ALI: +6
LUK: +7

Sun
HP : +4
STR: +3
AGI: -4
INT: +5
CHA: +3
ALI: +7
LUK: +1

Judgment
HP : -1
STR: -2
AGI: -3
INT: -1
CHA: +7
ALI: +2
LUK: -1

Fool
HP : -4
STR: -3
AGI: -2
INT: -4
CHA: +3
ALI: -3
LUK: -2

World
HP : -4
STR: +7
AGI: -7
INT: +7
CHA: +1
ALI: -7
LUK: -7
*/
