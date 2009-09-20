using sql

mixin Persistent {
    static SqlService service := SqlService.make("jdbc:postgresql:javabot", "javabot", "", PostgresqlDialect.make()).open
    
    static Persistent[] findAll() {
        statement := service.sql("select * from ${tableName}")
        list := [,]
        rows := statement.query
        rows.each | Row row | {
            object := type.base().make
        }
        return list
    }

    static Str tableName() {
        type.name
    }
}