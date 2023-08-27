package ogrebattle.tarot;

/**
 * Changing this order changes the order the hands get sorted in. This order is the order the game holds them in memory and, by extension, the order of cards held.<br>
 * Fool, noted as 0 in-game, occurs between Judgment and World, despite Judgment being XX and World being XI.
 * Not an accident. Is where Fool is placed in the famous Waite-Smith Tarot deck from 1909.<br><br>
 * <code>Magician, Priestess, Empress, Emperor, Hierophant, Lovers, Chariot, Strength, Hermit, Fortune, Justice,
	Hanged_Man, Death, Temperance, Devil, Tower, Star, Moon, Sun, Judgment, Fool, World</code><br>
 */
public enum Tarot {
	Magician, Priestess, Empress, Emperor, Hierophant, Lovers, Chariot, Strength, Hermit, Fortune, Justice,
	Hanged_Man, Death, Temperance, Devil, Tower, Star, Moon, Sun, Judgment, Fool, World;
  /*  
   If not using TarotComparators.java then can use these instead: 
   
    public static Comparator<Tarot> NaturalOrderComparator = new Comparator<Tarot>() {
        public int compare(Tarot t1, Tarot t2) {
          return t1.ordinal() - t2.ordinal();
        }
    };
    
	public static Comparator<Tarot> AlphabeticalComparator = new Comparator<Tarot>() {
		public int compare(Tarot t1, Tarot t2) {
			return t1.toString().compareTo(t2.toString());	
		}
	};
	*/
}