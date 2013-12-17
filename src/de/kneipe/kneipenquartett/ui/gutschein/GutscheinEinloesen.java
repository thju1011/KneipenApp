package de.kneipe.kneipenquartett.ui.gutschein;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.data.Gutschein;
import de.kneipe.kneipenquartett.data.Kneipe;
import de.kneipe.kneipenquartett.ui.kneipe.KneipeDetails;

public class GutscheinEinloesen extends Fragment implements OnClickListener{
	private static final String LOG_TAG = GutscheinEinloesen.class.getSimpleName();
	private Bundle args;
	private Benutzer benutzer;
	private Gutschein gutschein;
	private Kneipe kneipe;
	private TextView textview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		args = getArguments();
		
		setHasOptionsMenu(false);
		
        benutzer = (Benutzer) args.get("be");
        gutschein = (Gutschein) args.get("Gutschein");
        kneipe = (Kneipe) args.get(KNEIPE_KEY);
        
        Log.d(LOG_TAG, benutzer.toString());
        
        
       
        
		return inflater.inflate(R.layout.gutschein_einloesen, container, false);
		 
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d(LOG_TAG, "onViewCreated wird aufgerufen");
		final Context ctxx = view.getContext();
		
		final Activity activity = getActivity();
		final ActionBar actionBar = activity.getActionBar();
		actionBar.setNavigationMode(NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		
		
	textview = (TextView) view.findViewById(R.id.gutschein_Code);
	textview.setText(gutschein.code);
		view.findViewById(R.id.btn_zurueck).setOnClickListener(this);
		}

	
	@Override
	public void onClick(View view)
	{
		Fragment nf = new KneipeDetails();
		//args.putSerializable("Gutschein", gutschein);
		args.putSerializable("be", benutzer);
		args.putSerializable(KNEIPE_KEY, kneipe);
		nf.setArguments(args);
		getFragmentManager().beginTransaction()
		 .addToBackStack(null) 
		 .replace(R.id.details, nf)			
		.commit();
		
		
	}
	
	
}
