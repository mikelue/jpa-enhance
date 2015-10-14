package org.no_ip.mikelue.jpa.test.liquibase;

import liquibase.Liquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.apache.commons.lang3.Validate.isTrue;

import java.util.Arrays;

/**
 * <p>The executor for {@link Liquibase#rollback}.</p>
 *
 * <p>The execution of this object: {@link #executeLiquibase}.</p>
 */
public class RollbackSchemaExecutor implements LiquibaseExecutor {
    /**
     * The [null] value and ""(empty string) value of context have
     * <p>differential behaviors in Liquibase. This is also the default value</p>
     *
     * <p>This value is used to indicated [null] value in {@link Liquibase#rollback}.</p>
     */
    public static final String[] NO_CONTEXTS = null;

    private Logger logger = LoggerFactory.getLogger(RollbackSchemaExecutor.class);

    private String[] contexts = NO_CONTEXTS;
    private Integer changesToRollbackTo = null;
    private String tagToRollbackTo = null;

    /**
     * <p>Construct this object with number of changes to rollback to.</p>
     *
     * @param newChangesToRollbackTo The number of changes to rollback to
     *
     * @see #setContexts
     * @see #getChangesToRollbackTo
     * @see #RollbackSchemaExecutor(String)
     */
    public RollbackSchemaExecutor(int newChangesToRollbackTo)
    {
        isTrue(newChangesToRollbackTo >= 1, "The number changes to rollback to need to be 1 at least.");

        changesToRollbackTo = newChangesToRollbackTo;
    }
    /**
     * <p>Construct this object with tag to rollback to.</p>
     *
     * @param newTagToRollbackTo The tag to rollback to
     *
     * @see #setContexts
     * @see #getTagToRollbackTo
     * @see #RollbackSchemaExecutor(int)
     */
    public RollbackSchemaExecutor(String newTagToRollbackTo)
    {
        newTagToRollbackTo = trimToNull(newTagToRollbackTo);
        isTrue(newTagToRollbackTo != null, "The tag to rollback to should not be null.");

        tagToRollbackTo = newTagToRollbackTo;
    }

    /**
     * <p>Get the number of changes which is going to be {@link Liquibase#rollback rollbacked}.</p>
     *
     * @return The number of changes
     */
	public Integer getChangesToRollbackTo() { return this.changesToRollbackTo; }

    /**
     * <p>Get the tag to rollback to.</p>
     *
     * @return The tag to rollbck to
     */
    public String getTagToRollbackTo() { return this.tagToRollbackTo; }

    /**
     * <p>Set the contexts which is going to be {@link Liquibase#rollback rollbacked}.</p>
     *
     * @param newContexts The contexts is going to be rollbacked
     *
     * @see #getContexts
     */
    public void setContexts(String... newContexts) { this.contexts = newContexts; }
    /**
     * <p>Get the contexts which is going to be {@link Liquibase#rollback rollbcked}. <b>Default is {@link #NO_CONTEXTS}</b></p>
     *
     * @return The contexts is going to be rollbcked
     *
     * @see #setContexts
     */
    public String[] getContexts()
    {
        if (this.contexts == NO_CONTEXTS) {
            return NO_CONTEXTS;
        }

        return Arrays.copyOf(this.contexts, this.contexts.length);
    }

    /**
     * <p>Execute the rollback operation based on properties.</p>
     *
     * The execution of this method is one of following list:
     * <ol>
     *  <li>{@link Liquibase#rollback(String, String)} with {@link #getChangesToRollbackTo} and {@link #getContexts}</li>
     *  <li>{@link Liquibase#rollback(int, String)} with {@link #getTagToRollbackTo} and {@link #getContexts}</li>
     * </ol>
     *
     * @param liquibase The initialized {@link Liquibase} object built by {@link LiquibaseBuilder}
     *
     * @see #getChangesToRollbackTo
     * @see #getTagToRollbackTo
     * @see #getContexts
     */
    @Override
    public void executeLiquibase(Liquibase liquibase) throws Exception
    {
        logger.info("Rollback schema");

        /**
         * Process Rollback
         */
        String contexts = null;
        if (getContexts() != NO_CONTEXTS) {
            contexts = join(getContexts(), ",");
        }

        if (getChangesToRollbackTo() != null) {
            logger.info("Rollback schema with number of changes: [{}], contexts: [{}]", getChangesToRollbackTo(), contexts);
            liquibase.rollback(getChangesToRollbackTo(), contexts);

        }
        if (getTagToRollbackTo() != null) {
            logger.info("Rollback schema with tag: [{}], contexts: [{}]", getTagToRollbackTo(), contexts);
            liquibase.rollback(getTagToRollbackTo(), contexts);
        }
        // :~)

        logger.info("Rollback schema finished");
    }
}
