package javabot.dao.util;

/*
* Created by: Andrew Lombardi
 * Copyright 2006 Mystic Coders, LLC
 */

//
// User: joed
// Date: Apr 11, 2007
// Time: 2:53:22 PM

//

public class EntityNotFoundException extends javax.persistence.EntityNotFoundException {

    public EntityNotFoundException(Class clazz, Long id) {
        super("An object of type " + clazz + " with ID " + id + " does not exist.");
    }

    public EntityNotFoundException() {
        super();
    }

    /*
            public EntityNotFoundException(String arg0, Throwable arg1) {
                    super(arg0, arg1);
            }
    */

    public EntityNotFoundException(String arg0) {
        super(arg0);
    }

    /*
            public EntityNotFoundException(Throwable arg0) {
                    super(arg0);
            }
    */

}

