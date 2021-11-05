package org.th3shadowbroker.nexus.mapping.annotations;

import org.th3shadowbroker.nexus.mapping.parsing.AbstractParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parse the annotated field with the given parser.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParseWith {

    /**
     * The parser that should be used.
     * @return The parser.
     */
    Class<? extends AbstractParser> value();

}
