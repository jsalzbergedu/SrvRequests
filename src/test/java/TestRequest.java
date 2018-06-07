package com.ncsurobotics.srvrequests;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Tests for the source requests.
 * @author Jacob Salzberg
 */
public class TestRequest extends TestCase {
    /**
     * A random port to use
     */
    public static final int PORT = 4006;

    /**
     * An object mapper to deserialize objects with.
     */
    private ObjectMapper mapper;

    /**
     * For sending requests.
     */
    private RequestSender sender;

    /**
     * For receiving requests.
     */
    private RequestReceiver receiver;

    /**
     * Mock input stream.
     */
    private PipedInputStream input;

    /**
     * Mock output stream.
     */
    private PipedOutputStream output;

    /**
     * Set up the object mapper.
     * TODO get the @Before annotation to work
     */
    public void setup() {
        try {
            if (mapper == null) {
                mapper = new ObjectMapper();
                input = new PipedInputStream();
                output = new PipedOutputStream(input);
                receiver = RequestReceiver.builder()
                           .stream(input)
                           .mapper(mapper)
                           .build();
                sender = RequestSender.builder()
                         .stream(output)
                         .mapper(mapper)
                         .build();
            }
        } catch (IOException e) {
        }
    }

    @Test
    public void testOptionalRequests() {
        var request = ImmutableLifecycleGet.builder().build();
        var optionalRequests = RequestFactory.optionalRequests(request);
        /*
         * Both of the following should fail
         * if OptionalRequests does not contain a LifecycleGet
         */
        assertTrue(optionalRequests.lifecycleGet().isPresent());
        optionalRequests.lifecycleGet().get();
        /*
         * There should be no other feilds in optionalRequests
         */
        assertFalse(optionalRequests.sourceList().isPresent());
    }

    @Test
    public void testLifecycleGet() throws IOException {
        setup();
        final var get = ImmutableLifecycleGet.builder().build();
        final var json = mapper.writeValueAsString(get);
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
        final var kill = ImmutableLifecycleKill.builder().build();
        final var json = mapper.writeValueAsString(kill);
        assertEquals("{\"@class\":\"com.ncsurobotics.srvrequests.ImmutableLifecycleKill\"}",
                     json);
    }

    @Test
    public void testLifecycleStart() throws IOException {
        setup();
        final var start = ImmutableLifecycleStart.builder().build();
        final var json = mapper.writeValueAsString(start);
        assertEquals("{\"@class\":\"com.ncsurobotics.srvrequests.ImmutableLifecycleStart\"}",
                     json);
    }

    @Test
    public void testSourceClose() throws IOException {
        setup();
        final var close = ImmutableSourceClose.builder()
                          .id((byte) 0)
                          .build();
        assertEquals(close.id(), 0);
        final var json = mapper.writeValueAsString(close);
        assertEquals("{\"@class\":\"com.ncsurobotics.srvrequests.ImmutableSourceClose\",\"id\":0}",
                     json);
    }

    @Test
    public void testSourceList() throws IOException {
        setup();
        final var list = ImmutableSourceList.builder().build();
        final var json = mapper.writeValueAsString(list);
        assertEquals("{\"@class\":\"com.ncsurobotics.srvrequests.ImmutableSourceList\"}",
                     json);
    }

    @Test
    public void testSourceOpen() throws IOException {
        setup();
        final var name = "Test stream name";
        final var open = ImmutableSourceOpen.builder()
                         .name(name)
                         .port(PORT)
                         .build();
        assertEquals(open.name(), name);
        assertEquals(open.port(), PORT);
        final var json = mapper.writeValueAsString(open);
        assertEquals("{\"@class\":\"com.ncsurobotics.srvrequests.ImmutableSourceOpen\",\"name\":\"Test stream name\",\"port\":4006}",
                     json);
    }

    @Test
    public void testDeserialize() throws IOException {
        setup();
        final var name = "dummyname";
        final var open = ImmutableSourceOpen.builder()
                         .name(name)
                         .port(PORT)
                         .build();
        final var requests = ImmutableRequests.builder()
                             .request(open)
                             .build();
        final var json = mapper.writeValueAsString(requests);
        final var deserd = mapper.readValue(json, Requests.class).request();
        final var optionalRequests = RequestFactory.optionalRequests(deserd);
        final var deserdOpen = optionalRequests.sourceOpen().get();
        assertEquals(name, deserdOpen.name());
        assertEquals(PORT, deserdOpen.port());
    }

}
