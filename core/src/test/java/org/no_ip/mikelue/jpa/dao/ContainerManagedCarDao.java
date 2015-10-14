package org.no_ip.mikelue.jpa.dao;

import org.no_ip.mikelue.jpa.model.Car;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is a test DAO object with container-managed environment.
 */
@Repository
public class ContainerManagedCarDao extends InjectedTypedDaoFacade<Car, Integer> {
    public ContainerManagedCarDao() {}

    @Override @Transactional
    public void saveNew(Car newEntity)
    {
        super.saveNew(newEntity);
    }
    @Override @Transactional
    public Car saveExisted(Car newEntity)
    {
        return super.saveExisted(newEntity);
    }
    @Override @Transactional
    public boolean remove(Integer primaryKey)
    {
        return super.remove(primaryKey);
    }
}
