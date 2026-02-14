package ogrebattle.tarot.exact;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ogrebattle.printer.Util;
import ogrebattle.tarot.pojo.Tarot;

public class LookupTableBias {
	//String.format nice and all but ghetto-fabulous is more fun
	public static void main(String[] args) {
		final Tarot[] values = Tarot.values();
		int[] tracker = new int[Tarot.DECK_SIZE];
		String[] results = new String[256];
		StringBuilder sb = new StringBuilder("16 10   H  D   CARD").append(Util.newLine);

		for(int i=0; i<256; i++) {
			int number = i * Tarot.DECK_SIZE;//equivalent to game's multiply by 0x16
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
			Tarot card = values[base10];
			tracker[base10]++;
			
			if (i < 10) {
				sb.append(":   ");
			} else if (i < 100) {
				sb.append(":  ");
			} else {
				sb.append(": ");
			}
			sb.append(results[i]);
			
			if (i < 187) {
				sb.append(" ");
			}
			sb.append(" ").append(base10);
			
			if(i < 117) {
				sb.append("   ");
			} else {
				sb.append("  ");
			}
			sb.append(card).append(Util.newLine);
		}
		sb.append(Util.newLine);
		
		for(Tarot t : values) {
			String tarot = String.format("%-" + 12 + "." + 12 + "s", t.toString());//pad right
			sb.append(tarot).append(": ").append(tracker[t.ordinal()]).append(Util.newLine);
		}

		BigDecimal ELEVEN = new BigDecimal("11");
		BigDecimal TWO_HUNDRED_FIFTY_SIX = new BigDecimal("256");
		sb.append(Util.newLine)
		.append("11 out of 256 = " + new BigDecimal("11").divide(TWO_HUNDRED_FIFTY_SIX,
				7, RoundingMode.HALF_UP).movePointRight(2)).append("%").append(Util.newLine)
		.append("12 out of 256 = " + new BigDecimal("12").divide(TWO_HUNDRED_FIFTY_SIX,
				6, RoundingMode.HALF_UP).movePointRight(2)).append("%").append(Util.newLine)
		.append("Relative increase of 1 in 11 = " + (new BigDecimal("1").divide(ELEVEN, 4, RoundingMode.HALF_UP).movePointRight(2)) + "% for 12 versus 11");
		
		System.out.println(sb.toString());
	}
}
/*
16 10   H  D   CARD
00 0:   0  0   Magician
01 1:   0  0   Magician
02 2:   0  0   Magician
03 3:   0  0   Magician
04 4:   0  0   Magician
05 5:   0  0   Magician
06 6:   0  0   Magician
07 7:   0  0   Magician
08 8:   0  0   Magician
09 9:   0  0   Magician
0A 10:  0  0   Magician
0B 11:  0  0   Magician
0C 12:  1  1   Priestess
0D 13:  1  1   Priestess
0E 14:  1  1   Priestess
0F 15:  1  1   Priestess
10 16:  1  1   Priestess
11 17:  1  1   Priestess
12 18:  1  1   Priestess
13 19:  1  1   Priestess
14 20:  1  1   Priestess
15 21:  1  1   Priestess
16 22:  1  1   Priestess
17 23:  1  1   Priestess
18 24:  2  2   Empress
19 25:  2  2   Empress
1A 26:  2  2   Empress
1B 27:  2  2   Empress
1C 28:  2  2   Empress
1D 29:  2  2   Empress
1E 30:  2  2   Empress
1F 31:  2  2   Empress
20 32:  2  2   Empress
21 33:  2  2   Empress
22 34:  2  2   Empress
23 35:  3  3   Emperor
24 36:  3  3   Emperor
25 37:  3  3   Emperor
26 38:  3  3   Emperor
27 39:  3  3   Emperor
28 40:  3  3   Emperor
29 41:  3  3   Emperor
2A 42:  3  3   Emperor
2B 43:  3  3   Emperor
2C 44:  3  3   Emperor
2D 45:  3  3   Emperor
2E 46:  3  3   Emperor
2F 47:  4  4   Hierophant
30 48:  4  4   Hierophant
31 49:  4  4   Hierophant
32 50:  4  4   Hierophant
33 51:  4  4   Hierophant
34 52:  4  4   Hierophant
35 53:  4  4   Hierophant
36 54:  4  4   Hierophant
37 55:  4  4   Hierophant
38 56:  4  4   Hierophant
39 57:  4  4   Hierophant
3A 58:  4  4   Hierophant
3B 59:  5  5   Lovers
3C 60:  5  5   Lovers
3D 61:  5  5   Lovers
3E 62:  5  5   Lovers
3F 63:  5  5   Lovers
40 64:  5  5   Lovers
41 65:  5  5   Lovers
42 66:  5  5   Lovers
43 67:  5  5   Lovers
44 68:  5  5   Lovers
45 69:  5  5   Lovers
46 70:  6  6   Chariot
47 71:  6  6   Chariot
48 72:  6  6   Chariot
49 73:  6  6   Chariot
4A 74:  6  6   Chariot
4B 75:  6  6   Chariot
4C 76:  6  6   Chariot
4D 77:  6  6   Chariot
4E 78:  6  6   Chariot
4F 79:  6  6   Chariot
50 80:  6  6   Chariot
51 81:  6  6   Chariot
52 82:  7  7   Strength
53 83:  7  7   Strength
54 84:  7  7   Strength
55 85:  7  7   Strength
56 86:  7  7   Strength
57 87:  7  7   Strength
58 88:  7  7   Strength
59 89:  7  7   Strength
5A 90:  7  7   Strength
5B 91:  7  7   Strength
5C 92:  7  7   Strength
5D 93:  7  7   Strength
5E 94:  8  8   Hermit
5F 95:  8  8   Hermit
60 96:  8  8   Hermit
61 97:  8  8   Hermit
62 98:  8  8   Hermit
63 99:  8  8   Hermit
64 100: 8  8   Hermit
65 101: 8  8   Hermit
66 102: 8  8   Hermit
67 103: 8  8   Hermit
68 104: 8  8   Hermit
69 105: 9  9   Fortune
6A 106: 9  9   Fortune
6B 107: 9  9   Fortune
6C 108: 9  9   Fortune
6D 109: 9  9   Fortune
6E 110: 9  9   Fortune
6F 111: 9  9   Fortune
70 112: 9  9   Fortune
71 113: 9  9   Fortune
72 114: 9  9   Fortune
73 115: 9  9   Fortune
74 116: 9  9   Fortune
75 117: A  10  Justice
76 118: A  10  Justice
77 119: A  10  Justice
78 120: A  10  Justice
79 121: A  10  Justice
7A 122: A  10  Justice
7B 123: A  10  Justice
7C 124: A  10  Justice
7D 125: A  10  Justice
7E 126: A  10  Justice
7F 127: A  10  Justice
80 128: B  11  HangedMan
81 129: B  11  HangedMan
82 130: B  11  HangedMan
83 131: B  11  HangedMan
84 132: B  11  HangedMan
85 133: B  11  HangedMan
86 134: B  11  HangedMan
87 135: B  11  HangedMan
88 136: B  11  HangedMan
89 137: B  11  HangedMan
8A 138: B  11  HangedMan
8B 139: B  11  HangedMan
8C 140: C  12  Death
8D 141: C  12  Death
8E 142: C  12  Death
8F 143: C  12  Death
90 144: C  12  Death
91 145: C  12  Death
92 146: C  12  Death
93 147: C  12  Death
94 148: C  12  Death
95 149: C  12  Death
96 150: C  12  Death
97 151: C  12  Death
98 152: D  13  Temperance
99 153: D  13  Temperance
9A 154: D  13  Temperance
9B 155: D  13  Temperance
9C 156: D  13  Temperance
9D 157: D  13  Temperance
9E 158: D  13  Temperance
9F 159: D  13  Temperance
A0 160: D  13  Temperance
A1 161: D  13  Temperance
A2 162: D  13  Temperance
A3 163: E  14  Devil
A4 164: E  14  Devil
A5 165: E  14  Devil
A6 166: E  14  Devil
A7 167: E  14  Devil
A8 168: E  14  Devil
A9 169: E  14  Devil
AA 170: E  14  Devil
AB 171: E  14  Devil
AC 172: E  14  Devil
AD 173: E  14  Devil
AE 174: E  14  Devil
AF 175: F  15  Tower
B0 176: F  15  Tower
B1 177: F  15  Tower
B2 178: F  15  Tower
B3 179: F  15  Tower
B4 180: F  15  Tower
B5 181: F  15  Tower
B6 182: F  15  Tower
B7 183: F  15  Tower
B8 184: F  15  Tower
B9 185: F  15  Tower
BA 186: F  15  Tower
BB 187: 10 16  Star
BC 188: 10 16  Star
BD 189: 10 16  Star
BE 190: 10 16  Star
BF 191: 10 16  Star
C0 192: 10 16  Star
C1 193: 10 16  Star
C2 194: 10 16  Star
C3 195: 10 16  Star
C4 196: 10 16  Star
C5 197: 10 16  Star
C6 198: 11 17  Moon
C7 199: 11 17  Moon
C8 200: 11 17  Moon
C9 201: 11 17  Moon
CA 202: 11 17  Moon
CB 203: 11 17  Moon
CC 204: 11 17  Moon
CD 205: 11 17  Moon
CE 206: 11 17  Moon
CF 207: 11 17  Moon
D0 208: 11 17  Moon
D1 209: 11 17  Moon
D2 210: 12 18  Sun
D3 211: 12 18  Sun
D4 212: 12 18  Sun
D5 213: 12 18  Sun
D6 214: 12 18  Sun
D7 215: 12 18  Sun
D8 216: 12 18  Sun
D9 217: 12 18  Sun
DA 218: 12 18  Sun
DB 219: 12 18  Sun
DC 220: 12 18  Sun
DD 221: 12 18  Sun
DE 222: 13 19  Judgment
DF 223: 13 19  Judgment
E0 224: 13 19  Judgment
E1 225: 13 19  Judgment
E2 226: 13 19  Judgment
E3 227: 13 19  Judgment
E4 228: 13 19  Judgment
E5 229: 13 19  Judgment
E6 230: 13 19  Judgment
E7 231: 13 19  Judgment
E8 232: 13 19  Judgment
E9 233: 14 20  Fool
EA 234: 14 20  Fool
EB 235: 14 20  Fool
EC 236: 14 20  Fool
ED 237: 14 20  Fool
EE 238: 14 20  Fool
EF 239: 14 20  Fool
F0 240: 14 20  Fool
F1 241: 14 20  Fool
F2 242: 14 20  Fool
F3 243: 14 20  Fool
F4 244: 14 20  Fool
F5 245: 15 21  World
F6 246: 15 21  World
F7 247: 15 21  World
F8 248: 15 21  World
F9 249: 15 21  World
FA 250: 15 21  World
FB 251: 15 21  World
FC 252: 15 21  World
FD 253: 15 21  World
FE 254: 15 21  World
FF 255: 15 21  World

Magician    : 12
Priestess   : 12
Empress     : 11
Emperor     : 12
Hierophant  : 12
Lovers      : 11
Chariot     : 12
Strength    : 12
Hermit      : 11
Fortune     : 12
Justice     : 11
HangedMan   : 12
Death       : 12
Temperance  : 11
Devil       : 12
Tower       : 12
Star        : 11
Moon        : 12
Sun         : 12
Judgment    : 11
Fool        : 12
World       : 11

11 out of 256 = 4.29688%
12 out of 256 = 4.6875%
Relative increase of 1 in 11 = 9.09% for 12 versus 11
*/