package de.kneipe.kneipenquartett.data;

import static de.kneipe.KneipenQuartett.jsonBuilderFactory;

import java.io.Serializable;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Bewertung implements JsonMappable, Serializable{

	/**
	 
	 */
	
	private static final long serialVersionUID = 5591521793265277000L;
	
	public Long bid;
	public String kommentar;
	public double rating;
	public int freundlichkeit;
	public int preisleistung;
	public int sauberkeit;
	public Benutzer benutzer;
	public Kneipe kneipe;
	
	JsonObjectBuilder getJsonBuilderFactory() {
		return jsonBuilderFactory.createObjectBuilder()
                .add("bid", bid)
                .add("kommentar", kommentar)
                .add("rating", rating)
                .add("freundlichkeit", freundlichkeit)
                .add("preisleistung", preisleistung)
                .add("sauberkeit", sauberkeit);         
	}
	
	@Override
	public JsonObject toJsonObject() {
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
