package org.no_ip.mikelue.jpa.test.liquibase;

import liquibase.Liquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.apache.commons.lang.StringUtils.join;
import static org.apache.commons.lang.Validate.isTrue;

import java.util.Arrays;

/**
 * The executor for {@link Liquibase#update}.<p>
 *
 * This class has some configurations to perform additional operations to database.<p>
 *
 * The execution of this object: {@link #executeLiquibase}.<p>
 */
public class UpdateSchemaExecutor implements LiquibaseExecutor {
    private Logger logger = LoggerFactory.getLogger(UpdateSchemaExecutor.class);

    /**
     * The default number of changes is going to be updated.<p>
     */
    public static final Integer ALL_CHANGES = null;
    /**
     * The [null] value and ""(empty string) value of context have
     * differential behaviors in Liquibase. This is also the default value<p>
     *
     * This value is used to indicated [null] value in {@link Liquibase#update(String)}.<p>
     */
    public static final String[] NO_CONTEXTS = null;
    /**
     * The default value for undetermined schemas for {@link Liquibase#dropAll}.<p>
     */
    public static final String[] UNDETERMINED_SCHEMAS = null;

    private boolean dropFirst = false;
    private Integer changesToApply = ALL_CHANGES;
    private String[] contexts = NO_CONTEXTS;
    private String schemas[] = UNDETERMINED_SCHEMAS;

    /**
     * Construct this object with default value
     *
     * @see #setDropFirst
     * @see #setChangesToApply
     * @see #setContexts
     * @see #setSchemas
     */
    public UpdateSchemaExecutor() {}

    /**
     * Set whether to drop the schemas first which comes from {@link #getSchemas}. <b>Default is "false".</b><p>
     *
     * @param newDropFirst drop the schemas first if value is "true"
     *
     * @see #setSchemas
     * @see #getDropFirst
     */
    public void setDropFirst(boolean newDropFirst) { this.dropFirst = newDropFirst; }
    /**
     * Get whether to drop the schemas. <b>Default is "false"</b>.<p>
     *
     * @return drop the schemas first if value is "true"
     *
     * @see #getSchemas
     * @see #setDropFirst
     */
    public boolean getDropFirst() { return this.dropFirst; }

    /**
     * Set the number of changes {@link #getSchemas} which is going to be {@link Liquibase#update updated}. <b>Default is {@link #ALL_CHANGES}</b><p>
     *
     * @param newChangesToApply The number of changes is going to be updated
     *
     * @see #getChangesToApply
     */
	public void setChangesToApply(Integer newChangesToApply)
    {
        isTrue(
            newChangesToApply == ALL_CHANGES || newChangesToApply >= 1,
            "the number of changes to apply should be \"1\" at least or null"
        );

        this.changesToApply = newChangesToApply;
    }
    /**
     * Get the number of changes {@link #getSchemas} which is going to be {@link Liquibase#update updated}. <b>Default is {@link #ALL_CHANGES}</b><p>
     *
     * @return The number of changes or {@link #ALL_CHANGES} if it is the default value
     *
     * @see #setChangesToApply
     */
	public Integer getChangesToApply() { return this.changesToApply; }

    /**
     * Set the contexts which is going to be {@link Liquibase#update updated}. <b>Default is {@link #NO_CONTEXTS}</b><p>
     *
     * @param newContexts The contexts is going to be updated
     *
     * @see #getContexts
     */
    public void setContexts(String... newContexts) { this.contexts = newContexts; }
    /**
     * Get the contexts which is going to be {@link Liquibase#update updated}. <b>Default is {@link #NO_CONTEXTS}</b><p>
     *
     * @return The contexts is going to be updated
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
     * Set the schemas which is going to be {@link Liquibase#dropAll dropped}, if {@link #getDropFirst} is "true".<p>
     * <b>Default is {@link #UNDETERMINED_SCHEMAS}</b><p>
     *
     * @param newSchemas The schemas is going to be dropped
     *
     * @see #getSchemas
     */
    public void setSchemas(String... newSchemas) { this.schemas = newSchemas; }
    /**
     * Set the schemas which is going to be {@link Liquibase#dropAll dropped}, if {@link #getDropFirst} is "true".<p>
     * <b>Default is {@link #UNDETERMINED_SCHEMAS}</b><p>
     *
     * @return The schemas is going to be dropped
     *
     * @see #setSchemas
     */
    public String[] getSchemas()
    {
        if (this.schemas == UNDETERMINED_SCHEMAS) {
            return UNDETERMINED_SCHEMAS;
        }

        return Arrays.copyOf(this.schemas, this.schemas.length);
    }

    /**
     * Execute the update operation based on properties.<p>
     *
     * The execution of this method is as following list:
     * <ol>
     *  <li>{@link Liquibase#dropAll} if {@link #dropFirst} is "true". Dropping wit {@link #getSchemas}</li>
     *  <li>{@link Liquibase#update} with {@link #getContexts} and {@link #getChangesToApply}</li>
     * </ol>
     *
     * @param liquibase The initialized {@link Liquibase} object built by {@link LiquibaseBuilder}
     *
     * @see #getDropFirst
     * @see #getChangesToApply
     * @see #getContexts
     * @see #getSchemas
     */
    @Override
    public void executeLiquibase(Liquibase liquibase) throws Exception
    {
        logger.info("Update schema");

        /**
         * Process drop first
         */
        if (getDropFirst()) {
            if (getSchemas() == UNDETERMINED_SCHEMAS) {
                logger.info("Drop schema first");
                liquibase.dropAll();
            } else {
				String[] schemas = getSchemas();

                logger.info(
					"Drop schema first with schemas: [{}]",
					(Object)schemas
				);
                liquibase.dropAll(schemas);
            }
        }
        // :~)

        /**
         * Process update
         */
        String contexts = null;
        if (getContexts() != NO_CONTEXTS) {
            contexts = join(getContexts(), ",");
        }

        if (getChangesToApply() == ALL_CHANGES) {
            logger.info("Update schema with context: [{}]", contexts);
            liquibase.update(contexts);
        } else {
            logger.info("Update schema with context: [{}], changes: [{}]", contexts, getChangesToApply());
            liquibase.update(getChangesToApply(), contexts);
        }
        // :~)

        logger.info("Update schema finished");
    }
}
