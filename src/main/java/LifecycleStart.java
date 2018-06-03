package com.ncsurobotics.srvrequests;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

/**
 * A request to start the server.
 * @author Jacob Salzberg
 */
@DefaultStyle
@Value.Immutable
@Gson.TypeAdapters(emptyAsNulls = true)
public interface LifecycleStart extends Request {
    /**
     * A marker feild, allowing the serialized
     * form of this object to be deserialized.
     * @return a useless value.
     */
    @Value.Default
    default boolean isLifecycleStart() {
        return true;
    };
}
