# That's Interesting

*That's interesting* is a strongly typed logging framework targeting applications that want to aggregate their log messages into a single system, e.g. a [Elasticsearch](https://www.elastic.co/) cluster.

## Usage

The library works by defining logger interfaces (called "point of interest") such as: 

```java
package your.own.pkg;

public interface YourOwnPOI {

    void somethingInteresting(YourPOJO pojo);

    void businessWarning(YourCustomer pojo);

}
```

You use that interface together with the `Interested` class like this to create an instance at runtime:

[source, java]
----
import wtf.metio.ti.Interested;

YourOwnPOI poi = Interested.in(YourOwnPOI.class)
                .invocationHandler(systemOut())
                .createLogger();
----

Finally, use the created instance at runtime to log something that is of interest to your application:

[source, java]
----
poi.somethingInteresting(pojo);
----

With its current default settings the above call will result in something like this on `System.out`:

[source]
----
Class: [your.own.package.YourOwnPOI] Method: [somethingInteresting] Arguments: [pojo: {field1: value1, field2: value2}]
----

You probably don't want to write to `System.out` but log into a file on your filesystem like this:

[source]
----
import static de.xn__ho_hia.interesting.handler.StandardInvocationHandlers.logFile;

import de.xn__ho_hia.interesting.Interested;
import java.nio.file.Paths;

YourOwnPOI poi = Interested.in(YourOwnPOI.class)
                .invocationHandler(logFile(Paths.get("target/file.log")))
                .createLogger();
----

You can customize the outgoing message like this:

[source]
----
import static de.xn__ho_hia.interesting.converter.StandardConverters.stringFormat;
import static de.xn__ho_hia.interesting.sink.StandardSinks.logFile;

import de.xn__ho_hia.interesting.Interested;
import java.nio.file.Paths;

YourOwnPOI poi = Interested.in(YourOwnPOI.class)
                .buildHandler()
                .converter(stringFormat("%s is the class, %s is the method, %s are the args"))
                .sinks(fileAppender(Paths.get("target/file.log")))
                .createLogger();
----

However that is still no fun since you probably want to aggregate all your logs from all your applications in a central place like an Elasticsearch cluster or your next best Kafka broker. Users of the ELK stack can ignore Logstash and push directly into ES like this:

[source, java]
----
import static de.xn__ho_hia.interesting.handler.ElasticsearchInvocationHandlers.elasticsearch;

import de.xn__ho_hia.interesting.Interested;

YourOwnPOI poi = Interested.in(YourOwnPOI.class)
                .invocationHandler(elasticsearch("localhost:9300", "indexName"))
                .createLogger();
----

The above configuration sends objects similar to the following JSON:

[source, json]
----
{
  "class": "your.own.package.YourOwnPOI",
  "method": "somethingInteresting",
  "arguments": {
    "pojo": {
      "field1": "value1",
      "field2": "value2"
    }
  }
}
----

If you want to add additional key-value pairs, do the following:

[source, java]
----
import static de.xn__ho_hia.interesting.converter.JacksonConverters.json;
import static de.xn__ho_hia.interesting.sink.ElasticsearchSinks.elasticsearch;

import de.xn__ho_hia.interesting.Interested;
import java.time.LocalDateTime;

YourOwnPOI poi = Interested.in(YourOwnPOI.class)
                .buildHandler()
                .converter(json())
                .sinks(elasticsearch("localhost:9300", "indexName"))
                .withStaticExtra("application", "yourCoolApp")
                .withStaticExtra("hostname", "your.custom.domain.tld")
                .withSuppliedExtra("timestamp", () -> LocalDateTime.now())
                .createLogger();
----

Which then produces:

[source, json]
----
{
  "class": "your.own.package.YourOwnPOI",
  "method": "somethingInteresting",
  "arguments": {
    "pojo": {
      "field1": "value1",
      "field2": "value2"
    }
  },
  "application": "yourCoolApp",
  "hostname": "your.custom.domain.tld",
  "timestamp": "your-local-date-time"
}
----

== Tips & Tricks

Configuring your points of interest outside of the classes that are using those has the advantage that none of the using classes have a compile time dependency on any classes in this library.

Manage your points of interest in their own source module. Configure your Kibana dashboard according to the interfaces or vice versa.

== License

To the extent possible under law, the author(s) have dedicated all copyright
and related and neighboring rights to this software to the public domain
worldwide. This software is distributed without any warranty.

You should have received a copy of the CC0 Public Domain Dedication along
with this software. If not, see http://creativecommons.org/publicdomain/zero/1.0/.
