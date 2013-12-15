package de.kneipe.kneipenquartett.ui.map;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Kneipe;
import de.kneipe.kneipenquartett.service.KneipeService;
import de.kneipe.kneipenquartett.service.KneipeService.KneipeServiceBinder;


public class MapActivity extends Activity{
	
	private ServiceConnection kneipeServiceConnection;
	private KneipeServiceBinder kneipeServiceBinder;
	private List<Kneipe> kneipenArray = null;	 
	// Google Map
	private GoogleMap googleMap;
	    
	    public void connectService(){
	    	kneipeServiceConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
				//Log.v(LOG_TAG, "onServiceConnected() fuer KundeServiceBinder");
				kneipeServiceBinder = (KneipeServiceBinder) serviceBinder;
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				kneipeServiceBinder = null;
			}
		};
	    }
	 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.map_activity);


	        try {
	            // Loading map
	            initilizeMap();
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        connectService();
	        kneipenEinzeichen();
	        zoom();
	        googleMap.setMyLocationEnabled(true);
	 
	    }
	 
	    /**
	     * function to load map. If map is not created it will create it for you
	     * */
	    private void initilizeMap() {
	        if (googleMap == null) {
	            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
	                    R.id.map)).getMap();
	 
	            // check if map is created successfully or not
	            if (googleMap == null) {
	                Toast.makeText(getApplicationContext(),
	                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
	                        .show();
	            }
	        }
	    }
	    
	    public void kneipenEinzeichen(){
	 

	    	 kneipenArray = initKneipen();
		        
		    	for(Kneipe k : kneipenArray){
		    		// latitude and longitude
		    		double latitude = k.latitude;
		    		double longitude = k.longitude;
		    		 
		    		// create marker
		    		MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(k.name);
		    		 
		    		// adding marker
		    		googleMap.addMarker(marker);
		    	}
	    }
	    
	    public void zoom(){
	    	CameraPosition cameraPosition = new CameraPosition.Builder().target(
	                new LatLng(49.008956,8.403489)).zoom(13).build();
	 
	googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	    }
	    
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
	    

	    @Override
	    protected void onResume() {
	        
  	        Intent intent = new Intent(this, KneipeService.class);
  			bindService(intent, kneipeServiceConnection, Context.BIND_AUTO_CREATE);
  			super.onResume();
	        initilizeMap();
	    }
	    
		@Override
		public void onPause() {
			unbindService(kneipeServiceConnection);
			super.onPause();
		}
	    

		
	 
	}