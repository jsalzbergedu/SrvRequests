package com.ncsurobotics.srvrequests;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
@Value.Immutable
@JsonSerialize(as = ImmutableSourceClose.class)
@JsonDeserialize(as = ImmutableSourceClose.class)
public interface SourceClose extends SrvRequest {
    /**
     * Allow SourceClose to accept a visitor.
     * @return what visit returns.
     */
    @Override
    default <T> T accept(RequestVisitor<T> v) {
        return v.visit(this);
    }

    /**
     * Return the id of the stream.
     * @return The id of the stream. May be no longer than a byte.
     */
    byte id();
}
