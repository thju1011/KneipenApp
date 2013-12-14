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
		
		setHasOptionsMenu(true);

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
    }
	public void onClick(View view) {
		final Context ctx = view.getContext();
		switch(view.getId()){
		case R.id.btnKneipeSuchen:	
			 String kneipeNameStr = kneipeName.getText().toString();
			 
			 if(TextUtils.isEmpty(kneipeNameStr))
					kneipeName.setError("Eingabe fehlt");

				
				
			 suchen(view, kneipeNameStr);
//				List<Kneipe> k = suchen2(view, kneipeNameStr);
//				//if(k==null)System.out.println("Fehler");
//				//Log.v(LOG_TAG,k.toString());
//			
//					
//					args.putSerializable("kneipe", k.get(0));
//					Log.v(LOG_TAG, "bundle key anlegen");
//					
//					Fragment nf = new KneipeList();
//					nf.setArguments(args);
//					
//					Log.v(LOG_TAG,"Fragment KneipeListe aufrufen");
//					
//					getFragmentManager().beginTransaction()
//		            .replace(R.id.details, nf)
//		            .commit();

				
				
				break;
			
			
				
			
		case R.id.btn_reg:
			getFragmentManager().beginTransaction()
			.replace(R.id.details, new BenutzerCreate())
			.commit();
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
		 if(k.name.equals(name)){
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
	
	
	
	
	
	
	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
