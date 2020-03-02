package datenbank;

import java.util.Vector;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Account.class)
public abstract class Account_ {

	public static volatile SingularAttribute<Account, String> password;
	public static volatile SingularAttribute<Account, String> rolle;
	public static volatile SingularAttribute<Account, Integer> id;
	public static volatile SingularAttribute<Account, Vector> gruppen;
	public static volatile SingularAttribute<Account, String> username;

}

