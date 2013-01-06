package yonsei.ac.wedlibrary;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;

public class SongHandler {
	ArrayList<SongScore> scores;
	ArrayList<String> lyric;
	int index;
	int level;
	double Max_point = 1000;
	public HashMap<String, Double> scoreToFreq;
	
	public SongHandler(){};
	
	public SongHandler(Activity activity, int songid, int lyricid, int level) {
		setTable();
		index = 0;
		this.level = level;
		scoreRead(activity, songid);
		lyricRead(activity, lyricid);
	}
	
	private void scoreRead(Activity activity, int songid){
		try {
			scores = new ArrayList<SongScore>();
			
			Resources resource = activity.getResources();
			InputStream in = resource.openRawResource(songid);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			String[] tokens = null;
			while((line = reader.readLine()) != null){
				tokens = line.split(" ");
				SongScore score = new SongScore(Integer.parseInt(tokens[0]), getFreq(tokens[1]));
				scores.add(score);
				Log.d("score", score.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void lyricRead(Activity activity, int lyricid){
		try {
			lyric = new ArrayList<String>();
			
			Resources resource = activity.getResources();
			InputStream in = resource.openRawResource(lyricid);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String line = null;
			while((line = reader.readLine()) != null){
				lyric.add(line);
				Log.d("score", line.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setTable(){
		this.scoreToFreq = new HashMap<String, Double>();
		scoreToFreq.put("C0", 32.7032);
		scoreToFreq.put("C#", 34.6478);
		scoreToFreq.put("D0", 36.7081);
		scoreToFreq.put("D#", 38.8909);
		scoreToFreq.put("E0", 41.2034);
		scoreToFreq.put("F0", 43.6535);
		scoreToFreq.put("F#", 46.2493);
		scoreToFreq.put("G0", 48.9994);
		scoreToFreq.put("G#", 51.9130);
		scoreToFreq.put("A0", 55.0000);
		scoreToFreq.put("A#", 58.2705);
		scoreToFreq.put("B0", 61.7354);
	}
	
	private double getFreq(String freq) {
		Log.d("socres", freq);
		if(freq.equals("z")){
			return 0;
		}
		return scoreToFreq.get(freq.substring(0, 2)) * Math.pow(2, Integer.parseInt(freq.substring(2))-1);
	}
	
	public static Complex[] bufferToFreq(short[] buffer) {
		double[] micBufferData = new double[buffer.length/2];//size may need to change
	    final int bytesPerSample = 2; // As it is 16bit PCM
	    final double amplification = 1.0; // choose a number as you like
	    int floatIndex = 0;
	    for (int index = 0; index < buffer.length - bytesPerSample + 1; index += bytesPerSample, floatIndex++) {
	        double sample = 0;
	        for (int b = 0; b < bytesPerSample; b++) {
	            int v = buffer[index + b];
	            if (b < bytesPerSample - 1 || bytesPerSample == 1) {
	                v &= 0xFF;
	            }
	            sample += v << (b * 8);
	        }
	        double sample32 = amplification * (sample / 32768.0);
	        micBufferData[floatIndex] = sample32;
	    }
//	    Log.d("N", floatIndex+" "+buffer.length);
	    
	    Complex[] x = new Complex[floatIndex%2==0?floatIndex:floatIndex-1];
	    for(int i=0; i<x.length; i++) {
	    	x[i] = new Complex(micBufferData[i], 0);
	    }
	    
	    Complex[] y = FFT.fft(x);
	    
	    return y;
	}
	
	public double getPoint(Complex[] y, int time) {
//		double total=0, target=0;
//	    double min,max;
//	    double exactFreq = y.length * timeToFreq(time) / 4000.0;
//	    
//	    min = exactFreq *0.8;
//	    max = exactFreq *1.2;
//	   
//	    Log.d("min", time+" "+timeToFreq(time)+" "+min+" "+max);
//	    
//	    for(int i=0;i<y.length;i++) {
//	    	if(i > min && i < max) target+=y[i].abs();
//	    	total+=y[i].abs();
//	    	if(y[i].abs() > 10) Log.d("peak",i+"");
//	    }
//	    Log.d("total",total+"");
//	    return target/total * 100;
		
		double max=0;
		int index_at_max=0;
		double inputFreq;
		double exactFreq = y.length * timeToFreq(time) / 4000.0;
		double score;
		
		for(int i=0;i<y.length/2;i++) {
	    	if(y[i].abs() > max) {
	    		max=y[i].abs();
		    	index_at_max=i;
	    	}
//			Log.d("fft",y[i].abs()+"");
	    }
		inputFreq = index_at_max*4000.0/y.length;
		
		Log.d("freq",inputFreq+" "+index_at_max+" "+exactFreq);
		score = Max_point - Math.abs(inputFreq - exactFreq)*level;
		if(score < 0) score = 0;
		return score; // 최고 점수 1000으로 임의 지정
		
	}
	
	public double timeToFreq(int time){
		if(scores.get(index).endTime>=time) {
			return scores.get(index).frequency;
		}
		else {
			index++;
			return timeToFreq(time);
		}
	}
}
