package de.kneipe.kneipenquartett.ui.benutzer;


import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.service.HttpResponse;
import de.kneipe.kneipenquartett.service.BenutzerService.BenutzerServiceBinder;
import de.kneipe.kneipenquartett.ui.main.Main;
import de.kneipe.kneipenquartett.ui.main.Prefs;

public class BenutzerEdit extends Fragment implements OnClickListener {
	private static final String LOG_TAG = BenutzerEdit.class.getSimpleName();
	
	private Bundle args;
	
	private Benutzer benutzer = new Benutzer();
	private EditText edtNachname;
	private EditText edtVorname;
	private EditText edtEmail;
	private EditText edtGeschlecht;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		args = getArguments();
		
		
		Log.v(LOG_TAG, "OnCreateView-BenutzerEdit");
        
		// Voraussetzung fuer onOptionsItemSelected()
		setHasOptionsMenu(true);
		

		
		benutzer = (Benutzer) getArguments() .get("be");
		Log.v(LOG_TAG,benutzer.toString());
		
		// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
		return inflater.inflate(R.layout.benutzer_edit, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
//    	final TextView txtId = (TextView) view.findViewById(R.id.benutzer_id);
//    	txtId.setText(String.valueOf(benutzer.uid));
		view.findViewById(R.id.btn_edit_speicher).setOnClickListener(this);
		
		Bundle args = new Bundle(1);
		args.putSerializable("be", benutzer);

    	edtNachname = (EditText) view.findViewById(R.id.nachname_edt);
    	edtNachname.setText(benutzer.nachname);
    	
    	edtVorname = (EditText) view.findViewById(R.id.vorname_edt);
    	edtVorname.setText(benutzer.vorname);
    	
    	edtEmail = (EditText) view.findViewById(R.id.email_edt);
    	edtEmail.setText(benutzer.email);
    	

    	edtGeschlecht = (EditText) view.findViewById(R.id.geschlecht_edt);
    	edtGeschlecht.setText(benutzer.geschlecht);
    	
    	
    	}
    
    

	
	@Override // OnClickListener
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_edit_speicher:
				Log.d(LOG_TAG,"create wird ausgeführt");
				setBenutzer(view);
				break;
				
			default:
				break;
		}
		
	}
	
	private void setBenutzer(View view) {
		final Context ctxx = view.getContext();
		Log.d(LOG_TAG,"Create Aufruf ");
		
		benutzer.nachname = edtNachname.getText().toString();
		Log.d(LOG_TAG,"nachname wird geändert");
		benutzer.vorname = edtVorname.getText().toString();
		Log.d(LOG_TAG,"vorname wird geändert ");
		benutzer.email = edtEmail.getText().toString();
		Log.d(LOG_TAG,"email wird geändert ");
		benutzer.geschlecht = edtGeschlecht.getText().toString();
		Log.d(LOG_TAG,"geschlecht wird geändert ");
		
		
		Log.d(LOG_TAG, benutzer.toString());
		
		Log.d(LOG_TAG,view.toString());
		
		Log.d(LOG_TAG,benutzer.toString());
		final Main mainActivity = (Main) getActivity();
		Log.d(LOG_TAG,mainActivity.toString());
		final HttpResponse<? extends Benutzer> result = mainActivity.getBenutzerServiceBinder().updateBenutzer(benutzer, ctxx);	
		
		Log.d(LOG_TAG, benutzer.toString());
		 

		 final Benutzer benutzer = result.resultObject;
			final Bundle args = new Bundle(1);
			args.putSerializable("be", benutzer);
			 final Fragment neuesFragment = new BenutzerStammdaten();
			neuesFragment.setArguments(args);
				
				
				// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
				getFragmentManager().beginTransaction()
				                    .replace(R.id.details, neuesFragment)
				                    .addToBackStack(null)
				                    .commit();
	}
}

