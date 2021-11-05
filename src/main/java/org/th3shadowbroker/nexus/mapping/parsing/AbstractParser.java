package org.th3shadowbroker.nexus.mapping.parsing;

import org.th3shadowbroker.nexus.exceptions.ParsingException;
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
     * @throws ParsingException Thrown if something goes wrong while parsing. The original exception can be accessed by using getCause().
     */
    public abstract Object parse(Field field, Object object) throws ParsingException;

}
