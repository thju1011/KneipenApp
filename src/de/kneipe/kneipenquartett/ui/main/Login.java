package de.kneipe.kneipenquartett.ui.main;



import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ToggleButton;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.service.BenutzerService;
import de.kneipe.kneipenquartett.service.HttpResponse;
import de.kneipe.kneipenquartett.service.BenutzerService.BenutzerServiceBinder;
import de.kneipe.kneipenquartett.service.KneipeService;
import de.kneipe.kneipenquartett.service.KneipeService.KneipeServiceBinder;
import de.kneipe.kneipenquartett.ui.benutzer.BenutzerCreate;
import de.kneipe.kneipenquartett.ui.benutzer.BenutzerEdit;
import de.kneipe.kneipenquartett.ui.benutzer.BenutzerSucheEmail;
import de.kneipe.kneipenquartett.util.Startseite;

	
	public class Login extends Fragment implements OnClickListener {


		

		private static final String LOG_TAG = Login.class.getSimpleName();
		private Bundle args;
		private EditText email;
		private EditText password;
		
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//			super.onCreate(savedInstanceState);
//			setContentView(R.layout.benutzer_create);
//			findViewById(R.id.btn_reg).setOnClickListener(this);
			
			args = getArguments();
			
			setHasOptionsMenu(true);
			
			
			return inflater.inflate(R.layout.login, container, false);
	
			
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
	    	
			Log.d(LOG_TAG, "View wird aufgebaut");
		
			 email= (EditText) view.findViewById(R.id.emaillogin);
			 
			 password = (EditText) view.findViewById(R.id.passwordlogin);
//			final TextView txtId = (TextView) view.findViewById(R.id.benutzer_id);
	    	//txtId.setText(String.valueOf(kunde.id));
	    	
			


	    	final Main mainActivity = (Main) getActivity();
			
			mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			
			view.findViewById(R.id.btn_reg).setOnClickListener(this);
			view.findViewById(R.id.btn_login).setOnClickListener(this);
	    }
		
		public void onClick(View view) {
			final Context ctx = view.getContext();
			switch(view.getId()){
			case R.id.btn_login:	
				 String benutzerStr = email.getText().toString();
				 String benutzerpw = password.getText().toString();
				
				
				 
							
				
				 Bundle args = new Bundle(1);
				
					
				 try {
					Benutzer db = suchen(view, benutzerStr);
					Log.v(LOG_TAG,db.toString());
					if(benutzerpw.equals(db.password))
					{
						
						args.putSerializable("be", db);
						Log.v(LOG_TAG, "bundle key anlegen");
						
						Fragment nf = new Startseite();
						nf.setArguments(args);
						
						Log.v(LOG_TAG,"Fragment Startseite aufrufen");
						
						getFragmentManager().beginTransaction()
			            .replace(R.id.details, nf)
			            .commit();
			
					}
					
					 if (benutzerpw != db.password)
						 password.setError("Ihre Eingabedaten sind fehlerhaft");
				 }
			
				 catch(Exception e) {
					 if(TextUtils.isEmpty(benutzerStr) && TextUtils.isEmpty(benutzerpw)){
						 	 password.setError("Bitte geben Sie Daten ein");
						 	 email.setError("Bitte geben Sie Daten ein"); }
					 else {	 
					
					 	email.setError("Ihre Eingabedaten sind fehlerhaft");
					 	password.setError("Ihre Eingabedaten sind fehlerhaft");}
				 }
					
			
					
					 

					 
					
					
					break;
				
				
					
				
			case R.id.btn_reg:
				getFragmentManager().beginTransaction()
				.replace(R.id.details, new BenutzerCreate())
				.addToBackStack(null)
				.commit();
				break;
							
			}
			
			// Eingabetext ermitteln
			
				
				
		}
		   
			
			private Benutzer suchen(View view, String emaileingabe) {
				final Context ctx = view.getContext();
				if (TextUtils.isEmpty(emaileingabe)) {
					
					email.setError(getString(R.string.b_nachname_fehlt));
		    		return null;
		    	}
				final Main mg = (Main) getActivity();
				final HttpResponse<Benutzer> result = mg.getBenutzerServiceBinder().sucheBenutzerByEmail(emaileingabe, ctx);

				if (result.responseCode == HTTP_NOT_FOUND) {
					final String msg = getString(R.string.b_benutzer_not_found, email);
					email.setError(msg);
					return null;
				}
				
				Log.d(LOG_TAG, result.toString());
				
				Benutzer be=(Benutzer) result.resultObject;
				
				return be;
				/*
				final Intent intent = new Intent(mainActivity, Benutzer.class);
				intent.putExtra(BENUTZER_KEY, result.resultList);
				startActivity(intent);*/
			}
		
	}


