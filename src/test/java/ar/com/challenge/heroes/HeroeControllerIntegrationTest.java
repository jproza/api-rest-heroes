package ar.com.challenge.heroes;

import ar.com.challenge.heroes.controller.HeroeController;
import ar.com.challenge.heroes.loginflow.controllers.AuthController;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ActiveProfiles("test")
@SpringBootTest(classes = {SuperHeroesApplication.class, AuthController.class, AuthenticationManager.class},  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "security.basic.enabled=false"})
@AutoConfigureMockMvc(addFilters = false)
//@TestPropertySource(locations="classpath:application.properties")
public class HeroeControllerIntegrationTest {

  @LocalServerPort
  int randomServerPort;

  private TestRestTemplate testRestTemplate;

  @BeforeEach
  public void setUp() {
    this.testRestTemplate = new TestRestTemplate();
  }

  //@Sql({ "/data.sql" })
  @Test
  public void deletingKnownEntityShouldReturn404AfterDeletion() {
    long heroeId = 1;
    String baseUrl = "http://localhost:" + randomServerPort;

    ResponseEntity<JsonNode> firstResult = this.testRestTemplate
      .getForEntity(baseUrl + "/api/heroes/" + heroeId, JsonNode.class);

    assertThat(firstResult.getStatusCode(), is(HttpStatus.OK));

    this.testRestTemplate.delete(baseUrl + "/api/heroes/" + heroeId);

    ResponseEntity<JsonNode> secondResult = this.testRestTemplate
      .getForEntity(baseUrl + "/api/heroes/" + heroeId, JsonNode.class);

    assertThat(secondResult.getStatusCode(), is(HttpStatus.NOT_FOUND));
  }
}
