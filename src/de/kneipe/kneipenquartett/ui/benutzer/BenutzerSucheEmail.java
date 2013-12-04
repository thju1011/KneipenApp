package de.kneipe.kneipenquartett.ui.benutzer;

import static de.kneipe.kneipenquartett.util.Constants.BENUTZER_KEY;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.TextView;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.service.HttpResponse;
import de.kneipe.kneipenquartett.ui.main.Main;
import de.kneipe.kneipenquartett.ui.main.Prefs;
import de.kneipe.kneipenquartett.util.InternalShopError;

public class BenutzerSucheEmail extends Fragment{

	
	


		private static final String LOG_TAG = BenutzerSucheEmail.class.getSimpleName();
		
		private AutoCompleteTextView emailTxt;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			setHasOptionsMenu(true);
			// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
			return inflater.inflate(R.layout.benutzer_suche_email, container, false);
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			emailTxt = (AutoCompleteTextView) view.findViewById(R.id.email_auto);
		//	final ArrayAdapter<String> adapter = new AutoCompleteNachnameAdapter(emailTxt.getContext());
//			emailTxt.setAdapter(adapter);
			
			// IME
			emailTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
					suchen(view);
					return true;
				}
			});
	    	
			// Listener fuer Button
			view.findViewById(R.id.btn_suchen).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					suchen(view);
				}
			});
	    	
		    // Evtl. vorhandene Tabs der ACTIVITY loeschen
	    	final ActionBar actionBar = getActivity().getActionBar();
	    	actionBar.setDisplayShowTitleEnabled(true);
	    	actionBar.removeAllTabs();
	    }
	    
		private Benutzer suchen(View view) {
			final Context ctx = view.getContext();
			
			final String email = emailTxt.getText().toString();
			if (TextUtils.isEmpty(email)) {
				emailTxt.setError(getString(R.string.b_nachname_fehlt));
	    		return null;
	    	}
			final Main mainActivity = (Main) getActivity();
			final HttpResponse<Benutzer> result = mainActivity.getBenutzerServiceBinder().sucheBenutzerByEmail(email, ctx);

			if (result.responseCode == HTTP_NOT_FOUND) {
				final String msg = getString(R.string.b_benutzer_not_found, email);
				emailTxt.setError(msg);
				return null;
			}
			
			Log.d(LOG_TAG, result.toString());
			
			Benutzer be=(Benutzer) result.resultObject;
			
			return be;
			/*
			final Intent intent = new Intent(mainActivity, Benutzer.class);
			intent.putExtra(BENUTZER_KEY, result.resultList);
			startActivity(intent);*/
		}
		
		@Override
		// Nur aufgerufen, falls setHasOptionsMenu(true) in onCreateView() aufgerufen wird
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			super.onCreateOptionsMenu(menu, inflater);
			inflater.inflate(R.menu.main, menu);
		}
		
//		@Override
//		public boolean onOptionsItemSelected(MenuItem item) {
//			switch (item.getItemId()) {
//				case R.id.einstellungen:
//					getFragmentManager().beginTransaction()
//	                                    .replace(R.id.details, new Prefs())
//	                                    .addToBackStack(null)
//	                                    .commit();
//					return true;
//					
//				default:
//					return super.onOptionsItemSelected(item);
//			}
		

	    // Fuer die Verwendung von AutoCompleteTextView in der Methode onViewCreated()
//	    private class AutoCompleteNachnameAdapter extends ArrayAdapter<String> {
//	    	private LayoutInflater inflater;
//	     
//	    	public AutoCompleteNachnameAdapter(Context ctx) {
//	    		super(ctx, -1);
//	    		inflater = LayoutInflater.from(ctx);
//	    	}
//	     
//	    	@Override
//	    	public View getView(int position, View convertView, ViewGroup parent) {
//	    		// TextView ist die Basisklasse von EditText und wiederum AutoCompleteTextView
//	    		final TextView tv = convertView != null
//	    				            ?  (TextView) convertView
//	    		                    : (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
//	    		tv.setText(String.valueOf(getItem(position)));
//	    		return tv;
//	    	}
//	     
//	    	@Override
//	    	public Filter getFilter() {
//	    		// Filter ist eine abstrakte Klasse.
//	    		// Zu einer davon abgeleiteten ANONYMEN Klasse wird ein Objekt erzeugt
//	    		// Abstrakte Methoden muessen noch implementiert werden, und zwar HIER
//	    		// performFiltering() wird durch Android in einem eigenen (Worker-) Thread aufgerufen
//	    		Filter filter = new Filter() {
//	    			@Override
//	    			protected FilterResults performFiltering(CharSequence constraint) {
//	    				List<String> nachnameList = null;
//	    				if (constraint != null) {
//	    					// Liste der IDs, die den bisher eingegebenen Praefix (= constraint) enthalten
//	    					nachnameList = sucheNachnamen((String) constraint);
//	    				}
//	    				if (nachnameList == null) {
//	    					// Leere Liste, falls keine IDs zum eingegebenen Praefix gefunden werden
//	    					nachnameList = Collections.emptyList();
//	    				}
//	     
//	    				final FilterResults filterResults = new FilterResults();
//	    				filterResults.values = nachnameList;
//	    				filterResults.count = nachnameList.size();
//	     
//	    				return filterResults;
//	    			}
//	    			
//	    	    	private List<String> sucheNachnamen(String nachnamePrefix) {
//	    	    		final Main mainActivity = (Main) getActivity();
//	    				List<String> nachnamen = null;
//	    				try {
//	    					nachnamen = mainActivity.getBenutzerServiceBinder().sucheBenutzerByEmail(email, ctx);
//	    				}
//	    				catch (InternalShopError e) {
//	    					final Throwable t = e.getCause();
//	    					if (t != null && t instanceof TimeoutException) {
//	    						nachnamen = Collections.emptyList();
//	    						Log.e(LOG_TAG, e.getMessage(), t);					
//	    					}
//	    					else {
//	    						Log.e(LOG_TAG, e.getMessage(), e);
//	    					}
//	    				}
//	    				return nachnamen;
//	    	    	}
//	     
//	    			@Override
//	    			protected void publishResults(CharSequence contraint, FilterResults results) {
//	    				clear();
//	    				@SuppressWarnings("unchecked")
//						final List<String> nachnameList = (List<String>) results.values;
//	    				// Ermittelte IDs in die anzuzeigende Vorschlagsliste uebernehmen
//	    				addAll(nachnameList);
//
//	    				if (results.count > 0) {
//	    					notifyDataSetChanged();
//	    				}
//	    				else {
//	    					notifyDataSetInvalidated();
//	    				}
//	    			}
//	     
//	    			@Override
//	    			public CharSequence convertResultToString(Object resultValue) {
//	    				// Long-Objekt als String
//	    				return resultValue == null ? "" : String.valueOf(resultValue);
//	    			}
//	    		};
//	    		
//	    		return filter;
//	    	}
//	    }
	
}

