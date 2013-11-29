package de.kneipe.kneipenquartett.service;

import static android.app.ProgressDialog.STYLE_SPINNER;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Gutschein;

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
		
		
}