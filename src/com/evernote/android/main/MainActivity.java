package com.evernote.android.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.InvalidAuthenticationException;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.type.Note;
import com.evernote.thrift.transport.TTransportException;

/**
 * This simple Android app demonstrates how to integrate with the
 * Evernote API (aka EDAM).
 * In this sample, the user authorizes access to their account using OAuth
 */
public class MainActivity extends ParentActivity {

	// Name of this application, for logging
	private static final String LOGTAG = "MainActivity";

	// UI elements that we update
	//private Button mLoginButton;
	private Button mLogoutButton;
	private TextView mMessageTextView;
	//private ListView mListView;
	//private ArrayAdapter mAdapter;

	//Listener to act on clicks
	/*private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
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
	};*/

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		login();
		mLogoutButton = (Button) findViewById(R.id.logout);
		mMessageTextView = (TextView) findViewById(R.id.message);
		mMessageTextView.append("login...");
		/*mLoginButton = (Button) findViewById(R.id.login);

		mListView = (ListView) findViewById(R.id.list);
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				android.R.id.text1,
				getResources().getStringArray(R.array.esdk__main_list));

		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mItemClickListener);*/
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
		//mLoginButton.setEnabled(!mEvernoteSession.isLoggedIn());

		//Show logout button if logged in
		mLogoutButton.setEnabled(mEvernoteSession.isLoggedIn());

		//disable clickable elements until logged in
		//mListView.setEnabled(mEvernoteSession.isLoggedIn());
	}

	/**
	 * Called when the user taps the "Log in to Evernote" button.
	 * Initiates the Evernote OAuth process
	 */

	public void login() {
		mEvernoteSession.authenticate(this);
	}

	/**
	 * Called when the user taps the "Log in to Evernote" button.
	 * Clears Evernote Session and logs out
	 */
	public void onLogoutClick(View view) {
		try {
			mEvernoteSession.logOut(this);
		} catch (InvalidAuthenticationException e) {
			mMessageTextView.append("Tried to call logout with not logged in");
			mMessageTextView.append(e.getMessage());
		}
		updateAuthUi();
	}

	public void onSyncClick(View view) {
		mMessageTextView.append("onSyncClick");
		String contect = SMS.getSmsInPhone(this);
		mMessageTextView.append("contect:"+contect);
		saveNote(contect);
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

	public void saveNote(String content) {
		String title = "SMS";
		if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
			mMessageTextView.append(getString(R.string.empty_content_error));
		}

		mMessageTextView.append("contect:"+content);

		Note note = new Note();
		note.setTitle(title);

		//TODO: line breaks need to be converted to render in ENML
		note.setContent(EvernoteUtil.NOTE_PREFIX + content + EvernoteUtil.NOTE_SUFFIX);

		try {
			mEvernoteSession.getClientFactory().createNoteStoreClient().createNote(note, new OnClientCallback<Note>() {
				@Override
				public void onSuccess(Note data) {
					mMessageTextView.append(getString(R.string.note_saved));
				}

				@Override
				public void onException(Exception exception) {
					mMessageTextView.append("Error saving note");
					mMessageTextView.append(exception.getMessage());
				}
			});
		} catch (TTransportException exception) {
			mMessageTextView.append("Error creating notestore");
			mMessageTextView.append(exception.getMessage());
		}
	}
}
