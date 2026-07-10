package ogrebattle.tarot.pojo;

public enum EQUIPMENT {//8 hex is 8 base 10, and 5B hex is 91 base 10, meant for hex values
START(0x8), RANGE(0x5B), FORMAT_HEX(46), FORMAT_DEC(29);//-1 on formats if range is unbugged 5C (92)
	
	public final int i;
	private EQUIPMENT(int ordinal) {
		i = ordinal;
	}
}
