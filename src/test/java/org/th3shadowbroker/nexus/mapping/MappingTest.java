package org.th3shadowbroker.nexus.mapping;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.th3shadowbroker.nexus.mapping.parsing.AbstractParser;
import org.th3shadowbroker.nexus.mapping.parsing.MaterialIdentifierParser;
import org.th3shadowbroker.nexus.mapping.parsing.RangeParser;
import org.th3shadowbroker.nexus.util.MaterialIdentifier;
import org.th3shadowbroker.nexus.util.NumericRange;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
public class MappingTest {

    private static Map<String, Object> values;

    @BeforeAll
    static void beforeAll() {
        log.info("Initializing {}...", MappingTest.class.getSimpleName());

        values = new HashMap<>();

        values.put("some.string", "foobar");
        values.put("some.int", 42);
        values.put("some.double", 4.2);
        values.put("some.range", "2.0-4.0");
        values.put("some.other.range", "5.2-7.1");
        values.put("some.list", Collections.singletonList("somevalue"));
    }

    @Test
    @SneakyThrows
    public void testMapping() {
        // For mocking
        MappingDummy mapping = new MappingDummy();

        // Object hasn't been mapped yet and should be null
        assertNull(mapping.someString);

        // Process object
        Map<Class<?>, AbstractParser> parsers = new HashMap<>();
        parsers.put(NumericRange.class, new RangeParser());
        parsers.put(MaterialIdentifier.class, new MaterialIdentifierParser());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getParserRegistry().register(parsers);

        objectMapper.map(mapping, values);

        // Check fields
        log.info("Testing mapping:");

        assertEquals(values.get("some.string"), mapping.someString);
        log.info("\tsomeString => {}", mapping.someString);

        assertEquals(values.get("some.int"), mapping.someInt);
        log.info("\tsomeInt => {}", mapping.someInt);

        assertEquals(values.get("some.double"), mapping.someDouble);
        log.info("\tsomeDouble => {}", mapping.someDouble);

        assertEquals(values.get("some.range"), mapping.someRange.toString());
        log.info("\tsomeRange => {}", mapping.someRange.toString());

        assertEquals(values.get("some.other.range"), mapping.someOtherRange.toString());
        log.info("\tsomeOtherRange => {}", mapping.someOtherRange.toString());

        List<String> dummyList = mapping.someList;
        assertEquals(1, dummyList.size());
        assertEquals("somevalue", dummyList.get(0));
        log.info("\tsomeList => {}", mapping.someList.get(0));

        log.info("Done");
    }

}
