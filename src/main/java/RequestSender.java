package com.ncsurobotics.srvrequests;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handles the sending of requests.
 * Should be a Singleton.
 * @author Jacob Salzberg
 */
public final class RequestSender {
    /**
     * The object with which to write JSON.
     */
    private final PrintWriter printWriter;

    /**
     * The object mapper that maps between json and types.
     */
    private final ObjectMapper mapper;


    /**
     * Construct the RequestSender with a PrintWriter.
     * @param printWriter the writer with which to write the JSON.
     * @param mapper maps between json and Reqeusts
     */
    private RequestSender(PrintWriter printWriter, ObjectMapper mapper) {
        this.printWriter = printWriter;
        this.mapper = mapper;
    }

    /**
     * A builder, at the point when some
     * sort of output should be supplied.
     * @author Jacob Salzberg
     */
    public static class BuilderOutputStage {
        /**
         * Add a PrintWriter.
         * @param writer a print writer to write to.
         * @return After calling this method,
         * these fields have not yet been supplied:
         * an optional ObjectMapper.
         */
        public BuilderMapperStage writer(PrintWriter writer) {
            return new BuilderMapperStage(writer);
        }

        /**
         * Add an OutputStream.
         * @param stream an output stream to read from.
         * @return After calling this method,
         * these fields have not yet been supplied:
         * an optional ObjectMapper.
         */
        public BuilderMapperStage stream(OutputStream stream) {
            return writer(new PrintWriter(stream, true));
        }

        /**
         * Add a Socket.
         * @param socket the socket to read from.
         * @return After calling this method,
         * these fields have not yet been supplied:
         * an optional ObjectMapper.
         * @throws IOException if getOutputStream fails.
         */
        public BuilderMapperStage socket(Socket socket) throws IOException {
            return stream(socket.getOutputStream());
        }
    }

    /**
     * A builder, at the point where an ObjectMapper may be supplied, or
     * the RequestSender may be built.
     * @author Jacob Salzberg
     */
    public static class BuilderMapperStage {
        /**
         * The PrintWriter supplied.
         */
        private PrintWriter writer;

        /**
         * The optional ObjectMapper.
         */
        private Optional<ObjectMapper> mapper = Optional.empty();

        /**
         * Construct the builder at the mapper stage.
         * @param printWriter the required printWriter.
         */
        public BuilderMapperStage(PrintWriter printWriter) {
            this.writer = writer;
        }

        /**
         * Add an ObjectMapper.
         * @param mapper the ObjectMapper with which to map
         * from Requests to JSON.
         * @return after this funciton, all of the fields have been supplied.
         * You may .build() the object.
         */
        public BuilderMapperStage mapper(ObjectMapper mapper) {
            this.mapper = Optional.of(mapper);
            return this;
        }

        /**
         * Build the object.
         * @return the RequestSender.
         */
        public RequestSender build() {
            return new RequestSender(writer,
                                     mapper.orElseGet(() -> {
                                             return new ObjectMapper();
                                         }));
        }
    }

    /**
     * Build the RequestSender.
     * @return at this point, neither the output
     * nor the optional ObjectMapper has been supplied.
     */
    public static BuilderOutputStage builder() {
        return new BuilderOutputStage();
    }


    /**
     * Send a request.
     * @param request the request to send.
     * @throws IOException if printing to the stream fails
     */
    public void send(SrvRequest request) throws IOException {
        final Requests outgoing = ImmutableRequests.builder()
                                  .request(request)
                                  .build();
        final String json = mapper.writeValueAsString(outgoing);
        printWriter.println(json);
        printWriter.flush();
    }

}
