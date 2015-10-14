package guru.mikelue.jpa.springframework;

import guru.mikelue.jpa.model.Car;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This DAO class is a test DAO object with default bean of persistence context in SpringFramework.<p>
 */
@Repository
public class SpringDefaultCarDao extends SpringInjectedTypedDaoFacade<Car, Integer> {}
