package com.evernote.android.main;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

public class SMS {
	public static String getSmsInPhone(Context context) {
		final String SMS_URI_ALL = "content://sms/";  
		final String SMS_URI_INBOX = "content://sms/inbox";
		final String SMS_URI_SEND = "content://sms/sent";
		final String SMS_URI_DRAFT = "content://sms/draft";
		final String SMS_URI_OUTBOX = "content://sms/outbox";
		final String SMS_URI_FAILED = "content://sms/failed";
		final String SMS_URI_QUEUED = "content://sms/queued";

		/*StringBuilder smsBuilder = new StringBuilder();

		try {  
			Uri uri = Uri.parse(SMS_URI_ALL);  
			Cursor cur = context.getContentResolver().query(uri, null, null, null, "date desc");      // 获取手机内部短信  
			int count = cur.getColumnCount();
			String[] ColumnNames = cur.getColumnNames();
			while(cur.moveToNext()) {
				smsBuilder.append("\n---------------\n");
				for(int i=0; i<count; i++){
					String value = cur.getString(i);
					Log.i(ColumnNames[i], value);
					smsBuilder.append(ColumnNames[i]);
					smsBuilder.append("   ");
					smsBuilder.append(value);
					smsBuilder.append("\n");
				}
				break;
			} 

			if (!cur.isClosed()) {
				cur.close();
				cur = null;
			}
		} catch (SQLiteException ex) {
			Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
		}
		return smsBuilder.toString();*/
		return "zheng wen hui fist note!";
	}  
}
