package com.ncsurobotics.srvrequests;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.multij.Module;
import org.multij.MultiJ;

import junit.framework.TestCase;

/**
 * Tests for all of the SrvRequests.
 * @author Jacob Salzberg
 */
public class TestSrvRequest extends TestCase {
    /**
     * A random port to use
     */
    public static final int PORT = 4006;

    /**
     * An object mapper to deserialize objects with.
     */
    private ObjectMapper mapper;


    /**
     * Set up the object mapper.
     * TODO get the @Before annotation to work
     */
    public void setup() {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
    }

    @Test
    public void testLifecycleGet() throws IOException {
        setup();
        final LifecycleGet get = ImmutableLifecycleGet.builder().build();
        final String json = mapper.writeValueAsString(get);
        /*
         * When raw string literals come around
         * http://openjdk.java.net/jeps/326
         * these should all be switched to raw
         */
        assertEquals("{\"@class\":\"com.ncsurobotics.srvrequests.ImmutableLifecycleGet\"}",
                     json);
    }

    @Test
    public void testLifecycleKill() throws IOException {
        setup();
        final LifecycleKill kill = ImmutableLifecycleKill.builder().build();
        final String json = mapper.writeValueAsString(kill);
        assertEquals("{\"@class\":\"com.ncsurobotics.srvrequests.ImmutableLifecycleKill\"}",
                     json);
    }

    @Test
    public void testLifecycleStart() throws IOException {
        setup();
        final LifecycleStart start = ImmutableLifecycleStart.builder().build();
        final String json = mapper.writeValueAsString(start);
        assertEquals("{\"@class\":\"com.ncsurobotics.srvrequests.ImmutableLifecycleStart\"}",
                     json);
    }

    @Test
    public void testSourceClose() throws IOException {
        setup();
        final SourceClose close = ImmutableSourceClose.builder()
                          .id((byte) 0)
                          .build();
        assertEquals(close.id(), 0);
        final String json = mapper.writeValueAsString(close);
        assertEquals("{\"@class\":\"com.ncsurobotics.srvrequests.ImmutableSourceClose\",\"id\":0}",
                     json);
    }

    @Test
    public void testSourceList() throws IOException {
        setup();
        final SourceList list = ImmutableSourceList.builder().build();
        final String json = mapper.writeValueAsString(list);
        assertEquals("{\"@class\":\"com.ncsurobotics.srvrequests.ImmutableSourceList\"}",
                     json);
    }

    @Test
    public void testSourceOpen() throws IOException {
        setup();
        final String name = "Test stream name";
        final SourceOpen open = ImmutableSourceOpen.builder()
                                .name(name)
                                .port(PORT)
                                .build();
        assertEquals(open.name(), name);
        assertEquals(open.port(), PORT);
        final String json = mapper.writeValueAsString(open);
        assertEquals("{\"@class\":\"com.ncsurobotics.srvrequests.ImmutableSourceOpen\",\"name\":\"Test stream name\",\"port\":4006}",
                     json);
    }

    @Test
    public void testDeserialize() throws IOException {
        setup();
        final String name = "dummyname";
        final SourceOpen open = ImmutableSourceOpen.builder()
                                .name(name)
                                .port(PORT)
                                .build();
        final Requests requests = ImmutableRequests.builder()
                                  .request(open)
                                  .build();
        final String json = mapper.writeValueAsString(requests);
        final SrvRequest deserd = mapper.readValue(json, Requests.class).request();
        final DispatchSourceOpen dispatch =
                MultiJ.instance(DispatchSourceOpen.class);
        dispatch.dispatch(deserd, name, PORT);
    }

    /**
     * A multimethod for testDeserialize.
     * @author Jacob Salzberg
     */
    @Module
    public interface DispatchSourceOpen {
        default void dispatch(SrvRequest request, String name, int port) {
            throw new RuntimeException("Wrong type dispatched.");
        }

        default void dispatch(SourceOpen request, String name, int port) {
            assertEquals(name, request.name());
            assertEquals(port, request.port());
        }
    }
}
