package datenbank;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Nachricht.class)
public abstract class Nachricht_ {

	public static volatile SingularAttribute<Nachricht, String> absender;
	public static volatile SingularAttribute<Nachricht, Integer> id;
	public static volatile SingularAttribute<Nachricht, String> gruppe;
	public static volatile SingularAttribute<Nachricht, String> inhalt;

}

