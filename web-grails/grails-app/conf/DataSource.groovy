dataSource {
  pooled = true
  driverClassName = "org.postgresql.Driver"
  username = "javabot"
  password = "javabot"
  dialect = org.hibernate.dialect.PostgreSQLDialect
}
hibernate {
  cache.use_second_level_cache = true
  cache.use_query_cache = true
  cache.provider_class = 'com.opensymphony.oscache.hibernate.OSCacheProvider'
}

// environment specific settings
environments {
  development {
    dataSource {
      url = "jdbc:postgresql:javabot"
      logSql = true
    }
  }
  test {
    dataSource {
      url = "jdbc:postgresql:javabot"
    }
  }
  production {
    dataSource {
      url = "jdbc:postgresql:javabot"
    }
  }
}