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
     * @return an OptionalRequests object.
     */
    public OptionalRequests receive() {
        return Optional.empty()
                // s here is actually useless
                .flatMap((s) -> {
                        try {
                            return Optional.ofNullable(reader.readLine());
                        } catch (IOException e) {
                            return Optional.empty();
                        }
                    })
                .flatMap((json) -> {
                        try {
                            var req = mapper.readValue(json, Requests.class);
                            return Optional.of(req);
                        } catch (IOException e) {
                            return Optional.empty();
                        }
                    })
                .map((request) -> {
                        return request.request();
                    })
                .map(RequestFactory::optionalRequests)
                .orElseGet(RequestFactory::empty);
    }
}
