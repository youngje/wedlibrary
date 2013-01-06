package yonsei.ac.wedlibrary;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RankAdapter extends ArrayAdapter<Points>{
	
    private ArrayList<Points> rankList = null;
    private Context mContext;
	
	public RankAdapter(Context context, int textViewResourceId, ArrayList<Points> objects) {
		super(context, textViewResourceId, objects);
		mContext = context;
		rankList = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v==null) {
			LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.rank_list, null);
		}
		Points p = rankList.get(position);
		if(p != null){
			TextView rank = (TextView) v.findViewById(R.id.rank_rank);
			TextView point = (TextView) v.findViewById(R.id.rank_point);
			TextView songTitle = (TextView) v.findViewById(R.id.rank_songtitle);
			TextView name = (TextView) v.findViewById(R.id.rank_name);
			
			rank.setText(p.rank+"");
			point.setText(p.points);
			songTitle.setText(p.songTitle);
			name.setText(p.name);
		}
		return v;
	}
}
