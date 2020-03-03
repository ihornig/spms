package beans;

import java.util.ArrayList;
import java.util.List;
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
import datenbank.Termin;

@ManagedBean(name = "TerminHandler")
@RequestScoped
public class TerminHandler {

	public TerminHandler() {
		tempTermin = new Termin();
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Resource
	private UserTransaction utx;
	
	private DataModel<Termin> termine;
	private Termin tempTermin;
	private String terminName;
	private String gruppe;
	
	private List<String> nameholder = new ArrayList<String>();
	private List<Termin> eigeneTermine = new ArrayList<Termin>();
	
	/**
	 * Löscht den ausgewählten Termin aus der jeweiligen Gruppe.
	 */
	public void terminLoeschen() {
		boolean found = false;
		int i = 0;
		while(!found && i < termine.getRowCount()) {
			termine.setRowIndex(i++);
			tempTermin = termine.getRowData();
			if(tempTermin.getTitel().equals(terminName) && tempTermin.getGruppe().equals(gruppe)) {
				found = true;
			}
		}
		try {
			utx.begin();
		} catch (Exception exc) {}
		tempTermin = em.merge(tempTermin);
		em.remove(tempTermin);
		termine.setWrappedData(em.createNamedQuery("SelectTermin").getResultList());
		try {
			utx.commit();
		} catch (Exception exc) {}
	}
	
	public String getGruppe() {
		return gruppe;
	}
	
	public void setGruppe(String gruppe) {
		this.gruppe = gruppe;
	}
	
	public String getTerminName() {
		return terminName;
	}
	
	public void setTerminName(String terminName) {
		this.terminName = terminName;
	}
	
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
		tempTermin=new Termin();
		removeCheck();
		updateNameholder();
		fillEigeneTermine();
	}
	
	public void removeCheck() {
		ELContext fc = FacesContext.getCurrentInstance().getELContext();
		GruppeHandler gruppeHandler = (GruppeHandler) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(fc, null, "gruppeHandler");
		for(Termin t : termine) {
			int i = 0;
			boolean found = false;
			while(!found && 0 < gruppeHandler.getGruppen().getRowCount()) {
				gruppeHandler.getGruppen().setRowIndex(i);
				if(t.getGruppe().equals(gruppeHandler.getGruppen().getRowData().getName())) {
					found = true;
				} else {
					++i;
				}
			}
			if(!found) {
				tempTermin = t;
				try {
					utx.begin();
				} catch (Exception exc) {}
				tempTermin = em.merge(tempTermin);
				em.remove(tempTermin);
				termine.setWrappedData(em.createNamedQuery("SelectTermin").getResultList());
				try {
					utx.commit();
				} catch (Exception exc) {}
			}
		}
	}
	
	/**
	 * Fügt den angelegten Termin zur Liste der eigenen Termine hinzu.
	 * Dadurch können eigenhändig angelegte Termine, bei Bedarf auch wieder gelöscht werden.
	 */
	public void fillEigeneTermine() {
		ELContext fc = FacesContext.getCurrentInstance().getELContext();
		Login loginHandler = (Login) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(fc, null, "loginHandler");
		eigeneTermine = new ArrayList<Termin>();
		for(String name : loginHandler.kunde.getGruppen()) {
			for(Termin termin : termine) {
				if(name.equals(termin.getGruppe())) {
					eigeneTermine.add(termin);
				}
			}
		}
	}
	
	/**
	 * Speichert den angelegten Termin im DataModeler.
	 * Durch die Abfrage-Methode wird dabei verhindert, dass in der ausgewählten Gruppe mehrere gleichnamige Termine angelegt
	 * werden können.
	 */
	public void speichern(ActionEvent event) {
		if(!abfrage()) {
			String name = (String) event.getComponent().getAttributes().get("username");
			tempTermin.setAutor(name);
			try {
				utx.begin();
			} catch (Exception exc) {}
				tempTermin = em.merge(tempTermin);
				em.persist(tempTermin);
				termine.setWrappedData(em.createNamedQuery("SelectTermin").getResultList());
				try {
					utx.commit();
				} catch (Exception exc) {}
				updateNameholder();
				neu();
				fillEigeneTermine();
		} else {
			return;
		}
	}
	
	/**
	 * Prüft, ob Termine mit dem jeweiligen Titel, bereits in der jeweiligen Gruppe existieren.
	 */
	public boolean abfrage() {
		int i = 0;
		while(i < termine.getRowCount()) {
			termine.setRowIndex(i++);
			Termin anotherTempTermin = termine.getRowData();
			if(tempTermin.getTitel().equals(anotherTempTermin.getTitel()) && tempTermin.getGruppe().equals(anotherTempTermin.getGruppe())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Aktualisiert die Liste mit den Termin-Titeln.
	 */
	public void updateNameholder() {
		nameholder.clear();
		for(Termin termin : termine) {
			nameholder.add(termin.getTitel());
		}
	}
	
	public void setNameholder(List<String> name) {
		nameholder = name;
	}
	
	public List<String> getNameholder(){
		return nameholder;
	}

	public List<Termin> getEigeneTermine() {
		return eigeneTermine;
	}

	public void setEigeneTermine(List<Termin> eigeneTermine) {
		this.eigeneTermine = eigeneTermine;
	}
}