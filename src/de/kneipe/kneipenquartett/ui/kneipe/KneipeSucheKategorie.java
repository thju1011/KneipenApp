package de.kneipe.kneipenquartett.ui.kneipe;

import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_KEY;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.data.Kneipe;
import de.kneipe.kneipenquartett.service.HttpResponse;
import de.kneipe.kneipenquartett.ui.benutzer.BenutzerCreate;
import de.kneipe.kneipenquartett.ui.main.Login;
import de.kneipe.kneipenquartett.ui.main.Main;


public class KneipeSucheKategorie extends Fragment implements OnClickListener, OnEditorActionListener{
	private static final String LOG_TAG = KneipeSucheKategorie.class.getSimpleName();
	private Bundle args;
	private EditText kneipeName;
	private Benutzer benutzer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.kneipe_suche_kategorie);
		
		args = getArguments();
		

		benutzer = (Benutzer) getArguments() .get("be");
		
		
		return inflater.inflate(R.layout.kneipe_suche_kategorie, container, false);
	

	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
    	
		Log.d(LOG_TAG, "View wird aufgebaut");
		final  String LOG_TAG = Login.class.getSimpleName();
		 kneipeName = (EditText) view.findViewById(R.id.txtKneipeSuchen);

		 final Main mainActivity = (Main) getActivity();
		
		mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		view.findViewById(R.id.btnKneipeSuchen).setOnClickListener(this);
		view.findViewById(R.id.btn_Navigieren).setOnClickListener(this);

		view.findViewById(R.id.btn_KategorieKneipe).setOnClickListener(this);
		view.findViewById(R.id.btn_KategorieBar).setOnClickListener(this);
		view.findViewById(R.id.btn_KategorieClub).setOnClickListener(this);
		view.findViewById(R.id.btn_KategorieRestaurant).setOnClickListener(this);
		view.setOnKeyListener(keyListener);
    }
	android.view.View.OnKeyListener keyListener = new android.view.View.OnKeyListener() {

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if( keyCode == KeyEvent.KEYCODE_BACK){  
                return false;
            }
                return false;
        }
    };
    
    
	public void onClick(View view) {
		final Context ctx = view.getContext();
		switch(view.getId()){
		case R.id.btnKneipeSuchen:	
			 String kneipeNameStr = kneipeName.getText().toString();
			 
			 if(TextUtils.isEmpty(kneipeNameStr))
					kneipeName.setError("Eingabe fehlt");

				
				
			
				 suchen(view, kneipeNameStr);
			 
		

				
				
				break;
			
			
				
			
		case R.id.btn_Navigieren:
			
			alleKneipen();
			break;
			
			
			
		case R.id.btn_KategorieKneipe:
			kneipeKategorie("Kneipe");
			break;
			
		case R.id.btn_KategorieBar:

			kneipeKategorie("Bar");
			break;
			
		case R.id.btn_KategorieClub:

			kneipeKategorie("Club");
			break;
			
		case R.id.btn_KategorieRestaurant:

			kneipeKategorie("Restaurant");
			break;
	
		
	
	}
		
		// Eingabetext ermitteln
		
			
			
	}
	/*
	 *alternative Suche nach Kneipen im lokalen Array 
	 */
	private void suchen(View view, String name) {

			final Main mg = (Main) getActivity();
	        final List<Kneipe> kneipenArray = mg.getKneipeServiceBinder().initKneipen();
	       List<Kneipe> result = new ArrayList<Kneipe>();
	 
	 for(Kneipe k : kneipenArray){
		 String suche = k.name.toUpperCase();
		 if(suche.contains(name.toUpperCase())){
			 result.add(k);
		 }
	 }
	 if(result.isEmpty()){	 
			kneipeName.setError("suchbegriff ist falsch");
			return;	 	 
	 }
	 
		final Bundle args = new Bundle(2);
		args.putSerializable("be", benutzer);
		args.putSerializable(KNEIPE_KEY,(ArrayList<Kneipe>) result);
		
		final Fragment neuesFragment = new KneipeList();
		neuesFragment.setArguments(args);
		
		// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
		getFragmentManager().beginTransaction()
		                    .replace(R.id.details, neuesFragment)
		                    .addToBackStack(null)
		                    .commit();
	}
	
	/*
	 * 
	 * Suche nach Kneipen im Webservice
	 */
	private Kneipe suchenWeb(View view, String name) {
		final Context ctx = view.getContext();
		if (TextUtils.isEmpty(name)) {
			
			kneipeName.setError("suchbegriff ist falsch");
    		return null;

    	}
		final Main mg = (Main) getActivity();
		final HttpResponse<Kneipe> result = mg.getKneipeServiceBinder().sucheKneipenByName(name, ctx);

		if (result.responseCode == HTTP_NOT_FOUND) {
			final String msg = "Keiene Kneipe gefunden mit dem Namen: " + name;
			kneipeName.setError(msg);
			return null;
		}
		
		Log.d(LOG_TAG, result.toString());
		
		Kneipe k = (Kneipe)result.resultObject;
		
		
		return k;
		/*
		final Intent intent = new Intent(mainActivity, Kneipe.class);
		intent.putExtra(KNEIPEN_KEY, result.resultList);
		startActivity(intent);*/
	}
	
	private void alleKneipen() {

		final Main mg = (Main) getActivity();
        List<Kneipe> kneipenArray = mg.getKneipeServiceBinder().initKneipen();

 
	final Bundle args = new Bundle(2);
	args.putSerializable("be", benutzer);
	args.putSerializable(KNEIPE_KEY,(ArrayList<Kneipe>) kneipenArray);
	
	final Fragment neuesFragment = new KneipeList();
	neuesFragment.setArguments(args);
	
	// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
	getFragmentManager().beginTransaction()
	                    .replace(R.id.details, neuesFragment)
	                    .addToBackStack(null)  
	                    .commit();
}
	
	private void kneipeKategorie(String kategorie){

		final Main mg = (Main) getActivity();
        final List<Kneipe> kneipenArray = mg.getKneipeServiceBinder().initKneipen();
       List<Kneipe> result = new ArrayList<Kneipe>();
 
 for(Kneipe k : kneipenArray){
	 if(k.art.contains(kategorie)){
		 result.add(k);
	 }
 }
 
	final Bundle args = new Bundle(2);
	args.putSerializable("be", benutzer);
	args.putSerializable(KNEIPE_KEY,(ArrayList<Kneipe>) result);
	
	final Fragment neuesFragment = new KneipeList();
	neuesFragment.setArguments(args);
	
	// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
	getFragmentManager().beginTransaction()
	                    .replace(R.id.details, neuesFragment)
	                    .addToBackStack(null)  
	                    .commit();
		
	}
	

	
	
	
	
	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
