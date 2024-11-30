# OgreBattle
## Purpose of the Repository
Educational, hacking and speedrunning efforts to understand the amazingly obtuse and complex mechances behind SNES/PSX/SS Ogre Battle continue to this day.  
   
Herein is an Ogre Battle Tarot card generator to determine the exact odds of various Tarot cards in the opening hands of 1 to 7 cards. A New Game begins with 7 cards: 6 from questions and 1 from the bonus pick.    
Brute-forcing all hand combinations where order does not matter is done for 6 cards in under 1 second and under 2 seconds for 7 cards on a stock i5 desktop computer.    
No external Java libraries are used. No need for Maven or Gradle.            

The focus is solving for the optimal set of answers to Warren's 6 question for each Lord type as well as calculating exact odds of specific Tarot cards in the first 7 cards.    
Lord type answers are solved in **ogrebattle.tarot.exact.LordOddsExample.java** and Tarot card odds are solved in **ogrebattle.tarot.exact.LordOddsExample.java** and can be seen by running the mains.     
Alternatively and equivalently, calculations for Tarot cards odds are calculated exactly in **ogrebattle.tarot.simulate.CardOddsExample.java**'s Javadoc. More complex probabilities would need the code approach.  
     
Previously published sets of answers for each Lord type had about a 65% success rate. The odds are raised here to 99.18-100%.   
The Lord type with the second highest and third highest point totals do affect starting units. Solutions for Ianuki 1st, Ice Cloud 2nd and Phantom 1st, Ice Cloud 2nd are given since they are relevant for FIRESEAL speedrunning. These odds are 80.64% and 61.45%, respectively.    
Tarot card odds for iterating each hand and counting in-code, compared with both combinatorics and hypergeometic distribution calculations, match perfectly.    

## Additional Details
The code is robust enough to work with hands of 1 to 7 cards, or more cards if using the simulate package that converges to the correct odds where brute-forcing would be impractical.    
The bonus card drawn at the end alters the Opinion Leader's stats. This repository does not record or delve into the stat changes.    

## Note of Caution
OF COURSE, the RNG is biased and the calculated odds do not take RNG bias into account. 22 Tarot don't cleanly divide 256 and there seems to be some bias to avoid repitition on Joker card pulls. PSX can draw an "impossible" 1 World in the questions and another World as the bonus card.     

The true in-game odds could be higher or lower or variable to some extent based on the 0-255 RNG byte(s), Opinion Leader name and sex, the frame counter, or other sources of entropy for the starting seed and randomness. Most likely in SNES not all starting 7 card hands are possible.       

A hardcore approach would attempt frame perfect timing in SNES/SFC to obtain consistent starting cards that would make optimal answer sets irrelevant. Could manip a card set where every answer is 1.   
This approach is far more difficul in PSX due to the card selection not locking after selecting male or female. Maniping more than the first two cards may be impossible in practice.    

## Order of Cards
The enumerated Tarot cards preserve the in-game ordering of Fool before World. Fool not appearing first is deliberate. It is placed before World in the The Pictoral Key to the Tarot that followed the successful and influential Waite-Smith Tarot deck of 1909.   
Perhaps the ordering will be retconned in a future remaster/revision considering Fool came first in the physical Tactics Ogre PSP preorder deck and is first in most any deck found in modern times.   

## Notice
Not for distribution, or use, or use or distribution of a deriviative work on:     
```
    - Fandon or Fandom-owned properties, including GameFAQs.
    - Video with video sponsor or other form of paid promotion.
    - Video with any association to lootboxes or gacha games.
```
Derivate works must include and follow this notice.
