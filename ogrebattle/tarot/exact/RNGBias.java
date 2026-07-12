package ogrebattle.tarot.exact;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ogrebattle.tarot.pojo.Quantity;
import ogrebattle.tarot.pojo.Tarot;
import ogrebattle.util.Printer;
import ogrebattle.util.Util;

public class RNGBias {
	/**
	 * Buried treasured and liberation rewards from temples<br>
	 * Only valid for SNES and SFC releases due to PSX and Saturn adding Nue's Shield / らいじゅうのたて into the pool.
	 * I pulled Nue's Shield on English disc with real PS2 and Spockrocket pulled it on real Saturn. Bug fixed.<br>
	 * Could adjust for those by upping RANGE to 0x5D (93) from 0x5C (92).
	 */
	protected static final Quantity EQUIP = new Quantity(0x8, 0x5C, 45, 28);//8 hex is 8 base 10, and 5C hex is 92 base 10, meant for hex values
	
	/**
	 * Consumable items received from defeating an enemy or neutral unit<br>
	 * Range is 0x1F (31). not 0x200 (32) due to SNES and SFC bug that makes Crystal Ball impossible to obtain from defeating an enemy or neutral unit.<br>
	 * Bug is most likely fixed in PSX and Saturn but not confirmed. Can be bought at Galf's / Antanjyl's shop in every release.<br>
	 * No RNG bias if range is unbugged to 0x20 (32) since it's a factor of 256.
	 */
	protected static final Quantity ITEM =  new Quantity(0x97, 0x1F, 133, 83);//97 hex is 151 base 10, and 1F hex is 31 base 10, meant for hex values
	
	/**
	 * Also for drawing from Liberation. Not showing redraw effect.
	 */
	protected static final Quantity CARD =  new Quantity(0, Tarot.DECK_SIZE, 187, 117);//22 Tarot cards
	
	public static void main(String[] args) {
		RNGBias generator = new RNGBias();
		
		System.out.println(generator.findRNGBias(EQUIP));
		System.out.println(generator.findRNGBias(ITEM));
		System.out.println(generator.findRNGBias(CARD));
		
		System.out.println(generator.moreBias());
	}
	
	public StringBuilder findRNGBias(Quantity q) {
		return findRNGBias(q.getClass().getSimpleName(), q.START, q.RANGE, q.FORMAT_HEX, q.FORMAT_DEC);
	}
	
	//String.format nice and all but ghetto-fabulous is more fun
	protected StringBuilder findRNGBias(String name, int start, int range, int formatHex, int formatDec) {
		String key = "16 10   H  D   " + name;
		final int[] values = new int[range];
		for(int i=0; i<range; i++) {
			values[i] = start + i;
		}
		String[] results = new String[Util.RNG_RANGE];
		StringBuilder sb = new StringBuilder(key).append(Printer.newLine);
	
		for(int i=0; i<Util.RNG_RANGE; i++) {
			//Multiply by 3 for stat bonus at level up, or 6 for stat item boost, or 92 for buried treasure
			int number = i * range;
			String temp = Integer.toHexString(number).toUpperCase();
			if (temp.length() > 2) {
				results[i] = temp.substring(0, temp.length()-2);
			} else {
				results[i] = "0";
			}
			temp = Integer.toHexString(i).toUpperCase();//may as well reuse for same purpose
			if (temp.length() < 2) {
				temp = "0" + temp;
			}
			sb.append(temp).append(" ").append(i);
			
			int base10 = Integer.parseInt(results[i], 16);
			int index = values[base10];
	
			if (i < 10) {
				sb.append(":   ");
			} else if (i < 100) {
				sb.append(":  ");
			} else {
				sb.append(": ");
			}
			sb.append(results[i]);
			
			if (i < formatHex) {
				sb.append(" ");
			}
			
			sb.append(" ").append(base10).append(":");
			
			if(i < formatDec) {
				sb.append("  ");
			} else {
				sb.append(" ");
			}

			if (range == Tarot.DECK_SIZE) {
				sb.append(Tarot.values()[base10]).append(Printer.newLine);//i.e. append "World" instead of "21"
			} else {
				sb.append(Integer.toHexString(index).toUpperCase()).append(Printer.newLine);
			}
		}
		return sb;
	}
	
	protected StringBuilder tarotBuilder(StringBuilder sb) {
		int[] tracker = new int[Util.RNG_RANGE];
		for(int i=0; i<Util.RNG_RANGE; i++) {
			tracker[i] = i;//index 0=0, index 1=1, etc.
		}
		
		sb.append(Printer.newLine);
		for(Tarot t : Tarot.values()) {
			String tarot = String.format("%-" + 12 + "." + 12 + "s", t.toString());//pad right
			sb.append(tarot).append(": ").append(tracker[t.ordinal()]).append(Printer.newLine);
		}

		BigDecimal ELEVEN = new BigDecimal("11");
		BigDecimal TWO_HUNDRED_FIFTY_SIX = new BigDecimal(Util.RNG_RANGE);
		//print Tarot card results
		sb.append(Printer.newLine)
		.append("11 out of 256 = " + new BigDecimal("11").divide(TWO_HUNDRED_FIFTY_SIX,
				7, RoundingMode.HALF_UP).movePointRight(2)).append("%").append(Printer.newLine)
		.append("12 out of 256 = " + new BigDecimal("12").divide(TWO_HUNDRED_FIFTY_SIX,
				6, RoundingMode.HALF_UP).movePointRight(2)).append("0%").append(Printer.newLine)//the 0 added due to the 9 rounding up
		.append("Relative increase of 1 in 11 = " + (new BigDecimal("1").divide(ELEVEN, 4, RoundingMode.HALF_UP).movePointRight(2)) + "% for 12 versus 11");
		
		return sb;
	}
	
	protected StringBuilder moreBias() {
		StringBuilder sb = new StringBuilder();
		//start hit rate explanation
		//int number = i * range
		sb.append("If 'range' in int number = i * range;' is 10 for the "
				+ "hit rate that is sorted into 10% bins, ").append(Printer.newLine).append("the RNG bias from rolling 0 to 9 for attack hit rates, "
						+ "with sufficiently random RNG, can be seen:")
		.append(Printer.newLine).append("0  00 to 19 (00  to 25)  26")
		.append(Printer.newLine).append("1  1A to 33 (26  to 51)  26")
		.append(Printer.newLine).append("2  34 to 4C (52  to 76)  25")
		.append(Printer.newLine).append("3  4D to 66 (77  to 102) 26")
		.append(Printer.newLine).append("4  67 to 7F (103 to 127) 25")		
		.append(Printer.newLine).append("5  80 to 99 (128 to 153) 26")
		.append(Printer.newLine).append("6  9A to B3 (154 to 179) 26")
		.append(Printer.newLine).append("7  B4 to CC (180 to 204) 25")
		.append(Printer.newLine).append("8  CD to E6 (205 to 230) 26")
		.append(Printer.newLine).append("9  E7 to FF (231 to 255) 25")	
		.append(Printer.doubleNewLine).append("Intended hit rate versus actual hit rate, no rounding:")
		.append(Printer.newLine).append("10%  26/255  = 10.15625%")
		.append(Printer.newLine).append("20%  52/256  = 20.3125%")
		.append(Printer.newLine).append("30%  77/256  = 30.078125%")
		.append(Printer.newLine).append("40%  103/256 = 40.234375%")		
		.append(Printer.newLine).append("50%  128/256 = 50%")
		.append(Printer.newLine).append("60%  154/256 = 60.15625%")
		.append(Printer.newLine).append("70%  180/256 = 70.3125%")
		.append(Printer.newLine).append("80%  205/256 = 80.078125%")
		.append(Printer.newLine).append("90%  231/256 = 90.234375%")
		
		//start level up stat bonus explanation
		.append(Printer.doubleNewLine).append("If 'range' in int number = i * range;' is 3 for the "
				+ "HP/STR/AGI/INT +0, +1, +2 level up stat bonus,").append(Printer.newLine).append("+0 is 0x00 to 0x55 (86 values), +1 is 0x86 "
				+ "to 0x170 (85 values) and +2 is 0x171 to 0x255 (85 values).").append(Printer.newLine).append("EV is 255/256 due to +0 mapping to an extra bit "
				+ "= +0.99609375 gained per stat across 4 stats: HP, STR, AGI, INT.").append(Printer.newLine)
				.append("That translates to 1 stat point lost due to RNG bias every (256 levels)/(4 stats) = 64 levels gained across your army.")
		//start item stat bonus explanation
		.append(Printer.doubleNewLine).append("Bonus spread for stat up items such as from Anywhere Jack:")
		.append(Printer.newLine).append("+5  00 to 2A (00  to 42)  43")
		.append(Printer.newLine).append("+6  2B to 55 (43  to 85)  43")
		.append(Printer.newLine).append("+7  56 to 7F (86  to 127) 42")
		.append(Printer.newLine).append("+8  80 to AA (128 to 170) 43")
		.append(Printer.newLine).append("+9  AB to D5 (171 to 213) 43")
		.append(Printer.newLine).append("+10 D6 to DD (214 to 255) 42")
		.append(Printer.doubleNewLine).append("The 43 totals are 1/256 more likely than the 42 totals. Relative increase of 1 in 42 = 2.38%.");
		
		return sb;
	}
}
/*
16 10   H  D   Quantity
00 0:   0  0:  8
01 1:   0  0:  8
02 2:   0  0:  8
03 3:   1  1:  9
04 4:   1  1:  9
05 5:   1  1:  9
06 6:   2  2:  A
07 7:   2  2:  A
08 8:   2  2:  A
09 9:   3  3:  B
0A 10:  3  3:  B
0B 11:  3  3:  B
0C 12:  4  4:  C
0D 13:  4  4:  C
0E 14:  5  5:  D
0F 15:  5  5:  D
10 16:  5  5:  D
11 17:  6  6:  E
12 18:  6  6:  E
13 19:  6  6:  E
14 20:  7  7:  F
15 21:  7  7:  F
16 22:  7  7:  F
17 23:  8  8:  10
18 24:  8  8:  10
19 25:  8  8:  10
1A 26:  9  9:  11
1B 27:  9  9:  11
1C 28:  A  10: 12
1D 29:  A  10: 12
1E 30:  A  10: 12
1F 31:  B  11: 13
20 32:  B  11: 13
21 33:  B  11: 13
22 34:  C  12: 14
23 35:  C  12: 14
24 36:  C  12: 14
25 37:  D  13: 15
26 38:  D  13: 15
27 39:  E  14: 16
28 40:  E  14: 16
29 41:  E  14: 16
2A 42:  F  15: 17
2B 43:  F  15: 17
2C 44:  F  15: 17
2D 45:  10 16: 18
2E 46:  10 16: 18
2F 47:  10 16: 18
30 48:  11 17: 19
31 49:  11 17: 19
32 50:  11 17: 19
33 51:  12 18: 1A
34 52:  12 18: 1A
35 53:  13 19: 1B
36 54:  13 19: 1B
37 55:  13 19: 1B
38 56:  14 20: 1C
39 57:  14 20: 1C
3A 58:  14 20: 1C
3B 59:  15 21: 1D
3C 60:  15 21: 1D
3D 61:  15 21: 1D
3E 62:  16 22: 1E
3F 63:  16 22: 1E
40 64:  17 23: 1F
41 65:  17 23: 1F
42 66:  17 23: 1F
43 67:  18 24: 20
44 68:  18 24: 20
45 69:  18 24: 20
46 70:  19 25: 21
47 71:  19 25: 21
48 72:  19 25: 21
49 73:  1A 26: 22
4A 74:  1A 26: 22
4B 75:  1A 26: 22
4C 76:  1B 27: 23
4D 77:  1B 27: 23
4E 78:  1C 28: 24
4F 79:  1C 28: 24
50 80:  1C 28: 24
51 81:  1D 29: 25
52 82:  1D 29: 25
53 83:  1D 29: 25
54 84:  1E 30: 26
55 85:  1E 30: 26
56 86:  1E 30: 26
57 87:  1F 31: 27
58 88:  1F 31: 27
59 89:  1F 31: 27
5A 90:  20 32: 28
5B 91:  20 32: 28
5C 92:  21 33: 29
5D 93:  21 33: 29
5E 94:  21 33: 29
5F 95:  22 34: 2A
60 96:  22 34: 2A
61 97:  22 34: 2A
62 98:  23 35: 2B
63 99:  23 35: 2B
64 100: 23 35: 2B
65 101: 24 36: 2C
66 102: 24 36: 2C
67 103: 25 37: 2D
68 104: 25 37: 2D
69 105: 25 37: 2D
6A 106: 26 38: 2E
6B 107: 26 38: 2E
6C 108: 26 38: 2E
6D 109: 27 39: 2F
6E 110: 27 39: 2F
6F 111: 27 39: 2F
70 112: 28 40: 30
71 113: 28 40: 30
72 114: 28 40: 30
73 115: 29 41: 31
74 116: 29 41: 31
75 117: 2A 42: 32
76 118: 2A 42: 32
77 119: 2A 42: 32
78 120: 2B 43: 33
79 121: 2B 43: 33
7A 122: 2B 43: 33
7B 123: 2C 44: 34
7C 124: 2C 44: 34
7D 125: 2C 44: 34
7E 126: 2D 45: 35
7F 127: 2D 45: 35
80 128: 2E 46: 36
81 129: 2E 46: 36
82 130: 2E 46: 36
83 131: 2F 47: 37
84 132: 2F 47: 37
85 133: 2F 47: 37
86 134: 30 48: 38
87 135: 30 48: 38
88 136: 30 48: 38
89 137: 31 49: 39
8A 138: 31 49: 39
8B 139: 31 49: 39
8C 140: 32 50: 3A
8D 141: 32 50: 3A
8E 142: 33 51: 3B
8F 143: 33 51: 3B
90 144: 33 51: 3B
91 145: 34 52: 3C
92 146: 34 52: 3C
93 147: 34 52: 3C
94 148: 35 53: 3D
95 149: 35 53: 3D
96 150: 35 53: 3D
97 151: 36 54: 3E
98 152: 36 54: 3E
99 153: 36 54: 3E
9A 154: 37 55: 3F
9B 155: 37 55: 3F
9C 156: 38 56: 40
9D 157: 38 56: 40
9E 158: 38 56: 40
9F 159: 39 57: 41
A0 160: 39 57: 41
A1 161: 39 57: 41
A2 162: 3A 58: 42
A3 163: 3A 58: 42
A4 164: 3A 58: 42
A5 165: 3B 59: 43
A6 166: 3B 59: 43
A7 167: 3C 60: 44
A8 168: 3C 60: 44
A9 169: 3C 60: 44
AA 170: 3D 61: 45
AB 171: 3D 61: 45
AC 172: 3D 61: 45
AD 173: 3E 62: 46
AE 174: 3E 62: 46
AF 175: 3E 62: 46
B0 176: 3F 63: 47
B1 177: 3F 63: 47
B2 178: 3F 63: 47
B3 179: 40 64: 48
B4 180: 40 64: 48
B5 181: 41 65: 49
B6 182: 41 65: 49
B7 183: 41 65: 49
B8 184: 42 66: 4A
B9 185: 42 66: 4A
BA 186: 42 66: 4A
BB 187: 43 67: 4B
BC 188: 43 67: 4B
BD 189: 43 67: 4B
BE 190: 44 68: 4C
BF 191: 44 68: 4C
C0 192: 45 69: 4D
C1 193: 45 69: 4D
C2 194: 45 69: 4D
C3 195: 46 70: 4E
C4 196: 46 70: 4E
C5 197: 46 70: 4E
C6 198: 47 71: 4F
C7 199: 47 71: 4F
C8 200: 47 71: 4F
C9 201: 48 72: 50
CA 202: 48 72: 50
CB 203: 48 72: 50
CC 204: 49 73: 51
CD 205: 49 73: 51
CE 206: 4A 74: 52
CF 207: 4A 74: 52
D0 208: 4A 74: 52
D1 209: 4B 75: 53
D2 210: 4B 75: 53
D3 211: 4B 75: 53
D4 212: 4C 76: 54
D5 213: 4C 76: 54
D6 214: 4C 76: 54
D7 215: 4D 77: 55
D8 216: 4D 77: 55
D9 217: 4D 77: 55
DA 218: 4E 78: 56
DB 219: 4E 78: 56
DC 220: 4F 79: 57
DD 221: 4F 79: 57
DE 222: 4F 79: 57
DF 223: 50 80: 58
E0 224: 50 80: 58
E1 225: 50 80: 58
E2 226: 51 81: 59
E3 227: 51 81: 59
E4 228: 51 81: 59
E5 229: 52 82: 5A
E6 230: 52 82: 5A
E7 231: 53 83: 5B
E8 232: 53 83: 5B
E9 233: 53 83: 5B
EA 234: 54 84: 5C
EB 235: 54 84: 5C
EC 236: 54 84: 5C
ED 237: 55 85: 5D
EE 238: 55 85: 5D
EF 239: 55 85: 5D
F0 240: 56 86: 5E
F1 241: 56 86: 5E
F2 242: 56 86: 5E
F3 243: 57 87: 5F
F4 244: 57 87: 5F
F5 245: 58 88: 60
F6 246: 58 88: 60
F7 247: 58 88: 60
F8 248: 59 89: 61
F9 249: 59 89: 61
FA 250: 59 89: 61
FB 251: 5A 90: 62
FC 252: 5A 90: 62
FD 253: 5A 90: 62
FE 254: 5B 91: 63
FF 255: 5B 91: 63

16 10   H  D   Quantity
00 0:   0  0:  97
01 1:   0  0:  97
02 2:   0  0:  97
03 3:   0  0:  97
04 4:   0  0:  97
05 5:   0  0:  97
06 6:   0  0:  97
07 7:   0  0:  97
08 8:   0  0:  97
09 9:   1  1:  98
0A 10:  1  1:  98
0B 11:  1  1:  98
0C 12:  1  1:  98
0D 13:  1  1:  98
0E 14:  1  1:  98
0F 15:  1  1:  98
10 16:  1  1:  98
11 17:  2  2:  99
12 18:  2  2:  99
13 19:  2  2:  99
14 20:  2  2:  99
15 21:  2  2:  99
16 22:  2  2:  99
17 23:  2  2:  99
18 24:  2  2:  99
19 25:  3  3:  9A
1A 26:  3  3:  9A
1B 27:  3  3:  9A
1C 28:  3  3:  9A
1D 29:  3  3:  9A
1E 30:  3  3:  9A
1F 31:  3  3:  9A
20 32:  3  3:  9A
21 33:  3  3:  9A
22 34:  4  4:  9B
23 35:  4  4:  9B
24 36:  4  4:  9B
25 37:  4  4:  9B
26 38:  4  4:  9B
27 39:  4  4:  9B
28 40:  4  4:  9B
29 41:  4  4:  9B
2A 42:  5  5:  9C
2B 43:  5  5:  9C
2C 44:  5  5:  9C
2D 45:  5  5:  9C
2E 46:  5  5:  9C
2F 47:  5  5:  9C
30 48:  5  5:  9C
31 49:  5  5:  9C
32 50:  6  6:  9D
33 51:  6  6:  9D
34 52:  6  6:  9D
35 53:  6  6:  9D
36 54:  6  6:  9D
37 55:  6  6:  9D
38 56:  6  6:  9D
39 57:  6  6:  9D
3A 58:  7  7:  9E
3B 59:  7  7:  9E
3C 60:  7  7:  9E
3D 61:  7  7:  9E
3E 62:  7  7:  9E
3F 63:  7  7:  9E
40 64:  7  7:  9E
41 65:  7  7:  9E
42 66:  7  7:  9E
43 67:  8  8:  9F
44 68:  8  8:  9F
45 69:  8  8:  9F
46 70:  8  8:  9F
47 71:  8  8:  9F
48 72:  8  8:  9F
49 73:  8  8:  9F
4A 74:  8  8:  9F
4B 75:  9  9:  A0
4C 76:  9  9:  A0
4D 77:  9  9:  A0
4E 78:  9  9:  A0
4F 79:  9  9:  A0
50 80:  9  9:  A0
51 81:  9  9:  A0
52 82:  9  9:  A0
53 83:  A  10: A1
54 84:  A  10: A1
55 85:  A  10: A1
56 86:  A  10: A1
57 87:  A  10: A1
58 88:  A  10: A1
59 89:  A  10: A1
5A 90:  A  10: A1
5B 91:  B  11: A2
5C 92:  B  11: A2
5D 93:  B  11: A2
5E 94:  B  11: A2
5F 95:  B  11: A2
60 96:  B  11: A2
61 97:  B  11: A2
62 98:  B  11: A2
63 99:  B  11: A2
64 100: C  12: A3
65 101: C  12: A3
66 102: C  12: A3
67 103: C  12: A3
68 104: C  12: A3
69 105: C  12: A3
6A 106: C  12: A3
6B 107: C  12: A3
6C 108: D  13: A4
6D 109: D  13: A4
6E 110: D  13: A4
6F 111: D  13: A4
70 112: D  13: A4
71 113: D  13: A4
72 114: D  13: A4
73 115: D  13: A4
74 116: E  14: A5
75 117: E  14: A5
76 118: E  14: A5
77 119: E  14: A5
78 120: E  14: A5
79 121: E  14: A5
7A 122: E  14: A5
7B 123: E  14: A5
7C 124: F  15: A6
7D 125: F  15: A6
7E 126: F  15: A6
7F 127: F  15: A6
80 128: F  15: A6
81 129: F  15: A6
82 130: F  15: A6
83 131: F  15: A6
84 132: F  15: A6
85 133: 10 16: A7
86 134: 10 16: A7
87 135: 10 16: A7
88 136: 10 16: A7
89 137: 10 16: A7
8A 138: 10 16: A7
8B 139: 10 16: A7
8C 140: 10 16: A7
8D 141: 11 17: A8
8E 142: 11 17: A8
8F 143: 11 17: A8
90 144: 11 17: A8
91 145: 11 17: A8
92 146: 11 17: A8
93 147: 11 17: A8
94 148: 11 17: A8
95 149: 12 18: A9
96 150: 12 18: A9
97 151: 12 18: A9
98 152: 12 18: A9
99 153: 12 18: A9
9A 154: 12 18: A9
9B 155: 12 18: A9
9C 156: 12 18: A9
9D 157: 13 19: AA
9E 158: 13 19: AA
9F 159: 13 19: AA
A0 160: 13 19: AA
A1 161: 13 19: AA
A2 162: 13 19: AA
A3 163: 13 19: AA
A4 164: 13 19: AA
A5 165: 13 19: AA
A6 166: 14 20: AB
A7 167: 14 20: AB
A8 168: 14 20: AB
A9 169: 14 20: AB
AA 170: 14 20: AB
AB 171: 14 20: AB
AC 172: 14 20: AB
AD 173: 14 20: AB
AE 174: 15 21: AC
AF 175: 15 21: AC
B0 176: 15 21: AC
B1 177: 15 21: AC
B2 178: 15 21: AC
B3 179: 15 21: AC
B4 180: 15 21: AC
B5 181: 15 21: AC
B6 182: 16 22: AD
B7 183: 16 22: AD
B8 184: 16 22: AD
B9 185: 16 22: AD
BA 186: 16 22: AD
BB 187: 16 22: AD
BC 188: 16 22: AD
BD 189: 16 22: AD
BE 190: 17 23: AE
BF 191: 17 23: AE
C0 192: 17 23: AE
C1 193: 17 23: AE
C2 194: 17 23: AE
C3 195: 17 23: AE
C4 196: 17 23: AE
C5 197: 17 23: AE
C6 198: 17 23: AE
C7 199: 18 24: AF
C8 200: 18 24: AF
C9 201: 18 24: AF
CA 202: 18 24: AF
CB 203: 18 24: AF
CC 204: 18 24: AF
CD 205: 18 24: AF
CE 206: 18 24: AF
CF 207: 19 25: B0
D0 208: 19 25: B0
D1 209: 19 25: B0
D2 210: 19 25: B0
D3 211: 19 25: B0
D4 212: 19 25: B0
D5 213: 19 25: B0
D6 214: 19 25: B0
D7 215: 1A 26: B1
D8 216: 1A 26: B1
D9 217: 1A 26: B1
DA 218: 1A 26: B1
DB 219: 1A 26: B1
DC 220: 1A 26: B1
DD 221: 1A 26: B1
DE 222: 1A 26: B1
DF 223: 1B 27: B2
E0 224: 1B 27: B2
E1 225: 1B 27: B2
E2 226: 1B 27: B2
E3 227: 1B 27: B2
E4 228: 1B 27: B2
E5 229: 1B 27: B2
E6 230: 1B 27: B2
E7 231: 1B 27: B2
E8 232: 1C 28: B3
E9 233: 1C 28: B3
EA 234: 1C 28: B3
EB 235: 1C 28: B3
EC 236: 1C 28: B3
ED 237: 1C 28: B3
EE 238: 1C 28: B3
EF 239: 1C 28: B3
F0 240: 1D 29: B4
F1 241: 1D 29: B4
F2 242: 1D 29: B4
F3 243: 1D 29: B4
F4 244: 1D 29: B4
F5 245: 1D 29: B4
F6 246: 1D 29: B4
F7 247: 1D 29: B4
F8 248: 1E 30: B5
F9 249: 1E 30: B5
FA 250: 1E 30: B5
FB 251: 1E 30: B5
FC 252: 1E 30: B5
FD 253: 1E 30: B5
FE 254: 1E 30: B5
FF 255: 1E 30: B5

16 10   H  D   Quantity
00 0:   0  0:  Magician
01 1:   0  0:  Magician
02 2:   0  0:  Magician
03 3:   0  0:  Magician
04 4:   0  0:  Magician
05 5:   0  0:  Magician
06 6:   0  0:  Magician
07 7:   0  0:  Magician
08 8:   0  0:  Magician
09 9:   0  0:  Magician
0A 10:  0  0:  Magician
0B 11:  0  0:  Magician
0C 12:  1  1:  Priestess
0D 13:  1  1:  Priestess
0E 14:  1  1:  Priestess
0F 15:  1  1:  Priestess
10 16:  1  1:  Priestess
11 17:  1  1:  Priestess
12 18:  1  1:  Priestess
13 19:  1  1:  Priestess
14 20:  1  1:  Priestess
15 21:  1  1:  Priestess
16 22:  1  1:  Priestess
17 23:  1  1:  Priestess
18 24:  2  2:  Empress
19 25:  2  2:  Empress
1A 26:  2  2:  Empress
1B 27:  2  2:  Empress
1C 28:  2  2:  Empress
1D 29:  2  2:  Empress
1E 30:  2  2:  Empress
1F 31:  2  2:  Empress
20 32:  2  2:  Empress
21 33:  2  2:  Empress
22 34:  2  2:  Empress
23 35:  3  3:  Emperor
24 36:  3  3:  Emperor
25 37:  3  3:  Emperor
26 38:  3  3:  Emperor
27 39:  3  3:  Emperor
28 40:  3  3:  Emperor
29 41:  3  3:  Emperor
2A 42:  3  3:  Emperor
2B 43:  3  3:  Emperor
2C 44:  3  3:  Emperor
2D 45:  3  3:  Emperor
2E 46:  3  3:  Emperor
2F 47:  4  4:  Hierophant
30 48:  4  4:  Hierophant
31 49:  4  4:  Hierophant
32 50:  4  4:  Hierophant
33 51:  4  4:  Hierophant
34 52:  4  4:  Hierophant
35 53:  4  4:  Hierophant
36 54:  4  4:  Hierophant
37 55:  4  4:  Hierophant
38 56:  4  4:  Hierophant
39 57:  4  4:  Hierophant
3A 58:  4  4:  Hierophant
3B 59:  5  5:  Lovers
3C 60:  5  5:  Lovers
3D 61:  5  5:  Lovers
3E 62:  5  5:  Lovers
3F 63:  5  5:  Lovers
40 64:  5  5:  Lovers
41 65:  5  5:  Lovers
42 66:  5  5:  Lovers
43 67:  5  5:  Lovers
44 68:  5  5:  Lovers
45 69:  5  5:  Lovers
46 70:  6  6:  Chariot
47 71:  6  6:  Chariot
48 72:  6  6:  Chariot
49 73:  6  6:  Chariot
4A 74:  6  6:  Chariot
4B 75:  6  6:  Chariot
4C 76:  6  6:  Chariot
4D 77:  6  6:  Chariot
4E 78:  6  6:  Chariot
4F 79:  6  6:  Chariot
50 80:  6  6:  Chariot
51 81:  6  6:  Chariot
52 82:  7  7:  Strength
53 83:  7  7:  Strength
54 84:  7  7:  Strength
55 85:  7  7:  Strength
56 86:  7  7:  Strength
57 87:  7  7:  Strength
58 88:  7  7:  Strength
59 89:  7  7:  Strength
5A 90:  7  7:  Strength
5B 91:  7  7:  Strength
5C 92:  7  7:  Strength
5D 93:  7  7:  Strength
5E 94:  8  8:  Hermit
5F 95:  8  8:  Hermit
60 96:  8  8:  Hermit
61 97:  8  8:  Hermit
62 98:  8  8:  Hermit
63 99:  8  8:  Hermit
64 100: 8  8:  Hermit
65 101: 8  8:  Hermit
66 102: 8  8:  Hermit
67 103: 8  8:  Hermit
68 104: 8  8:  Hermit
69 105: 9  9:  Fortune
6A 106: 9  9:  Fortune
6B 107: 9  9:  Fortune
6C 108: 9  9:  Fortune
6D 109: 9  9:  Fortune
6E 110: 9  9:  Fortune
6F 111: 9  9:  Fortune
70 112: 9  9:  Fortune
71 113: 9  9:  Fortune
72 114: 9  9:  Fortune
73 115: 9  9:  Fortune
74 116: 9  9:  Fortune
75 117: A  10: Justice
76 118: A  10: Justice
77 119: A  10: Justice
78 120: A  10: Justice
79 121: A  10: Justice
7A 122: A  10: Justice
7B 123: A  10: Justice
7C 124: A  10: Justice
7D 125: A  10: Justice
7E 126: A  10: Justice
7F 127: A  10: Justice
80 128: B  11: HangedMan
81 129: B  11: HangedMan
82 130: B  11: HangedMan
83 131: B  11: HangedMan
84 132: B  11: HangedMan
85 133: B  11: HangedMan
86 134: B  11: HangedMan
87 135: B  11: HangedMan
88 136: B  11: HangedMan
89 137: B  11: HangedMan
8A 138: B  11: HangedMan
8B 139: B  11: HangedMan
8C 140: C  12: Death
8D 141: C  12: Death
8E 142: C  12: Death
8F 143: C  12: Death
90 144: C  12: Death
91 145: C  12: Death
92 146: C  12: Death
93 147: C  12: Death
94 148: C  12: Death
95 149: C  12: Death
96 150: C  12: Death
97 151: C  12: Death
98 152: D  13: Temperance
99 153: D  13: Temperance
9A 154: D  13: Temperance
9B 155: D  13: Temperance
9C 156: D  13: Temperance
9D 157: D  13: Temperance
9E 158: D  13: Temperance
9F 159: D  13: Temperance
A0 160: D  13: Temperance
A1 161: D  13: Temperance
A2 162: D  13: Temperance
A3 163: E  14: Devil
A4 164: E  14: Devil
A5 165: E  14: Devil
A6 166: E  14: Devil
A7 167: E  14: Devil
A8 168: E  14: Devil
A9 169: E  14: Devil
AA 170: E  14: Devil
AB 171: E  14: Devil
AC 172: E  14: Devil
AD 173: E  14: Devil
AE 174: E  14: Devil
AF 175: F  15: Tower
B0 176: F  15: Tower
B1 177: F  15: Tower
B2 178: F  15: Tower
B3 179: F  15: Tower
B4 180: F  15: Tower
B5 181: F  15: Tower
B6 182: F  15: Tower
B7 183: F  15: Tower
B8 184: F  15: Tower
B9 185: F  15: Tower
BA 186: F  15: Tower
BB 187: 10 16: Star
BC 188: 10 16: Star
BD 189: 10 16: Star
BE 190: 10 16: Star
BF 191: 10 16: Star
C0 192: 10 16: Star
C1 193: 10 16: Star
C2 194: 10 16: Star
C3 195: 10 16: Star
C4 196: 10 16: Star
C5 197: 10 16: Star
C6 198: 11 17: Moon
C7 199: 11 17: Moon
C8 200: 11 17: Moon
C9 201: 11 17: Moon
CA 202: 11 17: Moon
CB 203: 11 17: Moon
CC 204: 11 17: Moon
CD 205: 11 17: Moon
CE 206: 11 17: Moon
CF 207: 11 17: Moon
D0 208: 11 17: Moon
D1 209: 11 17: Moon
D2 210: 12 18: Sun
D3 211: 12 18: Sun
D4 212: 12 18: Sun
D5 213: 12 18: Sun
D6 214: 12 18: Sun
D7 215: 12 18: Sun
D8 216: 12 18: Sun
D9 217: 12 18: Sun
DA 218: 12 18: Sun
DB 219: 12 18: Sun
DC 220: 12 18: Sun
DD 221: 12 18: Sun
DE 222: 13 19: Judgment
DF 223: 13 19: Judgment
E0 224: 13 19: Judgment
E1 225: 13 19: Judgment
E2 226: 13 19: Judgment
E3 227: 13 19: Judgment
E4 228: 13 19: Judgment
E5 229: 13 19: Judgment
E6 230: 13 19: Judgment
E7 231: 13 19: Judgment
E8 232: 13 19: Judgment
E9 233: 14 20: Fool
EA 234: 14 20: Fool
EB 235: 14 20: Fool
EC 236: 14 20: Fool
ED 237: 14 20: Fool
EE 238: 14 20: Fool
EF 239: 14 20: Fool
F0 240: 14 20: Fool
F1 241: 14 20: Fool
F2 242: 14 20: Fool
F3 243: 14 20: Fool
F4 244: 14 20: Fool
F5 245: 15 21: World
F6 246: 15 21: World
F7 247: 15 21: World
F8 248: 15 21: World
F9 249: 15 21: World
FA 250: 15 21: World
FB 251: 15 21: World
FC 252: 15 21: World
FD 253: 15 21: World
FE 254: 15 21: World
FF 255: 15 21: World

If 'range' in int number = i * range;' is 10 for the hit rate that is sorted into 10% bins, 
the RNG bias from rolling 0 to 9 for attack hit rates, with sufficiently random RNG, can be seen:
0  00 to 19 (00  to 25)  26
1  1A to 33 (26  to 51)  26
2  34 to 4C (52  to 76)  25
3  4D to 66 (77  to 102) 26
4  67 to 7F (103 to 127) 25
5  80 to 99 (128 to 153) 26
6  9A to B3 (154 to 179) 26
7  B4 to CC (180 to 204) 25
8  CD to E6 (205 to 230) 26
9  E7 to FF (231 to 255) 25

Intended hit rate versus actual hit rate, no rounding:
10%  26/255  = 10.15625%
20%  52/256  = 20.3125%
30%  77/256  = 30.078125%
40%  103/256 = 40.234375%
50%  128/256 = 50%
60%  154/256 = 60.15625%
70%  180/256 = 70.3125%
80%  205/256 = 80.078125%
90%  231/256 = 90.234375%

If 'range' in int number = i * range;' is 3 for the HP/STR/AGI/INT +0, +1, +2 level up stat bonus,
+0 is 0x00 to 0x55 (86 values), +1 is 0x86 to 0x170 (85 values) and +2 is 0x171 to 0x255 (85 values).
EV is 255/256 due to +0 mapping to an extra bit = +0.99609375 gained per stat across 4 stats: HP, STR, AGI, INT.
That translates to 1 stat point lost due to RNG bias every (256 levels)/(4 stats) = 64 levels gained across your army.

Bonus spread for stat up items such as from Anywhere Jack:
+5  00 to 2A (00  to 42)  43
+6  2B to 55 (43  to 85)  43
+7  56 to 7F (86  to 127) 42
+8  80 to AA (128 to 170) 43
+9  AB to D5 (171 to 213) 43
+10 D6 to DD (214 to 255) 42

The 43 totals are 1/256 more likely than the 42 totals. Relative increase of 1 in 42 = 2.38%.
*/
