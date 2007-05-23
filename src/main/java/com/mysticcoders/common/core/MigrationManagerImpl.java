package com.mysticcoders.common.core;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;

/**
 * MigrationManagerImpl provides a simple automated database migration/update tool.  The classpath is searched
 * for implementers of the Migration interface which are then processed in order updating, and migrating data.
 */
public class MigrationManagerImpl implements MigrationManager, InitializingBean {
    private static final Logger LOG = Logger.getLogger(MigrationManagerImpl.class);

    protected String userName;
    private Set<Migration> migrations = new HashSet<Migration>();
    protected JdbcTemplate jdbcTemplate;
    protected TransactionTemplate transactionTemplate;

    protected MigrationManagerImpl(TransactionTemplate transactionTemplate, JdbcTemplate jdbcTemplate, String userName) {
        this.transactionTemplate = transactionTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.userName = userName;
    }


    public void afterPropertiesSet() throws Exception {
        migrateDataBase();
    }

    public void migrateDataBase() {

        final Set<Migration> sortedMigrations = new TreeSet<Migration>(new MigrationComparator());
        sortedMigrations.addAll(migrations);

        // Find current version number
        final long initialVersion = jdbcTemplate.queryForLong("SELECT dbversion FROM dbversion");

        LOG.info("MigrationManagerImpl:");
        LOG.info("  * Current database version is " + initialVersion);
        LOG.info("  * Found " + sortedMigrations.size() + " update tasks, looking for updates to run...");


        Long newVersion = (Long) transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction(TransactionStatus transactionStatus) {

                long currentVersion = initialVersion;

                try {
                    for (Iterator iterator = sortedMigrations.iterator(); iterator.hasNext();) {
                        Migration migration = (Migration) iterator.next();
                        if (migration.getVersion() > currentVersion) {
                            LOG.info("  * Found migration " + migration.getVersion() + ": " + migration.getDescription() + ", processing...");
                            migration.migrateDatabase(MigrationManagerImpl.this);
                            currentVersion = migration.getVersion();
                            jdbcTemplate.update("UPDATE dbversion SET dbversion = ?", new Object[]{currentVersion});
                        } else {
                            LOG.info("  * Skipping migration " + migration.getVersion() + ": " + migration.getDescription() + ".");
                        }
                    }

                    return currentVersion;

                } catch (Exception e) {
                    LOG.warn("  * Data migration failed - all migrations will be rolled back", e);
                    transactionStatus.setRollbackOnly();
                    return null;
                }
            }
        });

        if (newVersion == null) {
            throw new TransactionSystemException("Transaction was rolled back due to errors");
        } else {
            LOG.info("  * Database version is now: " + newVersion);
        }

    }


    private class MigrationComparator implements Comparator<Migration> {
        public int compare(Migration o1, Migration o2) {
            return new Long(o1.getVersion()).compareTo(o2.getVersion());
        }
    }

    public void setMigrations(Set<Migration> migrations) {
        this.migrations = migrations;
    }


    public String getUserName() {
        return userName;
    }

    public int update(String string) throws DataAccessException {
        return jdbcTemplate.update(string);
    }

    public int update(String string, Object[] objects) throws DataAccessException {
        return jdbcTemplate.update(string, objects);
    }


    public Object query(String s, ResultSetExtractor resultSetExtractor) throws DataAccessException {
        return jdbcTemplate.query(s, resultSetExtractor);
    }


    public Object query(String s, Object[] objects, ResultSetExtractor resultSetExtractor) throws DataAccessException {
        return jdbcTemplate.query(s, objects, resultSetExtractor);
    }

    public void info(String string) {
        LOG.info("    * " + string);
    }
}
