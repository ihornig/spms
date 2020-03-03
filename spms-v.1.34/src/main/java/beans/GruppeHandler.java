package beans;

import datenbank.Gruppe;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.el.ELContext;
import javax.enterprise.event.Event;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.*;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import java.util.*;
import events.GroupDeletionEvent;

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
		updateNameholder();
		ELContext fc = FacesContext.getCurrentInstance().getELContext();
		AccountHandler accountHandler = (AccountHandler) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(fc, null, "accountHandler");
		accountHandler.removeAllUsersFromGroup(name);
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
