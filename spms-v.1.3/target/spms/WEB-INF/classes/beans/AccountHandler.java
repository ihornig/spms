package beans;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.*;
import javax.persistence.*;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import datenbank.Account;

@ManagedBean(name = "accountHandler")
@SessionScoped
public class AccountHandler {

	@PersistenceContext
	private EntityManager em;
	
	@Resource
	private UserTransaction utx;
	
	private DataModel<Account> accounts;
	private Account tempAccount = new Account();
	
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
	
	public void neu() {
		tempAccount = new Account();
		speichern();
	}
	
	@PostConstruct
	public void init() {
		try {
			utx.begin();
		} catch (Exception exc) {}
		kunden = new ListDataModel<Kunde>();
		kunden.setWrappedData(em.createNamedQuery("SelectAccount").getResultList());
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
		kunden.setWrappedData(em.createNamedQuery("SelectAccount").getResultList());
		try {
			utx.commit();
		} catch (Exception exc) {}
	}
	
}
