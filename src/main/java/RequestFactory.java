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
    public RequestFactory(final SrvRequest request) {
        optionalRequests = request.accept(
            new RequestVisitor<OptionalRequests>() {
            @Override
            public OptionalRequests visit(final SrvRequest r) {
                return ImmutableOptionalRequests.builder().build();
            }

            @Override
            public OptionalRequests visit(final LifecycleGet r) {
                return ImmutableOptionalRequests.builder()
                        .lifecycleGet(r)
                        .build();
            }

            @Override
            public OptionalRequests visit(final LifecycleKill r) {
                return ImmutableOptionalRequests.builder()
                        .lifecycleKill(r)
                        .build();
            }

            @Override
            public OptionalRequests visit(final LifecycleStart r) {
                return ImmutableOptionalRequests.builder()
                        .lifecycleStart(r)
                        .build();
            }

            @Override
            public OptionalRequests visit(final SourceList r) {
                return ImmutableOptionalRequests.builder()
                        .sourceList(r)
                        .build();
            }

            @Override
            public OptionalRequests visit(final SourceOpen r) {
                return ImmutableOptionalRequests.builder()
                        .sourceOpen(r)
                        .build();
            }

            @Override
            public OptionalRequests visit(final SourceClose r) {
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
    OptionalRequests optionalRequests() {
        return optionalRequests;
    }
}
