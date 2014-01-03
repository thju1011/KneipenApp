package de.kneipe.kneipenquartett.data;

import static de.kneipe.KneipenQuartett.jsonBuilderFactory;

import java.io.Serializable;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


public class Kneipe implements JsonMappable, Serializable {
	/**
	 *  
	 */
	private static final long serialVersionUID = -8465867216246621016L;
	public int kid;
	public String name;
	public String adresse;
	public String internetadresse;
	public String haltestelle;
	public String art;
	public double guenstigstesBier;
	public int personalanzahl;
	public int gruendungsjahr;
	public String specials;
	public double longitude;
	public double latitude;
	public String rating;
	public Bewertung bewertung;
	public int icon;
	
	public Kneipe(){
		super();
	}
	
	public Kneipe(int Kid, String Name, String Adresse, String Internetadresse, String Haltestelle, String Art,
			double GuenstigstesBier, int Personalanzahl, int Gruendungsjahr, String Specials, double Latitude, double Longitude, String drating, int Icon) {
		kid = Kid;
		name = Name;
		adresse = Adresse;
		internetadresse = Internetadresse;
		haltestelle = Haltestelle;
		art = Art;
		guenstigstesBier = GuenstigstesBier;
		personalanzahl = Personalanzahl;
		gruendungsjahr = Gruendungsjahr;
		specials = Specials;
		longitude = Longitude;
		latitude = Latitude;
		rating = drating;
		icon = Icon;
		
	}
	
	// fuer Kneipe.toJsonObject()
	JsonObjectBuilder getJsonBuilderFactory() {
		return jsonBuilderFactory.createObjectBuilder()
		                         .add("kid", kid)
		                         .add("name", name)
		                         .add("adresse", adresse)
		                         .add("internetadresse", internetadresse)
								 .add("guenstigstesBier", guenstigstesBier)
								 .add("personalanzahl",personalanzahl)
								 .add("haltestelle", haltestelle)
								 .add("specials", specials)
								 .add("longitude",longitude)
								 .add("latitude",latitude)
								 .add("art", art)
								 .add("rating", rating);
							//	 .add ("bewertung", bewertung.getJsonBuilderFactory());
	}
	@Override
	public JsonObject toJsonObject() {
		return getJsonBuilderFactory().build();
	}
	@Override
	public void fromJsonObject(JsonObject jsonObject) {
		//kid = Long.valueOf(jsonObject.getJsonNumber("id").longValue());
		kid = jsonObject.getInt("kid");
		name = jsonObject.getString("name");
		adresse = jsonObject.getString("adresse");
		guenstigstesBier = Float.valueOf(jsonObject.getJsonNumber("guenstigstesBier").toString());
		internetadresse = jsonObject.getString("internetadresse");
		personalanzahl = jsonObject.getInt("personalanzahl");
		haltestelle = jsonObject.getString("haltestelle");
		specials = jsonObject.getString("specials");
		art = jsonObject.getString("art");
		rating = jsonObject.getJsonNumber("rating").toString();
		/**
		 *  ACHTUNG  "Zahlendreher" in Lat und Lng daher hier der tausch der beiden werte!!!!
		 */
		latitude = Float.valueOf(jsonObject.getJsonNumber("longitude").toString());
		longitude = Float.valueOf(jsonObject.getJsonNumber("latitude").toString());
//		bewertung.fromJsonObject(jsonObject.getJsonObject("bewertung"));
		
	}
	
	

	@Override
	public String toString() {
		return "Kneipe [kid=" + kid + ", name=" + name + ", adresse=" + adresse + ", internetadresse="
				+ internetadresse + ", haltestelle=" + haltestelle + ", art=" + art + ", guenstigstesBier="
				+ guenstigstesBier + ", personalanzahl=" + personalanzahl + ", specials=" + specials + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adresse == null) ? 0 : adresse.hashCode());
		result = prime * result + ((art == null) ? 0 : art.hashCode());
		result = prime * result
				+ ((bewertung == null) ? 0 : bewertung.hashCode());
		result = prime * result + gruendungsjahr;
		long temp;
		temp = Double.doubleToLongBits(guenstigstesBier);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((haltestelle == null) ? 0 : haltestelle.hashCode());
		result = prime * result
				+ ((internetadresse == null) ? 0 : internetadresse.hashCode());
		result = prime * result + kid;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + personalanzahl;
		result = prime * result + ((rating == null) ? 0 : rating.hashCode());
		result = prime * result
				+ ((specials == null) ? 0 : specials.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kneipe other = (Kneipe) obj;
		if (adresse == null) {
			if (other.adresse != null)
				return false;
		} else if (!adresse.equals(other.adresse))
			return false;
		if (art == null) {
			if (other.art != null)
				return false;
		} else if (!art.equals(other.art))
			return false;
		if (bewertung == null) {
			if (other.bewertung != null)
				return false;
		} else if (!bewertung.equals(other.bewertung))
			return false;
		if (gruendungsjahr != other.gruendungsjahr)
			return false;
		if (Double.doubleToLongBits(guenstigstesBier) != Double
				.doubleToLongBits(other.guenstigstesBier))
			return false;
		if (haltestelle == null) {
			if (other.haltestelle != null)
				return false;
		} else if (!haltestelle.equals(other.haltestelle))
			return false;
		if (internetadresse == null) {
			if (other.internetadresse != null)
				return false;
		} else if (!internetadresse.equals(other.internetadresse))
			return false;
		if (kid != other.kid)
			return false;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (personalanzahl != other.personalanzahl)
			return false;
		if (rating == null) {
			if (other.rating != null)
				return false;
		} else if (!rating.equals(other.rating))
			return false;
		if (specials == null) {
			if (other.specials != null)
				return false;
		} else if (!specials.equals(other.specials))
			return false;
		return true;
	}


	
		

}
