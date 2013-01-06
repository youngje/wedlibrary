package yonsei.ac.wedlibrary;


import java.util.ArrayList;
import java.util.Calendar;

import yonsei.ac.wedlibrary.R;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class Ranking extends ListActivity {
	
	SQLiteDatabase db;
	RankDatabaseHelper rankDBHelper;
	ArrayList<Points> rankList;
	Calendar calendar;
	String date;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ranking);
		rankDBHelper = new RankDatabaseHelper(this);
		db = rankDBHelper.getWritableDatabase();
		rankList = new ArrayList<Points>();
		calendar = Calendar.getInstance();
		date = calendar.get(Calendar.MONTH)+"."+calendar.get(Calendar.DATE);
			
		Intent intent = getIntent();
        if(intent != null) {
        	double total_point = intent.getDoubleExtra("point", 0);
        	if(intent.getBooleanExtra("hasSong", false)){
            	Song song = intent.getExtras().getParcelable("song");
            	String name = intent.getStringExtra("name");
            	db.execSQL("INSERT INTO rank(songTitle, points, date, name) VALUES('"+song.name+"', '"+(int)total_point+"', '"+date+"', '"+name+"');");
        	}
        }
		Cursor result = db.rawQuery("SELECT * FROM rank ORDER BY points desc limit 15", null);
		result.moveToFirst();
		int i=1;
		while (!result.isAfterLast()){
			   rankList.add(new Points(result.getString(1), result.getString(2), result.getString(3), result.getString(4), i));
			   result.moveToNext();
			   i++;
		}
		result.close();
		db.close();
		
		RankAdapter rankAdapter = new RankAdapter(this, R.layout.rank_list, rankList);
		setListAdapter(rankAdapter);
	}
		
	
}
