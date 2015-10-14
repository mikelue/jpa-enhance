package org.no_ip.mikelue.jpa.dao;

import org.no_ip.mikelue.jpa.model.Person;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import static javax.persistence.Persistence.createEntityManagerFactory;

/**
 * The test DAO class for application-managed environment.
 */
public class AppManagedTypedDaoFacadeBaseTest {
    private AppManagedPersonDao testDao;

    private final int FIRST_ID = 1;
    private final int SECOND_ID = 2;

    public AppManagedTypedDaoFacadeBaseTest() {}

    /**
     * Test finding and finding with lock
     */
    @Test
    public void find()
    {
        Assert.assertNotNull(
            findPerson(FIRST_ID)
        );

		testDao.beginTransaction();
        Assert.assertNotNull(
            testDao.find(FIRST_ID, LockModeType.PESSIMISTIC_WRITE)
        );
		testDao.commitTransaction();

        testDao.getEntityManager().close();
    }
    @Test
    public void createTypedNamedQuery()
    {
        Assert.assertNotNull(
            testDao.createTypedNamedQuery("findByName")
                .setParameter("name", "first person")
                .getSingleResult()
        );
    }
    @Test
    public void createTypedQuery()
    {
        Assert.assertNotNull(
            testDao.createTypedQuery(
                " SELECT p" +
                " FROM Person AS p"  +
                " WHERE p.name = :name"
            )
                .setParameter("name", "first person")
                .getSingleResult()
        );
    }
    @Test(dependsOnMethods="find")
    public void saveNew()
    {
        final int testId = 1001;

        Person newPerson = new Person();
        newPerson.setId(testId);
        newPerson.setName("Bob");
        newPerson.setAddress("Bob's address");

        testDao.saveNew(newPerson);

        Assert.assertNotNull(
            findPerson(testId)
        );
    }
    @Test(dependsOnMethods="find")
    public void saveExisted()
    {
        final String testName = "New-Name";

        Person p = findPerson(SECOND_ID);

        p.setName(testName);
        testDao.saveExisted(p);

        Assert.assertEquals(
            findPerson(SECOND_ID).getName(),
            testName
        );
    }
    @Test(dependsOnMethods={"saveNew", "saveExisted"})
    public void remove()
    {
        Assert.assertTrue(
            testDao.remove(SECOND_ID)
        );
        Assert.assertNull(findPerson(SECOND_ID));

        Assert.assertFalse(
            testDao.remove(SECOND_ID)
        );
    }

    private Person findPerson(int personId)
    {
        Person p = testDao.find(personId);
        testDao.getEntityManager().close();

        return p;
    }

    @BeforeClass
    public void init()
    {
        testDao = new AppManagedPersonDao();
        testDao.init();

        /**
         * Build test data
         */
        testDao.beginTransaction();

        testDao.getEntityManager().createNativeQuery(
            " INSERT INTO tt_person(ps_id, ps_name, ps_address)" +
            " VALUES(?, 'first person', 'address: first person')"
        )
            .setParameter(1, FIRST_ID)
            .executeUpdate();
        testDao.getEntityManager().createNativeQuery(
            " INSERT INTO tt_person(ps_id, ps_name, ps_address)" +
            " VALUES(?, 'second person', 'address: second person')"
        )
            .setParameter(1, SECOND_ID)
            .executeUpdate();

        testDao.commitTransaction();
        testDao.getEntityManager().close();
        // :~)
    }
    @AfterClass
    public void release()
    {
        testDao.release();
    }
}

/**
 * As application-managed DAO object for testing, this object is stateful.<p>
 *
 * <strong>*DO NOT USE THIS CLASS IN MULTI-THREADS ENVIRONMENT*</strong>
 */
class AppManagedPersonDao extends AbstractTypedDaoFacadeBase<Person, Integer> {
    private EntityManagerFactory emf = null;
    private EntityManager currentEm = null;
    private EntityTransaction currentTx = null;

    AppManagedPersonDao() {}

    @Override
	public void saveNew(Person newEntity)
	{
        beginTransaction();
        super.saveNew(newEntity);
        commitTransaction();

        getEntityManager().close();
	}
    @Override
	public Person saveExisted(Person newEntity)
	{
        beginTransaction();
        Person savedEntity = super.saveExisted(newEntity);
        commitTransaction();

        getEntityManager().close();

        return savedEntity;
	}
    @Override
	public boolean remove(Integer primaryKey)
	{
        beginTransaction();
        boolean result = super.remove(primaryKey);
        commitTransaction();

        getEntityManager().close();

        return result;
	}

    @Override
    public EntityManager getEntityManager()
    {
        if (currentEm == null || !currentEm.isOpen()) {
            currentEm = emf.createEntityManager();
        }

        return currentEm;
    }

    public void beginTransaction()
    {
        if (currentTx != null && currentTx.isActive()) {
            currentTx.rollback();
        }

        currentTx = getEntityManager().getTransaction();
        currentTx.begin();
    }
    public void commitTransaction()
    {
        if (currentTx == null || !currentTx.isActive()) {
            return;
        }

        currentTx.commit();
        currentTx = null;
    }

    public void init()
    {
        if (emf != null && emf.isOpen()) {
            return;
        }

        emf = createEntityManagerFactory("app-managed");
    }
    public void release()
    {
        if (currentTx != null && currentTx.isActive()) {
            currentTx.rollback();
        }
        if (currentEm != null && currentEm.isOpen()) {
            currentEm.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
