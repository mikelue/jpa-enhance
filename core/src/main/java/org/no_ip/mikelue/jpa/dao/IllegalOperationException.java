package org.no_ip.mikelue.jpa.dao;

import java.lang.reflect.Method;

/**
 * This exception is used to throw from DAO object to indicate that
 * this operation which inherited from {@link AbstractTypedDaoFacadeBase} is not supported.
 */
public class IllegalOperationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public IllegalOperationException(Method illegalMethod)
    {
        this(String.format("Illegal method get called: %s", illegalMethod));
    }
    public IllegalOperationException(String message)
    {
        super(message);
    }
}
