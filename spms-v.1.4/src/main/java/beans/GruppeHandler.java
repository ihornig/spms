package beans;

import datenbank.Gruppe;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.*;
import javax.persistence.*;
import javax.transaction.UserTransaction;

@ManagedBean(name = "gruppeHandler")
@SessionScoped
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
	}
	
	public void speichern() {
		try {
			utx.begin();
		} catch (Exception exc) {}
		tempGruppe = em.merge(tempGruppe);
		em.persist(tempGruppe);
		gruppen.setWrappedData(em.createNamedQuery("SelectGruppe").getResultList());
		try {
			utx.commit();
		} catch (Exception exc) {}
	}
	
}
