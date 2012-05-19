package ua.nevis.controller;

import ua.nevis.R;
import ua.nevis.model.Model;
import ua.nevis.model.Player;
import ua.nevis.view.AutorizationActivity;
import ua.nevis.view.CheckersActivity;
import ua.nevis.view.ChooseGameActivity;
import ua.nevis.view.PlayerListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ClientCommand {
	private Handler handler;
	private Context context;
	private Intent intent;
	public ClientCommand() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String [] str = msg.getData().getString("answer").split("\\;");
		        String command = str[0];
		        switch (Model.getInstance().getCommands().getClientCommand(command)) {
	            case Connected: {
	                connected(str);
	                break;
	            }
	            case PlayerList: {
	                playerList(str);
	                break;
	            }
	            case AddPlayer: {
	                addPlayer(str);
	                break;
	            }
	            case RemovePlayer: {
	                removePlayer(str);
	                break;
	            }
	            case Invite: {
	                invite(str);
	                break;
	            }
	            case NoFindPlayer: {
	                noFind(str);
	                break;
	            }
	            case AcceptGame: {
	                acceptGame(str);
	                break;
	            }
	            case RejectInvite: {
	                reject(str);
	                break;
	            }
	            case Lose: {
	                ((CheckersActivity) context).showDialog(1);
	                break;
	            }
	            case Win: {
	            	((CheckersActivity) context).showDialog(2);
	                break;
	            }
	            case EnemyOff: {
	            	((CheckersActivity) context).showDialog(3);
	                break;
	            }
	            case Turn: {
	                turn(str);
	                break;
	            }
	            case Kill: {
	                kill(str);
	                break;
	            }
	            default: {
	                break;
	            }
		        }
				super.handleMessage(msg);
			}
		};
	}
	public Handler getHandler() {
		return handler;
	}
	public void setContext(Context context) {
		this.context = context;		
	}
	public Context getContext() {
		return context;
	}
	private void connected(String [] str) {
		Model.getInstance().getClient().setConnected(true);
		intent = new Intent(context, ChooseGameActivity.class);
		intent.putExtra("games", str);
		context.startActivity(intent);
		((AutorizationActivity) context).finish();
    }
    private void playerList(String [] str) {
    	Model.getInstance().setPlAdapter(context);
    	Model.getInstance().getPlAdapter().removeAllPlayers();
    	for (int i = 1; i < str.length; ) {
            Model.getInstance().getPlAdapter().addPlayer(new Player(str[i], Integer.parseInt(str[i + 1])));
            i += 2;
        }
    }
    private void addPlayer(String [] str) {
        Model.getInstance().getPlAdapter().addPlayer(new Player(str[1], Integer.parseInt(str[2])));
    }
    private void removePlayer(String [] str) {
        Model.getInstance().getPlAdapter().removeFromPlayerListByHashCode(Integer.parseInt(str[2]));
    }
    private void invite(String [] str) {
        ((PlayerListActivity) context).setMessage(str);
        ((PlayerListActivity) context).showDialog(1);
    }
    private void noFind(String [] str) {
        Toast.makeText(context, "Player [" + str[1] + "] no find!", Toast.LENGTH_LONG).show();
    	((PlayerListActivity) context).getInvite().setEnabled(true);
    }
    private void reject(String [] str) {
    	Toast.makeText(context, "Player [" + str[1] + "] reject you invite!", Toast.LENGTH_LONG).show();
    	((PlayerListActivity) context).getInvite().setEnabled(true);
    }
    private void acceptGame(String [] str) {
    	((PlayerListActivity) context).setActiv(false);
		switch (Model.getInstance().getGameType()) {
        case 1: {
            CheckersGame(str);
            break;
        }
        /*case 2: {
            BattleshipGame();
            break;
        }*/
        default: {
            break;
        }
		}
		((PlayerListActivity) context).finish();
    }
    private void CheckersGame(String [] str) {
    	Model.getInstance().getCheckers().setChip(Integer.parseInt(str[1]));
    	Model.getInstance().getCheckers().refreshCheckersBoard();
    	intent = new Intent(context, CheckersActivity.class);
		context.startActivity(intent);
    }
    private void kill(String [] str) {
        Model.getInstance().getCheckers().setCheckersBoardValue(reverse(Integer.parseInt(str[1])),
                reverse(Integer.parseInt(str[2])), 0);
        Model.getInstance().getCheckers().setCheckersBoardValue(reverse(Integer.parseInt(str[3])),
                reverse(Integer.parseInt(str[4])),  Integer.parseInt(str[8]));
        Model.getInstance().getCheckers().setCheckersBoardValue(reverse(Integer.parseInt(str[1]) + Integer.parseInt(str[5])),
                reverse(Integer.parseInt(str[2]) + Integer.parseInt(str[6])), 0);
        Model.getInstance().getCheckers().notifyDataSetChanged();
        if (str[7].equals("end")) {
        	Model.getInstance().getCheckers().setTurn(true);
        	((CheckersActivity) context).getTurn().setText(R.string.youturn);
        }
    }
    private void turn(String [] str) {
        Model.getInstance().getCheckers().setCheckersBoardValue(reverse(Integer.parseInt(str[1])),
                reverse(Integer.parseInt(str[2])), 0);
        Model.getInstance().getCheckers().setCheckersBoardValue(reverse(Integer.parseInt(str[3])),
                reverse(Integer.parseInt(str[4])), Integer.parseInt(str[5]));
        Model.getInstance().getCheckers().notifyDataSetChanged();
        Model.getInstance().getCheckers().setTurn(true);
        ((CheckersActivity) context).getTurn().setText(R.string.youturn);
    }
    private int reverse(int number) {
        switch (number) {
            case 0: return 7;
            case 1: return 6;
            case 2: return 5;
            case 3: return 4;
            case 4: return 3;
            case 5: return 2;
            case 6: return 1;
            case 7: return 0;
            default: return -1;
        }
    }
}
