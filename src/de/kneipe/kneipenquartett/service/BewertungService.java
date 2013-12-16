package de.kneipe.kneipenquartett.service;

import static android.app.ProgressDialog.STYLE_SPINNER;
import static de.kneipe.kneipenquartett.ui.main.Prefs.timeout;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_PATH;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.concurrent.TimeUnit.SECONDS;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Bewertung;
import de.kneipe.kneipenquartett.data.Gutschein;
import de.kneipe.kneipenquartett.util.InternalShopError;
import static de.kneipe.kneipenquartett.util.Constants.BENUTZER_PATH;
import static de.kneipe.kneipenquartett.util.Constants.BEWERTUNG_PATH;

public class BewertungService extends Service {
	private static final String LOG_TAG = BewertungService.class
			.getSimpleName();
	private BewertungServiceBinder binder = new BewertungServiceBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	private ProgressDialog progressDialog;

	private ProgressDialog showProgressDialog(Context ctx) {
		progressDialog = new ProgressDialog(ctx);
		progressDialog.setProgressStyle(STYLE_SPINNER); // Kreis (oder
														// horizontale Linie)
		progressDialog.setMessage(getString(R.string.s_bitte_warten));
		progressDialog.setCancelable(true); // Abbruch durch Zuruecktaste
		progressDialog.setIndeterminate(true); // Unbekannte Anzahl an Bytes
												// werden vom Web Service
												// geliefert
		progressDialog.show();
		return progressDialog;
	}

	public class BewertungServiceBinder extends Binder {

		public BewertungService getService() {
			return BewertungService.this;
		}
	}

	public HttpResponse<Bewertung> createBewertung(Bewertung be,
			final Context ctx) {
		// (evtl. mehrere) Parameter vom Typ "Bewertung", Resultat vom Typ
		// "void"
		final AsyncTask<Bewertung, Void, HttpResponse<Bewertung>> createBewertungTask = new AsyncTask<Bewertung, Void, HttpResponse<Bewertung>>() {
			@Override
			protected void onPreExecute() {
				progressDialog = showProgressDialog(ctx);
			}

			@Override
			// Neuer Thread, damit der UI-Thread nicht blockiert wird
			protected HttpResponse<Bewertung> doInBackground(
					Bewertung... bewertung) {
				final Bewertung be = bewertung[0];
				final String path = BEWERTUNG_PATH;
				Log.v(LOG_TAG, "path = " + path);

				final HttpResponse<Bewertung> result = WebServiceClient
						.postJson(be, path);

				Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
				return result;
			}

			@Override
			protected void onPostExecute(HttpResponse<Bewertung> unused) {
				progressDialog.dismiss();
			}
		};

		createBewertungTask.execute(be);
		HttpResponse<Bewertung> response = null;
		try {
			response = createBewertungTask.get(timeout, SECONDS);
		} catch (Exception e) {
			throw new InternalShopError(e.getMessage(), e);
		}

		be.bid = Long.valueOf(response.content);
		final HttpResponse<Bewertung> result = new HttpResponse<Bewertung>(
				response.responseCode, response.content, be);
		return result;
	}
	
	public HttpResponse<Bewertung> findBewertungbyKneipe(Long id, final Context ctx) {
		
		final AsyncTask<Long, Void, HttpResponse<Bewertung>> findBewertungbyKneipeTask = new AsyncTask<Long, Void, HttpResponse<Bewertung>>() {
			protected HttpResponse<Bewertung> doInBackground(Long... ids) {
				final Long id = ids[0];
				final String path = KNEIPE_PATH + "/" + id + "/bewertungen";
				Log.v(LOG_TAG, "path = " + path);
				
				//macht er nicht
				final HttpResponse<Bewertung> resultList = WebServiceClient.getJsonList(path, Bewertung.class);
				Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + resultList);
			
				return resultList;
				
			}
		};
		
		findBewertungbyKneipeTask.execute(id);
		HttpResponse<Bewertung> result = null;
    	try {
    		result = findBewertungbyKneipeTask.get(timeout, SECONDS);
		}
    	catch (Exception e) {
    		throw new InternalShopError(e.getMessage(), e);
		}
    	
		if (result.responseCode != HTTP_OK) {
    		return result;
	    }
		
	    return result;
	}

}
