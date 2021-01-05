package micronaut.r2dbc.multiple.datasources

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.reactivex.Single
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid

@Controller("/authors")
class AuthorController(private val repository: AuthorRepository) {

    @Post("/")
    fun create(author: @Valid Author): Single<Author> {
        return Single.fromPublisher(repository.save(author))
    }

    @Get("/")
    fun all(): Flux<Author> {
        return repository.findAll()
    }

    @Get("/id")
    fun get(id: Long): Mono<Author> {
        return repository.findById(id)
    }
}