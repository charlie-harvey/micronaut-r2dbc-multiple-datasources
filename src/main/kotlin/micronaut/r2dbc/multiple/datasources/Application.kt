package micronaut.r2dbc.multiple.datasources

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("micronaut.r2dbc.multiple.datasources")
		.start()
}

