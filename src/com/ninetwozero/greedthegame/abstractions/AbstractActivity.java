package com.ninetwozero.greedthegame.abstractions;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public abstract class AbstractActivity extends Activity {
	
	/**
	 * Variable to hold the currently displaying Toast
	 * @see  AbstractActivity.showToast(String)
	 */
	private Toast mToast;
	
	/**
	 * A wrapper for showToast(String) for passing String resources correctly
	 *
	 * @param  stringResource  A string resource ID
	 * @see    AbstractActivity.showToast(String)
	 */
	protected final void showToast(final int stringResource) {
		showToast(getString(stringResource));
	}
	
	/**
	 * Shows a Toast on the screen, and removes a currently showing one if needed to
	 *
	 * @param  message The text you want to display
	 */
	
	protected final void showToast(final String message) {
		if(null != mToast) {
			mToast.cancel();
		}
		mToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		mToast.show();
	}
	
	/**
	 * Sets a String resource as content of a TextView
	 *
	 * @param  parent The parent view/container
	 * @param  viewId The resource ID for a given view
	 * @param  stringResource The resource ID for the String
	 */
	
	protected void setTextWithResource(final View parent, final int viewId, final int stringResource) {
		((TextView) parent.findViewById(viewId)).setText(stringResource);	
	}

	/**
	 * Sets an Object in String form as content of a TextView
	 *
	 * @param  parent The parent view/container
	 * @param  viewId The resource ID for a given view
	 * @param  text The Object to display in String form
	 */
	
	protected void setText(final View parent, final int resourceId, final Object text) {
		((TextView) parent.findViewById(resourceId)).setText(String.valueOf(text));
	}	
	
	/**
	 * Sets the text color for a specific TextView
	 *
	 * @param  parent The parent view/container
	 * @param  viewId The resource ID for a given view
	 * @param  color  The hex code for the color
	 */
	protected void setTextColor(final View parent, final int viewId, final int color) {
		((TextView) parent.findViewById(viewId)).setTextColor(color);
	}

}
