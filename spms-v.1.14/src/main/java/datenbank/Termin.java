package datenbank;

import java.io.*;
import javax.persistence.*;

@NamedQuery(name = "SelectTermin", query = "Select k from Termin k")
@Entity

public class Termin implements Serializable{
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Integer id;
		
		private String gruppe;
		private String titel;
		private String datum;
		private String beginn;
		private String ende;
		private String beschreibung;
		
		public Termin() {}
		public Termin(String gruppe,String titel,String datum,String beginn,String ende,String beschreibung){
			this.gruppe = gruppe;
			this.titel = titel;
			this.datum = datum;
			this.beginn = beginn;
			this.ende = ende;
			this.beschreibung = beschreibung;
		}
		public void setGruppe(String gruppe) {
			this.gruppe = gruppe;
		}
		public void setTitel(String titel) {
			this.titel = titel;
		}
		public void setDatum(String datum) {
			this.datum = datum;
		}
		public void setBeginn(String beginn) {
			this.beginn = beginn;
		}
		public void setEnde(String ende) {
			this.ende = ende;
		}
		public void setBeschreibung(String beschreibung) {
			this.beschreibung = beschreibung;
		}
		public String getGruppe() {
			return gruppe;
		}
		public String getTitel() {
			return titel;
		}
		public String getDatum() {
			return datum;
		}
		public String getBeginn() {
			return beginn;
		}
		public String getEnde() {
			return ende;
		}
		public String getBeschreibung() {
			return beschreibung;
		}
	}