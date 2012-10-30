package org.no_ip.mikelue.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This class provides a skeleton for an injected entity manager.<p>
 *
 * If your want to override the {@link PersistenceContext} setting,
 * be sure that your container would inject default persistence context(without unitName) correctly.
 *
 * @param <T>		The type of JPA entity.
 * @param <PK_T>	The type of JPA entity's primary key
 **/
public class InjectedTypedDaoFacade<T, PK_T> extends AbstractTypedDaoFacadeBase<T, PK_T> {
    /**
     * An injected entity manager could be overrided by sub-class.<p>
     */
    @PersistenceContext
    private EntityManager entityManager;

    protected InjectedTypedDaoFacade() {}

    /**
     * This method get entity manager from default persist context.<p>
     *
     * @return default persist context
     */
    @Override
    public EntityManager getEntityManager()
    {
        return entityManager;
    }
}
