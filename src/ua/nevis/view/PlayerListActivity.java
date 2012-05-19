package ua.nevis.view;

import ua.nevis.R;
import ua.nevis.model.Model;
import ua.nevis.model.Player;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class PlayerListActivity extends Activity {
	private boolean activ = true;
	private ListView playerlist;
	private Button invite;
	private String [] message;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playerlist);
		Model.getInstance().getClientCommand().setContext(this);
		playerlist = (ListView) findViewById(R.id.playerlist);
		playerlist.setAdapter(Model.getInstance().getPlAdapter());
		playerlist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				Model.getInstance().getPlAdapter().setOpponent(position);
			}
		});
		invite = (Button) findViewById(R.id.invitebtn);
		invite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Player _opponent = Model.getInstance().getPlAdapter().getOpponent();
				if (_opponent != null) {
					invite.setEnabled(false);
					Model.getInstance().getClient().sendMessage("@invite;" + _opponent.getName() + ";"
		                    + _opponent.getHashCode() + ";");
				}
			}
		});
	}
	public void setMessage(String [] msg) {
		message = msg;
	}
	@Override
	protected void onDestroy() {
		if (activ) Model.getInstance().getClient().sendMessage("@exit;");
		super.onDestroy();
	}
	public void setActiv(boolean act) {
		activ = act;
	}
	public Button getInvite() {
		return invite;
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 1) {
	        AlertDialog.Builder adb = new AlertDialog.Builder(this);
	        adb.setTitle(R.string.inv);
	        adb.setMessage("Player [" + message[1] + "] invite you to game!");
	        adb.setPositiveButton(R.string.yes, myClickListener);
	        adb.setNegativeButton(R.string.no, myClickListener);
	        return adb.create();
	      }
		return super.onCreateDialog(id);
	}
	OnClickListener myClickListener = new OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
	      switch (which) {
	      case Dialog.BUTTON_POSITIVE:{
	    	  Model.getInstance().getClient().sendMessage("@acceptinvite;" + message[1] + ";" + message[2] + ";");
	    	  break;
	      }
	      case Dialog.BUTTON_NEGATIVE:{
	    	  Model.getInstance().getClient().sendMessage("@rejectinvite;" + message[1] + ";" + message[2] + ";");
	    	  break;
	      }
	      }
		}
	};
}
