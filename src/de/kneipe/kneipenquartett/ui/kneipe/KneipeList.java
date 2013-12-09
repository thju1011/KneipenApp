package de.kneipe.kneipenquartett.ui.kneipe;

import static android.app.ActionBar.NAVIGATION_MODE_TABS;
import static android.content.Intent.getIntent;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPEN_KEY;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Kneipe;
import de.kneipe.kneipenquartett.service.KneipeService.KneipeServiceBinder;
import de.kneipe.kneipenquartett.ui.main.Main;
import de.kneipe.kneipenquartett.ui.main.Prefs;

public class KneipeList extends ListFragment{
	
	private static final String LOG_TAG = KneipeList.class.getSimpleName();
	private List<Kneipe> kneipen;
	private List<Long> bewertungIds;
	private KneipeServiceBinder kneipeServiceBinder;

//	private LazyAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		kneipen = (List<Kneipe>) getArguments().get(KNEIPEN_KEY);
		Log.d(LOG_TAG, kneipen.toString());
		setHasOptionsMenu(true);
		// attachToRoot = false, weil die Verwaltung des Fragments durch die
		// Activity erfolgt
		return inflater.inflate(R.layout.kneipe_liste, container, false);
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
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
	        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
	        "Linux", "OS/2" };
	    ArrayAdapter<Kneipe> adapter = new ArrayAdapter<Kneipe>(getActivity(),
	        android.R.layout.simple_list_item_1, kneipen);
	    setListAdapter(adapter);
	  }

	  @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
	    // do something with the data

	  }
	  
	  @Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			final Activity activity = getActivity();
			final ActionBar actionBar = activity.getActionBar();
			// (horizontale) Tabs; NAVIGATION_MODE_LIST fuer Dropdown Liste
			actionBar.setNavigationMode(NAVIGATION_MODE_TABS);
			actionBar.setDisplayShowTitleEnabled(false); // Titel der App
															// ausblenden, um mehr
															// Platz fuer die Tabs
															// zu haben

			final Fragment details = new KneipeList();
			final Bundle extras = new Bundle();
	        if (extras != null) {
	        	@SuppressWarnings("unchecked")
				final List<Kneipe> kneipen = (List<Kneipe>) extras.get(KNEIPEN_KEY);
	        	if (kneipen != null && !kneipen.isEmpty()) {
	        		final Bundle args = new Bundle(1);
	        		args.putSerializable(KNEIPEN_KEY, kneipen.get(0));
	        		details.setArguments(args);
	        	}
	        }
			
			
			
//			args.putSerializable(KNEIPEN_KEY, (Serializable) kneipen);

			Main main = (Main) activity;
			kneipeServiceBinder = main.getKneipeServiceBinder();
	}
}
