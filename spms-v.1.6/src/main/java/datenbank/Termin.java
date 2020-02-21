package datenbank;

import java.io.*;
import javax.persistence.*;

@NamedQuery(name = "SelectTermin", query = "Select t from Termin t")
@Entity

public class Termin{
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Integer id;	
		
		private String titel;
		private String datum;
		private double beginn;
		private double ende;
		private String beschreibung;
		
		public Termin() {}
		public Termin(String titel,String datum,double beginn,double ende,String beschreibung){
			this.titel = titel;
			this.datum = datum;
			this.beginn = beginn;
			this.ende = ende;
			this.beschreibung = beschreibung;
		}
		public void setTitel(String titel) {
			this.titel = titel;
		}
		public void setDatum(String datum) {
			this.datum = datum;
		}
		public void setBeginn(double beginn) {
			this.beginn = beginn;
		}
		public void setEnde(double ende) {
			this.ende = ende;
		}
		public void setBeschreibung(String beschreibung) {
			this.beschreibung = beschreibung;
		}
		public String getTitel() {
			return titel;
		}
		public String getDatum() {
			return datum;
		}
		public double getBeginn() {
			return beginn;
		}
		public double getEnde() {
			return ende;
		}
		public String getBeschreibung() {
			return beschreibung;
		}
	}