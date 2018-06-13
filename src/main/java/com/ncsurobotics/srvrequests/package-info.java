/**
 * This package contains definitions for
 * requests that control SRV.
 * This library uses Immutables and GSON
 * for creating value objects and serializing those objects
 * respectivley.
 * To create one of these requests, do something like
 * <pre>
 * {@code
 * final SourceOpen request = ImmutableSourceOpen.builder()
 *                            .name("sourcename")
 *                            .port(1000)
 *                            .build();
 * }
 * </pre>
 * And then to send it, use
 * <pre>
 * {@code
 * RequestSender sender = RequestSender.builder()
 *                       .socket(outputSocket);
 *                       .build();
 * requestSender.send(request);
 * }
 * </pre>
 * Then, on the other end, one receives the request
 * <pre>
 * {@code
 * RequestReceiver receiver = RequestReceiver.builder()
 *                            .socket(inputSocket)
 *                            .build();
 * SrvRequest request = receiver.receive()
 * }
 * </pre>
 * Finally, to dispatch on the request, one can
 * use an org.multij.Module multimethod
 * <pre>
 * {@code
 * @org.multij.Module
 * public interface Tester {
 *     default boolean isSourceOpen(SourceOpen request) {
 *         return true;
 *     }
 *     default boolean isSourceOpen(SrvRequest request) {
 *         return false;
 *     }
 * }
 * Tester tester = org.multij.MultiJ.instance(Tester.class);
 * tester.isSourceOpen(request); // should return true
 * </pre>
 */
package com.ncsurobotics.srvrequests;
