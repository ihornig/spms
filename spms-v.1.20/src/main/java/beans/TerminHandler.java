package beans;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.event.Observes;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.*;
import javax.persistence.*;
import javax.transaction.UserTransaction;

import datenbank.Account;
import datenbank.Termin;
import events.GroupDeletionEvent;

@ManagedBean(name = "TerminHandler")
@SessionScoped
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
			if(tempTermin.getTitel().equals(terminName)) {
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
		updateNameholder();
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
		neu();
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
}