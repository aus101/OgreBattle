# OgreBattle

Educational, hacking and speedrunning efforts to understand the amazingly obtuse mechances behind SNES/PSX/SS Ogre Battle continue to this day.  
   
Herein is a Warren Tarot card pulling simulator to determine the exact odds of various Tarot cards in the opening hands of 1 to 7 cards.    
Brute-forcing all hand combinations where order does not matter is done for 6 cards in under 1 second.    
By extension, the optimal answers to the 6 Tarot questions are already derived and given in **ogrebattle.tarot.exact.OddsExample.java** along with Tarot odds of 7 card hands.    

The focus is on optimizing the FIRESEAL (Dragon's Haven/Heaven) stage where starting with Fool or Tower is a necessity, along with another damaging card.   
Damage cards other than Tower and Sun do more damage the fewer the enemies in the unit, thus Fool boosts damage substantially.  
Magical Tarot cards animate faster than Physical but Albeleo's high INT gives STR based cards a chance to be faster. Optimal PSX is faster in part due to later time of day of battle that derates Albeleo's stats more.    
   
Can find mathematical derivation of the exact odds of various Tarot card combinations in the opening hand of 7 cards in Javadoc of **ogrebattle.tarot.simulate.OddsExample.java** that the brute-forced odds verify.   
Computer simulation of the odds remains in the simulate package even though there is no real reason to use in favor of the brute-force code.    
  
The bonus card drawn at the end alters the Opinion Leader's stats.  
A bonus card with significant STR or INT penalty or insufficient bonus may reduce the odds of success in a FIRESEAL run.  
  
OF COURSE, the RNG is biased. 22 Tarot don't cleanly divide 256 and there seems to be some bias to avoid repitition on Joker card pulls, as well as World being favored in PSX.  
Calcualted or simulated odds must be used with the understanding of the inevitable RNG bias of retro video games.  
The true in-game odds could be higher or lower or variable to some extent based on the frame counter, console VRAM or other sources of entropy.

A hardcore approach would attempt frame perfect timing in SNES/SFC to obtain consistent starting cards that would make optimal answer sets irrelevant. Could manip a card set where every answer is 1.   
This approach is far more difficul in PSX due to the card selection not locking after selecting male or female. Maniping more than the first two cards may be impossible in practice.    

The enumerated Tarot cards preserve the in-game ordering of Fool before World. Fool not appearing first is deliberate. It is placed before World in the The Pictoral Key to the Tarot that followed the successful and influential Waite-Smith Tarot deck of 1909.   
Perhaps the ordering will be retconned in a future remaster/revision considering Fool came first in the physical Tactics Ogre PSP preorder deck and is first in most any deck found in modern times.   
