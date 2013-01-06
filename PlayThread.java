package yonsei.ac.wedlibrary;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PlayThread extends Thread {
	MediaPlayer mp = null;
	Handler pHandler;
	
	public PlayThread(Context context, int id, Handler playHandler) {
		mp = MediaPlayer.create(context, id);
		this.pHandler = playHandler;
		mp.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				Log.i("Play", "===============================================");
				mp.stop();
				mp.release();
				pHandler.sendEmptyMessage(10);
			}
		});
	}
	
	@Override
	public void run() {
		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
		mp.seekTo(0);
		mp.start();
	}

	public void close() {
		mp.stop();
		mp.release();
	}
	
	public void pause() {
		if(mp.isPlaying() == true) mp.pause();
		else mp.start();
	}
	
	public void time() {
		Message msg = Message.obtain();
		msg.what = 0;
		msg.arg1 = mp.getCurrentPosition();
		pHandler.sendMessage(msg);
	}
	
}
