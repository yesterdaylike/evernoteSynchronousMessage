package com.evernote.android.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.InvalidAuthenticationException;

/**
 * This simple Android app demonstrates how to integrate with the
 * Evernote API (aka EDAM).
 * In this sample, the user authorizes access to their account using OAuth
 */
public class MainActivity extends ParentActivity {

	// Name of this application, for logging
	private static final String LOGTAG = "MainActivity";

	// UI elements that we update
	private Button mLoginButton;
	private Button mLogoutButton;
	private ListView mListView;
	private ArrayAdapter mAdapter;

	//Listener to act on clicks
	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			switch(position) {
			case 0:
				startActivity(new Intent(getApplicationContext(), ImagePicker.class));
				break;
			case 1:
				startActivity(new Intent(getApplicationContext(), SimpleNote.class));
				break;
			case 2:
				startActivity(new Intent(getApplicationContext(), SearchNotes.class));
			}
		}
	};

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mLoginButton = (Button) findViewById(R.id.login);
		mLogoutButton = (Button) findViewById(R.id.logout);
		mListView = (ListView) findViewById(R.id.list);
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				android.R.id.text1,
				getResources().getStringArray(R.array.esdk__main_list));

		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mItemClickListener);
	}

	@Override
	public void onResume() {
		super.onResume();
		updateAuthUi();
	}

	/**
	 * Update the UI based on Evernote authentication state.
	 */
	private void updateAuthUi() {
		//show login button if logged out
		mLoginButton.setEnabled(!mEvernoteSession.isLoggedIn());

		//Show logout button if logged in
		//    mLogoutButton.setEnabled(mEvernoteSession.isLoggedIn());

		//disable clickable elements until logged in
		mListView.setEnabled(mEvernoteSession.isLoggedIn());
	}

	/**
	 * Called when the user taps the "Log in to Evernote" button.
	 * Initiates the Evernote OAuth process
	 */

	public void login(View view) {
		mEvernoteSession.authenticate(this);
	}

	/**
	 * Called when the user taps the "Log in to Evernote" button.
	 * Clears Evernote Session and logs out
	 */
	public void logout(View view) {
		try {
			mEvernoteSession.logOut(this);
		} catch (InvalidAuthenticationException e) {
			Log.e(LOGTAG, "Tried to call logout with not logged in", e);
		}
		updateAuthUi();
	}

	/**
	 * Called when the control returns from an activity that we launched.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		//Update UI when oauth activity returns result
		case EvernoteSession.REQUEST_CODE_OAUTH:
			if (resultCode == Activity.RESULT_OK) {
				updateAuthUi();
			}
			break;
		}
	}
}
