package org.th3shadowbroker.nexus.mapping;

import org.bukkit.configuration.ConfigurationSection;
import org.th3shadowbroker.nexus.exceptions.MappingException;
import org.th3shadowbroker.nexus.exceptions.ParsingException;
import org.th3shadowbroker.nexus.mapping.annotations.ConfigPath;
import org.th3shadowbroker.nexus.mapping.annotations.ParseWith;
import org.th3shadowbroker.nexus.mapping.parsing.AbstractParser;
import org.th3shadowbroker.nexus.registry.ParserRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility class for mapping object fields.
 */
public class ObjectMapper {

    /**
     * Get all fields declared in the given object.
     * @param object The object.
     * @return A list containing the declared fields.
     */
    private static List<Field> getFields(Object object) {
        return Arrays.asList(object.getClass().getDeclaredFields());
    }

    /**
     * Get the @ParseWith annotation of the given field.
     * @param field The field.
     * @return An optional of the annotation.
     */
    private static Optional<ParseWith> getParserAnnotation(Field field) {
        return Optional.ofNullable(field.getAnnotation(ParseWith.class));
    }

    /**
     * Get the @ConfigPath annotation of the given field.
     * @param field The field.
     * @return An optional of the annotation.
     */
    private static Optional<ConfigPath> getConfigPathAnnotation(Field field) {
        return Optional.ofNullable(field.getAnnotation(ConfigPath.class));
    }

    /**
     * Map all @ConfigPath annotated fields of the given object with the key-value pairs from the given map.
     * @param object The object.
     * @param values A map containing the values.
     * @throws MappingException If an exception occurs while mapping the given object. (The original exception can bee accessed using getCause()).
     */
    public static void map(Object object, Map<String, Object> values) throws MappingException {
        List<Field> annotatedFields = getFields(object).stream().filter(field -> field.canAccess(object)).collect(Collectors.toList());

        for (Field field : annotatedFields) {

            // Get config path
            Optional<ConfigPath> configPath = getConfigPathAnnotation(field);
            if (configPath.isEmpty()) continue;

            String path = configPath.get().value();

            // Get parser
            Optional<ParseWith> parser = getParserAnnotation(field);

            // Use parser
            if (parser.isPresent()) {
                AbstractParser assignedParser = null;

                // Instantiate parser and assign value
                try {
                    Optional<AbstractParser> parserInstance = ParserRegistry.getInstance().getParser(parser.get().value());
                    if (parserInstance.isEmpty()) {
                        assignedParser = parser.get().value().getConstructor().newInstance();
                        ParserRegistry.getInstance().register(assignedParser);
                    } else {
                        assignedParser = parserInstance.get();
                    }

                    field.set(object, assignedParser.parse(field, values.get(path)));

                    // Presented parser is abstract
                } catch (InstantiationException e) {
                    throw new MappingException(String.format("The class '%s' is abstract and therefore cannot be used as a parser!", parser.get().value().getName()), e);

                    // Cannot access constructor
                } catch (IllegalAccessException | NoSuchMethodException e) {
                    throw new MappingException(String.format("The class '%s' requires and empty and public constructor!", parser.get().value().getName()), e);

                    // Error while invoking the default constructor
                } catch (InvocationTargetException e) {
                    throw new MappingException(String.format("The class '%s' returned an error during invocation!", parser.get().value().getName()), e);

                // Parsing failed
                } catch (ParsingException e) {
                    throw new MappingException(String.format("Unable to parse '%s' as '%s'!", values.get(path), parser.get().value().getName()), e);

                }

            // Set value directly
            } else {;

                try {

                    // No casting for primitives
                    if (field.getType().isPrimitive()) {
                        field.set(object, values.get(path));

                    // Complexer types
                    } else {
                        field.set(object, field.getType().cast(values.get(path)));
                    }

                // Cannot access field
                } catch (IllegalAccessException ex) {
                    throw new MappingException(String.format("Unable to access field '%s' in class '%s'!", field.getName(), field.getDeclaringClass().getName()));
                }

            }
        }
    }

    /**
     * Map all @ConfigPath annotated fields of the given object with the key-value pairs from the given ConfigurationSection.
     * @param object The object.
     * @param configurationSection A configuration section.
     * @throws MappingException If an exception occurs while mapping the given object. (The original exception can bee accessed using getCause()).
     */
    public static void map(Object object, ConfigurationSection configurationSection) throws MappingException {
        map(object, configurationSection.getValues(true));
    }

}
