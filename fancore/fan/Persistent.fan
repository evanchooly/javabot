using sql

@serializable
mixin Persistent {
    const static Log log := Log.get("javabot")

    private static const SqlService service := SqlService.make("jdbc:postgresql:javabot", "javabot", "javabot", PostgresqlDialect.make()).open

    Persistent save() {
        Str query := "insert into ${type.facet(@Table)} ${columns} values ${values}"
        echo("save query = ${query}")
        return this
    }

    static Persistent[] findAll(Type t) {
        executeQuery(t, "select * from ${tableName(t)}")
    }

    Str columns() {
        type.fields.join(",")
    }

    Str values() {
        ""
    }

    static Obj executeQuery(Type t, Str query, [Str:Obj?] params := [:]) {
        list := [,]
        try {
            statement := params.size == 0 ? service.sql(query) : service.sql(query).prepare
            try {
                rows := statement.query(params)
                rows.each | Row row | {
                    Persistent object := t->make
                    object.mapRow(row)
                    list.add(object)
                }
            } finally {
                statement.close
            }
        } catch(SqlErr err) {
            log.debug("query = ${query}")
            log.debug("params = ${params}")
            log.debug(err.message, err)
            throw Err.make("can't load ${t}: ${err.message}")
        }
        return list
    }
    
    private static Str tableName(Type t) {
        name := t.facets[@Table]
        return name == null ? t.name : name
    }

    private static Str colName(Field f) {
        facet := f.facets[@Column]
        return facet is Bool ? f.name : facet
    }

    Void mapRow(Row row) {
        type.fields.each | Field f | {
            name := f.facet(@Column)
            if(name != null) {
                col := row.col(colName(f))
                f.set(this, convert(f.of, row.get(col)))
            } else {
                query := f.facet(@Collection)
                if(query != null) {
                    loadCollection(f)
                }
            }
        }
    }

    Void loadCollection(Field f) {
        try {
            statement := service.sql(f.facet(@Collection)).prepare
            try {
                statement.queryEach(["id": this->id]) | Row row | {
                    f.get(this)->add(row[row.cols[0]])
                }
            } finally {
                statement.close
            }
        } catch(SqlErr err) {
            log.debug("can't set field ${f.of} on ${f.parent}:\n ${err}")
        }
    }

    Obj? convert(Type t, Obj? value) {
        Obj? converted := value
        if(value != null) {
            switch(t) {
                case Bool#:
                    converted = "true".equals(value)
                case DateTime#:
                case DateTime?#:
                    converted = parseDate(value)
                default:
                    if(!t.fits(Str#) && value?.type.fits(Str#)) {
                        converted = t.make()->fromStr(value)
                    }
            }
        }
        return converted
    }

    DateTime? parseDate(Str? value) {
        if(value == null) {
            return null
        }
        return DateTime.fromIso(value.replace(" ", "T") + "Z")
    }
}