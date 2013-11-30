package de.kneipe.kneipenquartett.ui.benutzer;

import static de.kneipe.kneipenquartett.util.Constants.BENUTZERN_KEY;
import static de.kneipe.kneipenquartett.util.Constants.BENUTZER_KEY;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.service.BenutzerService;
import de.kneipe.kneipenquartett.service.BenutzerService.BenutzerServiceBinder;

public class BenutzerListe extends Activity {
	private BenutzerServiceBinder benutzerServiceBinder;
	//private BestellungServiceBinder bestellungServiceBinder;
	
	// ServiceConnection ist ein Interface: anonyme Klasse verwenden, um ein Objekt davon zu erzeugen
	private ServiceConnection kundeServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
			benutzerServiceBinder = (BenutzerServiceBinder) serviceBinder;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			benutzerServiceBinder = null;
		}
	};
	
//	private ServiceConnection bestellungServiceConnection = new ServiceConnection() {
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
//			bestellungServiceBinder = (BestellungServiceBinder) serviceBinder;
//		}
//
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			bestellungServiceBinder = null;
//		}
//	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.benutzer_liste);
        
        final Fragment details = new BenutzerDetails();
		final Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	@SuppressWarnings("unchecked")
			final List<Benutzer> benutzern = (List<Benutzer>) extras.get(BENUTZER_KEY);
        	if (benutzern != null && !benutzern.isEmpty()) {
        		final Bundle args = new Bundle(1);
        		args.putSerializable(BENUTZER_KEY, benutzern.get(0));
        		details.setArguments(args);
        	}
        }
		
        getFragmentManager().beginTransaction()
                            .add(R.id.details, details)
                            .commit();
        
//      Entfaellt seit API 16 durch <activity android:parentActivityName="..."> in AndroidManifest.xml
//		final ActionBar actionBar = getActionBar();
//		actionBar.setHomeButtonEnabled(true);
//		actionBar.setDisplayHomeAsUpEnabled(true);
    }

  
//    Entfaellt seit API 16 durch <activity android:parentActivityName="..."> in AndroidManifest.xml
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // App Icon in der Actionbar wurde angeklickt: Main aufrufen
//                final Intent intent = new Intent(this, Main.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                return true;  // Ereignis ist verbraucht: nicht weiterreichen
//                
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
	
    @Override
	public void onStart() {
		super.onStart();
		
		Intent intent = new Intent(this,BenutzerService.class);
		bindService(intent, kundeServiceConnection, Context.BIND_AUTO_CREATE);
		
//		intent = new Intent(this, BestellungService.class);
//		bindService(intent, bestellungServiceConnection, Context.BIND_AUTO_CREATE);
    }
    
	@Override
	public void onStop() {
		super.onStop();
		
		unbindService(kundeServiceConnection);
		//unbindService(bestellungServiceConnection);
	}

	public BenutzerServiceBinder getBenutzerServiceBinder() {
		return benutzerServiceBinder;
	}

//	public BestellungServiceBinder getBestellungServiceBinder() {
//		return bestellungServiceBinder;
//	}
}
