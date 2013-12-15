package de.kneipe.kneipenquartett.ui.benutzer;


import static android.app.ActionBar.NAVIGATION_MODE_TABS;
import static de.kneipe.kneipenquartett.util.Constants.BENUTZER_KEY;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.ui.kneipe.KneipeSucheKategorie;
import de.kneipe.kneipenquartett.util.TabListener;

public class BenutzerDetails extends Fragment {
	private static final String LOG_TAG = BenutzerDetails.class.getSimpleName();
	
	private Bundle args;
	private Benutzer benutzer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		args = getArguments();
		
		setHasOptionsMenu(true);
		
        benutzer = (Benutzer) args.get("be");
        Log.d(LOG_TAG, benutzer.toString());
        
		// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
		return inflater.inflate(R.layout.details_tab, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		final Activity activity = getActivity();
		final ActionBar actionBar = activity.getActionBar();
		// (horizontale) Tabs; NAVIGATION_MODE_LIST fuer Dropdown Liste
		actionBar.setNavigationMode(NAVIGATION_MODE_TABS);
	    actionBar.setDisplayShowTitleEnabled(false);  // Titel der App ausblenden, um mehr Platz fuer die Tabs zu haben
    	
	    Tab tab = actionBar.newTab()
	                       .setText(getString(R.string.b_stammdaten))
	                       .setTabListener(new TabListener<BenutzerStammdaten>(activity,
	                    		                                            BenutzerStammdaten.class,
	                    		                                            args));
	    actionBar.addTab(tab);
	    
	    tab = actionBar.newTab()
				.setText("Kneipen")
				.setTabListener(new TabListener<KneipeSucheKategorie>(activity,KneipeSucheKategorie.class, args));

		actionBar.addTab(tab);
	    

	}
}
