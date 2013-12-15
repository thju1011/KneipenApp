package de.kneipe.kneipenquartett.data;

import static de.kneipe.KneipenQuartett.jsonBuilderFactory;

import java.io.Serializable;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import android.util.Log;

public class Bewertung implements JsonMappable, Serializable{

	/**
	 
	 */
	
	private static final long serialVersionUID = 5591521793265277000L;
	
	public Long bid;
	public String kommentar;
	public double rating;
	public double freundlichkeit;
	public double  preisleistung;
	public double sauberkeit;
	public Benutzer benutzer;
	public Kneipe kneipe;
	private static final String LOG_TAG = Bewertung.class.getSimpleName();
	
	public Bewertung(){
		super();
	}
	
	JsonObjectBuilder getJsonBuilderFactory() {
		Log.v(LOG_TAG,kneipe.toJsonObject().toString());
		Log.v(LOG_TAG,benutzer.toJsonObject().toString());
		return jsonBuilderFactory.createObjectBuilder()
                .add("kommentar", kommentar)
                .add("freundlichkeit", freundlichkeit)
                .add("preisleistung", preisleistung)
                .add("sauberkeit", sauberkeit)
                .add("kneipe", kneipe.toJsonObject())  
                .add("benutzer", benutzer.toJsonObject());}		
	
	@Override
	public JsonObject toJsonObject() {
		Log.v(LOG_TAG,getJsonBuilderFactory().build().toString());
		return getJsonBuilderFactory().build();
	}

	@Override
	public void fromJsonObject(JsonObject jsonObject) {
		bid = Long.valueOf(jsonObject.getJsonNumber("bid").longValue());
		kommentar = jsonObject.getString("kommentar");
		rating =  jsonObject.getJsonNumber("rating").doubleValue();
		freundlichkeit = jsonObject.getInt("freundlickeit");
		preisleistung = jsonObject.getInt("preisleistung");
		sauberkeit = jsonObject.getInt("sauberkeit");

	}
}
