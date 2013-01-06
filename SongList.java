package yonsei.ac.wedlibrary;

import java.util.ArrayList;

import yonsei.ac.wedlibrary.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.*;
import android.widget.Button;

public class SongList extends Activity {
	
	ArrayList<Song> songList;
	
	public void addSongs(){
		songList = new ArrayList<Song>();
		songList.add(new Song(0, "학교종", R.raw.schoolbell_score, R.raw.schoolbell_lyric,R.raw.schoolbell));
		songList.add(new Song(1, "비행기", R.raw.airplane_score, R.raw.airplane_lylic,R.raw.airplane));
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_song_list);
		
		addSongs();
		Intent intent3 = getIntent();
		int checker = intent3.getIntExtra("checker_temp",1 );
		if(checker == 0)
		{}
		else{			
			startActivity(new Intent(this, StartPage.class));
		}

		Button schoolbell_song=(Button)findViewById(R.id.schoolbell_song);
		Button airplane_song=(Button)findViewById(R.id.airplane_song);
		Button ranking=(Button)findViewById(R.id.ranking);
		Button setting=(Button)findViewById(R.id.setting);
		Button help=(Button)findViewById(R.id.help);

		schoolbell_song.setText(songList.get(0).name);
		schoolbell_song.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent play = new Intent(SongList.this, Play.class);
				play.putExtra("song", songList.get(0));
				startActivity(play);
			}
		});
		airplane_song.setText(songList.get(1).name);
		airplane_song.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent play = new Intent(SongList.this, Play.class);
				play.putExtra("song", songList.get(1));
				startActivity(play);
			}
		});
		ranking.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent ranking=new Intent(SongList.this, Ranking.class);
				ranking.putExtra("hasSong", false);
				startActivity(ranking);
			}
		});
		setting.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent setting=new Intent(SongList.this, Setting.class);
				startActivity(setting);
			}
		});
		help.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent help=new Intent(SongList.this, Help.class);
				startActivity(help);
			}
		});
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_song_list,menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.menu_setting:
				Intent menu_setting = new Intent(SongList.this, Setting.class);
				startActivity(menu_setting);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
