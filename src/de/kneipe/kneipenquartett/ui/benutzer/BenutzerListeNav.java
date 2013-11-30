package de.kneipe.kneipenquartett.ui.benutzer;



import static de.kneipe.kneipenquartett.util.Constants.BENUTZERN_KEY;
import static de.kneipe.kneipenquartett.util.Constants.BENUTZER_KEY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;

public class BenutzerListeNav extends ListFragment {
	private static final String LOG_TAG = BenutzerListeNav.class.getSimpleName();
	
	private static final String ID = "id";
	private static final String NACHNAME = "nachname";
	private static final String[] FROM = { ID, NACHNAME};
	private static final int[] TO = { R.id.benutzer_id, R.id.nachname_txt };
	
	private List<Benutzer> benutzern;
	private List<Map<String, Object>> benutzernItems;
	private int position = 0;
	
	@Override
	@SuppressWarnings("unchecked")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        benutzern = (List<Benutzer>) getActivity().getIntent().getExtras().get(BENUTZERN_KEY);
        Log.d(LOG_TAG, benutzern.toString());
        
		final ListAdapter listAdapter = createListAdapter();
        setListAdapter(listAdapter);
        
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	private ListAdapter createListAdapter() {
		benutzernItems = new ArrayList<Map<String, Object>>(benutzern.size());
		for (Benutzer k : benutzern) {
    		final Map<String, Object> benutzerItem = new HashMap<String, Object>(2, 1); // max 2 Eintraege, bis zu 100 % Fuellung
    		benutzerItem.put(ID, k.uid);
    		benutzerItem.put(NACHNAME, k.nachname);
    		benutzernItems.add(benutzerItem);        	
        }
		
		final ListAdapter listAdapter = new SimpleAdapter(getActivity(), benutzernItems, R.layout.benutzern_liste_item, FROM, TO);
		return listAdapter;
    }
	
	public void refresh(Benutzer benutzer) {
		benutzern.set(position, benutzer);
		final ListAdapter listAdapter = createListAdapter();
        setListAdapter(listAdapter);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> adapterView, View view, int itemPosition, long itemId) {
        		// view: TextView innerhalb von ListFragment
        		// itemPosition: Textposition innerhalb der Liste mit Zaehlung ab 0
        		// itemId = itemPosition bei String-Arrays bzw. = Primaerschluessel bei Listen aus einer DB
        		
        		Log.d(LOG_TAG, benutzern.get(itemPosition).toString());
        		
        		// Evtl. vorhandene Tabs der ACTIVITY loeschen
            	getActivity().getActionBar().removeAllTabs();
        		
            	// angeklickte Position fuer evtl. spaeteres Refresh merken, falls der angeklickte Benutzer noch aktualisiert wird
            	position = itemPosition;
            	
        		final Benutzer benutzer = benutzern.get(itemPosition);
        		final Bundle args = new Bundle(1);
        		args.putSerializable(BENUTZER_KEY, benutzer);
        		
        		final Fragment neuesFragment = new BenutzerDetails();
        		neuesFragment.setArguments(args);
        		
        		// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
        		getFragmentManager().beginTransaction()
        		                    .replace(R.id.details, neuesFragment)
        		                    .addToBackStack(null)  
        		                    .commit();
        	}
		});
	}
}

