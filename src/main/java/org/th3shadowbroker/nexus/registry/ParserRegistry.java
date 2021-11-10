package org.th3shadowbroker.nexus.registry;

import org.th3shadowbroker.nexus.mapping.parsing.AbstractParser;

import java.util.*;

/**
 * Registry for storing instances of different parsers.
 * @see AbstractParser
 */
public class ParserRegistry {

    private final List<AbstractParser> parsers;

    private static ParserRegistry instance;

    private ParserRegistry() {
        this.parsers = new ArrayList<>();
    }

    /**
     * Get an optional instance of the given parsers class.
     * @param clazz The parser class.
     * @see AbstractParser
     * @return An optional instance of the given class.
     */
    public Optional<AbstractParser> getParser(Class<? extends AbstractParser> clazz) {
        return parsers.stream().filter(p -> p.getClass().equals(clazz)).findFirst();
    }

    /**
     * Registers a new parser.
     * @param abstractParser The parser.
     * @see AbstractParser
     */
    public void register(AbstractParser abstractParser) {
        getParser(abstractParser.getClass()).ifPresent(p -> {
            throw new RuntimeException(String.format("There's already a parser of type '%s'!", abstractParser.getClass().getName()));
        });
        parsers.add(abstractParser);
    }

    /**
     * Get the current instance of this class.
     * @return The instance.
     */
    public static ParserRegistry getInstance() {
        if (instance == null) instance = new ParserRegistry();
        return instance;
    }

}
