package yonsei.ac.wedlibrary;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;

public class MicThread extends Thread {

	private static final int SAMPLING_RATE = 8000;
	private static final int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
	private static final int ENCODING = AudioFormat.ENCODING_PCM_16BIT; 
	
	private static int N = AudioRecord.getMinBufferSize(SAMPLING_RATE, CHANNEL, ENCODING);
	private short[] buffer = new short[256];
	private short[] sendAudioBuffer = new short[256];
	private boolean paused = false;

	private Handler micHandler = null;
	AudioRecord recorder = null;
	Context context = null;
	
	public MicThread(Context context, Handler handler) {
		this.context = context;
		this.micHandler = handler;
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLING_RATE,CHANNEL,ENCODING, N * 10);
		recorder.setPositionNotificationPeriod(256);
		recorder.setRecordPositionUpdateListener(mRecordListener);
	}
	
	public void run() {
		recorder.startRecording();
		recorder.read(buffer, 0, buffer.length);
//		while (!paused) {
//			index = ix++ % buffers.length;
//			buffer = buffers[index];
//			N = recorder.read(buffer, 0, buffer.length);
////			Log.d("N", N+"");
//		}
	}
	
	private AudioRecord.OnRecordPositionUpdateListener mRecordListener = new AudioRecord.OnRecordPositionUpdateListener() {
		@Override
		public void onPeriodicNotification(AudioRecord recorder) {
			recorder.read(sendAudioBuffer, 0, sendAudioBuffer.length);
			process(sendAudioBuffer);
		}
		 
		@Override
		public void onMarkerReached(AudioRecord recorder) {
		}
	};
	
	public void process(short[] sendAudioBuffer) {
		Message msg = Message.obtain();
		msg.what = 1;
		msg.obj = sendAudioBuffer;
		micHandler.sendMessage(msg);
	}
	
	public void pause() {
		if(!paused) {
			paused = true;
			recorder.stop();
		}
		else {
			paused = false;
			recorder.startRecording();
			recorder.read(buffer, 0, buffer.length);
		}
	}
	
	public void close() {
		recorder.stop();
		recorder.release();
		recorder = null;
	}

}
