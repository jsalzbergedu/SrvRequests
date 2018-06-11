package com.ncsurobotics.srvrequests;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

/**
 * A container for the one request(s).
 * @author Jacob Salzberg
 */
@DefaultStyle
@JsonSerialize(as = ImmutableRequests.class)
@JsonDeserialize(as = ImmutableRequests.class)
@Value.Immutable
public interface Requests {
    /**
     * Returns the request contained in this object.
     * @return the request contained in this object.
     */
    SrvRequest request();
}
