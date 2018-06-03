package com.ncsurobotics.srvrequests;
import org.immutables.gson.Gson;
import org.immutables.value.Value;


/**
 * A request to open a source.
 * @author Jacob Salzberg
 */
@DefaultStyle
@Value.Immutable
@Gson.TypeAdapters(emptyAsNulls = true)
public interface SourceOpen extends Request {
    /**
     * A marker feild, allowing the serialized
     * form of this object to be deserialized.
     * @return a useless value.
     */
    @Value.Default
    default boolean isSourceOpen() {
        return true;
    };

    /**
     * Return the name associatd with the source.
     * @return the name associated with the source.
     */
    String name();

    /**
     * Return the port to open for the source.
     * @return the port to open for the source
     */
    int port();
}
