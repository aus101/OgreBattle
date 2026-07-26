package ogrebattle.tarot.pojo;

public class Quantity {//not forcing Java 14+ to use records though would be nice
	public final String NAME;
	public final int START;
	public final int RANGE;
	public final int LENGTH;
	public final int FORMAT_HEX;
	public final int FORMAT_DEC;
	
	public Quantity(String name, int start, int range, int formatHex, int formatDec) {
		this.NAME = name;
		this.START = start;
		this.RANGE = range;
		this.FORMAT_HEX = formatHex;
		this.FORMAT_DEC = formatDec;
		this.LENGTH = this.RANGE - this.START +1;//+1 because initial index counts, i.e. 0 to 4 is length of 5
	}
}
