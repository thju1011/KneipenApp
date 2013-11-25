package de.kneipe;

import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.service.BenutzerService;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Main extends Activity implements OnClickListener {
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
		
		
		
		
		
		
		// SERVER AUFRUF! Vergleich gleicht ab ob Benutzername + pw = identisch mit benutzer
		Benutzer benutzer = new Benutzer();
		Intent intent = new Intent(this, Benutzer.class);
		intent.putExtra("loginbenutzer", benutzer);
		startActivity(intent);
	}
}
