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
		
//		Log.d(LOG_TAG, benutzer.toString());
		
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
    
    
//    
//	@Override
//	// Nur aufgerufen, falls setHasOptionsMenu(true) in onCreateView() aufgerufen wird
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		super.onCreateOptionsMenu(menu, inflater);
//		inflater.inflate(R.menu.benutzer_edit_options, menu);
//	}
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//			case R.id.speichern:
//				setBenutzer();
//
//				final Activity activity = getActivity();
//				
//				// Das Fragment BenutzerEdit kann von Main und von BenutzerListe aus aufgerufen werden
//				BenutzerServiceBinder benutzerServiceBinder;
//				if (Main.class.equals(activity.getClass())) {
//					Main main = (Main) activity;
//					benutzerServiceBinder = main.getBenutzerServiceBinder();
//				}
//				else if (BenutzerListe.class.equals(activity.getClass())) {
//					BenutzerListe benutzernListe = (BenutzerListe) activity;
//					benutzerServiceBinder = benutzernListe.getBenutzerServiceBinder();
//				}
//				else {
//					return true;
//				}
//				
//				final HttpResponse<Benutzer> result = benutzerServiceBinder.updateBenutzer( benutzer, activity);
//				final int statuscode = result.responseCode;
//				if (statuscode != HTTP_NO_CONTENT && statuscode != HTTP_OK) {
//					String msg = null;
//					switch (statuscode) {
//						case HTTP_CONFLICT:
//							msg = result.content;
//							break;
//						case HTTP_UNAUTHORIZED:
//							msg = getString(R.string.s_error_prefs_login, benutzer.uid);
//							break;
//						case HTTP_FORBIDDEN:
//							msg = getString(R.string.s_error_forbidden, benutzer.uid);
//							break;
//					}
//					
//		    		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//		    		final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {}
//                    };
//		    		builder.setMessage(msg)
//		    		       .setNeutralButton(R.string.s_ok, listener)
//		    		       .create()
//		    		       .show();
//		    		return true;
//				}
//				
//				benutzer = result.resultObject;  // ggf. erhoehte Versionsnr. bzgl. konkurrierender Updates
//				
//				// Gibt es in der Navigationsleiste eine BenutzernListe? Wenn ja: Refresh mit geaendertem Benutzer-Objekt
//				final Fragment fragment = getFragmentManager().findFragmentById(R.id.benutzer_liste_nav);
//				if (fragment != null) {
//					final BenutzerListeNav benutzernListeFragment = (BenutzerListeNav) fragment;
//					benutzernListeFragment.refresh(benutzer);
//				}
//				
//				final Fragment neuesFragment = new BenutzerDetails();
//				neuesFragment.setArguments(args);
//				
//				// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
//				getFragmentManager().beginTransaction()
//				                    .replace(R.id.details, neuesFragment)
//				                    .addToBackStack(null)  
//				                    .commit();
//				return true;
//				
//			case R.id.einstellungen:
//				getFragmentManager().beginTransaction()
//                                    .replace(R.id.details, new Prefs())
//                                    .addToBackStack(null)
//                                    .commit();
//				return true;
//				
//			default:
//				return super.onOptionsItemSelected(item);
//		}
//	}
	
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

