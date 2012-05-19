package ua.nevis.view;

import java.io.IOException;

import ua.nevis.R;
import ua.nevis.model.ClientThread;
import ua.nevis.model.Model;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AutorizationActivity extends Activity implements OnClickListener {
    private EditText serverName, clientName;
    private Button connection;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autorization);
        serverName = (EditText) findViewById(R.id.serverName);
        clientName = (EditText) findViewById(R.id.clientName);
        connection = (Button) findViewById(R.id.connection);
        connection.setOnClickListener(this);
    }
	@Override
	public void onClick(View v) {
		try {
            Model.getInstance().setClient(serverName.getText().toString(), clientName.getText().toString());
            Model.getInstance().getClient().sendMessage("@connect;" + clientName.getText().toString() + ";");
            Model.getInstance().getClientCommand().setContext(this);
            new ClientThread();
         } catch (IOException ex) {
        	Log.d(Model.LOG, ex.toString());
        }		
	}
}