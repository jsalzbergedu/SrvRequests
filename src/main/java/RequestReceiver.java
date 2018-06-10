package com.ncsurobotics.srvrequests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handles the receiving of requests.
 * Should be a Singleton.
 * @author Jacob Salzberg
 */
public final class RequestReceiver {
    /**
     * The buffered reader from which to read
     * the input.
     */
    private final BufferedReader reader;

    /**
     * The object mapper that maps between json and types.
     */
    private final ObjectMapper mapper;

    /**
     * Construct a new reqeuest receiver using a BufferedReader.
     * @param reader the reader from which to read the requests.
     * @param mapper the ObjectMapper that maps between strings and JSON.
     */
    private RequestReceiver(BufferedReader reader, ObjectMapper mapper) {
        this.reader = reader;
        this.mapper = mapper;
    }

    /**
     * The stage of the builder when
     * an input source has not yet been supplied.
     * @author Jacob Salzberg
     */
    public static class BuilderInputStage {
        /**
         * Add a buffered reader.
         * @param reader an object that strings are read from.
         * @return After calling this method,
         * these feilds have not yet been supplied:
         * an optional ObjectMapper.
         */
        public BuilderMapperStage reader(BufferedReader reader) {
            if (reader == null) {
                throw new IllegalArgumentException("Reader argument is null.");
            }

            return new BuilderMapperStage(reader);
        }

        /**
         * Add an unbuffered reader.
         * @param reader the object that strings are read from.
         * @return After calling this method,
         * these feilds have not yet been supplied:
         * an optional ObjectMapper.
         */
        public BuilderMapperStage reader(Reader reader) {
            if (reader == null) {
                throw new IllegalArgumentException("Reader argument is null.");
            }

            return reader(new BufferedReader(reader));
        }

        /**
         * Add an InputStream.
         * @param stream the input stream that strings are read from.
         * @return After calling this method,
         * these feilds have not yet been supplied:
         * an optional ObjectMapper.
         */
        public BuilderMapperStage stream(InputStream stream) {
            if (stream == null) {
                throw new IllegalArgumentException("Stream argument is null.");
            }

            // Will dispatch on reader(Reader), not reader(BufferedReader).
            return reader(new InputStreamReader(stream));
        }

        /**
         * Add a Socket.
         * @param socket the socket that strings are read from.
         * @return After calling this method,
         * these feilds have not yet been supplied:
         * an optional ObjectMapper.
         * @throws IOException if getInputStream fails.
         */
        public BuilderMapperStage socket(Socket socket) throws IOException {
            if (socket == null) {
                throw new IllegalArgumentException("Socket argument is null.");
            }

            return stream(socket.getInputStream());
        }
    }

    /**
     * Get the builder.
     * @return After calling this method, these fields
     * have not yet been supplied:
     * a source of input, and an optional ObjectMapper.
     */
    public static BuilderInputStage builder() {
        return new BuilderInputStage();
    }

    /**
     * The stage of the builder
     * when the input source has been supplied.
     * @author Jacob Salzberg
     */
    public static class BuilderMapperStage {
        /**
         * The optional ObjectMapper.
         */
        private Optional<ObjectMapper> mapper = Optional.empty();

        /**
         * The BufferedReader object.
         */
        private BufferedReader reader;

        /**
         * Create a BuilderMapperStage object.
         * @param reader the BufferedReader object.
         */
        public BuilderMapperStage(BufferedReader reader) {
            this.reader = reader;
        }

        /**
         * Add a mapper.
         * @param mapper the ObjectMapper to add.
         * @return After calling this method,
         * all feilds have been supplied. You can
         * .build() the object.
         */
        public BuilderMapperStage mapper(ObjectMapper mapper) {
            this.mapper = Optional.of(mapper);
            return this;
        }

        /**
         * Build the object.
         * @return a RequestReceiver object.
         */
        public RequestReceiver build() {
            return new RequestReceiver(reader,
                                       mapper.orElseGet(() -> {
                                               return new ObjectMapper();
                                           }));
        }
    }

    /**
     * Receive a request.
     * Map null values read from the input
     * source into empty requests.
     * @return a SrvRequest.
     * @throws IOException if deserializing from JSON fails,
     * or if reading from the input source fails.
     */
    public SrvRequest receive() throws IOException {
        final String json = reader.readLine();
        if (json == null) {
            return SrvRequest.empty();
        }
        final SrvRequest request = mapper.readValue(json, Requests.class)
                                  .request();
        return request;
    }
}
