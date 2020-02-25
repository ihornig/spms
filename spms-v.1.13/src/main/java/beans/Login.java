package beans;

import datenbank.Account;
import beans.AccountHandler;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
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
	private String rolle;
	public Account kunde;
	
	public Account getKunde() {
		return kunde;
	}
	
	public String login() {
		Query query = em.createQuery("select k from Account k where k.username = :username and k.password = :password");
		query.setParameter("username", usernamelogin);
		query.setParameter("password", passwordlogin);
		List<Account> kunden = query.getResultList();
		if (kunden.size() == 1) {
			kunde = kunden.get(0);
			username = kunde.getUsername();
			return "/startseite.xhtml?faces-redirect=true";
		} else {
			return null;
		}
	}
	
	public void checkLoggedIn(ComponentSystemEvent cse) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (kunde == null) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,"/login.xhtml?faces-redirect=true");
		}
	}
	
	public void checkLoggedInLogin(ComponentSystemEvent cse) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (kunde != null) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,"/startseite.xhtml?faces-redirect=true");
		}
	}
	
	public void checkRangVerwalter(ComponentSystemEvent cse) {
		FacesContext context = FacesContext.getCurrentInstance();
		Query query = em.createQuery("select k from Account k where k.username = :username");
		query.setParameter("username", username);
		List<Account> kunden = query.getResultList();
		if (kunden.size() == 1) {
			kunde = kunden.get(0);
		}
		if (kunde == null) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,"/login.xhtml?faces-redirect=true");
		} else if (kunde.getRolle() == "user") {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,"/startseite.xhtml?faces-redirect=true");
		}
	}
	
	public void checkRangAdmin(ComponentSystemEvent cse) {
		FacesContext context = FacesContext.getCurrentInstance();
		Query query = em.createQuery("select k from Account k where k.username = :username");
		query.setParameter("username", username);
		List<Account> kunden = query.getResultList();
		if (kunden.size() == 1) {
			kunde = kunden.get(0);
		}
		if (kunde == null) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,"/login.xhtml?faces-redirect=true");
		} else if (kunde.getRolle() == "user") {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,"/startseite.xhtml?faces-redirect=true");
		} else if (kunde.getRolle() == "verwalter") {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,"/startseite.xhtml?faces-redirect=true");
		}
	}
	
	
	public String logout () {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/login.xhtml?faces -redirect=true";
	}
	
}
