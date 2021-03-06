package beans;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.*;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import datenbank.Termin;

@ManagedBean(name = "TerminHandler")
@SessionScoped
public class TerminHandler {

	@PersistenceContext
	private EntityManager em;
	
	@Resource
	private UserTransaction utx;
	
	private DataModel<Termin> termine;
	private Termin tempTermin = new Termin();
	
	public DataModel<Termin> getTermine(){
		return termine;
	}
	
	public void setTermin(DataModel<Termin> termine) {
		this.termine = termine;
	}
	
	public void setTempTermin(Termin termin) {
		this.tempTermin = termin;
	}
	
	public Termin getTempTermin() {
		return tempTermin;
	}
	
	public void neu() {
		tempTermin = new Termin();
		speichern();
	}
	
	@PostConstruct
	public void init() {
		try {
			utx.begin();
		} catch (Exception exc) {}
		termine = new ListDataModel<Termin>();
		termine.setWrappedData(em.createNamedQuery("SelectTermin").getResultList());
		try {
			utx.commit();
		} catch (Exception exc) {}
	}
	
	public void speichern() {
		try {
			utx.begin();
		} catch (Exception exc) {}
		tempTermin = em.merge(tempTermin);
		em.persist(tempTermin);
		termine.setWrappedData(em.createNamedQuery("SelectTermin").getResultList());
		try {
			utx.commit();
		} catch (Exception exc) {}
	}
	
	public void entfernen(String titel) {
		
	}
	
}