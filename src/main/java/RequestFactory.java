package com.ncsurobotics.srvrequests;

/**
 * The RequestFactory can be used to
 * safely move a SrvRequest into a more
 * specific subtype, and then to put it into an
 * OptionalRequests object,
 * which can be queried for specific requests.
 * While RequestFactory isn't a proper factory,
 * it does handle the creation of OptionalRequests,
 * so its name isn't too far afield.
 * @author Jacob Salzberg
 */
public final class RequestFactory {
    /**
     * The optionalRequests object to be created
     * in the constructor.
     */
    private final OptionalRequests optionalRequests;

    /**
     * Construct the RequestFactory object,
     * moving the SrvRequest into a more
     * specific subtype.
     * @param request the SrvRequest to be moved
     */
    private RequestFactory(SrvRequest request) {
        optionalRequests = request.accept(
            new RequestVisitor<OptionalRequests>() {
            @Override
            public OptionalRequests visit(SrvRequest r) {
                return ImmutableOptionalRequests.builder().build();
            }

            @Override
            public OptionalRequests visit(LifecycleGet r) {
                return ImmutableOptionalRequests.builder()
                        .lifecycleGet(r)
                        .build();
            }

            @Override
            public OptionalRequests visit(LifecycleKill r) {
                return ImmutableOptionalRequests.builder()
                        .lifecycleKill(r)
                        .build();
            }

            @Override
            public OptionalRequests visit(LifecycleStart r) {
                return ImmutableOptionalRequests.builder()
                        .lifecycleStart(r)
                        .build();
            }

            @Override
            public OptionalRequests visit(SourceList r) {
                return ImmutableOptionalRequests.builder()
                        .sourceList(r)
                        .build();
            }

            @Override
            public OptionalRequests visit(SourceOpen r) {
                return ImmutableOptionalRequests.builder()
                        .sourceOpen(r)
                        .build();
            }

            @Override
            public OptionalRequests visit(SourceClose r) {
                return ImmutableOptionalRequests.builder()
                        .sourceClose(r)
                        .build();
            }
        });
    }

    /**
     * Get the OptionalRequests object,
     * which can be queried for the request.
     * @return an OptionalRequests instance.
     */
    private OptionalRequests getOptionalRequests() {
        return optionalRequests;
    }

    /**
     * Create the OptionalRequests object.
     * Up to one of the fields of the object
     * will contain a request of a specific type.
     * @param request the request to add to the
     * OptionalRequests object.
     * @return an OptionalRequests object.
     */
    public static OptionalRequests optionalRequests(SrvRequest request) {
        return new RequestFactory(request).getOptionalRequests();
    }

    /**
     * Return an OptionalRequests object,
     * no requests in all fields.
     * @return an empty OptionalRequests object
     */
    public static OptionalRequests empty() {
        final SrvRequest none = new SrvRequest() { };
        return new RequestFactory(none)
                .getOptionalRequests();
    }

}
