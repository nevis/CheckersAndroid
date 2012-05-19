package ua.nevis.view;

import ua.nevis.R;
import ua.nevis.model.Model;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ChooseGameActivity extends Activity implements OnClickListener {
	private Button btn;
	private LinearLayout cglayout;
	private LinearLayout.LayoutParams layoutParams;
	private String [] gameList;
	private boolean activ = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choosegame);
		
		cglayout = (LinearLayout) findViewById(R.id.cglayout);
		layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		Intent intent = getIntent();
		gameList = intent.getStringArrayExtra("games");
		createChooseButton();
	}
	private void createChooseButton() {
		for (int i = 1; i < gameList.length; i++) {
			btn = new Button(this);
			btn.setText(gameList[i].toString());
			btn.setTextSize(25);
			btn.setOnClickListener(this);
			cglayout.addView(btn, layoutParams);
		}
	}
	@Override
	public void onClick(View v) {
		Button b = (Button) v;
		String name = b.getText().toString();
        for (int i = 1; i < gameList.length; i++) {
            if (gameList[i].equals(name)) {
                Model.getInstance().getClient().sendMessage("@game;" + i);
                Model.getInstance().setGameType(i);
                activ = false;
                Intent intent = new Intent(this, PlayerListActivity.class);
            	startActivity(intent);
                finish();
            }
        }
	}
	@Override
	protected void onDestroy() {
		if (activ) Model.getInstance().getClient().sendMessage("@exit;");
		super.onDestroy();
	}
}
