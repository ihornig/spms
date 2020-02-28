package beans;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.event.Observes;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.*;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import datenbank.Nachricht;
import datenbank.Termin;
import events.GroupDeletionEvent;

@ManagedBean(name = "NachrichtenHandler")
@SessionScoped
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
	}
	
	public void speichern(ActionEvent event) {
		String name = (String) event.getComponent().getAttributes().get("username");
		tempNachricht.setAbsender(name);
		System.out.println(tempNachricht.getGruppe());
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