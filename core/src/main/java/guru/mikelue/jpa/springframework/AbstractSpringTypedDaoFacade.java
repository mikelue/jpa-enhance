package guru.mikelue.jpa.springframework;

import guru.mikelue.jpa.dao.AbstractTypedDaoFacadeBase;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class provides a skeleton for DAO object in <a href="http://www.springsource.org/">SpringFramework</a>.<p>
 *
 * The {@link #saveNew}, {@link #saveExisted}, and {@link #remove} methods are annotated with {@link Transactional},
 * which perform transactional boundary to parent class.<p>
 *
 * You should override the {@link #getEntityManager} method to accomplish the persistence context of this object.
 *
 * @param T type of entity
 * @param PK_T type of primary key of entity
 */
public abstract class AbstractSpringTypedDaoFacade<T, PK_T> extends AbstractTypedDaoFacadeBase<T, PK_T> {
    public AbstractSpringTypedDaoFacade() {}

    @Override @Transactional
    public void saveNew(T newEntity)
    {
        super.saveNew(newEntity);
    }
    @Override @Transactional
    public T saveExisted(T newEntity)
    {
        return super.saveExisted(newEntity);
    }
    @Override @Transactional
    public boolean remove(PK_T primaryKey)
    {
        return super.remove(primaryKey);
    }
}
