package com.ncsurobotics.srvrequests;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

/**
 * A request to kill the server.
 * @author Jacob Salzberg
 */
@DefaultStyle
@Value.Immutable
@JsonSerialize(as = ImmutableLifecycleKill.class)
@JsonDeserialize(as = ImmutableLifecycleKill.class)
public interface LifecycleKill extends SrvRequest {

    /**
     * Allow LifecycleKill to accept a visitor.
     * @return what visit returns.
     */
    @Override
    default <T> T accept(RequestVisitor<T> v) {
        return v.visit(this);
    }
}
