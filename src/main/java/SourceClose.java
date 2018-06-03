package com.ncsurobotics.srvrequests;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

/**
 * Request to close a port.
 * To close a port, the requester
 * must know the id of the port they
 * want to close. The requester
 * can get that id via the request
 * found in x.
 * @author Jacob Salzberg
 */
@DefaultStyle
@Value.Style(stagedBuilder = true)
@Value.Immutable
@Gson.TypeAdapters(emptyAsNulls = true)
public interface SourceClose extends Request {
    /**
     * A marker feild, allowing the serialized
     * form of this object to be deserialized.
     * @return a useless value.
     */
    @Value.Default
    default boolean isSourceClose() {
        return true;
    };

    /**
     * Return the id of the stream.
     * @return The id of the stream. May be no longer than a byte.
     */
    byte id();
}
