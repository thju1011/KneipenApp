package de.kneipe.kneipenquartett.ui.map;

import static de.kneipe.kneipenquartett.util.Constants.KNEIPE_KEY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import de.kneipe.R;
import de.kneipe.kneipenquartett.data.Kneipe;

public class NavigationActivity extends FragmentActivity implements LocationListener{

	private static final LatLng KA_WEST = new LatLng(49.010178,8.386575);
	private static final LatLng KA_OST = new LatLng(49.008659,8.413075);
	private static final LatLng KA_ZENTRUM = new LatLng(49.008956,8.403489);
	private LatLng destination;
	private LatLng currentLocation;
	private static final LatLng BRASIL = new LatLng(49.009168,8.391472);
	private GoogleMap map;
	private SupportMapFragment fragment;
	private LatLngBounds latlngBounds;
	private Button bNavigation;
	private Polyline newPolyline;
	private boolean isTravelingByCar = false;
	private int width, height;
	private Bundle args;
	private Kneipe kneipe;
	private List<Kneipe> kneipenArray = null;	
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		
        args = getIntent().getExtras();
        if (args != null) {
        	kneipe = (Kneipe) args.get(KNEIPE_KEY);
        }
        destination = new LatLng(kneipe.latitude, kneipe.longitude);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        
		getSreenDimanstions();
	    fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
		map = fragment.getMap(); 	
		kneipenEinzeichen();
		bNavigation = (Button) findViewById(R.id.bNavigation);
		bNavigation.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, NavigationActivity.this);
				 Location  location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				 Log.d("Latitude:", + location.getLatitude() + ", Longitude:" + location.getLongitude());
					currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
					
				if (!isTravelingByCar)
				{
					isTravelingByCar = true;
					findDirections( currentLocation.latitude, currentLocation.longitude,kneipe.latitude, kneipe.longitude, GMapV2Direction.MODE_DRIVING );
				}
				else
				{
					isTravelingByCar = false;
					findDirections( currentLocation.latitude, currentLocation.longitude,kneipe.latitude, kneipe.longitude, GMapV2Direction.MODE_WALKING );  
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
    	latlngBounds = createLatLngBoundsObject(KA_WEST, KA_OST);
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));

	}

	public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
		PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.RED);

		for(int i = 0 ; i < directionPoints.size() ; i++) 
		{          
			rectLine.add(directionPoints.get(i));
		}
		if (newPolyline != null)
		{
			newPolyline.remove();
		}
		newPolyline = map.addPolyline(rectLine);
		if (isTravelingByCar)
		{
			latlngBounds = createLatLngBoundsObject(currentLocation, destination);
	        map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
		}
		else
		{
			latlngBounds = createLatLngBoundsObject(KA_WEST, KA_OST);
	        map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
		}
		
	}
	
	private void getSreenDimanstions()
	{
		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth(); 
		height = display.getHeight(); 
	}
	
	private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation)
	{
		if (firstLocation != null && secondLocation != null)
		{
			LatLngBounds.Builder builder = new LatLngBounds.Builder();    
			builder.include(firstLocation).include(secondLocation);
			
			return builder.build();
		}
		return null;
	}
	
	public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
		map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
		map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
		map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
		map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);
		
		GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
		asyncTask.execute(map);	
	}
	
	  public void kneipenEinzeichen(){
			 

	    	 kneipenArray = initKneipen();
		        
		    	for(Kneipe k : kneipenArray){
		    		// latitude and longitude
		    		double latitude = k.latitude;
		    		double longitude = k.longitude;
		    		
		    		// create marker
		    		
		    		MarkerOptions marker;
		    		if(k.kid == kneipe.kid){
		    			marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(k.name);
		    			// GREEN color icon
		    		marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		    		}
		    		else{
		    			marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(k.name);
		    		}
		    		// adding marker
		    		map.addMarker(marker);
		    	}
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
	public void onLocationChanged(Location location) {
		Log.d("Latitude:", + location.getLatitude() + ", Longitude:" + location.getLongitude());
	currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
	//txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
	}
	 
	@Override
	public void onProviderDisabled(String provider) {
	Log.d("Latitude","disable");
	}
	 
	@Override
	public void onProviderEnabled(String provider) {
	Log.d("Latitude","enable");
	}
	 
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	Log.d("Latitude","status");
	}
}
