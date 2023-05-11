package com.cavendish.controller

import com.cavendish.dto.RouteDTO
import com.cavendish.service.LandRouteService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import javax.validation.constraints.Size
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Route", description = "Controller for searching land routes")
@RestController
class LandRouteController(
    private val landRouteService: LandRouteService,
) {

    @Operation(
        description = "Searches for the most efficient land route from one country to another.",
        tags = ["Route"],
    )
    @GetMapping(
        value = ["/routing/{origin}/{destination}"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun calculateLandRouteBetweenCountries(
        @PathVariable @Size(min = 3, max = 3) origin: String,
        @PathVariable @Size(min = 3, max = 3) destination: String,
    ): RouteDTO = landRouteService.searchForShortestPathBetweenCountries(
        origin.uppercase(),
        destination.uppercase(),
    )
}