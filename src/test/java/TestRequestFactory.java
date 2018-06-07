package com.ncsurobotics.srvrequests;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Unit tests for SRVRequests.
 * @author Jacob Salzberg.
 */
public class TestRequestFactory extends TestCase {
    @Test
    public void testEmpty() {
        RequestFactory.empty();
    }
}
