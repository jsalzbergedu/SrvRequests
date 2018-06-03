package com.ncsurobotics.srvrequests;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

/**
 * A container for the request
 * to send to the server.
 * Called requests because it represents
 * between 0 and 1 requests.
 * @author Jacob Salzberg
 */
@DefaultStyle
@Value.Immutable
@Gson.TypeAdapters
public interface Requests {
    /**
     * Represents the request sent to the server.
     * @return may return a request, or may return nothing.
     */
    @Gson.ExpectedSubtypes({LifecycleGet.class, LifecycleKill.class,
                    LifecycleStart.class, SourceList.class,
                    SourceOpen.class, SourceClose.class})
    Request request();
}
