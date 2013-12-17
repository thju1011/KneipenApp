package de.kneipe.kneipenquartett.ui.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.kneipe.R;

public class About extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.benutzer_create);
//		findViewById(R.id.btn_reg).setOnClickListener(this);
		
		
		setHasOptionsMenu(false);
		
		
		return inflater.inflate(R.layout.about, container, false);

		
	}

}
