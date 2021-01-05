# Multiple Datasources using R2DBC does not work

```yml
r2dbc:
  datasources:
    dataSourceOne:
      url: r2dbc:h2:mem:///dbOne
      username: sa
      password: ''
      schema-generate: CREATE_DROP
      dialect: H2
    dataSourceTwo:
      url: r2dbc:h2:mem:///dbTwo
      username: sa
      password: ''
      schema-generate: CREATE_DROP
      dialect: H2
```

```Kotlin
@Repository(value = "dataSourceOne")
@R2dbcRepository(dialect = Dialect.MYSQL)
interface AuthorRepository : ReactiveStreamsCrudRepository<Author, Long> {
    override fun findById(id: @NotNull Long): Mono<Author>
    override fun findAll(): Flux<Author>
}
```

```Kotlin
@Introspected
@MappedEntity
data class Author(val name: String) {
    @GeneratedValue
    @Id
    var id: Long? = null
}
```

## Run It

```bash
./gradlew run
```

```bash
curl -X POST "http://localhost:4800/authors" \
           -H "Accept: application/json" \
           -H "Content-Type: application/json" \
           -d '{"name":"sara.smith"}'
```

## Log

```
> Task :kaptKotlin
Note: Creating bean classes for 3 type elements
> Task :run
11:24:33.807 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Starting...
11:24:34.033 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Start completed.
11:24:34.041 [main] INFO  i.m.flyway.AbstractFlywayMigration - Running migrations for database with qualifier [data-source-one]
11:24:34.044 [main] INFO  o.f.c.i.license.VersionPrinter - Flyway Community Edition 7.0.4 by Redgate
11:24:34.083 [main] INFO  o.f.c.i.database.base.DatabaseType - Database: jdbc:h2:mem:dbOne (H2 1.4)
11:24:34.143 [main] INFO  o.f.core.internal.command.DbValidate - Successfully validated 1 migration (execution time 00:00.025s)
11:24:34.158 [main] INFO  o.f.c.i.s.JdbcTableSchemaHistory - Creating Schema History table "PUBLIC"."flyway_schema_history" ...
11:24:34.225 [main] INFO  o.f.core.internal.command.DbMigrate - Current version of schema "PUBLIC": << Empty Schema >>
11:24:34.229 [main] INFO  o.f.core.internal.command.DbMigrate - Migrating schema "PUBLIC" to version "1 - create-schema"
11:24:34.298 [main] INFO  o.f.core.internal.command.DbMigrate - Successfully applied 1 migration to schema "PUBLIC" (execution time 00:00.082s)run
11:24:34.384 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-2 - Starting...
11:24:34.387 [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-2 - Start completed.
11:24:34.387 [main] INFO  i.m.flyway.AbstractFlywayMigration - Running migrations for database with qualifier [data-source-two]
11:24:34.388 [main] INFO  o.f.c.i.license.VersionPrinter - Flyway Community Edition 7.0.4 by Redgate
11:24:34.389 [main] INFO  o.f.c.i.database.base.DatabaseType - Database: jdbc:h2:mem:dbTwo (H2 1.4)
11:24:34.399 [main] INFO  o.f.core.internal.command.DbValidate - Successfully validated 1 migration (execution time 00:00.006s)
11:24:34.406 [main] INFO  o.f.c.i.s.JdbcTableSchemaHistory - Creating Schema History table "PUBLIC"."flyway_schema_history" ...
11:24:34.422 [main] INFO  o.f.core.internal.command.DbMigrate - Current version of schema "PUBLIC": << Empty Schema >>
11:24:34.424 [main] INFO  o.f.core.internal.command.DbMigrate - Migrating schema "PUBLIC" to version "1 - create-schema"
11:24:34.450 [main] INFO  o.f.core.internal.command.DbMigrate - Successfully applied 1 migration to schema "PUBLIC" (execution time 00:00.032s)DLE
11:24:35.217 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 2515ms. Server Running: http://localhost:4800
11:24:41.725 [default-nioEventLoopGroup-1-2] ERROR i.m.h.s.netty.RoutingInBoundHandler - Unexpected error occurred: No backing RepositoryOperations configured for repository. Check your configuration and try again
io.micronaut.context.exceptions.ConfigurationException: No backing RepositoryOperations configured for repository. Check your configuration and try again
        at io.micronaut.data.intercept.DataIntroductionAdvice.findInterceptor(DataIntroductionAdvice.java:124)
        at io.micronaut.data.intercept.DataIntroductionAdvice.intercept(DataIntroductionAdvice.java:78)
        at io.micronaut.aop.chain.MethodInterceptorChain.proceed(MethodInterceptorChain.java:82)
        at io.micronaut.validation.ValidatingInterceptor.intercept(ValidatingInterceptor.java:137)
        at io.micronaut.aop.chain.MethodInterceptorChain.proceed(MethodInterceptorChain.java:82)
        at micronaut.r2dbc.multiple.datasources.AuthorRepository$Intercepted.save(Unknown Source)
        at micronaut.r2dbc.multiple.datasources.AuthorController.create(AuthorController.kt:16)
        at micronaut.r2dbc.multiple.datasources.$AuthorControllerDefinition$$exec1.invokeInternal(Unknown Source)
        at io.micronaut.context.AbstractExecutableMethod.invoke(AbstractExecutableMethod.java:146)
        at io.micronaut.context.DefaultBeanContext$4.invoke(DefaultBeanContext.java:474)
        at io.micronaut.web.router.AbstractRouteMatch.execute(AbstractRouteMatch.java:312)
        at io.micronaut.web.router.RouteMatch.execute(RouteMatch.java:118)
        at io.micronaut.http.server.netty.RoutingInBoundHandler.lambda$buildResultEmitter$10(RoutingInBoundHandler.java:1369)
        at io.reactivex.internal.operators.flowable.FlowableDefer.subscribeActual(FlowableDefer.java:35)
        at io.reactivex.Flowable.subscribe(Flowable.java:14935)
        at io.reactivex.Flowable.subscribe(Flowable.java:14882)
        at io.micronaut.http.server.context.ServerRequestContextFilter.lambda$doFilter$0(ServerRequestContextFilter.java:62)
        at io.reactivex.internal.operators.flowable.FlowableFromPublisher.subscribeActual(FlowableFromPublisher.java:29)
        at io.reactivex.Flowable.subscribe(Flowable.java:14935)
        at io.reactivex.Flowable.subscribe(Flowable.java:14885)
        at io.micronaut.http.server.netty.RoutingInBoundHandler.lambda$buildExecutableRoute$6(RoutingInBoundHandler.java:1074)
        at io.micronaut.web.router.DefaultUriRouteMatch$1.execute(DefaultUriRouteMatch.java:80)
        at io.micronaut.web.router.RouteMatch.execute(RouteMatch.java:118)
        at io.micronaut.http.server.netty.RoutingInBoundHandler$2.doOnComplete(RoutingInBoundHandler.java:1000)
        at io.micronaut.core.async.subscriber.CompletionAwareSubscriber.onComplete(CompletionAwareSubscriber.java:71)
        at io.micronaut.http.server.netty.jackson.JsonContentProcessor$1.doOnComplete(JsonContentProcessor.java:140)
        at io.micronaut.core.async.subscriber.CompletionAwareSubscriber.onComplete(CompletionAwareSubscriber.java:71)
        at java.base/java.util.Optional.ifPresent(Optional.java:183)
        at io.micronaut.core.async.processor.SingleThreadedBufferingProcessor.doOnComplete(SingleThreadedBufferingProcessor.java:48)
        at io.micronaut.jackson.parser.JacksonProcessor.doOnComplete(JacksonProcessor.java:137)
        at io.micronaut.core.async.subscriber.SingleThreadedBufferingSubscriber.onComplete(SingleThreadedBufferingSubscriber.java:70)
        at io.micronaut.http.server.netty.jackson.JsonContentProcessor.doOnComplete(JsonContentProcessor.java:165)
        at io.micronaut.core.async.subscriber.CompletionAwareSubscriber.onComplete(CompletionAwareSubscriber.java:71)
        at io.micronaut.http.netty.reactive.HandlerPublisher.complete(HandlerPublisher.java:416)
        at io.micronaut.http.netty.reactive.HandlerPublisher.handlerRemoved(HandlerPublisher.java:403)
        at io.netty.channel.AbstractChannelHandlerContext.callHandlerRemoved(AbstractChannelHandlerContext.java:946)
        at io.netty.channel.DefaultChannelPipeline.callHandlerRemoved0(DefaultChannelPipeline.java:637)
        at io.netty.channel.DefaultChannelPipeline.remove(DefaultChannelPipeline.java:477)
        at io.netty.channel.DefaultChannelPipeline.remove(DefaultChannelPipeline.java:423)
        at io.micronaut.http.netty.stream.HttpStreamsHandler.removeHandlerIfActive(HttpStreamsHandler.java:429)
        at io.micronaut.http.netty.stream.HttpStreamsHandler.handleReadHttpContent(HttpStreamsHandler.java:294)
        at io.micronaut.http.netty.stream.HttpStreamsHandler.channelRead(HttpStreamsHandler.java:257)
        at io.micronaut.http.netty.stream.HttpStreamsServerHandler.channelRead(HttpStreamsServerHandler.java:121)
        at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
        at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
        at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
        at io.netty.handler.codec.MessageToMessageDecoder.channelRead(MessageToMessageDecoder.java:103)
        at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
        at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
        at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
        at io.netty.handler.codec.MessageToMessageDecoder.channelRead(MessageToMessageDecoder.java:103)
        at io.netty.handler.codec.MessageToMessageCodec.channelRead(MessageToMessageCodec.java:111)
        at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
        at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
        at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
        at io.netty.channel.ChannelInboundHandlerAdapter.channelRead(ChannelInboundHandlerAdapter.java:93)
        at io.netty.handler.codec.http.HttpServerKeepAliveHandler.channelRead(HttpServerKeepAliveHandler.java:64)
        at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
        at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
        at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
        at io.netty.handler.flow.FlowControlHandler.dequeue(FlowControlHandler.java:200)
        at io.netty.handler.flow.FlowControlHandler.read(FlowControlHandler.java:139)
        at io.netty.channel.AbstractChannelHandlerContext.invokeRead(AbstractChannelHandlerContext.java:686)
        at io.netty.channel.AbstractChannelHandlerContext.read(AbstractChannelHandlerContext.java:671)
        at io.micronaut.http.netty.reactive.HandlerPublisher.requestDemand(HandlerPublisher.java:163)
        at io.micronaut.http.netty.stream.HttpStreamsHandler$2.requestDemand(HttpStreamsHandler.java:248)
        at io.micronaut.http.netty.reactive.HandlerPublisher$ChannelSubscription.receivedDemand(HandlerPublisher.java:547)
        at io.micronaut.http.netty.reactive.HandlerPublisher$ChannelSubscription.lambda$request$0(HandlerPublisher.java:474)
        at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:164)
        at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:472)
        at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:497)
        at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
        at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
        at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
        at java.base/java.lang.Thread.run(Thread.java:829)
Caused by: io.micronaut.context.exceptions.NoSuchBeanException: No bean of type [io.micronaut.data.r2dbc.operations.R2dbcRepositoryOperations] exists for the given qualifier: @Named('dataSourceOne'). Make sure the bean is not disabled by bean requirements (enable trace logging for 'io.micronaut.context.condition' to check) and if the bean is enabled then ensure the class is declared a bean and annotation processing is enabled (for Java and Kotlin the 'micronaut-inject-java' dependency should be configured as an annotation processor).
        at io.micronaut.context.DefaultBeanContext.getBeanInternal(DefaultBeanContext.java:2322)
        at io.micronaut.context.DefaultBeanContext.getBean(DefaultBeanContext.java:721)
        at io.micronaut.data.intercept.DataIntroductionAdvice.findInterceptor(DataIntroductionAdvice.java:119)
        ... 74 common frames omitted
```
