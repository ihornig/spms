package datenbank;

import java.io.*;
import javax.persistence.*;

@NamedQuery(name = "SelectNachricht", query = "Select n from Nachricht n")
@Entity

public class Nachricht implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String absender;
	private String gruppe;
	private String inhalt;
	
	public Nachricht(){}
	
	public Nachricht(String gruppe, String inhalt, String absender) {
		this.gruppe = gruppe;
		this.inhalt = inhalt;
		this.absender = absender;
	}
	
	public String getGruppe() {
		return gruppe;
	}

	public void setGruppe(String gruppe) {
		this.gruppe = gruppe;
	}

	public void setInhalt(String inhalt) {
		this.inhalt = inhalt;
	}

	public String getInhalt() {
		return inhalt;
	}
	
	public String getAbsender() {
		return absender;
	}

	public void setAbsender(String absender) {
		this.absender = absender;
	}
}