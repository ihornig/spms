package datenbank;

import java.io.*;
import java.util.Vector;
import javax.persistence.*;

@NamedQuery(name = "SelectAccount", query = "Select k from Account k")
@Entity
public class Account implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String username;
	private String password;
	private String rolle;
	private Vector<String> gruppen = new Vector<String>();
	
	public Account() {}
	
	public Account(String username, String password, String rolle) {
		this.username = username;
		this.password = password;
		this.rolle = rolle;
	}
	
	public Vector<String> getGruppen(){
		return gruppen;
	}
	
	public boolean belongsToGroup(String name) {
		boolean found = false;
		int i = 0;
		while(!found && i < gruppen.size()) {
			String tempname = gruppen.elementAt(i++);
			if(tempname.equals(name)) {
				found = true;
			}
		}
		if(found) { return true; }
		return false;
	}
	
	public void deleteFromGroup(String groupname) {
		boolean found = false;
		int i = 0;
		while(!found && i < gruppen.size()) {
			if(gruppen.elementAt(i).equals(groupname)) {
				gruppen.remove(i);
				found = true;
			}
			++i;
		}
	}
	
	public void addToGroup(String groupname) {
		this.gruppen.add(groupname);
	}
	
	public void setGruppen(Vector<String> gruppen) {
		this.gruppen = gruppen;
	}
	
	public String getRolle(){
		return rolle;
	}
	
	public void setRolle(String rolle) {
		if(rolle.equals("admin")) { //Sollte noch geändert werden, admintoken eintragen
			this.rolle = "admin";
		} else if(rolle.equals("verwalter")){
			this.rolle = "verwalter";
		} else {
			this.rolle = "user";
		}
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
}
