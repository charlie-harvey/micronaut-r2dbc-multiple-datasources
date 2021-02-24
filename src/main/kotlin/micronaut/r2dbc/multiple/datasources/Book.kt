package micronaut.r2dbc.multiple.datasources

import io.micronaut.core.annotation.Introspected
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.time.LocalDateTime

@Introspected
@MappedEntity
data class Book(
    val title: String,
    val pages: Int,
    val authorId: Long
) {
    @GeneratedValue
    @Id
    var id: Long? = null
    var dateCreated: LocalDateTime? = null
}