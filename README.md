# SrvRequests

A library providing a set of requests, as well as tools to
send and receive them.

## Adding to your program

As of now, to add SrvRequests to your program, you must first install this
library to your local repository using gradle's `publishToMavenLocal` task.

```bash
gradle publishToMavenLocal
```

Then it can be added to a `build.gradle` as

```groovy
dependencies {
    // ,,,
    compile 'com.ncsurobotics.srvrequests:0.2.9'
}
```

## Sending and Receiving Requests

### Sending a Request

The api for sending a request is in
`com,ncsurobotics.srvrequests.RequestSender`.
To send a request, you must first create a RequestSender.
A RequestSender requires some sort of output,
which will generally be a `java.net.Socket`.
To create a RequestSender with a socket,
create the socket and supply it to the RequestSender.
(There are some [other options](), but they are mostly irrelivant)

```Java
final String address = "127.0.0.1";
final int port = 5000;
Socket socket = new Socket(address, port);
final RequestSender sender = RequestSender.builder()
                             .socket(socket)
                             .build();
```

Then, you must have some sort of request to send.
Each request is an [Immutables](https://immutables.github.io/)
value object and descends from the parent type
`com.ncsurobotics.srvrequests.SrvRequest`.
For a list and for descriptions of the requests,
look at the design document for [SRV](https://github.com/jsalzbergedu/srv).

Here is an example of a request being created:
```Java
final String name = "name";
final int openPort = 4005;
final SourceOpen open = ImmutableSourceOpen.builder()
                        .name(name)
                        .port(openPort)
                        .build();
```

Then, the request can be sent.

```Java
sender.send(open);
```


### Receiving a request

Receiving requests is slightly more complicated than sending them,
but the pattern is similar. First create a
`com.ncsurobotics.srvrequests.RequestReceiver` on a port:

```Java
final int port = 5000;
ServerSocket serverSocket = new ServerSocket(port);
Socket socket = serverSocket.accept();
RequestReceiver receiver = RequestReceiver.builder()
                           .socket(socket)
                           .build();
```
Here's where it gets complicated.
To dispatch on the subtype of the request,
one can either use the `com.ncsurobotics.srvrequests.RequestVisitor`
interface, which is not only an unstable api but infact gaurenteed to break,
or instead use [multij](http://www.multij.org/) to dispatch
on the run time type of the object.
Here's an example of the two strategies:


#### RequestVisitor
The RequestVisitor API is bound to change. If and when MultiJ gets to version
1.0, the RequestVisitor will be deprecated.

```Java
// Check if a request is a LifecycleGet request
RequestVisitor visitor = new RequestVisitor<Boolean> {
    Boolean visit(SrvRequest r) {
        return false;
    }

    Boolean visit(LifecycleGet r) {
        return true;
    }

    Boolean visit(LifecycleKill r) {
        return false;
    }

    Boolean visit(LifecycleStart r) {
        return false;
    }

    Boolean visit(SourceList r) {
        return false;
    }

    Boolean visit(SourceList r) {
        return false;
    }

    Boolean visit(SourceOpen r) {
        return false;
    }

    Boolean visit(SourceClose r) {
        return false;
    }
}
boolean isLifecycleGet = request.accept(visitor);
```

#### MultiJ
This is the more elegant and therefore perferred way to do it.

```Java
@org.multij.Module
public interface LifecycleGetTester {
    default boolean test(LifecycleGet r) {
        return true;
    }

    default boolean test(SrvRequest r) {
        return false;
    }
}

LifecycleGetTester tester =
    org.multij.MultiJ.instance(LifecycleGetTester.class);

boolean isLifecycleGet = tester.test(request); // returns true
```

### Mapper option

Since there should only be one `com.fasterxml.jackson.databind.ObjectMapper`.
per program, `RequestSender` and `RequestReceiver` both provide a
`.mapper` option on their builder after `.socket`. If you have both a
`RequestSender` and a `RequestReceiver` you should use this option, otherwise,
you should ignore it.
