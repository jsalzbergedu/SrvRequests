package com.ncsurobotics.srvrequests;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;


/**
 * A request to open a source.
 * @author Jacob Salzberg
 */
@DefaultStyle
@Value.Immutable
@JsonSerialize(as = ImmutableSourceOpen.class)
@JsonDeserialize(as = ImmutableSourceOpen.class)
public interface SourceOpen extends SrvRequest {
    /**
     * Allow SourceOpen to accept a visitor.
     * @return what visit returns.
     */
    @Override
    default <T> T accept(RequestVisitor<T> v) {
        return v.visit(this);
    }

    /**
     * Return the name associatd with the source.
     * @return the name associated with the source.
     */
    String name();

    /**
     * Return the port to open for the source.
     * @return the port to open for the source
     */
    int port();
}
