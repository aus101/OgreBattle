# OgreBattle

Educational, hacking and speedrunning efforts to understand the amazingly obtuse mechances behind SNES/PSX/SS Ogre Battle continue to this day.  
  
Herein is a Warren Tarot card pulling simulator to determine the odds of various Tarot cards in the opening hand of 7: 6 questions and the 1 bonus card.  
The initial focus is on optimizing the FIRESEAL (Dragon's Haven/Heaven) stage where starting with Fool is a necessity. Then either 1 physical attack or 1 magical attack card that does more damage when the leader is alone.  
Magical Tarot cards animate faster than Physical but Albeleo's high INT makes a STR approach faster on SNES. INT is faster on PSX due to the greater overall damage of Tarot cards.  

In any case:  
Odds of 1 Fool or 1 of any desired card are 31.81%.  
Odds of at least 1 of 3 desired cards are 70.45%  
Combined odds of 1 desired card AND at least 1 of the 3 other cards are 9.09%.  
A bonus card with significant STR/INT penalty may reduce the odds of success.  
  
Mathematical derivation of these odds are in Init.java, which also executes the simulation that verifies these calculated odds.  
  
OF COURSE, the RNG is biased. 22 Tarot don't cleanly divide 256 and there seems to be some bias to avoid repitition on Joker card pulls.  
Calcualted or simulated odds must be used with the understanding of the inevitable RNG bias of retro video games.  
The true in-game odds could be higher or lower or variable to some extent based on the frame counter, console VRAM or other sources of entropy.  
