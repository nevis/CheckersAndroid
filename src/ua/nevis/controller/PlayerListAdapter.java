package ua.nevis.controller;

import java.util.ArrayList;

import ua.nevis.R;
import ua.nevis.model.Player;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayerListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Player> playerList = new ArrayList<Player>();
	private Context context;
    private Player opponent;
	public PlayerListAdapter(Context _context) {
		context = _context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return playerList.size();
	}

	@Override
	public Object getItem(int index) {
		return playerList.get(index);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.player, parent, false);
		}
		Player _player = playerList.get(position);
		((TextView) view.findViewById(R.id.playername)).setText(_player.getName());
		((TextView) view.findViewById(R.id.playerid)).setText("" + _player.getHashCode());
		return view;
	}
	
	public void addPlayer(Player _player) {
        playerList.add(_player);
        notifyDataSetChanged();
     }
	public void removeAllPlayers() {
		playerList.removeAll(playerList);
		notifyDataSetChanged();
	}
    public void removeFromPlayerListByHashCode(int hashCode) {
    	for (int i = 0; i < playerList.size(); i++){
    		Player _player = playerList.get(i);
    		if (_player.getHashCode() == hashCode) {
    			playerList.remove(_player);
    			notifyDataSetChanged();
    		}
    	}
    }
    public void setOpponent(int opponentIndex) {
        if (opponentIndex > -1) opponent = playerList.get(opponentIndex);
    }
    public Player getOpponent() {
        return opponent;
    }
}
