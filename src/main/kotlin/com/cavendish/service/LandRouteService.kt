package com.cavendish.service

import com.cavendish.dto.RouteDTO
import com.cavendish.model.Country
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.LinkedList
import java.util.Queue
import javax.annotation.PostConstruct

@Service
class LandRouteService {

    @Value("classpath:countries.json")
    lateinit var resource: Resource

    lateinit var countries: List<Country>

    @PostConstruct
    fun init() {
        countries = jacksonObjectMapper().readValue(resource.file, object : TypeReference<List<Country>>() {})
    }

    suspend fun searchForShortestPathBetweenCountries(
        origin: String,
        destination: String,
    ): RouteDTO {
        if (origin == destination) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Origin and destination country equals.")
        }
        val originCountry = countries.find { it.cca3 == origin }
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown origin country code.")

        val destinationCountry = countries.find { it.cca3 == destination }
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown destination country code.")

        // Queue to hold the paths from the origin.
        val queue: Queue<Country> = LinkedList()

        // List of visited paths
        val visitedCountryPaths = mutableListOf(mutableListOf(originCountry))

        // A set of visited countries starting from the origin.
        val visitedOrigin = mutableSetOf<Country>()

        visitedOrigin.add(originCountry)

        queue.add(originCountry)

        // Queue needs to be empty to exit the while loop.
        while (!queue.isEmpty()) {
            val nextCountry: Country = queue.remove()

            val countriesPath = visitedCountryPaths.first { it.contains(nextCountry) }
            val neighbouringCountries = countries.filter { it.cca3 in nextCountry.borders }.toMutableList()
            neighbouringCountries.removeAll(countriesPath)
            for (neighbouringCountry in neighbouringCountries) {
                // If the visited country contains the neighbouring country, then we can terminate the search.
                if (neighbouringCountry.cca3 == destinationCountry.cca3) {
                    return RouteDTO(route = countriesPath.map { it.cca3 }.toMutableList() + destinationCountry.cca3)
                }
                val copyCountriesPath = countriesPath.toMutableList()
                copyCountriesPath.add(neighbouringCountry)
                visitedCountryPaths.add(copyCountriesPath)

                if (visitedOrigin.add(neighbouringCountry)) {
                    queue.add(neighbouringCountry)
                }
            }
            visitedCountryPaths.removeAll { it.contains(nextCountry) && it.size == countriesPath.size }
        }
        // Specification says 400, but 404 seems better here as the request is good but the path could not be found.
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Land route not found.")
    }
}
