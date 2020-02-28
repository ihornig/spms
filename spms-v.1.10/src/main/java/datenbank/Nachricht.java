package datenbank;

import java.io.*;
import javax.persistence.*;

@NamedQuery(name = "SelectNachricht", query = "Select k from Nachricht k")
@Entity

public class Nachricht implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String empfaenger;
	private String betreff;
	private String inhalt;
	
	public Nachricht(){}
	public Nachricht(String empfaeng,String betre,String inha){
		empfaenger = empfaeng;
		betreff = betre;
		inhalt = inha;
	}
	public void setEmpfaenger(String empfaeng) {
		empfaenger = empfaeng;
	}
	public void setBetreff(String betre) {
		betreff = betre;
	}
	public void setInhalt(String inha) {
		inhalt = inha;
	}
	
	public String getEmpfaenger() {
		return empfaenger;
	}
	public String getBetreff() {
		return betreff;
	}
	public String getInhalt() {
		return inhalt;
	}
}