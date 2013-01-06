package yonsei.ac.wedlibrary;


import yonsei.ac.wedlibrary.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Setting extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		SharedPreferences pref = getSharedPreferences("savestate",MODE_PRIVATE);
		final SharedPreferences.Editor edit = pref.edit();

		final RankDatabaseHelper rankDBHelper = new RankDatabaseHelper(this);
		
		RadioGroup diffLevel = (RadioGroup)findViewById(R.id.radioGroup1);
		RadioButton diffEasy = (RadioButton)findViewById(R.id.radio0);
		RadioButton diffNormal = (RadioButton)findViewById(R.id.radio1);
		RadioButton diffHard = (RadioButton)findViewById(R.id.radio2);
		Button resetDB = (Button)findViewById(R.id.buttonResetDB);
		
		switch(pref.getInt("level", 3)){
		case 1:
			diffEasy.setChecked(true);
			break;
		case 3:
			diffNormal.setChecked(true);
			break;
		case 5:
			diffHard.setChecked(true);
			break;
		}
		
		diffLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(group.getId() == R.id.radioGroup1){
					switch(checkedId){
					case R.id.radio0:
						edit.putInt("level", 1);
						edit.commit();
						break;
					case R.id.radio1:
						edit.putInt("level", 3);
						edit.commit();
						break;
					case R.id.radio2:
						edit.putInt("level", 5);
						edit.commit();
						break;
					}
				}
			}
		});		
		
		
		resetDB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Builder dlg= new AlertDialog.Builder(Setting.this);
	                dlg.setTitle("선택하세요")
	                .setMessage("프로그램을 끝낼까요?")
	                .setIcon(R.drawable.ic_launcher)
	                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                		SQLiteDatabase db = rankDBHelper.getWritableDatabase();
	        				rankDBHelper.onUpgrade(db, 0, 0);
	        				db.close();
	                        Toast.makeText(Setting.this,"랭킹이 초기화되었습니다.", Toast.LENGTH_SHORT).show();
	                    }
	                })
	                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    }
	                })
	                .show();
				
				
			}
		});
	}
}
