package ua.nevis.model;

import java.io.IOException;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class ClientThread extends Thread{
	private Model model;
	private Bundle b = new Bundle();
	private Message msg;
	public ClientThread() {
        model = Model.getInstance();
        start();
    }
    @Override
    public void run() {
        String serverAnswer;
        try {
            while (!isInterrupted()) {
                if ((serverAnswer = model.getClient().getInput().readLine()) != null) {
                    Log.d(Model.LOG, serverAnswer);
                    msg = model.getClientCommand().getHandler().obtainMessage();
    				b.putString("answer", serverAnswer);
    				msg.setData(b);
    				model.getClientCommand().getHandler().sendMessage(msg);
    				Thread.sleep(100);
                }
            }
        } catch (IOException e) {
            Log.d(Model.LOG, e.toString());
        } catch (InterruptedException e) {
        	Log.d(Model.LOG, e.toString());
		}
    }
}
