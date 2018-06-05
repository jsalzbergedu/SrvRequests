package com.ncsurobotics.srvrequests;
import java.util.Optional;

import org.immutables.value.Value;

/**
 * A container for zero to one requests.
 * This request will be hidden behind an
 * Optional field.
 * @author Jacob Salzberg
 */
@DefaultStyle
@Value.Immutable
public interface OptionalRequests {
    /**
     * Represents (the possibility of)
     * a LifecycleGet request.
     * @return An optional wrapper around LifecycleGet.
     */
    Optional<LifecycleGet> lifecycleGet();

    /**
     * Represents (the possibility of)
     * a LifecycleKill request.
     * @return An optional wrapper around LifecycleKill.
     */
    Optional<LifecycleKill> lifecycleKill();

    /**
     * Represents (the possiblity of)
     * a LifecycleStart request.
     * @return An optional wrapper around LifecycleStart.
     */
    Optional<LifecycleStart> lifecycleStart();

    /**
     * Represents (the possiblity of)
     * a SourceList request.
     * @return An optional wrapper around SourceList.
     */
    Optional<SourceList> sourceList();

    /**
     * Represents (the possiblity of)
     * a SourceOpen request.
     * @return An optional wrapper around SourceOpen.
     */
    Optional<SourceOpen> sourceOpen();

    /**
     * Represents (the possibility of)
     * a SourceClose request.
     * @return An optional wrapper around SourceClose.
     */
    Optional<SourceClose> sourceClose();
}
