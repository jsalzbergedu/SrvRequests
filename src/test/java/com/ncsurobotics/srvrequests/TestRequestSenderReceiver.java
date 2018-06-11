package com.ncsurobotics.srvrequests;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

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
    private FileInputStream input;

    /**
     * Mock output stream.
     */
    private FileOutputStream output;

    /**
     * A temporary file to write in.
     */
    private Path tempFile;

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
        Path parent = Files.createTempDirectory("RequestSenderReceiver");
        Path child = parent.resolve("temp.json");
        Files.deleteIfExists(child);
        tempFile = Files.createFile(child);

        if (mapper == null) {
            mapper = new ObjectMapper();
        }

        if (input == null) {
            input = new FileInputStream(tempFile.toFile());
        }

        if (output == null) {
            output = new FileOutputStream(tempFile.toFile());
        }

        if (receiver == null) {
            receiver = RequestReceiver.builder().stream(input).mapper(mapper).build();
        }

        if (sender == null) {
            sender = RequestSender.builder().stream(output).mapper(mapper).build();
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
        sender.send(request);
        writer.close();
        SrvRequest received = receiver.receive();
        reader.close();
        Dispatch dispatch = MultiJ.instance(Dispatch.class);
        dispatch.dispatch(received);
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
