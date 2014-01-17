package de.kneipe.kneipenquartett.ui.kneipe;

import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_KEY;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Bewertung;
import de.kneipe.kneipenquartett.data.Kneipe;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class KommentareDetails extends Fragment {
Bundle args;
Bewertung bewertung;

	
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		
	
		bewertung = (Bewertung) getArguments().get("bw");
		args = getArguments();
		setHasOptionsMenu(false);

		return inflater.inflate(R.layout.kommentare_anzeigen, container, false);
	
}
	@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
		final TextView txtBenutzerVorname = (TextView) view.findViewById(R.id.k_benutzer_vorname);
		txtBenutzerVorname.setText(String.valueOf(bewertung.benutzer.vorname)); 
				
			final TextView txtKommentare = (TextView) view.findViewById(R.id.k_kommentare);
			txtKommentare.setText(String.valueOf(bewertung.kommentar));
			final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar1);
			float rating = Float.parseFloat(String.valueOf((bewertung.sauberkeit+ bewertung.preisleistung +bewertung.freundlichkeit)/3));
			ratingBar.setIsIndicator(true);
			ratingBar.setNumStars(5);
			ratingBar.setRating(rating);
		}
}
