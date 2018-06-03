package com.ncsurobotics.srvrequests;
import org.immutables.gson.Gson;
import org.immutables.value.Value;
/**
 * A request to kill the server.
 * @author Jacob Salzberg
 */
@DefaultStyle
@Value.Immutable
@Gson.TypeAdapters
public interface LifecycleKill extends Request {
    /**
     * A marker feild, allowing the serialized
     * form of this object to be deserialized.
     * @return a useless value.
     */
    @Value.Default
    default boolean isLifecycleKill() {
        return true;
    };
}
