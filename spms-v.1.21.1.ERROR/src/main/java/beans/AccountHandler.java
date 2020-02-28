package beans;

import datenbank.Account;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.event.Observes;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.*;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import java.util.*;
import events.GroupDeletionEvent;

@ManagedBean(name = "accountHandler")
@SessionScoped
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

	public void filter() {
		showGroupName = groupName;
		filteredNames = new ArrayList<String>();
		for(Account account : accounts) {
			if(account.belongsToGroup(groupName)){
				filteredNames.add(account.getUsername());
			}
		}
	}
	
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
		updateNameholder();
	}
	
	public void removeAllUsersFromGroup(@Observes GroupDeletionEvent event) {
		int i = 0;
		while(i < accounts.getRowCount()) {
			accounts.setRowIndex(i++);
			tempAccount = accounts.getRowData();
			tempAccount.deleteFromGroup(event.getGroupName());
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
		updateNameholder();
	}
	
	public void removeFromGroup() {
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
		speichern();
	}
	
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
		speichern();
	}

	public void addToGroup() {
		boolean found = false;
		int i = 0;
		while(!found && i < accounts.getRowCount()) {
			accounts.setRowIndex(i);
			tempAccount = accounts.getRowData();
			if(tempAccount.getUsername().equals(userName)) {
				tempAccount.addToGroup(groupName);
				found = true;
			}
			++i;
		}
		speichern();
	}
	
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
	
	public String speichern() {
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
		return "/login.xhtml?faces-redirect=true";
	}
	
	public void updateNameholder() {
		nameholder.clear();
		for(Account account : accounts) {
			nameholder.add(account.getUsername());
		}
	}

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
		return "/login.xhtml?faces-redirect=true";
		} else {
			return "/register.xhtml?faces-redirect=true";
		}
	}
	
	public boolean abfrage() {
		boolean found = false;
		int i = 0;
		while(!found && i < accounts.getRowCount()) {
			accounts.setRowIndex(i++);
			tempAccount = accounts.getRowData();
			if(tempAccount.getUsername().equals(userName)) {
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
		init();
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
