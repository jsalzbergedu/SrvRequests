package com.ncsurobotics.srvrequests;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

/**
 * A request to start the server.
 * @author Jacob Salzberg
 */
@DefaultStyle
@Value.Immutable
@JsonSerialize(as = ImmutableLifecycleStart.class)
@JsonDeserialize(as = ImmutableLifecycleStart.class)
public interface LifecycleStart extends SrvRequest {

    /**
     * Allow LifecycleStart to accept a visitor.
     * @return what visit returns.
     */
    @Override
    default <T> T accept(RequestVisitor<T> v) {
        return v.visit(this);
    }
}
