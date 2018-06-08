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
Here's where it gets complicated. There's a special container,
`com.ncsurobotics.srvrequests.OptionalRequests`, that contains
zero to one deserialized requests. You must check each of
the feilds of `OptionalRequests` to see if you got
any of the requests.
(I am not happy with this API, and if there is a better
way to dispatch on subtypes without using a visitor whose api may change,
I would rather do that.)

```Java
```

### Mapper option

and, optionally, a `com.fasterxml.jackson.databind.ObjectMapper`.
There should only be one `com.fasterxml.jackson.databind.ObjectMapper`
instance per program; if you want to reuse an ObjectMapper,

TODO
