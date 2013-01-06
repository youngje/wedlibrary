package yonsei.ac.wedlibrary;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable{

	public final int id;
	public final String name;
	public final int scores;
	public final int lyric;
	public final int mr;
	
	public Song(int id, String name, int scores, int lyric, int mr) {
		this.id = id;
		this.name = name;
		this.scores = scores;
		this.lyric = lyric;
		this.mr = mr;
	}
	
	public Song(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
		this.scores = in.readInt();
		this.lyric = in.readInt();
		this.mr = in.readInt();
	}
	
	public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
		public Song createFromParcel(Parcel in) {
			return new Song(in);
		}
		public Song[] newArray(int size) {
			return new Song[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeInt(id);
		out.writeString(name);
		out.writeInt(scores);
		out.writeInt(lyric);
		out.writeInt(mr);
	}
}
