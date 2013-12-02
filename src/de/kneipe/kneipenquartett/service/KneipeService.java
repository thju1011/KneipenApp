package de.kneipe.kneipenquartett.service;


import static android.app.ProgressDialog.STYLE_SPINNER;
import static de.kneipe.kneipenquartett.ui.main.Prefs.timeout;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_ID_PREFIX_PATH;
import static de.kneipe.kneipenquartett.util.Constants.USERNAMEN_PREFIX_PATH;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.concurrent.TimeUnit.SECONDS;



import java.util.ArrayList;
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
import de.kneipe.kneipenquartett.data.Kneipe;
import de.kneipe.kneipenquartett.util.InternalShopError;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_PATH;
import static de.kneipe.kneipenquartett.ui.main.Prefs.mock;
import static de.kneipe.kneipenquartett.ui.main.Prefs.timeout;
import static de.kneipe.kneipenquartett.util.Constants.NAME_PATH;

public class KneipeService extends Service {
	private static final String LOG_TAG = KneipeService.class.getSimpleName();
	
	private KneipeServiceBinder binder = new KneipeServiceBinder();
	
	static {
		// 2 Eintraege in die HashMap mit 100% = 1.0 Fuellgrad
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	public class KneipeServiceBinder extends Binder {
		
		public KneipeService getService() {
			return KneipeService.this;
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
		public HttpResponse<Kneipe> sucheKneipeById(Long id, final Context ctx) {
			
			// (evtl. mehrere) Parameter vom Typ "Long", Resultat vom Typ "Kneipe"
			final AsyncTask<Long, Void, HttpResponse<Kneipe>> sucheKneipeByIdTask = new AsyncTask<Long, Void, HttpResponse<Kneipe>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Kneipe> doInBackground(Long... ids) {
					final Long id = ids[0];
		    		final String path = KNEIPE_PATH + "/" + id;
		    		Log.v(LOG_TAG, "path = " + path);
		    		final HttpResponse<Kneipe> result = WebServiceClient.getJsonSingle(path, Kneipe.class);

					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Kneipe> unused) {
					progressDialog.dismiss();
	    		}
			};

    		sucheKneipeByIdTask.execute(id);
    		HttpResponse<Kneipe> result = null;
	    	try {
	    		result = sucheKneipeByIdTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}
	    	
    		if (result.responseCode != HTTP_OK) {
	    		return result;
		    }
    		
    
		    return result;
		}
	
//		public HttpResponse<Kunde> sucheKundenByName(String Name, final Context ctx) {
//			// (evtl. mehrere) Parameter vom Typ "String", Resultat vom Typ "List<Kunde>"
//			final AsyncTask<String, Void, HttpResponse<Kunde>> sucheKundenByNameTask = new AsyncTask<String, Void, HttpResponse<Kunde>>() {
//				@Override
//	    		protected void onPreExecute() {
//					progressDialog = showProgressDialog(ctx);
//				}
//				
//				@Override
//				// Neuer Thread, damit der UI-Thread nicht blockiert wird
//				protected HttpResponse<Kunde> doInBackground(String... namen) {
//					final String name = namen[0];
//					final String path = NAME_PATH + name;
//					Log.v(LOG_TAG, "path = " + path);
//		    		final HttpResponse<Kunde> result = mock
//		    				                                   ? Mock.sucheKundenByNachname(name)
//		    				                                   : WebServiceClient.getJsonList(path, Kunde.class);
//					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
//					return result;
//				}
//				
//				@Override
//	    		protected void onPostExecute(HttpResponse<Kunde> unused) {
//					progressDialog.dismiss();
//	    		}
//			};
//			
//			sucheKundenByNameTask.execute(Name);
//			HttpResponse<Kunde> result = null;
//			try {
//				result = sucheKundenByNameTask.get(timeout, SECONDS);
//			}
//	    	catch (Exception e) {
//	    		throw new InternalShopError(e.getMessage(), e);
//			}
//
//	    	if (result.responseCode != HTTP_OK) {
//	    		return result;
//	    	}
//	    	
//	    	final ArrayList<Kunde> kunden = result.resultList;
//	    	// URLs fuer Emulator anpassen
//	    	/*
//	    	for (Kunde k : kunden) {
//	    		setBestellungenUri(k);
//	    	}
//	    	*/
//			return result;
//	    }
//	
//	
//		
//		public List<Long> sucheIds(String prefix) {
//			final String path = KNEIPE_ID_PREFIX_PATH + "/" + prefix;
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
		/**
		 */
		public HttpResponse<Kneipe> createKneipe(Kneipe kneipe, final Context ctx) {
			// (evtl. mehrere) Parameter vom Typ "Kneipe", Resultat vom Typ "void"
			final AsyncTask<Kneipe, Void, HttpResponse<Kneipe>> createKneipeTask = new AsyncTask<Kneipe, Void, HttpResponse<Kneipe>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Kneipe> doInBackground(Kneipe... kneipe) {
					final Kneipe k = kneipe[0];
		    		final String path = KNEIPE_PATH;
		    		Log.v(LOG_TAG, "path = " + path);

		    		final HttpResponse<Kneipe> result = WebServiceClient.postJson(k, path);
		    		
					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Kneipe> unused) {
					progressDialog.dismiss();
	    		}
			};
			
			createKneipeTask.execute(kneipe);
			HttpResponse<Kneipe> response = null; 
			try {
				response = createKneipeTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}
			
			kneipe.kid = Long.valueOf(response.content);
			final HttpResponse<Kneipe> result = new HttpResponse<Kneipe>(response.responseCode, response.content, kneipe);
			return result;
	    }
		
		/**
		 */
		public HttpResponse<Kneipe> updateKneipe(Kneipe be, final Context ctx) {
			// (evtl. mehrere) Parameter vom Typ "Kneipe", Resultat vom Typ "void"
			final AsyncTask<Kneipe, Void, HttpResponse<Kneipe>> updateKneipeTask = new AsyncTask<Kneipe, Void, HttpResponse<Kneipe>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Kneipe> doInBackground(Kneipe... benutzer) {
					final Kneipe be = benutzer[0];
		    		final String path = KNEIPE_PATH;
		    		Log.v(LOG_TAG, "path = " + path);

		    		final HttpResponse<Kneipe> result = WebServiceClient.putJson(be, path);
					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Kneipe> unused) {
					progressDialog.dismiss();
	    		}
			};
			
			updateKneipeTask.execute(be);
			final HttpResponse<Kneipe> result;
			try {
				result = updateKneipeTask.get(timeout, SECONDS);
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
		
		/**
		 */
		public HttpResponse<Void> deleteKneipe(Long id, final Context ctx) {
			
			// (evtl. mehrere) Parameter vom Typ "Long", Resultat vom Typ "Kneipe"
			final AsyncTask<Long, Void, HttpResponse<Void>> deleteKneipeTask = new AsyncTask<Long, Void, HttpResponse<Void>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Void> doInBackground(Long... ids) {
					final Long kId = ids[0];
		    		final String path = KNEIPE_PATH + "/" + kId;
		    		Log.v(LOG_TAG, "path = " + path);

		    		final HttpResponse<Void> result =  WebServiceClient.delete(path);
			    	return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Void> unused) {
					progressDialog.dismiss();
	    		}
			};
			
			deleteKneipeTask.execute(id);
			final HttpResponse<Void> result;
	    	try {
	    		result = deleteKneipeTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}
			
			return result;
		}
	}
}

