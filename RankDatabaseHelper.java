package yonsei.ac.wedlibrary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RankDatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "rank";
	private static final int DB_VERSION = 1;
	
	
	public RankDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE rank(id INTEGER PRIMARY KEY AUTOINCREMENT, songTitle TEXT, points INTEGER, date TEXT, name TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS rank");
		onCreate(db);
	}

}
