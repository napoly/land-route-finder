package com.cavendish

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.web.reactive.server.WebTestClient

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = RANDOM_PORT)
class LandRouteCalculatorApplicationSpecIT : StringSpec() {

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun `Should return path from Sweden to South Africa`() {
        val expectedPath =
            listOf("SWE", "FIN", "RUS", "AZE", "TUR", "SYR", "ISR", "EGY", "SDN", "CAF", "COD", "AGO", "NAM", "ZAF")
        webClient.get()
            .uri("/routing/swE/zaF")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.route").value<List<String>> {
                it shouldBe expectedPath
            }
    }

    @Test
    fun `Returns bad request if land route can not be found`() {
        webClient.get()
            .uri("/routing/SvK/usa")
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody()
            .jsonPath("$.message").value<String> {
                it shouldBe "Land route not found."
            }
    }

    @Test
    fun `Returns bad request when origin and destination country equals`() {
        webClient.get()
            .uri("/routing/SvK/svk")
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody()
            .jsonPath("$.message").value<String> {
                it shouldBe "Origin and destination country equals."
            }
    }

    @Test
    fun `Returns bad request if origin country code is unknown`() {
        webClient.get()
            .uri("/routing/xXx/POL")
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody()
            .jsonPath("$.message").value<String> {
                it shouldBe "Unknown origin country code."
            }
    }

    @Test
    fun `Returns bad request if destination country code is unknown`() {
        webClient.get()
            .uri("/routing/CZE/666")
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody()
            .jsonPath("$.message").value<String> {
                it shouldBe "Unknown destination country code."
            }
    }

}
