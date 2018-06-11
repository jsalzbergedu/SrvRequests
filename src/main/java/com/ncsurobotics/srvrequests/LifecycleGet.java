package com.ncsurobotics.srvrequests;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

/**
 * A request to get the status of the lifecycle of the server.
 * @author Jacob Salzberg
 */
@DefaultStyle
@Value.Immutable
@JsonSerialize(as = ImmutableLifecycleGet.class)
@JsonDeserialize(as = ImmutableLifecycleGet.class)
public interface LifecycleGet extends SrvRequest {

    /**
     * Allow LifecycleGet to accept a visitor.
     * @return what visit returns.
     */
    @Override
    default <T> T accept(RequestVisitor<T> v) {
        return v.visit(this);
    }
}
