package de.kneipe.kneipenquartett.ui.kneipe;

import static android.app.ActionBar.NAVIGATION_MODE_TABS;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_KEY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Bewertung;
import de.kneipe.kneipenquartett.data.Gutschein;
import de.kneipe.kneipenquartett.data.Kneipe;
import de.kneipe.kneipenquartett.service.BewertungService.BewertungServiceBinder;
import de.kneipe.kneipenquartett.service.HttpResponse;
import de.kneipe.kneipenquartett.service.KneipeService.KneipeServiceBinder;
import de.kneipe.kneipenquartett.ui.main.Main;
import de.kneipe.kneipenquartett.ui.main.Prefs;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;



public class KommentareShow extends ListFragment{
	private static final String LOG_TAG = KommentareShow.class.getSimpleName();
	public Bundle args;
	public Kneipe kneipe;
	private Bewertung aktuelleBewertung;
	private ArrayList<Bewertung> bewertungen;
	private List<Map<String, Object>> bewertungItems;
	private BewertungServiceBinder bewertungServiceBinder;
	private KneipeServiceBinder kneipeServiceBinder;
	private static final String BEWERTUNG = "bewertung";
	private static final String VORNAME = "vorname";
	private static final String KOMMENTAR = "kommentar";
	private static final String[] FROM = {BEWERTUNG, VORNAME, KOMMENTAR};
	private static final int[] TO = {R.id.bewertung , R.id.vorname, R.id.kommentar};
	int position;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
	args = getArguments();
	
	setHasOptionsMenu(false);
    
    kneipe = (Kneipe) getArguments().get(KNEIPE_KEY);
    Log.v(LOG_TAG, kneipe.toString());
//    final ListAdapter listAdapter = createListAdapter();
//    setListAdapter(listAdapter);
	return inflater.inflate(R.layout.kommentare_liste, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		final Context ctxx = view.getContext();
		
		final Activity activity = getActivity();
		final ActionBar actionBar = activity.getActionBar();
		actionBar.setNavigationMode(NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		
		
		Main main = (Main) activity;
		kneipeServiceBinder = main.getKneipeServiceBinder();
		
		
		final HttpResponse<Bewertung> result = kneipeServiceBinder.findBewertungbyKneipe(Long.valueOf(kneipe.kid), ctxx);
				Log.v(LOG_TAG, result.toString());

		 bewertungen = (ArrayList<Bewertung>)result.resultList;
		//Log.v(LOG_TAG, String.valueOf(bewertungen.size()));
	//	Log.v(LOG_TAG, bewertungen.get(0).toString());
	if(bewertungen== null)
	{
		/*view.findViewById(R.id.k_kommentare).setVisibility(View.INVISIBLE);
		view.findViewById(R.id.k_benutzer_vorname).setVisibility(View.INVISIBLE);
		view.findViewById(R.id.k_benutzer_nachname).setVisibility(View.INVISIBLE);
		view.findViewById(R.id.ratingBar1).setVisibility(View.INVISIBLE);
		view.findViewById(R.id.kommentare_show_seinKommentar).setVisibility(View.INVISIBLE);
		view.findViewById(R.id.k_kommentare).setVisibility(View.INVISIBLE);*/
		Toast.makeText(ctxx, "Keine Kommentare vorhanden", Toast.LENGTH_LONG).show();
		
		
	}
	else{
		for (Bewertung b : bewertungen ) {
			Log.v(LOG_TAG, b.kneipe.toString());
			if (b.kneipe.kid==kneipe.kid){
				aktuelleBewertung = b;
				Log.v(LOG_TAG, b.toString());
			}
			
		
		    final ListAdapter listAdapter = createListAdapter();
		    setListAdapter(listAdapter);

	}
	}
}
	private ListAdapter createListAdapter() {
		bewertungItems = new ArrayList<Map<String, Object>>(bewertungen.size());
		for (Bewertung b : bewertungen) {
    		final Map<String, Object> bewertungItem = new HashMap<String, Object>(3, 1); // max 3 Eintraege, bis zu 100 % Fuellung
    		 double gesamtbewertung = (b.freundlichkeit + b.preisleistung + b.sauberkeit)/3;
    		bewertungItem.put(BEWERTUNG, gesamtbewertung);
    		bewertungItem.put(VORNAME, b.benutzer.vorname);
    		//Kneipenbild setzen  		
    		bewertungItem.put(KOMMENTAR, b.kommentar);

    		bewertungItems.add(bewertungItem);        	
        }
		final ListAdapter listAdapter = new SimpleAdapter(getActivity(), bewertungItems, R.layout.bewertungen_liste_item, FROM, TO);
		return listAdapter;
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
	// Implementierung zum Interface OnItemClickListener fuer die Item-Liste
	public void onListItemClick(ListView adapterView, View view, int itemPosition, long itemId) {
		// view: TextView innerhalb von ListFragment
		// itemPosition: Textposition innerhalb der Liste mit Zaehlung ab 0
		// itemId = itemPosition bei String-Arrays bzw. = Primaerschluessel bei Listen aus einer DB
		
		Log.d(LOG_TAG, bewertungen.get(itemPosition).toString());
		
		// Evtl. vorhandene Tabs der ACTIVITY loeschen
    	//getActivity().getActionBar().removeAllTabs();
		
    	// angeklickte Position fuer evtl. spaeteres Refresh merken, falls der angeklickte Kunde noch aktualisiert wird
    	position = itemPosition;
    	
		final Bewertung bewertung = bewertungen.get(itemPosition);
		final Bundle args = new Bundle(2);
		args.putSerializable("bw", bewertung);
		//args.putSerializable(BENUTZER_KEY, value)
		final Fragment neuesFragment = new KommentareDetails();
		neuesFragment.setArguments(args);
		
		// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
		getFragmentManager().beginTransaction()
		                    .replace(R.id.details, neuesFragment)
		                    .addToBackStack(null)  
		                    .commit();
	}
	
	
	
	
	
	
	
	
}