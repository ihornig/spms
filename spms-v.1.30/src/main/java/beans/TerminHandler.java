package beans;

import java.util.ArrayList;
import java.util.List;
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

import datenbank.Account;
import datenbank.Termin;
import events.GroupDeletionEvent;

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
	
	@ManagedProperty("#{loginHandler}")
	private Login loginHandler;
	
	private DataModel<Termin> termine;
	private Termin tempTermin;
	private String terminName;
	private String gruppe;
	
	private List<String> nameholder = new ArrayList<String>();
	private List<Termin> eigeneTermine = new ArrayList<Termin>();
	
	public void removeTermineFromGroup(@Observes GroupDeletionEvent event) {
		int i = 0;
		while(i < termine.getRowCount()) {
			termine.setRowIndex(i);
			tempTermin = termine.getRowData();
			if(tempTermin.getGruppe().equals(event.getGroupName())) {
				try {
					utx.begin();
				} catch (Exception exc) {}
				tempTermin = em.merge(tempTermin);
				em.remove(tempTermin);
				termine.setWrappedData(em.createNamedQuery("SelectTermin").getResultList());
				try {
					utx.commit();
				} catch (Exception exc) {}
				tempTermin = new Termin();
			} else {
				++i;
			}
		}
		updateNameholder();
	}
	
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
		init();
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
		init();
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
		updateNameholder();
		fillEigeneTermine();
	}
	
	public void fillEigeneTermine() {
		eigeneTermine = new ArrayList<Termin>();
		for(String name : loginHandler.kunde.getGruppen()) {
			for(Termin termin : termine) {
				if(name.equals(termin.getGruppe())) {
					eigeneTermine.add(termin);
				}
			}
		}
	}
	
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
		} else {
			return;
		}
	}
	
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
	
	public void setLoginHandler(Login name) {
		this.loginHandler = name;
	}
	
}