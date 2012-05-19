package ua.nevis.view;

import ua.nevis.R;
import ua.nevis.model.Model;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class CheckersActivity extends Activity {
	private TextView turn;
	private boolean activ = true;
	private Intent intent;
	private GridView checkersView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkers);
		Model.getInstance().getClientCommand().setContext(this);
		turn = (TextView) findViewById(R.id.turn);
		setTurnState();
		checkersView = (GridView) findViewById(R.id.checkersView);
		checkersView.setAdapter(Model.getInstance().getCheckers());
		checkersView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				if (Model.getInstance().getCheckers().getTurn()) {
					Log.d(Model.LOG, "row=" + position/8 + ", column=" + position%8);
					Model.getInstance().getCheckers().clientTurn((int)position/8, (int)position%8);
		            if (!Model.getInstance().getCheckers().getTurn()) turn.setText(R.string.enemyturn);
		        }
			}
		});
        intent = new Intent(this, PlayerListActivity.class);
	}
    private void setTurnState() {
    	if (Model.getInstance().getCheckers().getChip() == 1) {
    		Model.getInstance().getCheckers().setTurn(true);
    		turn.setText(R.string.youturn);
        }
        else {
        	Model.getInstance().getCheckers().setTurn(false);
        	turn.setText(R.string.enemyturn);
        }
    }
    public TextView getTurn() {
    	return turn;
    }
	@Override
	protected void onDestroy() {
		if (activ) Model.getInstance().getClient().sendMessage("@exit;");
		super.onDestroy();
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.app_name);
        adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {
    		      switch (which) {
    		      case Dialog.BUTTON_POSITIVE:{
    		    	  startActivity(intent);
    		    	  activ = false;
    		    	  finish();
    		    	  break;
    		      }
    		      }
    			}
    		});
        if (id == 1) {
	        adb.setMessage("You lose!");
	        return adb.create();
	    } else if (id == 2) {
	        adb.setMessage("You win!");
	        return adb.create();
	    } else if (id == 3) {
	        adb.setMessage("Enemy left game!");
	        return adb.create();
	    }
		return super.onCreateDialog(id);
	}
}
