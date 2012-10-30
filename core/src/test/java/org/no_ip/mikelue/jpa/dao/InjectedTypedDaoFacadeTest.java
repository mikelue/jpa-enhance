package org.no_ip.mikelue.jpa.dao;

import org.no_ip.mikelue.jpa.model.Car;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

/**
 * This class tests the skeleton of JPA operations in environment having injected persistence context.<p>
 */
@ContextConfiguration(locations={"classpath:testContainerContext.xml"})
@TestExecutionListeners(listeners={TransactionalTestExecutionListener.class})
public class InjectedTypedDaoFacadeTest extends AbstractTestNGSpringContextTests {
    @PersistenceContext(unitName="container-managed")
    private EntityManager entityManager;

    @Inject
    private ContainerManagedCarDao testDao;

    private final int CAR_ID = 1;

    public InjectedTypedDaoFacadeTest() {}

    @Test @Transactional
    public void saveNew()
    {
        Assert.assertNotNull(
            testDao.find(CAR_ID)
        );
    }
    @Test @Transactional
    public void saveExisted()
    {
        final String newName = "New Name Value";

        Car c = testDao.find(CAR_ID);
        c.setName(newName);
        testDao.saveExisted(c);
        entityManager.flush();
        entityManager.clear();

        c = testDao.find(CAR_ID);
        Assert.assertEquals(
            c.getName(), newName
        );
    }
    @Test @Transactional
    public void remove()
    {
        Car c = testDao.find(CAR_ID);
        testDao.remove(c.getId());
        entityManager.flush();
        entityManager.clear();

        Assert.assertNull(
            testDao.find(CAR_ID)
        );
    }

    @BeforeMethod
    public void prepareData()
    {
        /**
         * Prepare test data
         */
        Car c = new Car();
        c.setId(CAR_ID);
        c.setName("AAAA");
        c.setAddress("BBBB");

        testDao.saveNew(c);

        entityManager.flush();
        entityManager.clear();
        // :~)
    }
}
