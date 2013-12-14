package de.kneipe.kneipenquartett.ui.gutschein;

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
import de.kneipe.kneipenquartett.data.Gutschein;
import de.kneipe.kneipenquartett.util.TabListener;

public class GutscheinDetails extends Fragment{
private static final String LOG_TAG = GutscheinDetails.class.getSimpleName();
		
		private Bundle args;
		private Benutzer benutzer;

		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			args = getArguments();
			
			setHasOptionsMenu(true);
			
	        benutzer = (Benutzer) args.get("be");
	        Log.d(LOG_TAG, benutzer.toString());
	        
			// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
			return inflater.inflate(R.layout.gutschein_anzeigen, container, false);
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
		
		}
	}

