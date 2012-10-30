package org.no_ip.mikelue.jpa.springframework;

import org.no_ip.mikelue.jpa.model.Car;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This DAO class is a test DAO object with transactions of SpringFramework.<p>
 */
@Repository
public class SpringCarDao extends AbstractSpringTypedDaoFacade<Car, Integer> {
    @PersistenceContext(unitName="container-managed")
    private EntityManager entityManager;

    public SpringCarDao() {}

    @Override
    public EntityManager getEntityManager()
    {
        return entityManager;
    }
}
