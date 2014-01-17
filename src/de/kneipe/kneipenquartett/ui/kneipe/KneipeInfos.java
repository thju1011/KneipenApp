package de.kneipe.kneipenquartett.ui.kneipe;

import static android.app.ActionBar.NAVIGATION_MODE_TABS;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_KEY;
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
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Kneipe;


public class KneipeInfos extends Fragment{
	
	 public Kneipe kneipe;
	 public Bundle args;
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		args = getArguments();

        
        kneipe = (Kneipe) getArguments().get(KNEIPE_KEY);
        
		return inflater.inflate(R.layout.kneipe_infos, container, false);
		 
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

	
		final TextView txtgruendungsjahr = (TextView) view.findViewById(R.id.k_gruendungsjahr);
		txtgruendungsjahr.setText(String.valueOf(kneipe.gruendungsjahr));

		final TextView txtguenstigste = (TextView) view.findViewById(R.id.k_guenstigstes);
		txtguenstigste.setText(String.valueOf(kneipe.guenstigstesBier));
		
		final TextView txthaltestelle = (TextView) view.findViewById(R.id.k_haltestelle);
		txthaltestelle.setText(kneipe.haltestelle);
		
		final TextView txtpersonen = (TextView) view.findViewById(R.id.k_personenanzahl);
		txtpersonen.setText(String.valueOf(kneipe.personalanzahl));
		
		final TextView txtspecials = (TextView) view.findViewById(R.id.k_specials);
		txtspecials.setText(kneipe.specials);
		
	
		}

		
	}
	
	

