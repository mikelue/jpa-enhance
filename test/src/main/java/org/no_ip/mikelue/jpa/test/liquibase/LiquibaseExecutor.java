package org.no_ip.mikelue.jpa.test.liquibase;

import liquibase.Liquibase;

/**
 * This interface is implemented by client and is executed {@link Liquibase} by
 * {@link LiquibaseBuilder#runExecutor}.
 *
 * @see LiquibaseBuilder
 */
public interface LiquibaseExecutor {
    /**
     * This method gets called by {@link LiquibaseBuilder#runExecutor} with a initialized {@link Liquibase} object.
     *
     * @param liquibase The initizlied {@link Liquibase} object
     *
     * @throws Exception Any exception comes from liquibase for convenience of running.
     */
    public void executeLiquibase(Liquibase liquibase) throws Exception;
}
