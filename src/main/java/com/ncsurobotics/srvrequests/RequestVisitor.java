package com.ncsurobotics.srvrequests;

/**
 * A visitor for the requests,
 * To use this interface, make a concrete
 * RequestVisitor with the type of your choosing.
 * This interface is perpetually unstable,
 * and is gaurenteed to break every
 * time a new kind of SrvRequest is added.
 * @param <T> a type, to be chosen by the implementor.
 * @author Jacob Salzberg
 */
public interface RequestVisitor<T> {
    /**
     * Visit the SrvRequest.
     * All requests are also SrvRequests, so when this method is dispatched,
     * no SrvRequest has been matched.
     * @param r the request
     * @return the value and type of the implementor's choosing.
     */
    T visit(SrvRequest r);

    /**
     * Visit LifecycleGet.
     * @param r the request
     * @return the value and type of the implementor's choosing.
     */
    T visit(LifecycleGet r);

    /**
     * Visit LifecycleKill.
     * @param r the request
     * @return the value and type of the implementor's choosing.
     */
    T visit(LifecycleKill r);

    /**
     * Visit LifecycleStart.
     * @param r the request
     * @return the value and type of the implementor's choosing.
     */
    T visit(LifecycleStart r);

    /**
     * Visit SourceList.
     * @param r the request
     * @return the value and type of the implementor's choosing.
     */
    T visit(SourceList r);

    /**
     * Visit SourceOpen.
     * @param r the request
     * @return the value and type of the implementor's choosing.
     */
    T visit(SourceOpen r);

    /**
     * Visit SourceClose.
     * @param r the request
     * @return the value and type of the implementor's choosing.
     */
    T visit(SourceClose r);
}
