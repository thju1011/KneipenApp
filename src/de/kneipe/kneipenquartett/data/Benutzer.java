package de.kneipe.kneipenquartett.data;


import java.io.Serializable;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;



import static de.kneipe.KneipenQuartett.jsonBuilderFactory;

public class Benutzer implements JsonMappable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2843760959760554956L;

	public Long uid;
	public String username;
	public String email;
	public boolean agbAkzeptiert = true;
	public String password;
	public String geschlecht;
	public String nachname;
	public String vorname;
	
	protected JsonObjectBuilder getJsonObjectBuilder() {
		if(password==null) {
		
			return jsonBuilderFactory.createObjectBuilder()
	                .add("id", uid)
                    .add("username", username)
                    .add("email", email)
                    .add("name", nachname)
                    .add("vorname", vorname)
                    .add("agbAkzeptiert", agbAkzeptiert)
                    .add("geschlecht", geschlecht)
                    ;
		} else
		{		
			//zum Anlegen eines Benutzer
			return jsonBuilderFactory.createObjectBuilder()
                .add("username", username)
                .add("name", nachname)
                .add("vorname", vorname)
                .add("email", email)
                .add("agbAkzeptiert", agbAkzeptiert)
                .add("geschlecht", geschlecht)
                .add("password", password)
                ;
		}
	}

	public void fromJsonObject(JsonObject jsonObject) {

		uid = Long.valueOf(jsonObject.getJsonNumber("id").longValue());
	    username = jsonObject.getString("username");
		nachname = jsonObject.getString("name");
		vorname = jsonObject.getString("vorname");
		geschlecht = jsonObject.getString("geschlecht");
		email = jsonObject.getString("email");
		agbAkzeptiert = jsonObject.getBoolean("agbAkzeptiert");

	}
	
	
	@Override
	public JsonObject toJsonObject() {
		return getJsonObjectBuilder().build();
	}
		

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (agbAkzeptiert ? 1231 : 1237);
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		//result = prime * result + ((emailWdh == null) ? 0 : emailWdh.hashCode());
		result = prime * result + ((geschlecht == null) ? 0 : geschlecht.hashCode());
		result = prime * result + ((nachname == null) ? 0 : nachname.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		//result = prime * result + ((passwordWdh == null) ? 0 : passwordWdh.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((vorname == null) ? 0 : vorname.hashCode());
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
		Benutzer other = (Benutzer) obj;
		if (agbAkzeptiert != other.agbAkzeptiert)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
//		if (emailWdh == null) {
//			if (other.emailWdh != null)
//				return false;
//		} else if (!emailWdh.equals(other.emailWdh))
//			return false;
		if (geschlecht == null) {
			if (other.geschlecht != null)
				return false;
		} else if (!geschlecht.equals(other.geschlecht))
			return false;
		if (nachname == null) {
			if (other.nachname != null)
				return false;
		} else if (!nachname.equals(other.nachname))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
//		if (passwordWdh == null) {
//			if (other.passwordWdh != null)
//				return false;
//		} else if (!passwordWdh.equals(other.passwordWdh))
//			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (vorname == null) {
			if (other.vorname != null)
				return false;
		} else if (!vorname.equals(other.vorname))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Benutzer [uid=" + uid + ", username=" + username + ", email=" + email + ", emailWdh=" 
				+ ", agbAkzeptiert=" + agbAkzeptiert + ", password=" + password + ", passwordWdh="
				+ ", geschlecht=" + geschlecht + ", nachname=" + nachname + ", vorname=" + vorname + "]";
	}
	
}
