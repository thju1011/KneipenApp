package de.kneipe.kneipenquartett.ui.kneipe;

import static android.view.inputmethod.EditorInfo.IME_NULL;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPEN_KEY;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

	import java.util.Collections;
import java.util.List;

	import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Kneipe;
import de.kneipe.kneipenquartett.service.HttpResponse;
import de.kneipe.kneipenquartett.ui.main.Main;
import de.kneipe.kneipenquartett.ui.main.Prefs;

public class KneipenSucheName extends Fragment implements OnClickListener, OnEditorActionListener {
		
		private AutoCompleteTextView kneipeNameTxt;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			setHasOptionsMenu(true);
			// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
			return inflater.inflate(R.layout.kneipe_suche_name, container, false);
		}
		
		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			kneipeNameTxt = (AutoCompleteTextView) view.findViewById(R.id.name_auto);
			final ArrayAdapter<Long> adapter = new AutoCompleteIdAdapter(kneipeNameTxt.getContext());
			kneipeNameTxt.setAdapter(adapter);
			kneipeNameTxt.setOnEditorActionListener(this);
	    	
			// artikelSucheId (this) ist gleichzeitig der Listener, wenn der Suchen-Button angeklickt wird
			// und implementiert deshalb die Methode onClick() unten
	    	view.findViewById(R.id.btn_suchen).setOnClickListener(this);
	    	
		    // Evtl. vorhandene Tabs der ACTIVITY loeschen
	    	final ActionBar actionBar = getActivity().getActionBar();
	    	actionBar.setDisplayShowTitleEnabled(true);
	    	actionBar.removeAllTabs();
	    }
		
		@Override
		// Nur aufgerufen, falls setHasOptionsMenu(true) in onCreateView() aufgerufen wird
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			super.onCreateOptionsMenu(menu, inflater);
			inflater.inflate(R.menu.main, menu);
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
				case R.id.einstellungen:
					getFragmentManager().beginTransaction()
	                                    .replace(R.id.details, new Prefs())
	                                    .addToBackStack(null)
	                                    .commit();
					return true;
					
				default:
					return super.onOptionsItemSelected(item);
			}
		}
		
		@Override // OnClickListener
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.btn_suchen:
					suchen(view);
					break;
					
				default:
					break;
			}
	    }

		private void suchen(View view) {
			final Context ctx = view.getContext();
			final  String LOG_TAG = KneipenSucheName.class.getSimpleName();
			final String kneipeNameStr = kneipeNameTxt.getText().toString();
			if (TextUtils.isEmpty(kneipeNameStr)) {
				kneipeNameTxt.setError(getString(R.string.k_name_fehlt));
	    		return;
	    	}
			
			final String kneipeName = kneipeNameStr;
			final Main mainActivity = (Main) getActivity();
			final HttpResponse<? extends Kneipe> result = mainActivity.getKneipeServiceBinder().sucheKneipenByName(kneipeName, ctx);
			 Log.d(LOG_TAG,"http response in artikelsucheID wurde befüllt : " + result.toString());
			if (result.responseCode == HTTP_NOT_FOUND) {
				final String msg = getString(R.string.k_kneipe_not_found, kneipeNameStr);
				kneipeNameTxt.setError(msg);
				return;
			}
			
			final Kneipe kneipe =(Kneipe) result.resultObject;
			 Log.d(LOG_TAG,"umwandlung von json in artikel hat geklappt : " + kneipe.toString());
			final Bundle args = new Bundle(1);
			args.putSerializable(KNEIPEN_KEY, kneipe);
			
			final Fragment neuesFragment = new KneipeDetails();
			neuesFragment.setArguments(args);
			
			// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
			getFragmentManager().beginTransaction()
			                    .replace(R.id.details, neuesFragment)
			                    .addToBackStack(null)
			                    .commit();
		}
		
		@Override  // OnEditorActionListener
		public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
			if (actionId == R.id.ime_suchen || actionId == IME_NULL) {
				suchen(view);
				return true;
			}
			
			return false;
		}
		
	    // Fuer die Verwendung von AutoCompleteTextView in der Methode onViewCreated()
	    private class AutoCompleteIdAdapter extends ArrayAdapter<Long> {
	    	private LayoutInflater inflater;
	     
	    	public AutoCompleteIdAdapter(Context ctx) {
	    		super(ctx, -1);
	    		inflater = LayoutInflater.from(ctx);
	    	}
	     
	    	@Override
	    	public View getView(int position, View convertView, ViewGroup parent) {
	    		// TextView ist die Basisklasse von EditText und wiederum AutoCompleteTextView
	    		final TextView tv = convertView == null
	    				            ? (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false)
	    				            : (TextView) convertView;
	     
	    		tv.setText(String.valueOf(getItem(position)));  // Long als String innerhalb der Vorschlagsliste
	    		return tv;
	    	}
	     
	    	@Override
	    	public Filter getFilter() {
	    		// Filter ist eine abstrakte Klasse.
	    		// Zu einer davon abgeleiteten ANONYMEN Klasse wird ein Objekt erzeugt
	    		// Abstrakte Methoden muessen noch implementiert werden, und zwar HIER
	    		// performFiltering() wird durch Android in einem eigenen (Worker-) Thread aufgerufen
	    		Filter filter = new Filter() {
	    			@Override
	    			protected FilterResults performFiltering(CharSequence constraint) {
	    				List<Long> idList = null;
	    				if (constraint != null) {
	    					// Liste der IDs, die den bisher eingegebenen Praefix (= constraint) enthalten
	    					idList = sucheIds((String) constraint);
	    				}
	    				if (idList == null) {
	    					// Leere Liste, falls keine IDs zum eingegebenen Praefix gefunden werden
	    					idList = Collections.emptyList();
	    				}
	     
	    				final FilterResults filterResults = new FilterResults();
	    				filterResults.values = idList;
	    				filterResults.count = idList.size();
	     
	    				return filterResults;
	    			}
	    			
	    	    	private List<Long> sucheIds(String idPrefix) {
	    	    		final Main mainActivity = (Main) getActivity();
	    				final List<Long> ids = mainActivity.getKneipeServiceBinder().sucheIds(idPrefix);
	    				return ids;
	    	    	}
	     
	    			@Override
	    			protected void publishResults(CharSequence contraint, FilterResults results) {
	    				clear();
	    				@SuppressWarnings("unchecked")
						final List<Long> idList = (List<Long>) results.values;
	    				// Ermittelte IDs in die anzuzeigende Vorschlagsliste uebernehmen
	    				if (idList != null && !idList.isEmpty()) {
	    					addAll(idList);
	    				}

	    				if (results.count > 0) {
	    					notifyDataSetChanged();
	    				}
	    				else {
	    					notifyDataSetInvalidated();
	    				}
	    			}
	     
	    			@Override
	    			public CharSequence convertResultToString(Object resultValue) {
	    				// Long-Objekt als String
	    				return resultValue == null ? "" : String.valueOf(resultValue);
	    			}
	    		};
	    		
	    		return filter;
	    	}
	    }
	}


