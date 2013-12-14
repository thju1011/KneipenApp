package de.kneipe.kneipenquartett.ui.gutschein;

import static android.app.ActionBar.NAVIGATION_MODE_TABS;
import static de.kneipe.kneipenquartett.util.Constants.BENUTZER_KEY;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_KEY;

import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.data.Gutschein;
import de.kneipe.kneipenquartett.data.Kneipe;
import de.kneipe.kneipenquartett.service.BenutzerService.BenutzerServiceBinder;
import de.kneipe.kneipenquartett.service.GutscheinService.GutscheinServiceBinder;
import de.kneipe.kneipenquartett.service.KneipeService.KneipeServiceBinder;
import de.kneipe.kneipenquartett.ui.main.Main;
import de.kneipe.kneipenquartett.util.TabListener;
import de.kneipe.kneipenquartett.util.WischenListener;

public class GutscheinDetails extends Fragment{
private static final String LOG_TAG = GutscheinDetails.class.getSimpleName();
		
		private Bundle args;
		private Benutzer benutzer;
		private Kneipe kneipe;
		private Gutschein gutschein;
		private GutscheinServiceBinder gutscheinServiceBinder;
		private BenutzerServiceBinder benutzerServiceBinder;
		private KneipeServiceBinder kneipeServiceBinder;
		private List<Gutschein> gutscheine;
		private Gutschein aktuellerGutschein;
		private String gutscheinbeschreibung;
		private String gutscheincode;
		private List<Kneipe> kneipen;

		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			args = getArguments();
			
			setHasOptionsMenu(true);
			
	        benutzer = (Benutzer) args.get("be");
	        Log.d(LOG_TAG, benutzer.toString());
	        
	        kneipe = (Kneipe) getArguments().get(KNEIPE_KEY);
			
			return inflater.inflate(R.layout.gutschein_anzeigen, container, false);
			 
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			Log.d(LOG_TAG, "onViewCreated wird aufgerufen");
			final TextView txtbeschreibung = (TextView) view.findViewById(R.id.g_gutscheinAnzeigen_beschreibung);
			txtbeschreibung.setText(aktuellerGutschein.beschreibung);

			final TextView txtcode = (TextView) view.findViewById(R.id.g_gutscheinAnzeigen_code);
			txtcode.setText(aktuellerGutschein.code);
			
			final Activity activity = getActivity();
			final ActionBar actionBar = activity.getActionBar();
			actionBar.setNavigationMode(NAVIGATION_MODE_TABS);
			actionBar.setDisplayShowTitleEnabled(false);
			
			
			 final GestureDetector gestureDetector = new GestureDetector(getActivity(), new WischenListener(this));
			    view.setOnTouchListener(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						return gestureDetector.onTouchEvent(event);
					}
				});
			
			Main main = (Main) activity;
			gutscheinServiceBinder = main.getGutscheinServiceBinder();
			kneipeServiceBinder = main.getKneipeServiceBinder();
			
			gutscheine = benutzerServiceBinder.sucheGutscheinByUserID(benutzer.uid, view.getContext());
			Log.v(LOG_TAG, gutscheine.toString());
			
			// getGutscheinByBenutzerId
			// vergleichen mit benutzer(bundle)
			// zugehörige KneipeId FK raussuchen
			//diese kneipenId mit kneipe(bundle) vergleichen
		 
					
			kneipen = kneipeServiceBinder.initKneipen();
			Log.v(LOG_TAG,kneipen.toString());
			
			for (Gutschein g : gutscheine ) {
				if (g.kneipe.equals(kneipe)){
					aktuellerGutschein = g;
				}
				}
			}
//			if (gutscheine == null || gutscheine.isEmpty()) {
//				txtbeschreibung.setText(getString(R.string.b_benutzer_not_found, benutzer.uid));
//			}
//			else {
//				
//			}
			
		}
		
		

	
			
	   
	
