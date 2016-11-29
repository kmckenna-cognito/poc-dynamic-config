POC: Dynamic Spring Configuration
=================================

Springboot provides built-in features to allow you to reload configuration from a Spring Cloud Config server. For example, if you have configured the logging level in your `application.yml` file, e.g.
```yml
logging.level.com.kelseymckenna: DEBUG
```
you can change this in a git-tracked repository, then your Spring Cloud Config server will register the change, and Springboot applications can synchronise with this change.

To allow your Springboot application to reload configuration for beans, you must annotate beans/configuration classes with `@RefreshScope`. If you annotate the primary application class, then all configuration can be reloaded. Here is an example application class (from the `example-service` project):

```java
@SpringBootApplication
@RefreshScope
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);

        while (true) {
            log.trace("Trace");
            log.debug("Debug");
            log.info("Info");
            log.warn("Warn");

            Thread.sleep(3000);
            System.out.println();
        }
    }
}
```

**Note**: imports have been omitted for brevity, and the logging framework is SLF4J. Since this class has been annotated with the `@RefreshScope` annotation, any changes made to your spring configuration can be refreshed.

Exercise
--------
1. Execute `git init && git add -A && git commit -m "Initial commit"` inside the `cloud-config` folder.
2. Complete the `application.properties` file in `config-server` to point to the `cloud-config` folder, e.g. `spring.cloud.config.server.git.uri=/home/poc-dynamic-config/cloud-config`.
3. Launch the `config-service`.
4. Launch the `example-service`.
5. You should see the `example-service` logging "Info" and "Warn", because the log level is `INFO`.
6. Change the logging level to `DEBUG` in `cloud-config/application.properties` and run `git commit -a -m "Changed log level"`.
7. *POST* to the `refresh` endpoint on the `example-service`, i.e. `curl -X POST localhost:8080/refresh`.
8. The `example-service` should start logging "Debug", "Info", and "Warn".
9. Experiment with setting the log level.

Magic
-----
It is possible to set the log level for a Springboot application by posting to the `env` endpoint, e.g. `curl -X POST localhost:8081/env -d logging.level.com=DEBUG`, but this does not always work. This may be a permissions issue, but nevertheless, it is not a guaranteed solution in all cases.
