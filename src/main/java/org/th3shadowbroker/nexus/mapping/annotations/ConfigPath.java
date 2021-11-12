package org.th3shadowbroker.nexus.mapping.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Maps the annotated field with the given path.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigPath {

    /**
     * The path the annotated field should be mapped with.
     * @return The path.
     */
    String value();

}
