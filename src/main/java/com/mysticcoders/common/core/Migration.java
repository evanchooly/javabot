package com.mysticcoders.common.core;

/**
 * Migration
 * <p/>
 * Created by: Andrew Lombardi
 * Copyright 2006 Mystic Coders, LLC
 */
public interface Migration {
    public long getVersion();

    public String getDescription();

    public void migrateDatabase(MigrationManager migrationManager);
}
