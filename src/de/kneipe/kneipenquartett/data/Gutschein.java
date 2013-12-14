package de.kneipe.kneipenquartett.data;

import static de.kneipe.KneipenQuartett.jsonBuilderFactory;

import java.io.Serializable;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Gutschein implements JsonMappable, Serializable {
	// fuer Kneipe.toJsonObject()
		/**
	 * 
	 */
	private static final long serialVersionUID = -7603928897111081391L;
	
	public Long gid;
	public boolean status;
	public String code;
	public String beschreibung;
	public Kneipe kneipe;
	public Benutzer benutzer;
	
	
		JsonObjectBuilder getJsonBuilderFactory() {
			return jsonBuilderFactory.createObjectBuilder()
			                         .add("gid", gid)
			                         .add("status", status)
			                         .add("beschreibung", beschreibung)
									 .add("code", code);
		}
		@Override
		public JsonObject toJsonObject() {
			return getJsonBuilderFactory().build();
		}
		@Override
		public void fromJsonObject(JsonObject jsonObject) {
			gid = Long.valueOf(jsonObject.getJsonNumber("gid").longValue());
			beschreibung = jsonObject.getString("beschreibung");
			status = jsonObject.getBoolean("status");
			code = jsonObject.getString("code");

		}
}
