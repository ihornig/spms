package beans;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.*;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import datenbank.Nachricht;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "NachrichtenHandler")
@RequestScoped
public class NachrichtenHandler {

	@PersistenceContext
	private EntityManager em;
	
	@Resource
	private UserTransaction utx;
	
	private DataModel<Nachricht> nachrichten;
	private Nachricht tempNachricht = new Nachricht();
	
	public DataModel<Nachricht> getNachrichten(){
		return nachrichten;
	}
	
	private List<Nachricht> eigeneNachrichten = new ArrayList<Nachricht>();
	
	/**
	 * Kopiert alle Nachrichten, die Gruppen, in welcher sich der aktuelle User befindet zuzuordnen ist
	 * in eine Liste
	 */
	public void fillEigeneNachrichten() {
		ELContext fc = FacesContext.getCurrentInstance().getELContext();
		Login loginHandler = (Login) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(fc, null, "loginHandler");
		eigeneNachrichten = new ArrayList<Nachricht>();
		for(String name : loginHandler.kunde.getGruppen()) {
			for(Nachricht nachricht : nachrichten) {
				if(name.equals(nachricht.getGruppe())) {
					eigeneNachrichten.add(nachricht);
				}
			} 
		}
	}
	
	public List<Nachricht> getEigeneNachrichten() {
		fillEigeneNachrichten();
		return eigeneNachrichten;
	}

	public void setEigeneNachrichten(List<Nachricht> eigeneNachrichten) {
		this.eigeneNachrichten = eigeneNachrichten;
	}
	
	public void setNachricht(DataModel<Nachricht> nachrichten) {
		this.nachrichten = nachrichten;
	}
	
	public void setTempNachricht(Nachricht nachricht) {
		this.tempNachricht = nachricht;
	}
	
	public Nachricht getTempNachricht() {
		return tempNachricht;
	}
	
	public void neu() {
		tempNachricht = new Nachricht();
	}
	
	@PostConstruct
	public void init() {
		try {
			utx.begin();
		} catch (Exception exc) {}
		nachrichten = new ListDataModel<Nachricht>();
		nachrichten.setWrappedData(em.createNamedQuery("SelectNachricht").getResultList());
		try {
			utx.commit();
		} catch (Exception exc) {}
		fillEigeneNachrichten();
	}
	
	/**
	 * Sendet die Nachrichten an die jeweilige Gruppe und speichert sie im DataModeler, sowie in der Liste von eigenen Nachrichten.
	 */
	public void speichern(ActionEvent event) {
		String name = (String) event.getComponent().getAttributes().get("username");
		tempNachricht.setAbsender(name);
		try {
			utx.begin();
		} catch (Exception exc) {}
		tempNachricht = em.merge(tempNachricht);
		em.persist(tempNachricht);
		nachrichten.setWrappedData(em.createNamedQuery("SelectNachricht").getResultList());
		try {
			utx.commit();
		} catch (Exception exc) {}
		neu();
	}
}