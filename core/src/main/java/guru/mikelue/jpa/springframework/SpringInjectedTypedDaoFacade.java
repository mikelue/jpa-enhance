package guru.mikelue.jpa.springframework;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This class provides skeleton for default bean of persistence context in SpringFramework.<p>
 *
 * @param T type of entity
 * @param PK_T type of primary key of entity
 */
public class SpringInjectedTypedDaoFacade<T, PK_T> extends AbstractSpringTypedDaoFacade<T, PK_T> {
    @PersistenceContext
    private EntityManager entityManager;

    public SpringInjectedTypedDaoFacade() {}

    /**
     * Get the entity meanager without unit name(default bean).<p>
     *
     * @return the injected entity manager provided by SpringFramework Context
     */
    @Override
    public EntityManager getEntityManager()
    {
        return entityManager;
    }
}
