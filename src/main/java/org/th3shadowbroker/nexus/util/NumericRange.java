package org.th3shadowbroker.nexus.util;

import lombok.Data;
import org.th3shadowbroker.nexus.exceptions.RangeFormatException;

import java.util.Random;

@Data
public class NumericRange {

    private final double min;

    private final double max;

    private final Random random;

    public NumericRange(double a, double b) {
        this.min = Math.min(a, b);
        this.max = Math.max(a, b);
        this.random = new Random();
    }

    public int getIntWithin() {
        if (!isActualRange()) return getMinInt();
        return getMinInt() + random.nextInt(getMaxInt() + 1 - getMinInt());
    }

    public double getDoubleWithing() {
        if (!isActualRange()) return max;
        return min + (max - min) * random.nextDouble();
    }

    public boolean isActualRange() {
        return min != max;
    }

    public int getMinInt() {
        return (int) min;
    }

    public int getMaxInt() {
        return (int) max;
    }

    @Override
    public String toString() {
        return toString('-');
    }

    public String toString(char separator) {
        return String.format("%s%s%s",min, separator, max);
    }

    public static NumericRange parse(String value) throws RangeFormatException {
        return parse(value, '-');
    }

    public  static NumericRange parse(String value, char separator) throws RangeFormatException {
        String[] splitted = value.split(String.valueOf(separator));
        if (splitted.length == 2) {
            try {

                // Parse values
                double a = Double.parseDouble(splitted[0]);
                double b = Double.parseDouble(splitted[1]);

                return new NumericRange(a, b);

            // a or b is not a valid double
            } catch (NumberFormatException ex) {
                throw new RangeFormatException(value, ex);
            }
        }

        // Invalid input
        throw new RangeFormatException(value, null);
    }

}
