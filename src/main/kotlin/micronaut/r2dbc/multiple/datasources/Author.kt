package micronaut.r2dbc.multiple.datasources

import io.micronaut.core.annotation.Introspected
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

@Introspected
@MappedEntity
data class Author(val name: String) {
    @GeneratedValue
    @Id
    var id: Long? = null
}