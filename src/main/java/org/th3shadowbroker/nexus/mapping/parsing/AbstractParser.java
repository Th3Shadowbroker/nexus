package org.th3shadowbroker.nexus.mapping.parsing;

import org.th3shadowbroker.nexus.mapping.annotations.ParseWith;

import java.lang.reflect.Field;

/**
 * Superclass for parsers used in the @ParseWith annotation.
 * All subclasses of this class have to contain an empty constructor.
 * @see ParseWith
 */
public abstract class AbstractParser {

    /**
     * Parses the given object.
     * @param object The object that should be parsed.
     * @return The parsed object.
     */
    public abstract Object parse(Field field, Object object);

}
