package org.th3shadowbroker.nexus.mapping.parsing;

import org.th3shadowbroker.nexus.exceptions.ParsingException;
import org.th3shadowbroker.nexus.exceptions.RangeFormatException;
import org.th3shadowbroker.nexus.util.NumericRange;

import java.lang.reflect.Field;

/**
 * Parser for parsing NumericRanges.
 * @see NumericRange
 */
public class RangeParser extends AbstractParser {

    @Override
    public Object parse(Field field, Object object) throws ParsingException {
        try {
            return field.getType().cast(NumericRange.parse((String) object));
        } catch (RangeFormatException e) {
            throw new ParsingException(String.format("Failed to parse '%s' as '%s'!", String.valueOf(object), NumericRange.class.getName()), e);
        }
    }

}
