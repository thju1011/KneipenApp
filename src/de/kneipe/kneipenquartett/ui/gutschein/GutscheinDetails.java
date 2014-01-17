package de.kneipe.kneipenquartett.ui.gutschein;

import static android.app.ActionBar.NAVIGATION_MODE_TABS;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_KEY;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.data.Gutschein;
import de.kneipe.kneipenquartett.data.Kneipe;
import de.kneipe.kneipenquartett.service.BenutzerService.BenutzerServiceBinder;
import de.kneipe.kneipenquartett.service.GutscheinService.GutscheinServiceBinder;
import de.kneipe.kneipenquartett.service.HttpResponse;
import de.kneipe.kneipenquartett.service.KneipeService.KneipeServiceBinder;
import de.kneipe.kneipenquartett.ui.main.Main;
import de.kneipe.kneipenquartett.util.Startseite;

public class GutscheinDetails extends Fragment implements OnClickListener{
private static final String LOG_TAG = GutscheinDetails.class.getSimpleName();
		
		private Bundle args;
		private Benutzer benutzer;
		private Kneipe kneipe1;
		private Gutschein gutschein;
		private GutscheinServiceBinder gutscheinServiceBinder;
		private BenutzerServiceBinder benutzerServiceBinder;
		private KneipeServiceBinder kneipeServiceBinder;
		//private List <Gutschein> gutscheine;
		private Gutschein aktuellerGutschein;
		private String gutscheinbeschreibung;
		private String gutscheincode;
		private List<Kneipe> kneipen;

		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			args = getArguments();
			
			setHasOptionsMenu(false);
			
	        benutzer = (Benutzer) args.get("be");
	        Log.d(LOG_TAG, benutzer.toString());
	        
	        kneipe1 = (Kneipe) getArguments().get(KNEIPE_KEY);
	        Log.d(LOG_TAG, kneipe1.toString());
	        
			return inflater.inflate(R.layout.gutschein_anzeigen, container, false);
			 
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			Log.d(LOG_TAG, "onViewCreated wird aufgerufen");
			final Context ctxx = view.getContext();
			
			final Activity activity = getActivity();
			final ActionBar actionBar = activity.getActionBar();
			actionBar.setNavigationMode(NAVIGATION_MODE_TABS);
			actionBar.setDisplayShowTitleEnabled(false);
			
			
			Main main = (Main) activity;
			benutzerServiceBinder = main.getBenutzerServiceBinder();
			
			//gutschein zum Benutzer herausfonden
			final HttpResponse<Gutschein> result =  benutzerServiceBinder.sucheGutscheinByUserID(benutzer.uid, ctxx);
		
			Log.v(LOG_TAG, result.toString());
	
			List<Gutschein> gutscheine = (ArrayList<Gutschein>)result.resultList;
			Log.v(LOG_TAG, String.valueOf(gutscheine.size()));
			Log.v(LOG_TAG, gutscheine.get(0).toString());
		//Liste der Gutscheine von Benutzer durchgehen und den passenden zur Kneipe ausgeben
			for (Gutschein g : gutscheine ) {
				Log.v(LOG_TAG, g.kneipe.toString());
				if (g.kneipe.kid==kneipe1.kid){
					aktuellerGutschein = g;
					Log.v(LOG_TAG, g.toString());
				}
				
				}
			try{
			final TextView txtbeschreibung = (TextView) view.findViewById(R.id.g_gutscheinAnzeigen_beschreibung);
			
			txtbeschreibung.setText(aktuellerGutschein.beschreibung);
			}
			catch (Exception e){
			 	e.getMessage().toString();
			}
			
			if(!aktuellerGutschein.status)
			{
				view.findViewById(R.id.btn_einloesen).setVisibility(View.INVISIBLE);
				TextView textview = (TextView) view.findViewById(R.id.g_gutscheinAnzeigen_verbraucht);
				textview.setText("Sie haben Ihren Gutschein bereits eingelöst !");
				
			}
			view.findViewById(R.id.btn_einloesen).setOnClickListener(this);
			}
	
		
			public void gutscheindeaktivieren(Gutschein g, Context ctxx)
			{
				
				aktuellerGutschein.status = false;
				benutzerServiceBinder.updateGutschein(benutzer.uid,aktuellerGutschein, ctxx);
				
			}
			
			@Override
			public void onClick(View view)
			{
				
				gutscheindeaktivieren(aktuellerGutschein, view.getContext());
				Fragment nf = new GutscheinEinloesen();
				args.putSerializable("Gutschein", aktuellerGutschein);
				args.putSerializable(KNEIPE_KEY, kneipe1);
				args.putSerializable("be", benutzer);
				nf.setArguments(args);
				getFragmentManager().beginTransaction()
				 .addToBackStack(null) 
				 .replace(R.id.details, nf)			
				.commit();
				
			}
		}
		
		

	
			
	   
	
