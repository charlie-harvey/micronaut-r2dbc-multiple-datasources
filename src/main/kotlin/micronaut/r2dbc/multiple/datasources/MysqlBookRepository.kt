package micronaut.r2dbc.multiple.datasources

import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.constraints.NotNull

@R2dbcRepository(value = "mysql-data-source", dialect = Dialect.MYSQL)
interface MysqlBookRepository : ReactiveStreamsCrudRepository<Book, Long> {
    override fun findById(id: @NotNull Long): Mono<Book>
    override fun findAll(): Flux<Book>
}