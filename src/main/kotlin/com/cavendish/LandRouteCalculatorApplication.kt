package com.cavendish

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LandRouteCalculatorApplication

fun main(args: Array<String>) {
	runApplication<LandRouteCalculatorApplication>(*args)
}
