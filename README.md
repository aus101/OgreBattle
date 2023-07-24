# OgreBattle

Educational, hacking and speedrunning efforts to understand the amazingly obtuse mechances behind SNES/PSX/SS Ogre Battle continue to this day.  
   
Herein is a Warren Tarot card pulling simulator to determine the odds of various Tarot cards in the opening hand of 7: 6 questions and the 1 bonus card.  
The initial focus is on optimizing the FIRESEAL (Dragon's Haven/Heaven) stage where starting with Fool is a necessity. Then either 1 physical attack or 1 magical attack card.  
Damage cards other than Tower and Sun do more damage the fewer the enemies in the unit, thus Fool boosts damage substantially.  
Magical Tarot cards animate faster than Physical but Albeleo's high INT makes a STR approach faster. Optimal PSX is faster in part due to greater Tarot card damage.  
   
Mathematical derivation of the following odds are in Init.java, which also executes the simulation that verifies the calculated odds.  
  
Total card combinations = COMBIN(22,7) = 170544. All odds can be expressed as an integer ratio with 170544 in the denomiator.  
If not, as in, odds * 170544 is not an integer, then the math is wrong.  
  
Odds of 1 Fool or 1 of any desired card are 31.81%.  
  
Odds of 2 desired cards, such as Fool + Devil, are 9.09%
  
Odds of at least 1 of 2 desired cards are 54.54%  
Odds of at least 1 of 3 desired cards are 70.45%  
  
Odds of 1 desired (i.e. Fool) card AND at least 1 of 2 other cards are 15.09%.  
Odds of 1 desired card (i.e. Fool) AND at least 1 of 3 other cards are 20.93%.  
  
The bonus card drawn at the end alter the Opinion Leader's stats.  
A bonus card with significant STR or INT penalty or insufficient bonus may reduce the odds of success.  
  
OF COURSE, the RNG is biased. 22 Tarot don't cleanly divide 256 and there seems to be some bias to avoid repitition on Joker card pulls.  
Calcualted or simulated odds must be used with the understanding of the inevitable RNG bias of retro video games.  
The true in-game odds could be higher or lower or variable to some extent based on the frame counter, console VRAM or other sources of entropy.  
