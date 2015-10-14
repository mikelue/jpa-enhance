package guru.mikelue.jpa.dao;

import com.googlecode.gentyref.GenericTypeReflector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;

/**
 * This class provides type-safe DAO for data insert/delete/update.
 *
 * <p>This class is used to handle <strong>single entity manager</strong>.</p>
 *
 * <p>There are some write-operations in which the transaction is <strong>defined by sub-class</strong>.
 * That is, the default implementation is just a skeleton of operations.</p>
 *
 * <p>This class is used in container-managed environment.</p>
 *
 * <p>Moreover, this class provides {@link #getLogger() accessor} for <a href="http://www.slf4j.org/">SLF4J</a> logger.</p>
 *
 * @param <T>		The type of JPA entity.
 * @param <PK_T>	The type of JPA entity's primary key
 */
public abstract class AbstractTypedDaoFacadeBase<T, PK_T> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final Class<T> entityType;

	@SuppressWarnings("unchecked")
	protected AbstractTypedDaoFacadeBase()
	{
		entityType = (Class<T>)GenericTypeReflector.erase(
			GenericTypeReflector.getTypeParameter(this.getClass(), AbstractTypedDaoFacadeBase.class.getTypeParameters()[0])
		);

		getLogger().debug("Initialize entity type: {}", entityType);
		if (entityType == null) {
			throw new RuntimeException("Can't find generic entity type<T> of " + getClass().toString());
		}
	}

    /**
     * Get the entity manager.
     *
     * @return the initialized entity manager
     */
    abstract public EntityManager getEntityManager();

	/**
	 * Persist object into database. The transaction of this operation is <b>defined by sub-class</b>.
     *
     * <p>This method would call {@link javax.persistence.EntityManager#persist(Object) EntityManager.persist}.</p>
     *
	 * @param newEntity Persisted JPA {@link javax.persistence.Entity} object.
	 */
	public void saveNew(T newEntity)
	{
        getLogger().debug("Save new entity: {}", newEntity);

		getEntityManager().persist(newEntity);
	}

	/**
     * This method would call {@link javax.persistence.EntityManager#merge(Object) EntityManager.merge} to update entity to database. The transaction of this operation is <b>defined by sub-class</b>.
	 *
	 * @param existedEntity Persisted JPA {@link javax.persistence.Entity} object.
	 *
	 * @return The modified data
	 */
	public T saveExisted(T existedEntity)
	{
        getLogger().debug("Save existed entity: {}", existedEntity);

		return getEntityManager().merge(existedEntity);
	}

	/**
	 * Removing data uses single primary key.  The transaction of this operation is <b>defined by sub-class</b>.
     * The object would be {@link javax.persistence.EntityManager#refresh(Object) refresh} from database if the entity is not managed by current entity manager.
     *
     * <p>This method would call {@link javax.persistence.EntityManager#remove(Object) EntityManager.remove} to remove entity from database.</p>
	 *
	 * @param primaryKey The PK of existed entity to be removed
     *
     * @return return true if the removal of entity is effective.
	 */
	public boolean remove(PK_T primaryKey)
	{
        getLogger().debug("Remove existed entity for id: {}", primaryKey);

        T entity = find(primaryKey);
        if (entity == null) {
            return false;
        }

		getEntityManager().remove(entity);
        return true;
	}

	/**
	 * Finding data uses single primary key.
     *
     * <p>This method would call {@link javax.persistence.EntityManager#find(Class, Object) EntityManager.find}</p>
	 *
	 * @param primaryKey The primary key value
	 *
	 * @return result object
	 */
	public T find(PK_T primaryKey)
	{
		return getEntityManager().find(entityType, primaryKey);
	}

	/**
	 * Finding data uses single primary key and lock data.
     *
     * <p>This method would call {@link javax.persistence.EntityManager#find(Class, Object) EntityManager.find}</p>
	 *
	 * @param primaryKey The primary key value
	 * @param lockModeType The lock mode type
	 *
	 * @return result object
	 */
	public T find(PK_T primaryKey, LockModeType lockModeType)
	{
		return getEntityManager().find(entityType, primaryKey, lockModeType);
	}

	/**
     * Generating {@link TypedQuery} uses query name.
     *
	 * <p>This method would call {@link javax.persistence.EntityManager#createNamedQuery(String, Class) EntityManager.createNamedQuery}</p>
	 *
	 * @param queryName The name of named query
	 *
	 * @return typed query object
	 */
	public TypedQuery<T> createTypedNamedQuery(String queryName)
	{
		return getEntityManager().createNamedQuery(queryName, entityType);
	}

	/**
     * Generating {@link TypedQuery} uses query string.
     *
	 * <p>This method would call {@link javax.persistence.EntityManager#createQuery(String, Class) EntityManager.createQuery}</p>
	 *
	 * @param qlString query string
	 *
	 * @return typed query object
	 */
	public TypedQuery<T> createTypedQuery(String qlString)
	{
		return getEntityManager().createQuery(qlString, entityType);
	}

    /**
     * Get the logger object.
     *
     * @return logger object with name of containing class
     */
    public Logger getLogger()
    {
        return logger;
    }
}
