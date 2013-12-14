package de.kneipe.kneipenquartett.ui.kneipe;

import static android.app.ActionBar.NAVIGATION_MODE_TABS;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPEN_KEY;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_KEY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.data.Kneipe;
import de.kneipe.kneipenquartett.service.KneipeService.KneipeServiceBinder;
import de.kneipe.kneipenquartett.ui.main.Main;
import de.kneipe.kneipenquartett.ui.main.Prefs;

public class KneipeList extends ListFragment{
	
	private static final String LOG_TAG = KneipeList.class.getSimpleName();
	private ArrayList<Kneipe> kneipen;
	private List<Map<String, Object>> kneipenItems;
	private List<Long> bewertungIds;
	private KneipeServiceBinder kneipeServiceBinder;
	private Bundle args;
	private int position = 0;
	
	private static final String ID = "kid";
	private static final String KNEIPENNAME = "name";
	private static final String[] FROM = {KNEIPENNAME};
	private static final int[] TO = {R.id.txt_kneipeName };

//	private LazyAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		args = getArguments();
		
		setHasOptionsMenu(false);
		kneipen = (ArrayList<Kneipe>) args.getSerializable(KNEIPE_KEY);
		
//		kneipen = (List<Kneipe>) getActivity().getIntent().getExtras().get(KNEIPEN_KEY);
		Log.d(LOG_TAG, kneipen.toString());
		final ListAdapter listAdapter = createListAdapter();
        setListAdapter(listAdapter);
//		setHasOptionsMenu(true);
		// attachToRoot = false, weil die Verwaltung des Fragments durch die
		// Activity erfolgt
		return inflater.inflate(R.layout.kneipe_liste, container, false);
		//return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	private ListAdapter createListAdapter() {
		kneipenItems = new ArrayList<Map<String, Object>>(kneipen.size());
		for (Kneipe k : kneipen) {
    		final Map<String, Object> kneipeItem = new HashMap<String, Object>(2, 1); // max 2 Eintraege, bis zu 100 % Fuellung
    		//kneipeItem.put(ID, k.guenstigstesBier);TODO:Attribute setzen
    		kneipeItem.put(KNEIPENNAME, k.name);
    		kneipenItems.add(kneipeItem);        	
        }
		
		final ListAdapter listAdapter = new SimpleAdapter(getActivity(), kneipenItems, R.layout.kneipe_liste_item, FROM, TO);
		return listAdapter;
    }
	
	public void refresh(Kneipe kneipe) {
		kneipen.set(position, kneipe);
		final ListAdapter listAdapter = createListAdapter();
        setListAdapter(listAdapter);
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
	
//	  @Override
//	  public void onActivityCreated(Bundle savedInstanceState) {
//	    super.onActivityCreated(savedInstanceState);
//	    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
//	        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//	        "Linux", "OS/2" };
//	    ArrayAdapter<Kneipe> adapter = new ArrayAdapter<Kneipe>(getActivity(),
//	        android.R.layout.simple_list_item_1, kneipen);
//	    setListAdapter(adapter);
//	  }

	@Override
	// Implementierung zum Interface OnItemClickListener fuer die Item-Liste
	public void onListItemClick(ListView adapterView, View view, int itemPosition, long itemId) {
		// view: TextView innerhalb von ListFragment
		// itemPosition: Textposition innerhalb der Liste mit Zaehlung ab 0
		// itemId = itemPosition bei String-Arrays bzw. = Primaerschluessel bei Listen aus einer DB
		
		Log.d(LOG_TAG, kneipen.get(itemPosition).toString());
		
		// Evtl. vorhandene Tabs der ACTIVITY loeschen
    	getActivity().getActionBar().removeAllTabs();
		
    	// angeklickte Position fuer evtl. spaeteres Refresh merken, falls der angeklickte Kunde noch aktualisiert wird
    	position = itemPosition;
    	
		final Kneipe kneipe = kneipen.get(itemPosition);
		final Bundle args = new Bundle(1);
		args.putSerializable(KNEIPE_KEY, kneipe);
		
		final Fragment neuesFragment = new KneipeDetails();
		neuesFragment.setArguments(args);
		
		// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
		getFragmentManager().beginTransaction()
		                    .replace(R.id.details, neuesFragment)
		                    .addToBackStack(null)  
		                    .commit();
	}
	  
//	  @Override
//		public void onViewCreated(View view, Bundle savedInstanceState) {
//		  getListView().setOnItemClickListener(this);
//			final Activity activity = getActivity();
//			final ActionBar actionBar = activity.getActionBar();
//			// (horizontale) Tabs; NAVIGATION_MODE_LIST fuer Dropdown Liste
//			actionBar.setNavigationMode(NAVIGATION_MODE_TABS);
//			actionBar.setDisplayShowTitleEnabled(false); // Titel der App
//															// ausblenden, um mehr
//															// Platz fuer die Tabs
//															// zu haben
//
//			final Fragment details = new KneipeList();
//			final Bundle extras = new Bundle();
//	        if (extras != null) {
//	        	@SuppressWarnings("unchecked")
//				final List<Kneipe> kneipen = (List<Kneipe>) extras.get(KNEIPEN_KEY);
//	        	if (kneipen != null && !kneipen.isEmpty()) {
//	        		final Bundle args = new Bundle(1);
//	        		args.putSerializable(KNEIPEN_KEY, kneipen.get(0));
//	        		details.setArguments(args);
//	        	}
//	        }
//			
//			
//			
////			args.putSerializable(KNEIPEN_KEY, (Serializable) kneipen);
//
//			Main main = (Main) activity;
//			kneipeServiceBinder = main.getKneipeServiceBinder();
//	}
}
