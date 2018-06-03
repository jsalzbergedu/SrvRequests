package com.ncsurobotics.srvrequests;
import java.util.ServiceLoader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Request;

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
     * Flag set to false once
     * gson has been setup
     */
    private boolean gsonNeedsSetup;

    /**
     * A gson object for serialization
     * and deserialization
     */
    private Gson gson;

    /**
     * Set up the gson object.
     * TODO get the @Before annotation to work
     */
    @Before
    public void setup() {
        /*
         * Boilerplate for registering
         * Immutables types with gson's
         * json type registry
         */
        if (gson == null) { 
            var gsonBuilder = new GsonBuilder();
            for (var factory : ServiceLoader.load(TypeAdapterFactory.class)) {
                gsonBuilder.registerTypeAdapterFactory(factory);
            }
            gson = gsonBuilder.create();
        }
    }

    @Test
    public void testDispatch() {
        class IsLifecycleGet {
            public boolean visit(Request r) {
                // A more specific subtype could not be dispatched.
                return false;
            }

            public boolean visit(LifecycleGet r) {
                return true;
            }
        }
        var request = ImmutableLifecycleGet.builder().build();
        assertTrue(new IsLifecycleGet().visit(request));
    }

    @Test
    public void testLifecycleGet() {
        setup();
        final var get = ImmutableLifecycleGet.builder().build();
        final var json = gson.toJson(get);
        /*
         * When raw string literals come around
         * http://openjdk.java.net/jeps/326
         * these should all be switched to raw
         */
        assertEquals("{\"isLifecycleGet\":true}", json);
    }

    @Test
    public void testLifecycleKill() {
        setup();
        final var kill = ImmutableLifecycleKill.builder().build();
        final var json = gson.toJson(kill);
        assertEquals("{\"isLifecycleKill\":true}", json);
    }

    @Test
    public void testLifecycleStart() {
        setup();
        final var start = ImmutableLifecycleStart.builder().build();
        final var json = gson.toJson(start);
        assertEquals("{\"isLifecycleStart\":true}", json);
    }

    @Test
    public void testSourceClose() {
        setup();
        final var close = ImmutableSourceClose.builder()
                          .id((byte) 0)
                          .build();
        final var json = gson.toJson(close);
        assertEquals("{\"isSourceClose\":true,\"id\":0}", json);
    }

    @Test
    public void testSourceList() {
        setup();
        final var list = ImmutableSourceList.builder().build();
        final var json = gson.toJson(list);
        assertEquals("{\"isSourceList\":true}", json);
    }

    @Test
    public void testDeserialize() {
        setup();
        final var name = "dummyname";
        final var open = ImmutableSourceOpen.builder()
                         .name(name)
                         .port(PORT)
                         .build();
        final var requests = ImmutableRequests.builder()
                             .request(open);
        final var json = gson.toJson(requests);
        /*
         * If this fails, then there is a problem with the deserialization
         */
        final var deserialized = gson.fromJson(json, Requests.class);
        throw new RuntimeException(deserialized.toString());
        // throw new RuntimeException(gson.toJson(deserialized));
        // /*
        //  * If this fails, then there is a problem with the type
        //  * it is deserialized into. Specifically, if it throws
        //  * NoSuchElementException, then it is not being deserialized
        //  * into one of the Requests.
        //  */
        // final var deserializedObj = deserialized.request().get();
        // /*
        //  * If this fails, then it is not being deserialized into
        //  * a SourceOpen request as it should be
        //  */
        // final var deserializedCast = (SourceOpen) deserializedObj;
    }

    @Test
    public void testSourceOpen() {
        setup();
        final var name = "Test stream name";
        final var open = ImmutableSourceOpen.builder()
                         .name(name)
                         .port(PORT)
                         .build();
        assertEquals(open.name(), name);
        final var json = gson.toJson(open);
        assertEquals(String.format("{\"isSourceOpen\":true,\"name\":\"%s\",\"port\":%d}",
                                   name, PORT),
                     json);
    }

}
