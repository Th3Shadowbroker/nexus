package org.th3shadowbroker.nexus.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.th3shadowbroker.nexus.exceptions.RangeFormatException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NumericRangeTest {

    @Test
    @SneakyThrows
    public void testParsing() {

        // Parse integer range
        {
            String value = "0-10";
            NumericRange.parse(value);
        }

        // Parse double range
        {
            String value = "0.75-21.3";
            NumericRange.parse(value);
        }

    }

    @Test
    public void testExceptions() {

        // Test RangeFormatException
        {
            assertThrows(RangeFormatException.class, () -> {
                NumericRange.parse("a-z");
            });
        }

    }

}
