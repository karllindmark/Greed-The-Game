package com.ninetwozero.greedthegame.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.ninetwozero.greedthegame.R;

public class RuleActivity extends Activity {
	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.activity_rule);
		setup();
	}
	
	private void setup() {
		((TextView) findViewById(R.id.text1)).setText(Html.fromHtml(getString(R.string.text_rules)));
	}
}
