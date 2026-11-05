package com.ikea.spring.drools.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringDroolsApplication

fun main(args: Array<String>) {
	runApplication<com.ikea.spring.drools.api.SpringDroolsApplication>(*args)
}
