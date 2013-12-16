package de.kneipe.kneipenquartett.service;


import static android.app.ProgressDialog.STYLE_SPINNER;
import static de.kneipe.kneipenquartett.ui.main.Prefs.timeout;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_ID_PREFIX_PATH;
import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_PATH;
import static de.kneipe.kneipenquartett.util.Constants.NAME_PATH;
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
import de.kneipe.kneipenquartett.data.Bewertung;
import de.kneipe.kneipenquartett.data.Kneipe;
import de.kneipe.kneipenquartett.util.InternalShopError;

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
		public List<Kneipe> initKneipen()
		{
			
			Kneipe k1 = new Kneipe(101,"Marktlücke","Zähringerstrasse 96","www.karlsruhermarktluecke.de","Marktplatz","Kneipe,Restaurant,Bar,Club",3.40,80,2009,"TV,Essen,DJ",49.008956,8.403489,"3.5");
			Kneipe k2 = new Kneipe(102,"La Cage","Blumenstrasse 25","www.lacage.de","Europaplatz","Sportsbar,Restaurant",3.60,50,1983,"TV,Essen,DJ,Raucherbereich",49.008555,8.395995,"2.8");
			Kneipe k3 = new Kneipe(103,"Oxford Cafe","Kaiserstrasse 57","www.oxford-cafe.de","Durlacher Tor","Bar,Restaurant",2.20,20,2007,"TV,Essen",49.00919,8.411828,"2.8");
			Kneipe k4 = new Kneipe(104,"Agostea","Rüppurrer Strasse 1","www.agostea-karlsruhe.de","Mendelsohnplatz","Club",2.50,70,2005,"DJ,Essen,Raucherbereich",49.005303,8.410693,"2.6");
			Kneipe k5 = new Kneipe(105,"Badisch Brauhaus","Stephanienstrasse 38-40","www.badisch-brauhaus.de","Europaplatz","Brauhaus",3.00,45,1999,"TV,DJ,Essen,Raucherbereich",49.011811,8.393973,"2.7");
			Kneipe k6 = new Kneipe(106,"Monkeyz Club","Kaiserallee 3","www.monkeyz-club.de","Mühlburger Tor","Club",3.10,2013,260,"Raucherbereich,Essen,DJ",49.010178,8.386575,"2.8");
			Kneipe k7 = new Kneipe(107,"Brasil","Amalienstrasse 32a","www.brasil-ka.de","Europaplatz","Bar,Kneipe",3.50,1994,450,"TV,DJ,Raucherbereich",49.009168,8.391472,"2.8");
			Kneipe k8 = new Kneipe(108,"Lehners","Karlstrasse 21a","www.lehners-wirtshaus.de/karlsruhe","Europaplatz","Bar,Restaurant",3.60,50,2001,"TV,Essen",49.00914,8.395129,"2.7");
			Kneipe k9 = new Kneipe(109,"Oxford Pub","Fasanenstrasse 6","www.oxford-pub.de","Durlacher Tor","Bar,Restaurant",2.00,25,2013,"TV,Raucherbereich,Essen",49.008659,8.413075,"2.5");
			Kneipe k10 = new Kneipe(110,"App Club","Kaiserpassage 6","www.app-club.de","Europaplatz","Club",3.50,50,2010,"DJ",49.010282,8.397324,"4.0");
					
			List<Kneipe> res = new ArrayList<Kneipe>();
						res.add(k1);
						res.add(k2);
						res.add(k3);
						res.add(k4);
						res.add(k5);
						res.add(k6);
						res.add(k7);
						res.add(k8);
						res.add(k9);
						res.add(k10);
			return res;
						
		}
		
		
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
	
		public HttpResponse<Kneipe> sucheKneipenByName(String Name, final Context ctx) {
			// (evtl. mehrere) Parameter vom Typ "String", Resultat vom Typ "List<Kneipe>"
			final AsyncTask<String, Void, HttpResponse<Kneipe>> sucheKneipenByNameTask = new AsyncTask<String, Void, HttpResponse<Kneipe>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Kneipe> doInBackground(String... namen) {
					final String name = namen[0];
					final String path = NAME_PATH + name;
					Log.v(LOG_TAG, "path = " + path);
		    		final HttpResponse<Kneipe> result = WebServiceClient.getJsonList(path, Kneipe.class);
					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Kneipe> unused) {
					progressDialog.dismiss();
	    		}
			};
			
			sucheKneipenByNameTask.execute(Name);
			HttpResponse<Kneipe> result = null;
			try {
				result = sucheKneipenByNameTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}

	    	if (result.responseCode != HTTP_OK) {
	    		return result;
	    	}
	    	
	    	final ArrayList<Kneipe> kunden = result.resultList;
	    	// URLs fuer Emulator anpassen
	    	/*
	    	for (Kneipe k : kunden) {
	    		setBestellungenUri(k);
	    	}
	    	*/
			return result;
	    }
	
	
		
		public List<Long> sucheIds(String prefix) {
			final String path = KNEIPE_ID_PREFIX_PATH + "/" + prefix;
		    Log.v(LOG_TAG, "sucheIds: path = " + path);

    		final List<Long> ids =  WebServiceClient.getJsonLongList(path);

			Log.d(LOG_TAG, "sucheIds: " + ids.toString());
			return ids;
		}
		
		/**
		 * Annahme: wird ueber AutoCompleteTextView aufgerufen, wobei die dortige Methode
		 * performFiltering() schon einen neuen Worker-Thread startet, so dass AsyncTask hier
		 * ueberfluessig ist.
		 */
		public List<String> sucheUsernamen(String prefix) {
			final String path = USERNAMEN_PREFIX_PATH +  "/" + prefix;
		    Log.v(LOG_TAG, "sucheUsernamen: path = " + path);

    		final List<String> nachnamen = WebServiceClient.getJsonStringList(path);
			Log.d(LOG_TAG, "sucheUsernamen: " + nachnamen);

			return nachnamen;
		}
		
		
		
		/**
		 */
//		public HttpResponse<Kneipe> createKneipe(Kneipe kneipe, final Context ctx) {
//			// (evtl. mehrere) Parameter vom Typ "Kneipe", Resultat vom Typ "void"
//			final AsyncTask<Kneipe, Void, HttpResponse<Kneipe>> createKneipeTask = new AsyncTask<Kneipe, Void, HttpResponse<Kneipe>>() {
//				@Override
//	    		protected void onPreExecute() {
//					progressDialog = showProgressDialog(ctx);
//				}
//				
//				@Override
//				// Neuer Thread, damit der UI-Thread nicht blockiert wird
//				protected HttpResponse<Kneipe> doInBackground(Kneipe... kneipe) {
//					final Kneipe k = kneipe[0];
//		    		final String path = KNEIPE_PATH;
//		    		Log.v(LOG_TAG, "path = " + path);
//
//		    		final HttpResponse<Kneipe> result = WebServiceClient.postJson(k, path);
//		    		
//					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
//					return result;
//				}
//				
//				@Override
//	    		protected void onPostExecute(HttpResponse<Kneipe> unused) {
//					progressDialog.dismiss();
//	    		}
//			};
//			
//			createKneipeTask.execute(kneipe);
//			HttpResponse<Kneipe> response = null; 
//			try {
//				response = createKneipeTask.get(timeout, SECONDS);
//			}
//	    	catch (Exception e) {
//	    		throw new InternalShopError(e.getMessage(), e);
//			}
//			
//			kneipe.kid = response.content);
//			final HttpResponse<Kneipe> result = new HttpResponse<Kneipe>(response.responseCode, response.content, kneipe);
//			return result;
//	    }
		
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

