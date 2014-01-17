package de.kneipe.kneipenquartett.ui.benutzer;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.service.HttpResponse;
import de.kneipe.kneipenquartett.ui.main.Main;

	public class BenutzerCreate extends Fragment implements OnClickListener {
		private static final String LOG_TAG = BenutzerCreate.class.getSimpleName();
		

		
		private Bundle args;
		private Benutzer benutzer;
		private EditText createNachname;
		private EditText createVorname;
		private EditText createEmail;
		private EditText createUsername;
		private EditText createPasswort;
		

		
		
		private CheckBox tglAGBs;
		
		private RadioButton weiblich;
		private RadioButton maennlich;
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//			super.onCreate(savedInstanceState);
//			setContentView(R.layout.benutzer_create);
//			findViewById(R.id.btn_reg).setOnClickListener(this);
			
			args = getArguments();
			
			setHasOptionsMenu(false);
			
			
			return inflater.inflate(R.layout.benutzer_create, container, false);
		}
		public static void closeKeyboard(Context c, IBinder windowToken) {
		    InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
		    mgr.hideSoftInputFromWindow(windowToken, 0);
		}

		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
	    	
			Log.d(LOG_TAG, "View wird aufgebaut");
			

//			final TextView txtId = (TextView) view.findViewById(R.id.benutzer_id);
	    	//txtId.setText(String.valueOf(kunde.id));
	    	
			createPasswort = (EditText) view.findViewById(R.id.passwort_create);
	    	//createLogin.setText(kunde.login);
	    	
	    
	    	
	    	createNachname = (EditText) view.findViewById(R.id.nachname_create);
	    	//createNachname.setText(kunde.nachname);
	    	
	    	createVorname = (EditText) view.findViewById(R.id.vorname_create);
	    	//createVorname.setText(kunde.vorname);
	    	
	    	createEmail = (EditText) view.findViewById(R.id.email_create);
	    	//createEmail.setText(kunde.email);
	    	
//	    	if(weiblich.isChecked())
//				benutzer.geschlecht = "weiblich";
//				else {
//					benutzer.geschlecht = "maennlich";}
	    	
	    
	    	weiblich = (RadioButton) view.findViewById(R.id.geschlecht_weiblich);
	    	maennlich = (RadioButton) view.findViewById(R.id.geschlecht_maennlich);
	    	
	    	
	    	tglAGBs =(CheckBox) view.findViewById(R.id.agb_tgl);


	    	final Main mainActivity = (Main) getActivity();
			
			mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			
			view.findViewById(R.id.btn_reg).setOnClickListener(this);
	    }
		
		@Override // OnClickListener
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.btn_reg:
					 if(!createEmail.getText().toString().contains("@")){
						 Toast.makeText(view.getContext(), "Keine gültige Emailadresse", Toast.LENGTH_LONG).show();
						 Log.d(LOG_TAG, "email falsch");
						 return; 
					 }
						
					 
					Log.d(LOG_TAG,"create wird ausgeführt");
					createBenutzer(view);
					closeKeyboard(getActivity(), createEmail.getWindowToken());
					break;
					
				default:
					break;
			}
			
		}

//		@Override
//		// Nur aufgerufen, falls setHasOptionsMenu(true) in onCreateView() aufgerufen wird
//		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//			super.onCreateOptionsMenu(menu, inflater);
//			inflater.inflate(R.menu.benutzer_edit_options, menu);
//		}
//		
//		@Override
//		public boolean onOptionsItemSelected(MenuItem item) {
//			switch (item.getItemId()) {
//				case R.id.speichern:
//					createKunde();
//					
//					final Activity activity = getActivity();
//					
//					System.out.print(benutzer.toJsonObject().toString());
//					// Das Fragment KundeEdit kann von Main und von KundeListe aus aufgerufen werden
//					BenutzerServiceBinder benutzerServiceBinder;
//					if(benutzerServiceBinder.)
//					if (Main.class.equals(activity.getClass())) {
//						Main main = (Main) activity;
//						benutzerServiceBinder = main.getBenutzerServiceBinder();
//					}
//					else if (BenutzerListe.class.equals(activity.getClass())) {
//						BenutzerListe kundenListe = (BenutzerListe) activity;
//						benutzerServiceBinder = kundenListe.getBenutzerServiceBinder();
//					}
//					else {
//						return true;
//					}
//					
//					final HttpResponse<Benutzer> result = benutzerServiceBinder.createBenutzer(benutzer, activity);
//					final int statuscode = result.responseCode;
//					if (statuscode != HTTP_NO_CONTENT && statuscode != HTTP_OK) {
//						String msg = null;
//						switch (statuscode) {
//							case HTTP_CONFLICT:
//								msg = result.content;
//								break;
//							case HTTP_UNAUTHORIZED:
//								msg = getString(R.string.s_error_prefs_login, benutzer.uid);
//								break;
//							case HTTP_FORBIDDEN:
//								msg = getString(R.string.s_error_forbidden, benutzer.uid);
//								break;
//						}
//						
//			    		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//			    		final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//	                        public void onClick(DialogInterface dialog, int which) {}
//	                    };
//			    		builder.setMessage(msg)
//			    		       .setNeutralButton(R.string.s_ok, listener)
//			    		       .create()
//			    		       .show();
//			    		return true;
//					}
//					
//					benutzer = result.resultObject;  // ggf. erhoehte Versionsnr. bzgl. konkurrierender Updates
//					
//					// Gibt es in der Navigationsleiste eine BenutzerListe? Wenn ja: Refresh mit geaendertem Kunde-Objekt
//					final Fragment fragment = getFragmentManager().findFragmentById(R.id.benutzer_liste_nav);
//					if (fragment != null) {
//						final BenutzerListeNav kundenListeFragment = (BenutzerListeNav) fragment;
//						kundenListeFragment.refresh(benutzer);
//					}
//					
//					final Fragment neuesFragment = new BenutzerDetails();
//					neuesFragment.setArguments(args);
//					
//					// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
//					getFragmentManager().beginTransaction()
//					                    .replace(R.id.details, neuesFragment)
//					                    .addToBackStack(null)  
//					                    .commit();
//					return true;
//					
//				case R.id.einstellungen:
//					getFragmentManager().beginTransaction()
//	                                    .replace(R.id.details, new Prefs())
//	                                    .addToBackStack(null)
//	                                    .commit();
//					return true;
//					
//				default:
//					return super.onOptionsItemSelected(item);
//			}
//		}
//		
				
		
		
		
		private void createBenutzer(View view) {
			final  String LOG_TAG = BenutzerCreate.class.getSimpleName();
			final Context ctxx = view.getContext();
			Log.d(LOG_TAG,"Create Aufruf ");
			
			String password = createPasswort.getText().toString();
			String nachname = createNachname.getText().toString();
			String vorname = createVorname.getText().toString();
			String email = createEmail.getText().toString();
			Boolean agbAkzeptiert = tglAGBs.isChecked();
			if(password.isEmpty()||nachname.isEmpty()||vorname.isEmpty()||!agbAkzeptiert)
			{
				createNachname.setError("Alle Felder vollmachen ;)");
				
			}
			benutzer = new Benutzer();
			benutzer.password=password;
			
			benutzer.nachname=nachname;
			benutzer.vorname=vorname;
			benutzer.email=email;
			if(weiblich.isChecked())
				benutzer.geschlecht = "weiblich";
				else {
					benutzer.geschlecht = "maennlich";}
//			benutzer.geschlecht= geschlecht;
		
			benutzer.agbAkzeptiert= agbAkzeptiert;
				/*		
			final GregorianCalendar cal = new GregorianCalendar(Locale.getDefault());
			cal.set(dpSeit.getYear(), dpSeit.getMonth(), dpSeit.getDayOfMonth());
			benutzer.seit = cal.getTime();
			
			benutzer.kategorie = (short) npKategorie.getValue();
			
			benutzer.newsletter = tglNewsletter.isChecked();	
	*/
			
			
				
				Log.d(LOG_TAG,view.toString());
				
				Log.d(LOG_TAG,benutzer.toString());
				final Main mainActivity = (Main) getActivity();
				Log.d(LOG_TAG,mainActivity.toString());
				
				//Test ob email bereits existiert
				final HttpResponse<? extends Benutzer> emailtest = mainActivity.getBenutzerServiceBinder().sucheBenutzerByEmail(benutzer.email, ctxx);
				 final Benutzer emailtestB = emailtest.resultObject;
				 if(emailtest.responseCode == 200){
					 Toast.makeText(ctxx, "email bereits vorhanden", Toast.LENGTH_LONG).show();
					 Log.d(LOG_TAG, "email existiert bereits");
					 return;
				 }
				
				//Anlegen
				final HttpResponse<? extends Benutzer> result = mainActivity.getBenutzerServiceBinder().createBenutzer(benutzer, ctxx);	
				Log.d(LOG_TAG, result.toString());
				Log.d(LOG_TAG, benutzer.toString());
				 
				final HttpResponse<? extends Benutzer> angelegterbenutzer = mainActivity.getBenutzerServiceBinder().sucheBenutzerByEmail(benutzer.email, ctxx);
				 final Benutzer benutzer = angelegterbenutzer.resultObject;
					final Bundle args = new Bundle(1);
					args.putSerializable("be", benutzer);
					 final Fragment neuesFragment = new BenutzerDetails();
					neuesFragment.setArguments(args);
						
						
						// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
						getFragmentManager().beginTransaction()
						                    .replace(R.id.details, neuesFragment)
						                    .addToBackStack(null)
						                    .commit();
					
				
			
			
		
		}
		
		
	}


