package org.th3shadowbroker.nexus.mapping.parsing;

import org.th3shadowbroker.nexus.exceptions.IdentifierFormatException;
import org.th3shadowbroker.nexus.exceptions.ParsingException;
import org.th3shadowbroker.nexus.util.MaterialIdentifier;

import java.lang.reflect.Field;

public class MaterialIdentifierParser extends AbstractParser {

    @Override
    public Object parse(Field field, Object object) throws ParsingException {
        try {
            return field.getType().cast(MaterialIdentifier.parse((String) object));
        } catch (IdentifierFormatException e) {
            throw new ParsingException(String.format("Failed to parse '%s' as '%s'!", String.valueOf(object), MaterialIdentifier.class.getName()), e);
        }
    }

}
