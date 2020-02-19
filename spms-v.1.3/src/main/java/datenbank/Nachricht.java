package datenbank

import java.io.*;
import javax.persistence.*;

@NamedQuery(name = "SelectNachricht", query = "Select n from Nachricht n")
@Entity

public class Nachricht implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String empf�nger;
	private String betreff;
	private String inhalt;
	
	public Nachricht(){}
	public Nachricht(String empf�nger,String betreff,String inhalt){
		this.empf�nger = empf�nger;
		this.betreff = betreff;
		this.inhalt = inhalt;
	}
	public String getEmpf�nger() {
		return empf�nger;
	}
	public String getBetreff() {
		return betreff;
	}
	public String getInhalt() {
		return Inhalt;
	}
}