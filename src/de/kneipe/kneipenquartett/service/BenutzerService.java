package de.kneipe.kneipenquartett.service;

import static android.app.ProgressDialog.STYLE_SPINNER;
import static de.kneipe.kneipenquartett.ui.main.Prefs.timeout;
import static de.kneipe.kneipenquartett.util.Constants.KUNDEN_ID_PREFIX_PATH;
import static de.kneipe.kneipenquartett.util.Constants.BENUTZER_PATH;
import static de.kneipe.kneipenquartett.util.Constants.USERNAMEN_PATH;
import static de.kneipe.kneipenquartett.util.Constants.USERNAMEN_PREFIX_PATH;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.concurrent.TimeUnit.SECONDS;



import java.util.List;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Benutzer;
import de.kneipe.kneipenquartett.util.InternalShopError;

public class BenutzerService extends Service {
	private static final String LOG_TAG = BenutzerService.class.getSimpleName();
	
	private BenutzerServiceBinder binder = new BenutzerServiceBinder();
	
//	static {
//		// 2 Eintraege in die HashMap mit 100% = 1.0 Fuellgrad
//	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	public class BenutzerServiceBinder extends Binder {
		
		public BenutzerService getService() {
			return BenutzerService.this;
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
		
		/**
		 */
		public HttpResponse<Benutzer> sucheBenutzerByEmail(String email, final Context ctx) {
			
			// (evtl. mehrere) Parameter vom Typ "Long", Resultat vom Typ "Benutzer"
			final AsyncTask<String, Void, HttpResponse<Benutzer>> sucheBenutzerByEmailTask = new AsyncTask<String, Void, HttpResponse<Benutzer>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Benutzer> doInBackground(String... emails) {
					final String email = emails[0];
		    		final String path = BENUTZER_PATH + "/" + email;
		    		Log.v(LOG_TAG, "path = " + path);
		    		final HttpResponse<Benutzer> result = WebServiceClient.getJsonSingle(path, Benutzer.class);
					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Benutzer> unused) {
					progressDialog.dismiss();
	    		}
			};

    		sucheBenutzerByEmailTask.execute(email);
    		HttpResponse<Benutzer> result = null;
	    	try {
	    		result = sucheBenutzerByEmailTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}
	    	
    		if (result.responseCode != HTTP_OK) {
	    		return result;
		    }
    		
    	//	setBestellungenUri(result.resultObject);
		    return result;
		}
//	
//		
//		public List<Long> sucheIds(String prefix) {
//			final String path = KUNDEN_ID_PREFIX_PATH + "/" + prefix;
//		    Log.v(LOG_TAG, "sucheIds: path = " + path);
//
//    		final List<Long> ids =  WebServiceClient.getJsonLongList(path);
//
//			Log.d(LOG_TAG, "sucheIds: " + ids.toString());
//			return ids;
//		}
//		
//		/**
//		 * Annahme: wird ueber AutoCompleteTextView aufgerufen, wobei die dortige Methode
//		 * performFiltering() schon einen neuen Worker-Thread startet, so dass AsyncTask hier
//		 * ueberfluessig ist.
//		 */
//		public List<String> sucheUsernamen(String prefix) {
//			final String path = USERNAMEN_PREFIX_PATH +  "/" + prefix;
//		    Log.v(LOG_TAG, "sucheUsernamen: path = " + path);
//
//    		final List<String> nachnamen = WebServiceClient.getJsonStringList(path);
//			Log.d(LOG_TAG, "sucheUsernamen: " + nachnamen);
//
//			return nachnamen;
//		}
//		
//		
//		/**
//		 */
		public HttpResponse<Benutzer> createBenutzer(Benutzer be, final Context ctx) {
			Log.d(LOG_TAG,"create benutzer vom ServiceBinder wird aufgerufen");
			// (evtl. mehrere) Parameter vom Typ "Benutzer", Resultat vom Typ "void"
			final AsyncTask<Benutzer, Void, HttpResponse<Benutzer>> createBenutzerTask = new AsyncTask<Benutzer, Void, HttpResponse<Benutzer>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Benutzer> doInBackground(Benutzer... benutzer) {
					final Benutzer be = benutzer[0];
		    		final String path = BENUTZER_PATH;
		    		Log.v(LOG_TAG, "path = " + path);
		    		Log.v(LOG_TAG, benutzer.toString());
		    		Log.v(LOG_TAG,"jz kommmmmmmmmmmmmmmmmmmmmt jsssssssssssssssssssssssssssoooooooooooooooooooooooooooooooooooooooooooooooon!!!");
		    		final HttpResponse<Benutzer> result = WebServiceClient.postJson(be, path);
		    		Log.v(LOG_TAG,"WebServiceClient.postJson durchgelaufen!!");
					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Benutzer> unused) {
					progressDialog.dismiss();
	    		}
			};
			
			createBenutzerTask.execute(be);
			HttpResponse<Benutzer> response = null; 
			try {
				
				response = createBenutzerTask.get(1000, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}
			
			be.uid = Long.valueOf(response.content);
			final HttpResponse<Benutzer> result = new HttpResponse<Benutzer>(response.responseCode, response.content, be);
			return result;
	    }
		
		/**
		 */
		public HttpResponse<Benutzer> updateBenutzer(Benutzer be, final Context ctx) {
			// (evtl. mehrere) Parameter vom Typ "Benutzer", Resultat vom Typ "void"
			final AsyncTask<Benutzer, Void, HttpResponse<Benutzer>> updateBenutzerTask = new AsyncTask<Benutzer, Void, HttpResponse<Benutzer>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Benutzer> doInBackground(Benutzer... benutzer) {
					final Benutzer be = benutzer[0];
		    		final String path = BENUTZER_PATH;
		    		Log.v(LOG_TAG, "path = " + path);

		    		final HttpResponse<Benutzer> result = WebServiceClient.putJson(be, path);
					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Benutzer> unused) {
					progressDialog.dismiss();
	    		}
			};
			
			updateBenutzerTask.execute(be);
			final HttpResponse<Benutzer> result;
			try {
				result = updateBenutzerTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}
			
			if (result.responseCode == HTTP_NO_CONTENT || result.responseCode == HTTP_OK) {
				//be.updateVersion();  // kein konkurrierendes Update auf Serverseite
				result.resultObject = be;
			}
			
			return result;
	    }
	}
}
