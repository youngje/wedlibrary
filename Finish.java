package yonsei.ac.wedlibrary;

import yonsei.ac.wedlibrary.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Finish extends Activity{
	
	Song song;
	double points;
	boolean Ranked = false;
	EditText editname;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finish);
		
		Intent intent = getIntent();
		song = intent.getExtras().getParcelable("song");
		points = intent.getDoubleExtra("points", -100);
		Log.d("test", points+"");
		
		TextView pointView = (TextView)findViewById(R.id.finish_point_view);
		pointView.setText((int)points+"");
		
		Button replay = (Button)findViewById(R.id.finish_replay_btn);
		Button songList = (Button)findViewById(R.id.finish_sonlist_btn);
		Button rank = (Button)findViewById(R.id.finish_rank_btn);
		
		editname = (EditText)findViewById(R.id.editname);
		
		replay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent replayIntent = new Intent(Finish.this, Play.class);
				replayIntent.putExtra("song", song);
				startActivity(replayIntent);
			}
		});
		songList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent songListIntent = new Intent(Finish.this, SongList.class);
				songListIntent.putExtra("checker_temp", 0);
				startActivity(songListIntent);
			}
		});
		rank.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent rankIntent = new Intent(Finish.this, Ranking.class);
				if(!Ranked) {
					rankIntent.putExtra("hasSong", true);
					Ranked = true;
				}
				rankIntent.putExtra("song", song);
				rankIntent.putExtra("point", points);
				rankIntent.putExtra("name", editname.getText().toString() );
				startActivity(rankIntent);
			}
		});
	}
	
	public boolean onCreateOptionMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0,1,Menu.NONE,"돌아가기");
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case 1:
				Intent intent = new Intent(Finish.this, SongList.class);
				intent.putExtra("checker_temp", 0);
				startActivity(intent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

