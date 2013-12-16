package de.kneipe.kneipenquartett.ui.kneipe;

import static android.app.ActionBar.NAVIGATION_MODE_TABS;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_KEY;

import java.util.ArrayList;
import java.util.List;

import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Bewertung;
import de.kneipe.kneipenquartett.data.Gutschein;
import de.kneipe.kneipenquartett.data.Kneipe;
import de.kneipe.kneipenquartett.service.BewertungService.BewertungServiceBinder;
import de.kneipe.kneipenquartett.service.HttpResponse;
import de.kneipe.kneipenquartett.service.KneipeService.KneipeServiceBinder;
import de.kneipe.kneipenquartett.ui.main.Main;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class KommentareShow extends Fragment{
	private static final String LOG_TAG = KommentareShow.class.getSimpleName();
	public Bundle args;
	public Kneipe kneipe;
	private Bewertung aktuelleBewertung;
	private BewertungServiceBinder bewertungServiceBinder;
	private KneipeServiceBinder kneipeServiceBinder;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
	args = getArguments();
	
	setHasOptionsMenu(true);
    
    kneipe = (Kneipe) getArguments().get(KNEIPE_KEY);
    Log.v(LOG_TAG, kneipe.toString());
    
	return inflater.inflate(R.layout.kommentare_anzeigen, container, false);
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

		List<Bewertung> bewertungen = (ArrayList<Bewertung>)result.resultList;
		Log.v(LOG_TAG, String.valueOf(bewertungen.size()));
		Log.v(LOG_TAG, bewertungen.get(0).toString());
	
		for (Bewertung b : bewertungen ) {
			Log.v(LOG_TAG, b.kneipe.toString());
			if (b.kneipe.kid==kneipe.kid){
				aktuelleBewertung = b;
				Log.v(LOG_TAG, b.toString());
			}
		final TextView txtKommentare = (TextView) view.findViewById(R.id.k_kommentare);
		txtKommentare.setText(String.valueOf(aktuelleBewertung.kommentar));
		
	}
	}
}
