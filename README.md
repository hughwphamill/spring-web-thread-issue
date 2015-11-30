# spring-web-thread-issue
Reproduction of issue where attempting to build a HATEOAS link as part of a .parallelStream() call will result in an IllegalStateException

## Error Output
java.lang.IllegalStateException: Could not find current request via RequestContextHolder. Is this being called from a Spring MVC handler?

## Cause

[ControllerLinkBuilder](https://github.com/spring-projects/spring-hateoas/blob/master/src/main/java/org/springframework/hateoas/mvc/ControllerLinkBuilder.java) in spring-hateoas uses [RequestContextHolder](https://github.com/spring-projects/spring-framework/blob/master/spring-web/src/main/java/org/springframework/web/context/request/RequestContextHolder.java) from spring-web to retrieve the current request attributes, however because they are Thread Local they will not be available to the Threads spawned by parallelString() causing the above Exception.

## Workaround

Use .stream() instead of .parallelStream()
