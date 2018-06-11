package com.ncsurobotics.srvrequests;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

/**
 * A request to list all of the source
 * IDs and names.
 * @author Jacob Salzberg
 */
@DefaultStyle
@Value.Immutable
@JsonSerialize(as = ImmutableSourceList.class)
@JsonDeserialize(as = ImmutableSourceList.class)
public interface SourceList extends SrvRequest {
    /**
     * Allow SourceList to accept a visitor.
     * @return what visit returns.
     */
    @Override
    default <T> T accept(RequestVisitor<T> v) {
        return v.visit(this);
    }
}
