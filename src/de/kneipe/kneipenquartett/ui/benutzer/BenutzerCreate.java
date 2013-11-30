package de.kneipe.kneipenquartett.ui.benutzer;

	import static de.kneipe.kneipenquartett.util.Constants.BENUTZER_KEY;
	import static de.kneipe.kneipenquartett.util.Constants.MAX_KATEGORIE;
	import static de.kneipe.kneipenquartett.util.Constants.MIN_KATEGORIE;
	import static java.net.HttpURLConnection.HTTP_CONFLICT;
	import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
	import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
	import static java.net.HttpURLConnection.HTTP_OK;
	import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
	import static java.util.Calendar.DAY_OF_MONTH;
	import static java.util.Calendar.MONTH;
	import static java.util.Calendar.YEAR;

	import java.util.GregorianCalendar;
	import java.util.Locale;

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
	import android.widget.DatePicker;
	import android.widget.EditText;
	import android.widget.NumberPicker;
	import android.widget.RadioButton;
	import android.widget.TextView;
	import android.widget.ToggleButton;
	import de.kneipe.R;
	import de.kneipe.kneipenquartett.data.Benutzer;
	import de.kneipe.kneipenquartett.service.HttpResponse;
	import de.kneipe.kneipenquartett.service.BenutzerService.BenutzerServiceBinder;
	import de.kneipe.kneipenquartett.ui.main.Main;
	import de.kneipe.kneipenquartett.ui.main.Prefs;

	public class BenutzerCreate extends Fragment {
		private static final String LOG_TAG = BenutzerCreate.class.getSimpleName();
		
		private Bundle args;
		
		private Benutzer benutzer;
		private EditText createNachname;
		private EditText createVorname;
		private EditText createEmail;
		private EditText createUsername;
		private EditText createPasswort;

		private EditText createGeschlecht;
		
		
		private ToggleButton tglAGBs;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			args = getArguments();
			//kunde = (Kunde) args.get(KUNDE_KEY);
	/*		
			kunde = new Kunde();
			Log.d(LOG_TAG, kunde.toString());
	 */       
			// Voraussetzung fuer onOptionsItemSelected()
			setHasOptionsMenu(true);
			
			// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
			return inflater.inflate(R.layout.benutzer_create, container, false);
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
	    	
			
			final TextView txtId = (TextView) view.findViewById(R.id.benutzer_id);
	    	//txtId.setText(String.valueOf(kunde.id));
	    	
			createPasswort = (EditText) view.findViewById(R.id.passwort_create);
	    	//createLogin.setText(kunde.login);
	    	
	    	
	    	createUsername = (EditText) view.findViewById(R.id.login_create);
	    	//createLogin.setText(kunde.login);
	    	
	    	createNachname = (EditText) view.findViewById(R.id.nachname_create);
	    	//createNachname.setText(kunde.nachname);
	    	
	    	createVorname = (EditText) view.findViewById(R.id.vorname_create);
	    	//createVorname.setText(kunde.vorname);
	    	
	    	createEmail = (EditText) view.findViewById(R.id.email_create);
	    	//createEmail.setText(kunde.email);
	    	
	    	
	    	createGeschlecht = (EditText) view.findViewById(R.id.geschlecht_create);
	    	
	    	
	    	tglAGBs =(ToggleButton) view.findViewById(R.id.agb_tgl);
	    	/*
	    	if (kunde.geschlecht != null) {
		    	switch (kunde.geschlecht) {
			    	case MAENNLICH:
			        	rbMaennlich.setChecked(true);
				    	break;
				    	
			    	case WEIBLICH:
			        	rbWeiblich.setChecked(true);
				    	break;
				    	
				    default:
		    	}
		    	
	    	}
	    	*/
	    }
	    
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
					createKunde();

					final Activity activity = getActivity();
					
					// Das Fragment KundeEdit kann von Main und von KundeListe aus aufgerufen werden
					BenutzerServiceBinder benutzerServiceBinder;
					if (Main.class.equals(activity.getClass())) {
						Main main = (Main) activity;
						benutzerServiceBinder = main.getBenutzerServiceBinder();
					}
					else if (BenutzerListe.class.equals(activity.getClass())) {
						BenutzerListe kundenListe = (BenutzerListe) activity;
						benutzerServiceBinder = kundenListe.getBenutzerServiceBinder();
					}
					else {
						return true;
					}
					
					final HttpResponse<Benutzer> result = benutzerServiceBinder.createBenutzer(benutzer, activity);
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
					
					// Gibt es in der Navigationsleiste eine BenutzerListe? Wenn ja: Refresh mit geaendertem Kunde-Objekt
					final Fragment fragment = getFragmentManager().findFragmentById(R.id.benutzer_liste_nav);
					if (fragment != null) {
						final BenutzerListeNav kundenListeFragment = (BenutzerListeNav) fragment;
						kundenListeFragment.refresh(benutzer);
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
		
		private void createKunde() {
			benutzer=new Benutzer();
			
			benutzer.password = createPasswort.getText().toString();
			benutzer.username = createUsername.getText().toString();
			benutzer.nachname = createNachname.getText().toString();
			benutzer.vorname = createVorname.getText().toString();
			benutzer.email = createEmail.getText().toString();
			benutzer.geschlecht = createGeschlecht.getText().toString();
			benutzer.agbAkzeptiert = tglAGBs.isChecked();
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


