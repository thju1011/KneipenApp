package de.kneipe.kneipenquartett.ui.kneipe;

import static android.app.ActionBar.NAVIGATION_MODE_TABS;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_KEY;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.data.Bewertung;
import de.kneipe.kneipenquartett.data.Kneipe;
import de.kneipe.kneipenquartett.service.HttpResponse;
import de.kneipe.kneipenquartett.service.KneipeService.KneipeServiceBinder;
import de.kneipe.kneipenquartett.ui.benutzer.BenutzerDetails;
import de.kneipe.kneipenquartett.ui.main.Main;
import de.kneipe.kneipenquartett.ui.main.Prefs;

public class BewertungCreate extends Fragment implements OnRatingBarChangeListener, OnClickListener {
	private static final String LOG_TAG = BewertungCreate.class.getSimpleName();
	private Kneipe kneipe;
	private Bundle args;
	private Benutzer benutzer;
	private KneipeServiceBinder kneipeServiceBinder;
	private Bewertung bewertung = new Bewertung();
	private RatingBar freundlichkeit;
	private RatingBar preisleistung;
	private RatingBar sauberkeit;
	private EditText kommentar;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		kneipe = (Kneipe) getArguments().get(KNEIPE_KEY);
		benutzer = (Benutzer) getArguments().get("be");
		Log.d(LOG_TAG, kneipe.toString());
		args = getArguments();
		setHasOptionsMenu(false);
		// attachToRoot = false, weil die Verwaltung des Fragments durch die
		// Activity erfolgt
		return inflater.inflate(R.layout.bewertung_create, container, false);
	}

	@Override
	// Nur aufgerufen, falls setHasOptionsMenu(true) in onCreateView()
	// aufgerufen wird
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.einstellungen:
			getFragmentManager().beginTransaction()
					.replace(R.id.details, new Prefs()).addToBackStack(null)
					.commit();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		final Activity activity = getActivity();
		final ActionBar actionBar = activity.getActionBar();
		actionBar.removeAllTabs();
		// (horizontale) Tabs; NAVIGATION_MODE_LIST fuer Dropdown Liste
		actionBar.setNavigationMode(NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false); // Titel der App
														// ausblenden, um mehr
														// Platz fuer die Tabs
														// zu haben
		/*Tab tab2 = actionBar.newTab();
		Tab tab = actionBar.newTab()
				.setText("Kneipen")
				.setTabListener(new TabListener<KneipeSucheKategorie>(activity,KneipeSucheKategorie.class, args));

		actionBar.addTab(tab);
		
		 tab = actionBar.newTab()
							.setText("Profil")
							.setTabListener(new TabListener<BenutzerStammdaten>(activity, BenutzerStammdaten.class, args));

		actionBar.addTab(tab);*/
		Log.v(LOG_TAG,"tablistener");
		view.findViewById(R.id.btn_bewertung_bewertungaschicken).setOnClickListener(this);
//		final Bundle args = new Bundle(1);
//		args.putSerializable(KNEIPEN_KEY, kneipe);
		freundlichkeit = (RatingBar) view.findViewById(R.id.rtb_bewertung_freundlichkeit);
		freundlichkeit.setClickable(true);
		freundlichkeit.setOnRatingBarChangeListener(this);
		
		preisleistung = (RatingBar) view.findViewById(R.id.rtb_bewertung_preisleistung);
		preisleistung.setClickable(true);
		preisleistung.setOnRatingBarChangeListener(this);
		
		sauberkeit = (RatingBar) view.findViewById(R.id.rtb_bewertung_sauberkeit);
		sauberkeit.setClickable(true);
		sauberkeit.setOnRatingBarChangeListener(this);
		
		kommentar = (EditText) view.findViewById(R.id.edt_bewertung_kommentar);

	 /*Hab hier auskommentiert um emulator starten zu können
	  * final TextView txtverf = (TextView) view.findViewById(R.id.kunde_adresse);
		txtverf.setText(kneipe.adresse);*/

		// if (Main.class.equals(activity.getClass())) {
		Main main = (Main) activity;
		kneipeServiceBinder = main.getKneipeServiceBinder();
		
		
		
	}
	
	
	
	 @Override
	 public void onRatingChanged(RatingBar ratingBar, float rating,
	   boolean fromTouch) {
	  final int numStars = ratingBar.getNumStars();
	  switch(ratingBar.getId()){
	  case R.id.rtb_bewertung_freundlichkeit:
			String freundlichkeit = String.valueOf( ratingBar.getRating());
			  bewertung.freundlichkeit = Double.parseDouble(freundlichkeit);
		  break;
	
	  case R.id.rtb_bewertung_preisleistung:
			String preisleistung  = String.valueOf( ratingBar.getRating());
			  bewertung.preisleistung = Double.parseDouble(preisleistung);
	  break;
	  case R.id.rtb_bewertung_sauberkeit:
		String sauberkeit = String.valueOf( ratingBar.getRating());
		  bewertung.sauberkeit = Double.parseDouble(sauberkeit);
		  Log.v(LOG_TAG,String.valueOf(bewertung.sauberkeit));
		  break;
	  }  
	  
	 }
	
	
	public void onClick(View view) {
		final Context ctx = view.getContext();
		switch(view.getId()){
		case R.id.btn_bewertung_bewertungaschicken:	
			createBewertung(view);
				
				
			 
//				List<Kneipe> k = suchen2(view, kneipeNameStr);
//				//if(k==null)System.out.println("Fehler");
//				//Log.v(LOG_TAG,k.toString());
//			
//					

					Log.v(LOG_TAG, "bundle key anlegen");
					
					Fragment nf = new BewertungCreate();
					nf.setArguments(args);
					
					Log.v(LOG_TAG,"Fragment BewertungCreate aufrufen");
					
					getFragmentManager().beginTransaction()
		            .replace(R.id.details, nf)
		            .commit();

				
				
				break;
			
			
				
		
		}
		
		// Eingabetext ermitteln
		
			
			
	}
	private void createBewertung(View view) {
		final Context ctxx = view.getContext();
		Log.d(LOG_TAG,"Create BEWERTUNG Aufruf ");
		
		bewertung.kommentar = kommentar.getText().toString();
		bewertung.benutzer = benutzer;
		bewertung.kneipe = kneipe;
	
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
			final HttpResponse<? extends Bewertung> result = mainActivity.getBenutzerServiceBinder().createBewertung(bewertung, ctxx);
			Log.d(LOG_TAG, result.toString());
			Log.d(LOG_TAG, benutzer.toString());
			 

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

