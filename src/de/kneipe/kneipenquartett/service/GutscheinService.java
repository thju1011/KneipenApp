package de.kneipe.kneipenquartett.service;

import static android.app.ProgressDialog.STYLE_SPINNER;
import static de.kneipe.kneipenquartett.ui.main.Prefs.timeout;
import static de.kneipe.kneipenquartett.util.Constants.BENUTZER_PATH;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
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
import de.kneipe.kneipenquartett.data.Gutschein;
import de.kneipe.kneipenquartett.data.Gutschein;
import de.kneipe.kneipenquartett.util.InternalShopError;
import static de.kneipe.kneipenquartett.util.Constants.GUTSCHEIN_PATH;

public class GutscheinService extends Service {
	private static final String LOG_TAG = GutscheinService.class.getSimpleName();
	
	private GutscheinServiceBinder binder = new GutscheinServiceBinder();
	
	static {
		// 2 Eintraege in die HashMap mit 100% = 1.0 Fuellgrad
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	public class GutscheinServiceBinder extends Binder {
		
		public GutscheinService getService() {
			return GutscheinService.this;
		}
	}
		private ProgressDialog progressDialog;
		private ProgressDialog showProgressDialog(Context ctx) {
			progressDialog = new ProgressDialog(ctx);
			progressDialog.setProgressStyle(STYLE_SPINNER);  // Kreis (oder horizontale Linie)
			progressDialog.setMessage(getString(R.string.s_bitte_warten));
			progressDialog.setCancelable(true);      // Abbruch durch Zuruecktaste
			progressDialog.setIndeterminate(true);   // Unbekannte Anzahl an Bytes werden vom Web Service geliefert
			progressDialog.show();
			return progressDialog;
		}
		
		public HttpResponse<Gutschein> updateGutschein(Gutschein g, final Context ctx) {
			// (evtl. mehrere) Parameter vom Typ "Gutschein", Resultat vom Typ "void"
			final AsyncTask<Gutschein, Void, HttpResponse<Gutschein>> updateGutscheinTask = new AsyncTask<Gutschein, Void, HttpResponse<Gutschein>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Gutschein> doInBackground(Gutschein... gutschein) {
					final Gutschein g = gutschein[0];
		    		final String path = GUTSCHEIN_PATH;
		    		Log.v(LOG_TAG, "path = " + path);

		    		final HttpResponse<Gutschein> result = WebServiceClient.putJson(g, path);
					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Gutschein> unused) {
					progressDialog.dismiss();
	    		}
			};
			
			updateGutscheinTask.execute(g);
			final HttpResponse<Gutschein> result;
			try {
				result = updateGutscheinTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}
			
			if (result.responseCode == HTTP_NO_CONTENT || result.responseCode == HTTP_OK) {
				//be.updateVersion();  // kein konkurrierendes Update auf Serverseite
				result.resultObject = g;
			}
			
			return result;
	    }
}