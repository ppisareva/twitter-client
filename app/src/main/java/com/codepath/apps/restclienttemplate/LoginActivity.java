package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LoginActivity extends AppCompatActivity {

	TwitterLoginButton loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
		loginButton.setCallback(new Callback<TwitterSession>() {
			@Override
			public void success(Result<TwitterSession> result) {
				TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
				TwitterAuthToken authToken = session.getAuthToken();
				TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
				String token = authToken.token;
				Log.d(" token ========= ", token);
				SharedPreferences prefs = getSharedPreferences(Utils.APP_SHARED_VALUE
						, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(Utils.TOCKEN, token);
				editor.commit();
				startActivity(new Intent(LoginActivity.this, TweetsListActivity.class));

			}

			@Override
			public void failure(TwitterException exception) {
				Toast.makeText(LoginActivity.this, getString(R.string.fail_to_log_in), Toast.LENGTH_LONG);
				exception.printStackTrace();
			}
		});

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Pass the activity result to the login button.
		loginButton.onActivityResult(requestCode, resultCode, data);
	}



}
