package micronaut.r2dbc.multiple.datasources

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.reactivex.Single
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import javax.validation.Valid

@Controller("/mysql/books")
class MysqlBookController(private val repository: MysqlBookRepository) {

    @Post("/")
    fun create(book: @Valid Book): Single<Book> {
        book.dateCreated = LocalDateTime.now()
        return Single.fromPublisher(repository.save(book))
    }

    @Get("/")
    fun all(): Flux<Book> {
        return repository.findAll()
    }

    @Get("/{id}")
    fun get(id: Long): Mono<Book> {
        return repository.findById(id)
    }
}
