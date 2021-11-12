package org.th3shadowbroker.nexus.registry;

import org.th3shadowbroker.nexus.mapping.parsing.AbstractParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Utility class to re-use parser instances.
 * An instance of this class is included in every ObjectMapper to support individual ObjectMappers.
 * @see org.th3shadowbroker.nexus.mapping.ObjectMapper
 */
public class ParserRegistry {

    /**
     * The map associating Types with AbstractParsers.
     */
    private final Map<Class<?>, AbstractParser> registry;

    /**
     * Create a new ParserRegistry.
     */
    public ParserRegistry() {
        this.registry = new HashMap<>();
    }

    /**
     * Get an optional parser for the given type.
     * @param type The type.
     * @return An optional parser.
     */
    public Optional<AbstractParser> getParser(Class<?> type) {
        return Optional.ofNullable(registry.get(type));
    }

    /**
     * Get an optional parser instance for the given type of parser.
     * @param parserType The type of the parser.
     * @return An optional parser.
     */
    public Optional<AbstractParser> getSpecificParser(Class<? extends AbstractParser> parserType) {
        return registry.values().stream().filter(p -> p.getClass() == parserType).findFirst();
    }

    /**
     * Returns a set containing all types that can be parsed by AbstractParsers within the registry.
     * @return Set of all supported types.
     */
    public Set<Class<?>> getParsableTypes() {
        return registry.keySet();
    }

    /**
     * Registers a new parser.
     * @param type The type.
     * @param parser The parser instance.
     */
    public void register(Class<?> type, AbstractParser parser) {
        registry.put(type, parser);
    }

    /**
     * Register multiple parsers a t once.
     * @param parsers A map with the Type as the key and the AbstractParser as its value.
     * @see AbstractParser
     */
    public void register(Map<Class<?>,AbstractParser> parsers) {
        registry.putAll(parsers);
    }

}
