package org.th3shadowbroker.nexus.exceptions;

import lombok.Getter;

public class RangeFormatException extends Exception {

    @Getter
    private final String value;

    public RangeFormatException(String value, Exception cause) {
        super(String.format("Unable to parse range from '%s'", value), cause);
        this.value = value;
    }

}
