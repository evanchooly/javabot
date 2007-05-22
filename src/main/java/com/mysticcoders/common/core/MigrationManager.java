package com.mysticcoders.common.core;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * MigrationManager
 * <p/>
 * Created by: Andrew Lombardi
 * Copyright 2006 Mystic Coders, LLC
 */
public interface MigrationManager {

    String getUserName();

    int update(String string) throws DataAccessException;

    int update(java.lang.String string, java.lang.Object[] objects) throws DataAccessException;

    Object query(String s, ResultSetExtractor resultSetExtractor) throws DataAccessException;

    Object query(String s, Object[] objects, ResultSetExtractor resultSetExtractor) throws DataAccessException;

    void info(String string);

}
