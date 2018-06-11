package com.ncsurobotics.srvrequests;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.multij.Module;
import org.multij.MultiJ;

import junit.framework.TestCase;

/**
 * Tests for RequestSender and RequestReceiver.
 * @author Jacob Salzberg
 */
public class TestRequestSenderReceiver extends TestCase {
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
     * An object mapper to deserialize objects with.
     */
    private ObjectMapper mapper;

    /**
     * For writing to the mock output stream.
     */
    private PrintWriter writer;

    /**
     * For reading from the mock input stream.
     */
    private BufferedReader reader;

    /**
     * Setup the io objects.
     * TODO get the @Before annotation to work
     */
    public void setup() throws IOException {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        if (input == null) {
            input = new PipedInputStream();
        }

        if (output == null) {
            output = new PipedOutputStream(input);
        }

        if (receiver == null) {
            receiver = RequestReceiver.builder()
                       .stream(input)
                       .mapper(mapper)
                       .build();
        }

        if (sender == null) {
            sender = RequestSender.builder()
                     .stream(output)
                     .mapper(mapper)
                     .build();
        }

        if (writer == null) {
            writer = new PrintWriter(output, true);
        }

        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(input));
        }

    }

    @Test
    public void testSenderReceiver() throws IOException {
        setup();
        LifecycleGet request = ImmutableLifecycleGet.builder()
                               .build();
        Thread send = new Thread(() -> {
                writer.println("begin");
                writer.flush();
                try {
                    sender.send(request);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
                writer.close();
        });

        Thread receive = new Thread(() -> {
            try {
                while (reader.readLine() == null) {
                    // Do nothing
                    // This loop will break when "First line" is received
                }
                SrvRequest received = receiver.receive();
                reader.close();
                Dispatch dispatch = MultiJ.instance(Dispatch.class);
                dispatch.dispatch(received);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
        send.start();
        receive.start();

    }

    /**
     * Dispatch on the received request in
     * testSenderReceiver.
     * @author Jacob Salzberg
     */
    @Module
    public interface Dispatch {
        default void dispatch(LifecycleGet request) {
            // The correct type was dispatched.
            // Nothing has to be done.
        }

        default void dispatch(SrvRequest request) {
            throw new RuntimeException("Incorrect type dispatched.");
        }
    }
}
