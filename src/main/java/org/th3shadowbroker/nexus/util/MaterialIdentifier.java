package org.th3shadowbroker.nexus.util;

import org.th3shadowbroker.nexus.exceptions.IdentifierFormatException;

/**
 * A class to differentiate between materials on a config level.
 * This class is intended to be used for custom materials from plugins like ItemsAdder.
 *
 * NOTE: This is for config level only! An actual check has to be implemented on the side of the library user!
 */
public record MaterialIdentifier(String namespace, String name) {

    /**
     * Create a new MaterialIdentifier.
     *
     * @param namespace The namespace.
     * @param name      The name.
     */
    public MaterialIdentifier {
        // Just a placeholder
    }

    /**
     * Check if the MaterialIdentifier identifies a vanilla item (Namespace is "minecraft").
     *
     * @return True if the namespace is "minecraft"
     */
    public boolean isVanilla() {
        return namespace.equals("minecraft");
    }

    /**
     * Parses the given string as a MaterialIdentifier.
     *
     * @param str The string.
     * @return The parsed MaterialIdentifier.
     * @throws IdentifierFormatException Thrown if the string could not be parsed.
     */
    public static MaterialIdentifier parse(String str) throws IdentifierFormatException {
        return parse(str, ':');
    }

    /**
     * Parses the given string as a MaterialIdentifier.
     *
     * @param str       The string.
     * @param separator The separator.
     * @return The parsed MaterialIdentifier.
     * @throws IdentifierFormatException Thrown if the string could not be parsed.
     */
    public static MaterialIdentifier parse(String str, char separator) throws IdentifierFormatException {
        String[] splitted = str.toLowerCase().split(String.valueOf(separator));

        if (splitted.length == 1) {
            return new MaterialIdentifier("minecraft", splitted[0]);
        } else if (splitted.length == 2) {
            return new MaterialIdentifier(splitted[0], splitted[1]);
        }

        throw new IdentifierFormatException(String.format("Could not parse material identifier '%s'!", str));
    }

}
