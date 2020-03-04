package beans;

import datenbank.Account;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.*;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import java.util.*;

@ManagedBean(name = "accountHandler")
@RequestScoped
public class AccountHandler {

	public AccountHandler(){
		tempAccount = new Account();
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Resource
	private UserTransaction utx;
	
	private DataModel<Account> accounts;
	private Account tempAccount;
	private String groupName;
	private String userName;
	private String showGroupName = "";
	private String registerdoppelt;
	

	public String getRegisterdoppelt() {
		return registerdoppelt;
	}

	public void setRegisterdoppelt(String registerdoppelt) {
		this.registerdoppelt = registerdoppelt;
	}

	public void setShowGroupName(String name) {
		showGroupName = name;
	}
	
	public String getShowGroupName() {
		return showGroupName;
	}
	
	private List<String> filteredNames = new ArrayList<String>();
	
	public List<String> getFilteredNames(){
		return filteredNames;
	}
	
	public void setFilteredNames(List<String> names) {
		filteredNames = names;
	}
	
	private List<String> nameholder = new ArrayList<String>();

	/**
	 * Fügt alle Benutzer der ausgewählten Gruppe in eine Liste ein.
	 */
	public void filter() {
		showGroupName = groupName;
		filteredNames = new ArrayList<String>();
		for(Account account : accounts) {
			if(account.belongsToGroup(groupName)){
				filteredNames.add(account.getUsername());
			}
		}
	}
	
	/**
	 * Löscht den ausgewählten Account aus der Datenbank
	 */
	public void accountLoeschen() {
		boolean found = false;
		int i = 0;
		while(!found && i < accounts.getRowCount()) {
			accounts.setRowIndex(i++);
			tempAccount = accounts.getRowData();
			if(tempAccount.getUsername().equals(userName)) {
				found = true;
			}
		}
		try {
			utx.begin();
		} catch (Exception exc) {}
		tempAccount = em.merge(tempAccount);
		em.remove(tempAccount);
		accounts.setWrappedData(em.createNamedQuery("SelectAccount").getResultList());
		try {
			utx.commit();
		} catch (Exception exc) {}
	}
	
	/**
	 * Löscht alle Benutzer aus der ausgewählten Gruppe.
	 * Wird ausgeführt wenn eine Gruppe gelöscht wird.
	 */
	public void removeAllUsersFromGroup(String name) {
		int i = 0;
		while(i < accounts.getRowCount()) {
			accounts.setRowIndex(i++);
			tempAccount = accounts.getRowData();
			tempAccount.deleteFromGroup(name);
			try {
				utx.begin();
			} catch (Exception exc) {}
			tempAccount = em.merge(tempAccount);
			em.persist(tempAccount);
			accounts.setWrappedData(em.createNamedQuery("SelectAccount").getResultList());
			try {
				utx.commit();
			} catch (Exception exc) {}
			tempAccount = new Account();
		}
	}
	
	/**
	 * Löscht den ausgewählten Benutzer aus der ausgewählten Gruppe.
	 */
	public void removeFromGroup() {
		if(groupName == null) return;
		boolean found = false;
		int i = 0;
		while(!found && i < accounts.getRowCount()) {
			accounts.setRowIndex(i);
			tempAccount = accounts.getRowData();
			if(tempAccount.getUsername().equals(userName)) {
				tempAccount.deleteFromGroup(groupName);
				found = true;
			}
			++i;
		}
		changeSave();
	}
	
	/**
	 * Ändert den Berechtigungsrang des ausgewählten Benutzers.
	 */
	public void rolleAendern() {
		boolean found = false;
		int i = 0;
		while(!found && i < accounts.getRowCount()) {
			accounts.setRowIndex(i);
			tempAccount = accounts.getRowData();
			if(tempAccount.getUsername().equals(userName)) {
				tempAccount.setRolle(groupName);
				found = true;
			}
			++i;
		}
		changeSave();
	}
	
	/**
	 * Fügt den ausgewählten Benutzer in die gewählte Gruppe ein.
	 */
	public void addToGroup() {
		if(groupName == null || userName == null) return;
		boolean found = false;
		int i = 0;
		while(!found && i < accounts.getRowCount()) {
			accounts.setRowIndex(i);
			tempAccount = accounts.getRowData();
			if(tempAccount.getUsername().equals(userName)) {
				if(!tempAccount.belongsToGroup(groupName)) {
					tempAccount.addToGroup(groupName);
					found = true;
				}
			}
			++i;
		}
		changeSave();
	}
	
	/**
	 * Lädt die gespeicherten Benutzeraccounts aus dem DataModeler.
	 */
	@PostConstruct
	public void init() {
		try {
			utx.begin();
		} catch (Exception exc) {}
		accounts = new ListDataModel<Account>();
		accounts.setWrappedData(em.createNamedQuery("SelectAccount").getResultList());
		try {
			utx.commit();
		} catch (Exception exc) {}
		tempAccount = new Account();
		updateNameholder();
	}
	
	/**
	 * Updatet den Inhalt der Variable Nameholder, welcher die Inhalte für SelectOneMenus
	 * auf den JSF Seiten bereitstellt.
	 */
	public void updateNameholder() {
		nameholder.clear();
		for(Account account : accounts) {
			nameholder.add(account.getUsername());
		}
	}
	
	/**
	 * Änderungen an einem Account werden in der Datenbank gespeichert.
	 */
	public void changeSave() { 
		try {
			utx.begin();
		} catch (Exception exc) {}
		tempAccount = em.merge(tempAccount);
		em.persist(tempAccount);
		accounts.setWrappedData(em.createNamedQuery("SelectAccount").getResultList());
		try {
			utx.commit();
		} catch (Exception exc) {}
		tempAccount = new Account();
	}
	
	/**
	 * Ein neu angelegter Benutzer wird in der Datenbank gespeichert.
	 */
	public String speichern() {
		if(abfrage()) { 
			try {
				utx.begin();
			} catch (Exception exc) {}
			tempAccount = em.merge(tempAccount);
			em.persist(tempAccount);
			accounts.setWrappedData(em.createNamedQuery("SelectAccount").getResultList());
			try {
				utx.commit();
			} catch (Exception exc) {}
			tempAccount = new Account();
			updateNameholder();
			registerdoppelt = "";
			return "/login.xhtml?faces-redirect=true";
		} else {
			registerdoppelt = "Es gibt bereits einen Account mit dem Benutzernamen";
			return "/register.xhtml?faces-redirect=true";
		}
	}
	
	/**
	 * Überprüfung ob bereits ein Account mit dem gewählten Benutzername existiert.
	 */
	public boolean abfrage() {
		boolean found = false;
		int i = 0;
		while(!found && i < accounts.getRowCount()) {
			accounts.setRowIndex(i++);
			Account anotherTempAccount = accounts.getRowData();
			if(tempAccount.getUsername().equals(anotherTempAccount.getUsername())) {
				found = true;
			}
		}
		if(!found) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setNameholder(List<String> name) {
		nameholder = name;
	}
	
	public List<String> getNameholder(){
		return nameholder;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public DataModel<Account> getAccounts(){
		return accounts;
	}
	
	public void setAccount(DataModel<Account> accounts) {
		this.accounts = accounts;
	}
	
	public void setTempAccount(Account account) {
		this.tempAccount = account;
	}
	
	public Account getTempAccount() {
		return tempAccount;
	}
	
}
