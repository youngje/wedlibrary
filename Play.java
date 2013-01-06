package yonsei.ac.wedlibrary;

import java.util.ArrayList;


import yonsei.ac.wedlibrary.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class Play extends Activity implements OnClickListener {

	private ImageButton btn_menu;
	private TextView timeVeiw;	
	
	PlayThread playThread;
	MicThread micThread;
	SongHandler songHandle;
	int time;
	ArrayList<Double> points;
	Double point;
	Song song;
	double totalPoint;
	int level;
	boolean ContextMenuOpened = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);
        
		SharedPreferences pref = getSharedPreferences("savestate",MODE_PRIVATE);
		level = pref.getInt("level", 3);
		
        Intent intent = getIntent();
        song = intent.getExtras().getParcelable("song");
        
		songHandle = new SongHandler(this, song.scores, song.lyric, level);
		points = new ArrayList<Double>();
		
        TextView title = (TextView)findViewById(R.id.title);
        title.setText(song.name);
		title.setSelected(true);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.lyric, songHandle.lyric);
		ListView list=(ListView) findViewById(R.id.ListView01);
		list.setAdapter(adapter);
		
        btn_menu = (ImageButton) findViewById(R.id.pause);

        btn_menu.setOnClickListener(this);
        
        registerForContextMenu(btn_menu);               
        startThread();
    }
    
    @Override
    public void onClick(View v)
    {
    	if(v.getId() == R.id.pause)
		{
    		pauseThread();
			this.onButtonClickEvent(btn_menu);
		}
    }
    
    public void onButtonClickEvent(View sender)
    {    	
    	ContextMenuOpened = true;
        registerForContextMenu(sender); 
        openContextMenu(sender);
        unregisterForContextMenu(sender);
    }
    
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

    	switch(v.getId()){
    		case R.id.pause:
    			menu.clear();
    	        menu.add(Menu.NONE,0, Menu.NONE, "계속하기");
    	        menu.add(Menu.NONE,1, Menu.NONE, "다시시작하기");
    	        menu.add(Menu.NONE,2, Menu.NONE, "메인메뉴로");
    	        break;
    	}
        super.onCreateContextMenu(menu, v, menuInfo);
    }     
    
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
	        case 0:
//		    	playThread.pause();
//		    	micThread.pause();
	        	return true;
	        case 1:
	        	try
	    		{
        	        totalPoint=0;
        	        closeThreads();
        	        startThread();
	    		} catch(Exception e)
	    		{
					e.printStackTrace();
	    		}
	        	return true;
	        case 2:
	        	closeThreads();
	        	Intent intent3 = new Intent(Play.this,SongList.class);
	        	intent3.putExtra("checker_temp", 0);
	        	startActivity(intent3);
	        	return true;	        	
        }
		return super.onContextItemSelected(item);
    }
    
    public void onContextMenuClosed(Menu menu)
    {
    	if(ContextMenuOpened) pauseThread();
    	ContextMenuOpened = false;
    }
    
    private final Handler mainHandler = new Handler()	{
		public void handleMessage(Message msg){
			switch(msg.what) {
				case 0:
					timeVeiw.setText(((Integer)msg.arg1).toString());
					break;
				case 1:
					time += 16;
					double point = songHandle.getPoint(SongHandler.bufferToFreq((short[])msg.obj), time);
					points.add(point);
					totalPoint += point;
//					Log.d("point", point+"");
					break;
				case 10:
					Log.i("Play", "===============================================");
					Intent intent = new Intent(Play.this, Finish.class);
					intent.putExtra("points", totalPoint);
					intent.putExtra("song", song);
					playThread = null;
					micThread.close();
					micThread = null;
					startActivity(intent);
					break;
			}
		}
	};
	
	@Override
    public void onDestroy() {
		super.onDestroy();
    	closeThreads();
    }
	
	public void closeThreads() {
		Log.d("Thread", "closeThreads "+ContextMenuOpened);
		if(playThread != null){
			playThread.close();
	    	playThread = null;
		}
		if(micThread != null){
			micThread.close();
	    	micThread = null;
		}
	}
	
	public void pauseThread() {
		Log.d("Thread", "pauseThread "+ContextMenuOpened);
		if(playThread != null) playThread.pause();
		if(micThread != null) micThread.pause();
	}
	
	public void startThread() {
		Log.d("Thread", "startThread "+ContextMenuOpened);
        playThread = new PlayThread(this, song.mr, mainHandler);
        micThread = new MicThread(this, mainHandler);
		playThread.start();
		micThread.start();
	}

	public boolean onKeyDown( int KeyCode, KeyEvent event )
	{
		switch(KeyCode)
		{
			case KeyEvent.KEYCODE_BACK:
				if(ContextMenuOpened) {
//					closeContextMenu();
					return false;
				}
				else {
					this.closeThreads();
					finish();
					return true;
				}
			// true 일경우 back 버튼의 기본동작인 종료를 실행하게 된다.
			// false 일 경우 back 버튼의 기본동작을 하지 않는다.
		}
		return super.onKeyDown(KeyCode, event);
	}
}
