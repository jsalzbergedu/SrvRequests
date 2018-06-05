package com.ncsurobotics.srvrequests;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The unifying type for all requests.
 * @author Jacob Salzberg
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,
              include = JsonTypeInfo.As.PROPERTY,
              property = "@class")
public interface SrvRequest {
    /**
     * Allow SrvRequest to accept a visitor.
     * @param <T> the type that visit returns.
     * @param v the visitor
     * @return what visit returns.
     */
    default <T> T accept(RequestVisitor<T> v) {
        return v.visit(this);
    }
}
