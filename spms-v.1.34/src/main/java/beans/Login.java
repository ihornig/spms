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
	
	/**
	 * Prüft, ob der jeweilige Benutzer angemeldet ist. Leitet andernfalls auf die Login-Seite zurück.
	 */
	public void checkLoggedIn(ComponentSystemEvent cse) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (kunde == null) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,"/login.xhtml?faces-redirect=true");
		}
	}
	
	/**
	 * Prüft, ob der jeweilige Benutzer angemeldet ist. Leitet den Benutzer in dem Fall auf die Startseite.
	 * Wird verwendet bei der Login.xhtml und der Register.xhtml, damit man automatisch umgeleitet wird, wenn man angemeldet ist.
	 */
	public void checkLoggedInLogin(ComponentSystemEvent cse) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (kunde != null) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,"/startseite.xhtml?faces-redirect=true");
		}
	}
	
	/**
	 * Dient zur Feststellung, ob man die jeweilige Seite betreten darf.
	 * Ist man nur in der Rolle "User", wird man auf die Startseite zurückgeleitet.
	 * Ist man gar nicht angemeldet, wird man auf die Login-Seite umgeleitet.
	 */
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
		} else if (kunde.getRolle().equals("user")) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,"/startseite.xhtml?faces-redirect=true");
		}
	}
	
	/**
	 * Dient zur Feststellung, ob man die jeweilige Seite betreten darf.
	 * Ist man nur in der Rolle "User" oder "Verwalter", wird man auf die Startseite zurückgeleitet.
	 * Ist man gar nicht angemeldet, wird man auf die Login-Seite umgeleitet.
	 */
	public void checkRangAdmin(ComponentSystemEvent cse) {
		System.out.println("Done");
		FacesContext context = FacesContext.getCurrentInstance();
		Query query = em.createQuery("select k from Account k where k.username = :username");
		query.setParameter("username", username);
		List<Account> kunden = query.getResultList();
		if (kunden.size() == 1) {
			kunde = kunden.get(0);
		}
		if (kunde == null) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,"/login.xhtml?faces-redirect=true");
		} else if (kunde.getRolle().equals("user")) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,"/startseite.xhtml?faces-redirect=true");
		} else if (kunde.getRolle().equals("verwalter")) {
			context.getApplication().getNavigationHandler().handleNavigation(context, null,"/startseite.xhtml?faces-redirect=true");
		}
	}
	
	public String disableGruppenVerwaltung() {
		if(kunde.getRolle().equals("verwalter")) {
			return "";
		} else {
			return "disable";
		}
	}
	
	public String disableAccountManagement() {
		if(kunde.getRolle().equals("admin")) {
			return "";
		} else {
			return "disable";
		}
	}
	
	public String logout () {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/login.xhtml?faces -redirect=true";
	}
	
}
