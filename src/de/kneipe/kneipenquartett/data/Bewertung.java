package de.kneipe.kneipenquartett.data;

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
	
	// TO do - Get Json Builder

	@Override
	public JsonObject toJsonObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fromJsonObject(JsonObject jsonObject) {
		// TODO Auto-generated method stub
		
	}

}
