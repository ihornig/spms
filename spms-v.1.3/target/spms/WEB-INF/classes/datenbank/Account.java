package Datenbank;

import java.io.*;
import java.util.*;
import javax.persistence.*;

@NamedQuery(name = "SelectAccount", value = "Select k from Account k")
@Entity
public class Account implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String username;
	private String password;
	
	public Account() {}
	
	public Account(String username, String password) {
		this.username = username;
		this.password = password;
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
