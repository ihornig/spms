package beans;

import datenbank.Account;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.*;
import javax.persistence.*;
import javax.transaction.UserTransaction;

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
	private Account tempAccount = new Account();
	private String groupToAdd;
	private String usernameToAdd;
	
	public void addToGroup() {
		
	}
	
	public String getGroupToAdd() {
		return groupToAdd;
	}

	public void setGroupToAdd(String groupToAdd) {
		this.groupToAdd = groupToAdd;
	}

	public String getUsernameToAdd() {
		return usernameToAdd;
	}

	public void setUsernameToAdd(String usernameToAdd) {
		this.usernameToAdd = usernameToAdd;
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
	}
	
	public void speichern() {
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
