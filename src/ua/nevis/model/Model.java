package ua.nevis.model;

import java.io.IOException;

import ua.nevis.controller.CheckersAdapter;
import ua.nevis.controller.ClientCommand;
import ua.nevis.controller.PlayerListAdapter;
import android.content.Context;

public class Model {
	public static final String LOG = "log";
	private static Model instance = null;
	public static Model getInstance() {
		if (instance == null) {
			instance = new Model();
		}
		return instance;
	}
	private Client client;
    private PlayerListAdapter plAdapter;
	private Commands commands = new Commands();
    private ClientCommand clientCommand = new ClientCommand();
    private CheckersAdapter checkers = new CheckersAdapter(this);
    private int gameType = 0;
    
    public Client getClient() {
        return client;
    }
    public void setClient(String serverName, String name) throws IOException {
         client = new Client(serverName, name);
    }
    public Commands getCommands() {
        return commands;
    }
    public ClientCommand getClientCommand() {
        return clientCommand;
    }
    public CheckersAdapter getCheckers() {
        return checkers;
    }
    public void setGameType(int type) {
        gameType = type;
    }
    public int getGameType() {
        return gameType;
    }
	public PlayerListAdapter getPlAdapter() {
		return plAdapter;
	}
	public void setPlAdapter(Context _context) {
		plAdapter = new PlayerListAdapter(_context);
	}
}
