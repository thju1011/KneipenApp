package de.kneipe.kneipenquartett.ui.main;

import de.kneipe.R;
import de.kneipe.R.id;
import de.kneipe.R.layout;
import de.kneipe.R.menu;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.service.BenutzerService;
import de.kneipe.kneipenquartett.service.BenutzerService.BenutzerServiceBinder;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Main extends Activity implements OnClickListener {
	private static final String LOG_TAG = Main.class.getSimpleName();
	
	private BenutzerServiceBinder benutzerServiceBinder;
	
	private ServiceConnection benutzerServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
			Log.v(LOG_TAG, "onServiceConnected() fuer KundeServiceBinder");
			benutzerServiceBinder = (BenutzerServiceBinder) serviceBinder;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			benutzerServiceBinder = null;
		}
	};
	
	public BenutzerServiceBinder getBenutzerServiceBinder() {
		return benutzerServiceBinder;
	}
	
	
	BenutzerService bs = new BenutzerService();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onClick(View view) {
		// Eingabetext ermitteln
		EditText benutzerid = (EditText) findViewById(R.id.nutzername);
		EditText passwort = (EditText) findViewById(R.id.passwort);
		String benutzerStr = benutzerid.getText().toString();
		String benutzerpw = passwort.getText().toString();
		if(TextUtils.isEmpty(benutzerStr) ||TextUtils.isEmpty(benutzerpw) )
			benutzerid.setError("Eingabe fehlt");
		
		Benutzer login = new Benutzer();
		login.username= benutzerStr;
		login.password = benutzerpw;
		
		
		//VORÜBERGEHEND WIRD HIER NUR EIN GET ABGESETZT
		
		
		
		// SERVER AUFRUF! Vergleich gleicht ab ob Benutzername + pw = identisch mit benutzer
		Benutzer benutzer = new Benutzer();
		Intent intent = new Intent(this, Benutzer.class);
		intent.putExtra("loginbenutzer", benutzer);
		startActivity(intent);
	}
}
