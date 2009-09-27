using sql

mixin Persistent {
    const static Log log := Log.get("persistent")

    private static const SqlService service := SqlService.make("jdbc:postgresql:javabot", "javabot", "javabot", PostgresqlDialect.make()).open

    static Persistent[] findAll(Type t) {
        executeQuery(t, "select * from ${tableName(t)}")
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
            echo("query = ${query}")
            echo("params = ${params}")
            echo(err)
            throw Err.make("can't load ${t}")
        }
        return list
    }
    
    static Str tableName(Type t) {
        name := t.facets[@Table]
        return name == null ? t.name : name
    }

    static Str colName(Field f) {
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
        echo("done reading in ${this}")
    }

    Void loadCollection(Field f) {
        try {
            statement := service.sql(f.facet(@Collection)).prepare
            try {
                statement.queryEach(["id": this->id]) | Row row | {
                    f.get(this)->add(row[row.cols[0]])
                }
                echo("field type = ${f.of}")
            } finally {
                statement.close
            }
        } catch(SqlErr err) {
            log.error("can't set field ${f.of} on ${f.parent}:\n ${err}")
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
        Str[] date := value.split
        Int month := Month.fromStr(date[1].localeLower).ordinal + 1
        Str monthVal := (month < 10 ? "0" : "") + month
        return DateTime.fromStr("${date[5]}-${monthVal}-${date[2]}T${date[3]}+00:00 UTC")
    }
}