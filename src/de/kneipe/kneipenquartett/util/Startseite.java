package de.kneipe.kneipenquartett.util;

import static android.app.ActionBar.NAVIGATION_MODE_TABS;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.ui.benutzer.BenutzerStammdaten;


public class Startseite extends Fragment {
	
	Benutzer benutzer = (Benutzer) getArguments() .get("be");
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		final Activity a = getActivity();
		ActionBar actionBar = a.getActionBar();
		actionBar.setNavigationMode(NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		Bundle args = new Bundle(1);
		args.putSerializable("be", benutzer);
		
		Tab tab = actionBar.newTab()
							.setText(R.string.be)
							.setTabListener(new TabListener<BenutzerStammdaten>(a, BenutzerStammdaten.class, args));
		
		actionBar.addTab(tab);
		
		tab = actionBar.newTab()
				.setText(R.string.k)
				.setTabListener(new TabListener<BenutzerStammdaten>(a, BenutzerStammdaten.class, args));

actionBar.addTab(tab);
				 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
		return inflater.inflate(R.layout.startseite, container, false);
	}
}
