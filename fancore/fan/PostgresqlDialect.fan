using sql

const class PostgresqlDialect : Dialect {
    static const Int LENGTH := 512
    override Int maxTableNameLength() {
        LENGTH
    }

    override Int maxIndexNameLength() {
        LENGTH
    }

    override Str getBlobType(Int len) {
        "long"
    }

    override Str getClobType(Int len) {
        "text"
    }
}