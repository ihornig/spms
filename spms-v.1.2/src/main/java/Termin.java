import java.io.*;
import javax.persistence.*;

@NamedQuery(name = "SelectTermin", query = "Select t from Termin t")
@Entity

public class Termin{
		String titel;
		String datum;
		double beginn;
		double ende;
		String beschreibung;
		public Termin() {}
		public Termin(String t,String d,double beg,double e,String besch){
			titel = t;
			datum = d;
			beginn = beg;
			ende = e;
			beschreibung = besch;
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
}