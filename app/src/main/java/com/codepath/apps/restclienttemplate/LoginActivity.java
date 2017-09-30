package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.codepath.oauth.OAuthLoginActionBarActivity;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}


	@Override
	public void onLoginSuccess() {
		Intent i = new Intent(this, TweetsListActivity.class);
		startActivity(i);
	}


	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}


	public void loginToRest(View view) {
		getClient().connect();
	}
}
