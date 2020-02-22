package beans;

import datenbank.Account;
import beans.AccountHandler;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.*;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import javax.servlet.http.*;

@ManagedBean(name = "loginHandler")
@SessionScoped
public class Login implements Serializable, Cloneable {
	
	private String usernamelogin;
	private String passwordlogin;

	String temp = "test";

	@PersistenceContext
	private EntityManager em;

	@Resource
	private UserTransaction utx;

	public void setUsernamelogin(String usernamelogin) {
		this.usernamelogin = usernamelogin;
	}

	public String getUsernamelogin() {
		return usernamelogin;
	}

	public void setPasswordlogin(String passwordlogin) {
		this.passwordlogin = passwordlogin;
	}

	public String getPasswordlogin() {
		return passwordlogin;
	}

	private String username;
	private String password;
	
	private String erg = "leer";
	public String login() {
		Query query = em.createQuery("select k from Account k where k.username = :username and k.password = :password");
		query.setParameter("username", usernamelogin);
		query.setParameter("password", passwordlogin);
		List<Account> kunden = query.getResultList();
		if (kunden.size() == 1) {
			Account kunde = kunden.get(0);
			
			erg = usernamelogin;
			return "/alleKunden.xhtml?faces-redirect=true";
		} else {
			return null;
		}
	}

	public String getErg() {
		return erg;
	}

	public void setErg(String erg) {
		this.erg = erg;
	}

}
