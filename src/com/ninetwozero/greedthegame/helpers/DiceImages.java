package com.ninetwozero.greedthegame.helpers;

import com.ninetwozero.greedthegame.R;

public final class DiceImages {
	public final static int getFromValue(final int value) {
		switch(value) {
			case 1:
				return R.drawable.dice_1;
			case 2:
				return R.drawable.dice_2;
			case 3:
				return R.drawable.dice_3;
			case 4:
				return R.drawable.dice_4;
			case 5:
				return R.drawable.dice_5;
			case 6:
				return R.drawable.dice_6;
			default:
				return R.drawable.dice_unknown;
		}
	}
}
