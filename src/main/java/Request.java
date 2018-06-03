package com.ncsurobotics.srvrequests;

/**
 * The unifying type for all requests.
 * @author Jacob Salzberg
 */
@Gson.ExpectedSubtypes({LifecycleGet.class, LifecycleKill.class,
                LifecycleStart.class, SourceList.class,
                SourceOpen.class, SourceClose.class})
public interface Request {
}
