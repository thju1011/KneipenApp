package de.kneipe.kneipenquartett.ui.main;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.service.BenutzerService;
import de.kneipe.kneipenquartett.service.BenutzerService.BenutzerServiceBinder;
import de.kneipe.kneipenquartett.service.KneipeService;
import de.kneipe.kneipenquartett.service.KneipeService.KneipeServiceBinder;
import de.kneipe.kneipenquartett.ui.benutzer.BenutzerCreate;
import de.kneipe.kneipenquartett.ui.benutzer.BenutzerEdit;

public class Main extends Activity implements OnClickListener {

	

	private static final String LOG_TAG = Main.class.getSimpleName();
	
	private BenutzerServiceBinder benutzerServiceBinder;
	private KneipeServiceBinder kneipeServiceBinder;
	
	EditText loginemail;
	EditText passwort;
	
	private ServiceConnection benutzerServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
			Log.v(LOG_TAG, "onServiceConnected() fuer benutzerServiceBinder");
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
	
	private ServiceConnection kneipeServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
			Log.v(LOG_TAG, "onServiceConnected() fuer KundeServiceBinder");
			kneipeServiceBinder = (KneipeServiceBinder) serviceBinder;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			kneipeServiceBinder = null;
		}
	};
	

	/*public KneipenServiceBinder getKneipenServiceBinder() {
		return KneipenServiceBinder;
	}*/
	
	public KneipeServiceBinder getKneipeServiceBinder() {
		return kneipeServiceBinder;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findViewById(R.id.btn_registrieren).setOnClickListener(this);
		findViewById(R.id.btn_edit).setOnClickListener(this);
		
		loginemail = (EditText) findViewById(R.id.email);
		passwort = (EditText) findViewById(R.id.password);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onClick(View view) {
		
		switch(view.getId()){
		case R.id.btn_registrieren:	
			getFragmentManager().beginTransaction()
			.replace(R.id.details, new BenutzerCreate())
			.commit();
			
			break;
			
		case R.id.btn_edit:
			getFragmentManager().beginTransaction()
			.replace(R.id.details, new BenutzerEdit())
			.commit();
			
		
		
		// Eingabetext ermitteln
		case R.id.btn_login:
		String benutzerStr = loginemail.getText().toString();
		String benutzerpw = passwort.getText().toString();
		if(TextUtils.isEmpty(benutzerStr) ||TextUtils.isEmpty(benutzerpw) )
			loginemail.setError("Eingabe fehlt");
		
		Benutzer login = new Benutzer();
		login.email= benutzerStr;
		login.password = benutzerpw;
		
		
		//VORÜBERGEHEND WIRD HIER NUR EIN GET ABGESETZT
		
		
		
		// SERVER AUFRUF! Vergleich gleicht ab ob Benutzername + pw = identisch mit benutzer
		Benutzer benutzer = new Benutzer();
		Intent intent = new Intent(this, Benutzer.class);
		intent.putExtra("loginbenutzer", benutzer);
		startActivity(intent);
	
		break;
	
	
		}	
		
}
	
	   @Override
		public void onStart() {
			super.onStart();
			
			Intent intent = new Intent(this, BenutzerService.class);
			bindService(intent, benutzerServiceConnection, Context.BIND_AUTO_CREATE);
			
//			intent = new Intent(this, KneipeService.class);
//			bindService(intent, KneipeServiceConnection, Context.BIND_AUTO_CREATE);
	    }
	    
		@Override
		public void onStop() {
			super.onStop();
			
			unbindService(benutzerServiceConnection);
//			unbindService(artikelServiceConnection);
		}
	
}
