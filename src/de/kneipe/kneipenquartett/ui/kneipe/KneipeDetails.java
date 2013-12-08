package de.kneipe.kneipenquartett.ui.kneipe;


import static de.kneipe.kneipenquartett.util.Constants.KNEIPEN_KEY;
import static java.net.HttpURLConnection.HTTP_OK;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Bewertung;
import de.kneipe.kneipenquartett.data.Gutschein;
import de.kneipe.kneipenquartett.data.Kneipe;
import de.kneipe.kneipenquartett.service.HttpResponse;
import de.kneipe.kneipenquartett.service.KneipeService.KneipeServiceBinder;
import de.kneipe.kneipenquartett.ui.main.Main;
import de.kneipe.kneipenquartett.ui.main.Prefs;

public class KneipeDetails extends Fragment {

	private static final String LOG_TAG = KneipeDetails.class.getSimpleName();
	private Kneipe kneipe;
	private List<Long> bewertungIds;
	private KneipeServiceBinder kneipeServiceBinder;

//	private LazyAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		kneipe = (Kneipe) getArguments().get(KNEIPEN_KEY);
		Log.d(LOG_TAG, kneipe.toString());
		setHasOptionsMenu(true);
		// attachToRoot = false, weil die Verwaltung des Fragments durch die
		// Activity erfolgt
		return inflater.inflate(R.layout.kneipe_details, container, false);
	}

	@Override
	// Nur aufgerufen, falls setHasOptionsMenu(true) in onCreateView()
	// aufgerufen wird
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.einstellungen:
			getFragmentManager().beginTransaction()
					.replace(R.id.details, new Prefs()).addToBackStack(null)
					.commit();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		final Activity activity = getActivity();
		final ActionBar actionBar = activity.getActionBar();
		// (horizontale) Tabs; NAVIGATION_MODE_LIST fuer Dropdown Liste
		// actionBar.setNavigationMode(NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false); // Titel der App
														// ausblenden, um mehr
														// Platz fuer die Tabs
														// zu haben

		final Bundle args = new Bundle(1);
		args.putSerializable(KNEIPEN_KEY, kneipe);
		final TextView txtId = (TextView) view.findViewById(R.id.txt_KneipeName);
		txtId.setText(kneipe.kid);

		final TextView txtName = (TextView) view.findViewById(R.id.name);
		txtName.setText(kneipe.name);

		final TextView txtPreis = (TextView) view
				.findViewById(R.id.txt_KneipeInternetadresse);
		txtPreis.setText(kneipe.internetadresse);

	 /*Hab hier auskommentiert um emulator starten zu können
	  * final TextView txtverf = (TextView) view.findViewById(R.id.kunde_adresse);
		txtverf.setText(kneipe.adresse);*/

		// if (Main.class.equals(activity.getClass())) {
		Main main = (Main) activity;
		kneipeServiceBinder = main.getKneipeServiceBinder();
		// }
		// else {
		// Log.e(LOG_TAG, "Activity " + activity.getClass().getSimpleName() +
		// " nicht beruecksichtigt.");
		// return;
		// }
//
//		bewertungIds = kneipeServiceBinder.sucheBewertungIdsByKneipeId(
//				kneipe.kid, view.getContext());
//
//
//		if (bewertungIds == null || bewertungIds.isEmpty()) {
//
//		} else {
//			Log.d(LOG_TAG, "Starte get! (Alle Bewertungen)");
//			
//			
//			
//			HttpResponse<Bewertung> bstlngnRes = kneipeServiceBinder
//					.sucheBewertungByKneipeId(kneipe.kid, view.getContext());
//			List<Bewertung> bstlngn = bstlngnRes.resultList;
//			bstlngn.add(0, new Bewertung("ID", "Preis in "));
//
//			Log.d(LOG_TAG, "get beendet!");
//
//			final ListView list = (ListView) view.findViewById(R.id.best_list);
//			int anzahl = bewertungIds.size();
//
//			if (bstlngnRes.responseCode != HTTP_OK) {
//				return;
//			}
//			for (int i = 0; i < anzahl; i++) {
//				Log.d(LOG_TAG, String.valueOf(bstlngn.get(i).gesamtpreis));
//			}
//			adapter = new LazyAdapter(main, R.layout.row_layout,
//					bstlngn.toArray(new Bestellung[0]));
//			list.setAdapter(adapter);
//
//		}
//	}
//
//	public class LazyAdapter extends ArrayAdapter<Bewertung> {
//
//		public Context context;
//		public int layoutResourceId;
//		public Bewertung data[] = null;
//
//		public LazyAdapter(Context context, int layoutResourceId,
//				Bewertung[] data) {
//
//			super(context, layoutResourceId, data);
//			this.layoutResourceId = layoutResourceId;
//			this.context = context;
//			this.data = data;
//		}
//
//		public View getView(int position, View convertView, ViewGroup parent) {
//			View row = convertView;
//			BestellungHolder holder;
//			if (row == null) {
//				LayoutInflater inflater = ((Activity) context)
//						.getLayoutInflater();
//				row = inflater.inflate(layoutResourceId, parent, false);
//
//				holder = new BestellungHolder();
//				holder.id = (TextView) row.findViewById(R.id.best_id);
//				holder.gesamtpreis = (TextView) row
//						.findViewById(R.id.best_gesamtpreis);
//				row.setTag(holder);
//			}
//
//			Bewertung bewertung = data[position];
//
//			TextView bestId = (TextView) row.findViewById(R.id.best_id);
//			TextView bestGesPreis = (TextView) row
//					.findViewById(R.id.best_gesamtpreis);
//
//			// Setting all values in listview
//			bestId.setText(bewertung.bid + "");
//			bestGesPreis.setText(bestellung.gesamtpreis + "€");
//			return row;
//		}
//	}
//
//	static class BestellungHolder {
//		TextView id;
//		TextView gesamtpreis;
	}
		

}

