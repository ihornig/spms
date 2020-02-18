import java.io.*;
import javax.persistence.*;

@NamedQuery(name = "SelectNachricht", query = "Select n from Nachricht n")
@Entity

public class Nachricht{
	String empfänger;
	String betreff;
	String inhalt;
	public Nachricht(){}
	public Nachricht(String empfänger,String betreff,String inhalt){
		this.empfänger = empfänger;
		this.betreff = betreff;
		this.inhalt = inhalt;
	}
	public String getEmpfänger() {
		return empfänger;
	}
	public String getBetreff() {
		return betreff;
	}
	public String getInhalt() {
		return Inhalt;
	}
}