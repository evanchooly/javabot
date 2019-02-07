package javabot.model.javadoc

import javabot.dao.JavadocClassDao
import org.jsoup.nodes.Document

class Java6JavadocSource() : JavadocSource() {
    constructor(api: JavadocApi, file: String) : this() {
        this.api = api
        this.name = file
    }

    override fun process(dao: JavadocClassDao) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun className(document: Document): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun packageName(document: Document): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateType(document: Document, docClass: JavadocClass) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun discoverMembers(javadocClassDao: JavadocClassDao, document: Document, docClass: JavadocClass) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun discoverParentType(document: Document, klass: JavadocClass) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun discoverInterfaces(document: Document, klass: JavadocClass) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}