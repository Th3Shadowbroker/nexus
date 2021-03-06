package org.th3shadowbroker.nexus.mapping;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.th3shadowbroker.nexus.exceptions.MappingException;
import org.th3shadowbroker.nexus.exceptions.ParsingException;
import org.th3shadowbroker.nexus.mapping.annotations.ConfigPath;
import org.th3shadowbroker.nexus.mapping.parsing.AbstractParser;
import org.th3shadowbroker.nexus.registry.ParserRegistry;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility class for mapping object fields.
 */
public class ObjectMapper {

    @Getter
    private final ParserRegistry parserRegistry;

    public ObjectMapper() {
        this.parserRegistry = new ParserRegistry();
    }

    /**
     * Get all fields declared in the given object.
     * @param object The object.
     * @return A list containing the declared fields.
     */
    private List<Field> getFields(Object object) {
        return Arrays.asList(object.getClass().getDeclaredFields());
    }

    /**
     * Get the @ConfigPath annotation of the given field.
     * @param field The field.
     * @return An optional of the annotation.
     */
    private Optional<ConfigPath> getConfigPathAnnotation(Field field) {
        return Optional.ofNullable(field.getAnnotation(ConfigPath.class));
    }

    /**
     * Map all @ConfigPath annotated fields of the given object with the key-value pairs from the given map.
     * @param object The object.
     * @param values A map containing the values.
     * @throws MappingException If an exception occurs while mapping the given object. (The original exception can bee accessed using getCause()).
     */
    public void map(Object object, Map<String, Object> values) throws MappingException {
        List<Field> annotatedFields = getFields(object).stream().filter(field -> field.canAccess(object)).collect(Collectors.toList());

        for (Field field : annotatedFields) {

            // Get config path
            Optional<ConfigPath> configPath = getConfigPathAnnotation(field);
            if (configPath.isEmpty()) continue;

            String path = configPath.get().value();

            // Get parser
            Optional<AbstractParser> parser = parserRegistry.getParser(field.getType());

            // Use parser
            if (parser.isPresent()) {

                try {
                    field.set(object, parser.get().parse(field, values.get(path)));

                // Reflective access fails
                } catch (IllegalAccessException ex) {
                    throw new MappingException(String.format("The class '%s' requires and empty and public constructor!", parser.get().getClass().getName()), ex);

                // Parsing failed
                } catch (ParsingException ex) {
                    throw new MappingException(String.format("Unable to parse '%s' as '%s'!", values.get(path), parser.get().getClass().getName()), ex);
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
    public void map(Object object, ConfigurationSection configurationSection) throws MappingException {
        map(object, configurationSection.getValues(true));
    }

}
