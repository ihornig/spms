package beans;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.event.Observes;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.*;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import datenbank.Nachricht;
import datenbank.Termin;
import events.GroupDeletionEvent;

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
	 * Speichert die eigenen Nachrichten in der benutzereigenen Liste.
	 */
	public void fillEigeneNachrichten() {
		eigeneNachrichten = new ArrayList<Nachricht>();
		System.out.println(nachrichten.getRowCount());
		for(String name : loginHandler.kunde.getGruppen()) {
			for(Nachricht nachricht : nachrichten) {
				if(name.equals(nachricht.getGruppe())) {
					eigeneNachrichten.add(nachricht);
				}
			}
		}
	}
	
	public List<Nachricht> getEigeneNachrichten() {
		return eigeneNachrichten;
	}

	public void setEigeneNachrichten(List<Nachricht> eigeneNachrichten) {
		this.eigeneNachrichten = eigeneNachrichten;
	}

	@ManagedProperty("#{loginHandler}")
	private Login loginHandler;
	
	/**
	 * Entfernt die ausgewählte Nachricht aus der Gruppe.
	 */
	public void removeNachrichtFromGroup(@Observes GroupDeletionEvent event) {
		int i = 0;
		while(i < nachrichten.getRowCount()) {
			nachrichten.setRowIndex(i);
			tempNachricht = nachrichten.getRowData();
			if(tempNachricht.getGruppe().equals(event.getGroupName())) {
				try {
					utx.begin();
				} catch (Exception exc) {}
				tempNachricht = em.merge(tempNachricht);
				em.remove(tempNachricht);
				nachrichten.setWrappedData(em.createNamedQuery("SelectNachricht").getResultList());
				try {
					utx.commit();
				} catch (Exception exc) {}
				tempNachricht = new Nachricht();
			} else {
				++i;
			}
		}
		fillEigeneNachrichten();
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
	
	public void setLoginHandler(Login name) {
		this.loginHandler = name;
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
		fillEigeneNachrichten();
	}
}