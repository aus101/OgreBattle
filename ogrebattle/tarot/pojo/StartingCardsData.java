package ogrebattle.tarot.pojo;

import ogrebattle.util.Printer;

/**
 * Programmatically generate the bonus card stat changes using the array to prevent human error
 */
public class StartingCardsData {
	
	protected final static String BONUS_CARD_STATS     = "FC F8 FE 08 03 F8 05 03 FE 01 FD 04 05 FD 05 02 03 04 07 03 07 07 05 04 02 07 04 03 03 FB FD 04 02 FB FD 04 02 FD FC 03 07 04 07 03 07 FB 06 07 FB 04 07 03 F9 02 03 F9 FC FE FD 07 05 07 02 FC FB 07 FE 04 06 FF 02 02 03 04 02 07 01 FD F9 FC 05 06 07 FF FB FD FC FF 02 F9 07 01 01 01 FF 01 FF FF 03 07 01 FB 04 F9 FE FC 03 F9 05 05 FF FE 02 04 07 01 02 03 07 FD FE FC 05 02 06 07 04 03 FC 05 03 07 01 FF FE FD FF 07 02 FF FC FD FE FC 03 FD FE FC 07 F9 07 01 F9 F9";
	protected final static String WARREN_QUESTIONS     = "24 14 32 15 15 22 51 52 31 42 21 51 32 14 41 41 14 35 54 24 31 45 42 51 32 52 53 14 41 43 12 42 52 53 45 14 25 14 31 53 54 25 31 51 53 12 45 45 35 14 41 33 23 41 51 21 15 24 24 53 14 42 41 25 23 14 15 13 42 24 33 44 12 52 13 24 54 14 51 51 15 42 33 25 45 15 51 23 12 51 35 31 44 25 12 52 12 32 31 21 23 13 13 23 21 32 31 11 14 24 32 31 21 42 21 11 13 23 32 32 34 41 43 22 21 33 11 25 42 42 25 21";
	protected final static String WARREN_QUESTIONS_SFC = "24 14 32 15 15 22 21 51 32 14 14 35 54 24 31 45 42 51 32 52 53 14 41 43 12 42 52 53 45 14 25 14 31 53 54 25 31 51 53 12 45 45 35 14 41 33 23 41 51 21 15 24 24 53 14 42 41 25 23 14 15 13 42 24 33 44 12 52 13 24 54 14 51 51 15 42 33 25 45 15 51 23 12 51 35 31 44 25 12 52 12 32 31 21 23 13 13 23 21 32 31 11 14 24 32 31 21 42 21 11 13 23 32 32 34 41 43 22 21 33 11 25 42 42 25 21 FC F8 FE 08 03 F8";
			
	public static void main(String[] args) {
		final Tarot[] cards = Tarot.values();	
		
		final String[] bytes = BONUS_CARD_STATS.split(" ");
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<bytes.length; i++) {
			String temp = bytes[i];
			if (temp.startsWith("F")) {//Two's complement for negative numbers:
				switch (temp)//Java 12 can return in the switch and not need breaks but let's not force Java 12+
				{
				    case "FF": temp = "-1";
				        break;
				    case "FE": temp = "-2";
				        break;
				    case "FD": temp = "-3";
			        	break;
				    case "FC": temp = "-4";
				    	break;
				    case "FB": temp = "-5";
				    	break;
				    //case "FA": temp = "-6"; //no -6 used so may as well not check
			    	//	break;
				    case "F9": temp = "-7";
			    		break;
				    case "F8": temp = "-8";
			    		break;
				}
			} else {
				temp = "+" + temp.substring(1);
			}
			
			if ((i + 7) % 7 == 0) {//append new line for the next card
				sb.append(cards[i / 7].toString()).append(Printer.newLine);
			}
			sb.append(temp);
			if ((i + 1) % 7 == 0 && (i / 7) != 21) {//don't append extra lines after World the last card
				sb.append(Printer.doubleNewLine);
			} else {
				sb.append(" ");
			}
		}
		sb.replace(sb.length()-1, sb.length(), "");//remove last space after World's LUK

		String tarotBonusCardStats = Printer.bonusStatsString();
		String check = sb.toString().equals(tarotBonusCardStats) ? "Yes" : "No";
		System.out.println(new StringBuilder("Is TarotBonusCardStats.java bonus card data equal to StartingCardsData.java's? ").append(check).append(Printer.newLine).toString());
		System.out.println(TarotBonusCardStats.STATS + Printer.newLine);
		System.out.println(sb.toString());
	}
}
/*
Is TarotBonusCardStats.java bonus card data equal to DEC.java's? Yes

[HP, STR, AGI, INT, CHA, ALI, LUK]

Magician
-4 -8 -2 +8 +3 -8 +5

Priestess
+3 -2 +1 -3 +4 +5 -3

Empress
+5 +2 +3 +4 +7 +3 +7

Emperor
+7 +5 +4 +2 +7 +4 +3

Hierophant
+3 -5 -3 +4 +2 -5 -3

Lovers
+4 +2 -3 -4 +3 +7 +4

Chariot
+7 +3 +7 -5 +6 +7 -5

Strength
+4 +7 +3 -7 +2 +3 -7

Hermit
-4 -2 -3 +7 +5 +7 +2

Fortune
-4 -5 +7 -2 +4 +6 -1

Justice
+2 +2 +3 +4 +2 +7 +1

HangedMan
-3 -7 -4 +5 +6 +7 -1

Death
-5 -3 -4 -1 +2 -7 +7

Temperance
+1 +1 +1 -1 +1 -1 -1

Devil
+3 +7 +1 -5 +4 -7 -2

Tower
-4 +3 -7 +5 +5 -1 -2

Star
+2 +4 +7 +1 +2 +3 +7

Moon
-3 -2 -4 +5 +2 +6 +7

Sun
+4 +3 -4 +5 +3 +7 +1

Judgment
-1 -2 -3 -1 +7 +2 -1

Fool
-4 -3 -2 -4 +3 -3 -2

World
-4 +7 -7 +7 +1 -7 -7
*/