package de.kneipe.kneipenquartett.ui.benutzer;

import static de.kneipe.kneipenquartett.util.Constants.BENUTZER_KEY;
import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.service.HttpResponse;
import de.kneipe.kneipenquartett.service.BenutzerService.BenutzerServiceBinder;
import de.kneipe.kneipenquartett.ui.main.Main;
import de.kneipe.kneipenquartett.ui.main.Prefs;

public class BenutzerEdit extends Fragment {
	private static final String LOG_TAG = BenutzerEdit.class.getSimpleName();
	
	private Bundle args;
	
	private Benutzer benutzer;
	private EditText edtNachname;
	private EditText edtVorname;
	private EditText edtEmail;

	private EditText edtGeschlecht;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		args = getArguments();
		benutzer = (Benutzer) args.get(BENUTZER_KEY);
		Log.d(LOG_TAG, benutzer.toString());
        
		// Voraussetzung fuer onOptionsItemSelected()
		setHasOptionsMenu(true);
		
		// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
		return inflater.inflate(R.layout.benutzer_edit, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
    	final TextView txtId = (TextView) view.findViewById(R.id.benutzer_id);
    	txtId.setText(String.valueOf(benutzer.uid));

    	edtNachname = (EditText) view.findViewById(R.id.nachname_edt);
    	edtNachname.setText(benutzer.nachname);
    	
    	edtVorname = (EditText) view.findViewById(R.id.vorname_edt);
    	edtVorname.setText(benutzer.vorname);
    	
    	edtEmail = (EditText) view.findViewById(R.id.email_edt);
    	edtEmail.setText(benutzer.email);
    	

    	edtEmail = (EditText) view.findViewById(R.id.geschlecht_edt);
    	edtEmail.setText(benutzer.geschlecht);
    	
    	
    	}
    	/*
    	dpSeit = (DatePicker) view.findViewById(R.id.seit);
    	final GregorianCalendar cal = new GregorianCalendar(Locale.getDefault());
    	cal.setTime(benutzer.seit);
    	final int jahr = cal.get(YEAR);
    	final int monat = cal.get(MONTH);
    	final int tag = cal.get(DAY_OF_MONTH);
    	dpSeit.init(jahr, monat, tag, null);
    	
    	npKategorie = (NumberPicker) view.findViewById(R.id.kategorie);
    	npKategorie.setMinValue(MIN_KATEGORIE);
    	npKategorie.setMaxValue(MAX_KATEGORIE);
    	npKategorie.setWrapSelectorWheel(false); // kein zyklisches Scrollen
    	npKategorie.setValue(benutzer.kategorie);

    	tglNewsletter.setChecked(benutzer.newsletter);

	*/

    
    
	@Override
	// Nur aufgerufen, falls setHasOptionsMenu(true) in onCreateView() aufgerufen wird
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.benutzer_edit_options, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.speichern:
				setBenutzer();

				final Activity activity = getActivity();
				
				// Das Fragment BenutzerEdit kann von Main und von BenutzerListe aus aufgerufen werden
				BenutzerServiceBinder benutzerServiceBinder;
				if (Main.class.equals(activity.getClass())) {
					Main main = (Main) activity;
					benutzerServiceBinder = main.getBenutzerServiceBinder();
				}
				else if (BenutzerListe.class.equals(activity.getClass())) {
					BenutzerListe benutzernListe = (BenutzerListe) activity;
					benutzerServiceBinder = benutzernListe.getBenutzerServiceBinder();
				}
				else {
					return true;
				}
				
				final HttpResponse<Benutzer> result = benutzerServiceBinder.updateBenutzer( benutzer, activity);
				final int statuscode = result.responseCode;
				if (statuscode != HTTP_NO_CONTENT && statuscode != HTTP_OK) {
					String msg = null;
					switch (statuscode) {
						case HTTP_CONFLICT:
							msg = result.content;
							break;
						case HTTP_UNAUTHORIZED:
							msg = getString(R.string.s_error_prefs_login, benutzer.uid);
							break;
						case HTTP_FORBIDDEN:
							msg = getString(R.string.s_error_forbidden, benutzer.uid);
							break;
					}
					
		    		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		    		final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    };
		    		builder.setMessage(msg)
		    		       .setNeutralButton(R.string.s_ok, listener)
		    		       .create()
		    		       .show();
		    		return true;
				}
				
				benutzer = result.resultObject;  // ggf. erhoehte Versionsnr. bzgl. konkurrierender Updates
				
				// Gibt es in der Navigationsleiste eine BenutzernListe? Wenn ja: Refresh mit geaendertem Benutzer-Objekt
				final Fragment fragment = getFragmentManager().findFragmentById(R.id.benutzer_liste_nav);
				if (fragment != null) {
					final BenutzerListeNav benutzernListeFragment = (BenutzerListeNav) fragment;
					benutzernListeFragment.refresh(benutzer);
				}
				
				final Fragment neuesFragment = new BenutzerDetails();
				neuesFragment.setArguments(args);
				
				// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
				getFragmentManager().beginTransaction()
				                    .replace(R.id.details, neuesFragment)
				                    .addToBackStack(null)  
				                    .commit();
				return true;
				
			case R.id.einstellungen:
				getFragmentManager().beginTransaction()
                                    .replace(R.id.details, new Prefs())
                                    .addToBackStack(null)
                                    .commit();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void setBenutzer() {
		benutzer.nachname = edtNachname.getText().toString();
		
		benutzer.vorname = edtVorname.getText().toString();
		benutzer.email = edtEmail.getText().toString();
		benutzer.geschlecht = edtGeschlecht.getText().toString();
		
		
/*
		final GregorianCalendar cal = new GregorianCalendar(Locale.getDefault());
		cal.set(dpSeit.getYear(), dpSeit.getMonth(), dpSeit.getDayOfMonth());
		benutzer.seit = cal.getTime();
		
		benutzer.kategorie = (short) npKategorie.getValue();
		
		benutzer.newsletter = tglNewsletter.isChecked();	
*/
		Log.d(LOG_TAG, benutzer.toString());
	}
}

