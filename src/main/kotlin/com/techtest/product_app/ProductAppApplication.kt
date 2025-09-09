package com.techtest.product_app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProductAppApplication

fun main(args: Array<String>) {
	runApplication<ProductAppApplication>(*args)
}
