package datenbank;

import java.io.*;
import javax.persistence.*;

@NamedQuery(name = "SelectNachricht", query = "Select n from Nachricht n")
@Entity

public class Nachricht{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String empfaenger;
	private String betreff;
	private String inhalt;
	
	public Nachricht(){}
	public Nachricht(String empfaenger,String betreff,String inhalt){
		this.empfaenger = empfaenger;
		this.betreff = betreff;
		this.inhalt = inhalt;
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