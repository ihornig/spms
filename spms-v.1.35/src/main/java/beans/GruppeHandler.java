package beans;

import datenbank.Gruppe;
import datenbank.Nachricht;
import datenbank.Termin;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.*;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import java.util.*;

@ManagedBean(name = "gruppeHandler")
@RequestScoped
public class GruppeHandler {

	public GruppeHandler(){
		tempGruppe = new Gruppe();
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Resource
	private UserTransaction utx;
	
	private DataModel<Gruppe> gruppen;
	private Gruppe tempGruppe = new Gruppe();
	private String name;
	
	private List<String> nameholder = new ArrayList<String>();
	
	/**
	 * Eine Gruppe wird aus der Datenbank gelöscht.
	 */
	public void removeGroup() {
		if(name == null) {
			return;
		}
		boolean found = false;
		int i = 0;
		while(!found && i < gruppen.getRowCount()) {
			gruppen.setRowIndex(i++);
			tempGruppe = gruppen.getRowData();
			if(tempGruppe.getName().equals(name)) {
				found = true;
			}
		}
		try {
			utx.begin();
		} catch (Exception exc) {}
		tempGruppe = em.merge(tempGruppe);
		em.remove(tempGruppe);
		gruppen.setWrappedData(em.createNamedQuery("SelectGruppe").getResultList());
		try {
			utx.commit();
		} catch (Exception exc) {}
		ELContext fc = FacesContext.getCurrentInstance().getELContext(); 
		AccountHandler accountHandler = (AccountHandler) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(fc, null, "accountHandler");
		accountHandler.removeAllUsersFromGroup(name);
		removeCheckNachrichten(name);
		removeCheckTermine(name);
	}
	
	/**
	 * Prüft ob alle Termine noch existierenden Gruppen zuzuordnen sind.
	 */
	public void removeCheckTermine(String name) {
		DataModel<Termin> termine = new ListDataModel<>();
		try {
			utx.begin();
		} catch(Exception exc) {}
		termine.setWrappedData(em.createNamedQuery("SelectTermin").getResultList());
		try {
			utx.commit();
		} catch(Exception exc) {}
		int j = 0;
		while(j < termine.getRowCount()) {
			termine.setRowIndex(j);
			Termin termin = termine.getRowData();
			if(termin.getGruppe().equals(name)) {
				try {
					utx.begin();
				} catch (Exception exc) {}
				termin = em.merge(termin);
				em.remove(termin);
				termine.setWrappedData(em.createNamedQuery("SelectTermin").getResultList());
				try {
					utx.commit();
				} catch(Exception exc) {}
			} else { ++j; }
		}
	}
	
	/**
	 * Prüft ob alle Nachrichten noch existierenden Gruppen zuzuordnen sind
	 */
	public void removeCheckNachrichten(String name) {
		DataModel<Nachricht> nachrichten = new ListDataModel<>();
		try {
			utx.begin();
		} catch (Exception exc) {}
		nachrichten.setWrappedData(em.createNamedQuery("SelectNachricht").getResultList());
		try {
			utx.commit();
		} catch (Exception exc) {}
		int j = 0;
		while(j < nachrichten.getRowCount()) {
			nachrichten.setRowIndex(j);
			Nachricht nachricht = nachrichten.getRowData();
			if(nachricht.getGruppe().equals(name)) {
				try {
					utx.begin();
				} catch (Exception exc) {}
				nachricht = em.merge(nachricht);
				em.remove(nachricht);
				nachrichten.setWrappedData(em.createNamedQuery("SelectNachricht").getResultList());
				try {
					utx.commit();
				} catch (Exception exc) {}
			} else { ++j; }
		}
	}
	
	@PostConstruct
	public void init() {
		try {
			utx.begin();
		} catch (Exception exc) {}
		gruppen = new ListDataModel<Gruppe>();
		gruppen.setWrappedData(em.createNamedQuery("SelectGruppe").getResultList());
		try {
			utx.commit();
		} catch (Exception exc) {}
		updateNameholder();
	}
	
	/**
	 * Wenn noch keine Gruppe mit dem gewählten Namen existiert wird eine neue Gruppe angelegt und gespeichert.
	 */
	public void speichern() {
		for(String name : nameholder) {
			if(tempGruppe.getName().equals(name)) {
				return;
			}
		}
		try {
			utx.begin();
		} catch (Exception exc) {}
		tempGruppe = em.merge(tempGruppe);
		em.persist(tempGruppe);
		gruppen.setWrappedData(em.createNamedQuery("SelectGruppe").getResultList());
		try {
			utx.commit();
		} catch (Exception exc) {}
		tempGruppe = new Gruppe();
		updateNameholder();
	}
	
	private void updateNameholder() {
		nameholder.clear();
		for(Gruppe gruppe : gruppen) {
			nameholder.add(gruppe.getName());
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setNameholder(List<String> name){
		nameholder = name;
	}
	
	public List<String> getNameholder() {
		return nameholder;
	}
	
	public DataModel<Gruppe> getGruppen(){
		return gruppen;
	}
	
	public void setGruppen(DataModel<Gruppe> gruppen) {
		this.gruppen = gruppen;
	}
	
	public void setTempGruppe(Gruppe gruppe) {
		this.tempGruppe = gruppe;
	}
	
	public Gruppe getTempGruppe() {
		return tempGruppe;
	}
	
}
