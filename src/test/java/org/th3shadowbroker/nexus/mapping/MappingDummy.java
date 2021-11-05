package org.th3shadowbroker.nexus.mapping;

import org.th3shadowbroker.nexus.mapping.annotations.ConfigPath;
import org.th3shadowbroker.nexus.mapping.annotations.ParseWith;
import org.th3shadowbroker.nexus.mapping.parsing.RangeParser;
import org.th3shadowbroker.nexus.util.NumericRange;

import java.util.List;

public class MappingDummy {

    @ConfigPath("some.string")
    public String someString;

    @ConfigPath("some.int")
    public int someInt;

    @ConfigPath("some.double")
    public double someDouble;

    @ConfigPath("some.range")
    @ParseWith(RangeParser.class)
    public NumericRange someRange;

    @ConfigPath("some.list")
    public List<String> someList;

}
