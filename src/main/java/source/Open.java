package com.ncsurobotics.srvrequests.source;
import org.immutables.value.Value;

/**
 * A request to open a source
 * @author Jacob Salzberg
 */
@Value.Style(stagedBuilder = true)
@Value.Immutable
interface SourceOpen extends Source {
    /**
     * The name to associate with the source
     */
    String name();

    /**
     * The port to open for the source
     */
    int port();
}
