package com.ninetwozero.greedthegame.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ninetwozero.greedthegame.datatypes.Die;
import com.ninetwozero.greedthegame.datatypes.Die.Side;
import com.ninetwozero.greedthegame.datatypes.Roll;

public class ScoreCalculator{
    private enum Score {
        ONE(100), FIVE(50), TRIPLET(100), TRIPLET_ONES(1000), STRAIGHT(1000);

        private final int mValue;
        Score(final int value) {
            mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    public Roll getScoreForRoll(final List<Die> dice, final boolean skipDiceWithNormalState) {
    	final StringBuilder messageBuilder = new StringBuilder(48);
    	final List<Die> tempDice = new ArrayList<Die>(dice);
    	int rollScore = 0;
        int tempScore = 0;
        
        if( skipDiceWithNormalState ) {
        	for( Die die : dice ) {
        		if( die.isNormal() ) {
        			tempDice.remove(die);
        		}
        	}
        }
        
        if( isStraight(tempDice) ) {
            tempScore += Score.STRAIGHT.getValue();
            rollScore += tempScore;
            appendToText(messageBuilder, "Straight", tempScore);
        } else {
            int previousScore = -1;
            while((previousScore != rollScore) && !tempDice.isEmpty()) {
                previousScore = rollScore;
                
                if( hasTripletWithOnes(tempDice) ) {
                	tempScore = getScoreForTripletWithOnes(tempDice);
                	rollScore += tempScore;
                	
                	removeTripletWithOnes(tempDice);
                    appendToText(messageBuilder, "Super Triplet", tempScore);
                }
                
                if( hasRegularTriplet(tempDice) ) {
                    tempScore = getScoreForRegularTriplet(tempDice);
                	rollScore += tempScore;
                	
            		removeRegularTriplets(tempDice);
                    appendToText(messageBuilder, "Triplet", tempScore);
                }
                
                if( hasValue(1, tempDice) ) {
                    final int count = getNumberOfOccurences(1, tempDice);
                	tempScore = count * Score.ONE.getValue(); 
                	rollScore += tempScore;
                	
                    removeByValue(1, tempDice);
                    if(1 == count) {
                    	appendToText(messageBuilder, "1 One", tempScore);
                    } else {
                    	appendToText(messageBuilder, count + " Ones", tempScore);
                    }
                }

                if( hasValue(5, tempDice) ) {
                	final int count = getNumberOfOccurences(5, tempDice);
                	tempScore = count * Score.FIVE.getValue();
                	rollScore += tempScore;
                	
                    removeByValue(5, tempDice);
                    if(1 == count) {
                    	appendToText(messageBuilder, "1 Five", tempScore);
                    } else {
                    	appendToText(messageBuilder, count + " Fives", tempScore);
                    }
                }
            }
        }
        if(0 == messageBuilder.length()) {
        	messageBuilder.append("None");
        }
        return new Roll(rollScore, messageBuilder.toString());
    }
    
	private void appendToText(final StringBuilder builder, final String text, final int score) {
		if(0 < builder.length()) {
			builder.append(" & ");
		}
		builder.append(text).append(" (").append(score).append(')');
	}
	
	private int getScoreForTripletWithOnes(List<Die> dice) {
		int numberOfTriplets = getNumberOfOccurences(1, dice)/3;
		return Score.TRIPLET_ONES.getValue() * numberOfTriplets;
	}
	
	private int getScoreForRegularTriplet(final List<Die> tempDice) {
		Set<Side> sides = getTripletSides(tempDice);
		int score = 0;
				
		for( Side side : sides ) {
			score += Score.TRIPLET.getValue() * side.getValue();
		}
		return score;
	}

    private Set<Side> getTripletSides(final List<Die> dice) {
		final List<Die> triplets = getTripletsFromDice(dice);
		final Set<Side> sides = new HashSet<Side>(2);

		for( Die die : triplets ) {
			sides.add(die.getSide());
		}
		
		return sides;
	}
    
	private void removeTripletWithOnes(final List<Die> dice) {
        final List<Die> removalQueue = new ArrayList<Die>(6);
        for( Die die : dice ) {
        	if(1 == die.getValue()) {
                removalQueue.add(die);
            }
        }
        
        int numberInQueue = removalQueue.size();
        if(0 == (numberInQueue % 3)) {
        	dice.removeAll(removalQueue);
        } else {
        	final int numberOfTriplets = numberInQueue/3;
    		for( int i = 0; i < (numberOfTriplets * 3); i++ ) {
    			dice.remove(removalQueue.get(i));
    		}
        }
    }

    private void removeRegularTriplets(final List<Die> dice) {
    	dice.removeAll(getTripletsFromDice(dice));
    }
    
	private List<Die> getTripletsFromDice(final List<Die> dice) {
		final Map<Integer, List<Die>> valueMap = new HashMap<Integer, List<Die>>(2);
        final List<Die> triplets = new ArrayList<Die>(6);
        
        for( Die die : dice ) {
        	final int faceValue = die.getValue();
            if( !valueMap.containsKey(faceValue) ) {
            	valueMap.put(faceValue, new ArrayList<Die>(2));
            }
        	valueMap.get(faceValue).add(die);
        }
        
        for( List<Die> list : valueMap.values() ) {
        	final int numItems = list.size();
        	if(0 == (numItems % 3)) {
        		triplets.addAll(list);
        	} else {
        		final int numberOfTriplets = numItems/3;
        		for( int i = 0; i < (numberOfTriplets * 3); i++ ) {
        			triplets.add(list.get(i));
        		}
        	}
        }
		return triplets;
	}

    private void removeByValue(final int value, final List<Die> dice) {
        final List<Die> removalQueue = new ArrayList<Die>(6);
        for( Die die : dice ) {
        	if( die.getValue() == value ) {
        		removalQueue.add(die);
        	}
        }
        dice.removeAll(removalQueue);
    }

    private int getNumberOfOccurences(final int target, final List<Die> dice) {
        int num = 0;
        for( Die die : dice ) {
            if( die.getValue() == target ) {
                num++;
            }
        }
        return num;
    }

    public boolean isStraight(final List<Die> dice) {
        final int diceCount = dice.size();
    	int uniqueItemsFound = 0;
    	if(6 == diceCount) {
            for( int value = 1; 6 >= value; value++ ) {
                for (Die die : dice) {
                    if (die.getValue() == value) {
                        uniqueItemsFound++;
                        break;
                    }
                }
            }
        }
    	return 6 == uniqueItemsFound;
    }

    public boolean hasTripletWithOnes(final List<Die> dice) {
        int numberOfOnes = 0;
        for (Die die : dice) {
            if (1 == die.getValue()) {
                numberOfOnes++;
            }
        }
        return 3 <= numberOfOnes;
    }

    public boolean hasRegularTriplet(final List<Die> dice) {
        final Map<Integer, Integer> valueMap = new HashMap<Integer, Integer>(2);
        for (Die die : dice) {
            final int value = die.getValue();
            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1);
            }
        }

        for( Integer count : valueMap.values() ) {
            if(3 <= count) {
                return true;
            }
        }
        return false;
    }

    public boolean hasValue(final int value, final List<Die> dice) {
        for( Die die : dice ) {
            if( die.getValue() == value ) {
                return true;
            }
        }
        return false;
    }
}
