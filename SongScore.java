package yonsei.ac.wedlibrary;

public class SongScore {
	public int endTime;
	public double frequency;
	
	public SongScore(int end, double freq) {
		this.endTime = end;
		this.frequency = freq;
	}
	
	@Override
	public String toString() {
		return "SongScore [endTime=" + endTime + ", frequency=" + frequency
				+ "]";
	}
}
