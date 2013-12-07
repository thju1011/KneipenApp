package de.kneipe.kneipenquartett.ui.kneipe;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.ui.benutzer.BenutzerCreate;
import de.kneipe.kneipenquartett.ui.main.Login;
import de.kneipe.kneipenquartett.ui.main.Main;

public class KneipeSucheKategorie extends Fragment implements OnClickListener, OnEditorActionListener{
	private static final String LOG_TAG = KneipeSucheKategorie.class.getSimpleName();
	private Bundle args;
	private EditText kneipeName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.kneipe_suche_kategorie);
		
		args = getArguments();
		
		setHasOptionsMenu(true);
		
		
		return inflater.inflate(R.layout.kneipe_suche_kategorie, container, false);
	

	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
    	
		Log.d(LOG_TAG, "View wird aufgebaut");
		final  String LOG_TAG = Login.class.getSimpleName();
		 kneipeName = (EditText) view.findViewById(R.id.txtKneipeSuchen);

		 final Main mainActivity = (Main) getActivity();
		
		mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		view.findViewById(R.id.btnKneipeSuchen).setOnClickListener(this);
    }
	public void onClick(View view) {
		final Context ctx = view.getContext();
		switch(view.getId()){
		case R.id.btnKneipeSuchen:	
			 String kneipeNameStr = kneipeName.getText().toString();
			 
			 if(TextUtils.isEmpty(kneipeNameStr))
					kneipeName.setError("Eingabe fehlt");

			
				
				
				break;
			
			
				
			
		case R.id.btn_reg:
			getFragmentManager().beginTransaction()
			.replace(R.id.details, new BenutzerCreate())
			.commit();
			break;
		}
		
		// Eingabetext ermitteln
		
			
			
	}
	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
