/**
 * Requests that control SRV.
 * This library uses Immutables and GSON
 * for creating value objects and serializing those objects
 * respectivley.
 * To create one of these requests, do something like
 * <pre>
 * {@code
 * final var request = ImmutableRequestName.builder()
 *                     .field1(value1)
 *                     .field2(value2)
 *                     .field3(value3)
 *                     .build();
 * }
 * </pre>
 * And then to send it, use
 * <pre>
 * {@code
 * var requestSender = new RequestSender.builder()
 *                         .address(string)
 *                         .port(number)
 *                         .build();
 * requestSender.send(request);
 * }
 * </pre>
 * Finally, to dispatch on the request, one can do something like
 * <pre>
 * {@code
 * class IsLifecycleGet {
 *     public boolean visit(Request r) {
 *         // General case, so return false
 *         return false;
 *     }
 *
 *     public boolean visit(LifecycleGet r) {
 *         // In this case, it is LifecycleGet, so return true
 *         return true;
 *     }
 * }
 * var isLifecycleGet = IsLifecycleGet.visit(requst);
 * }
 * </pre>
 */
package com.ncsurobotics.srvrequests;
