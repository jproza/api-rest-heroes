package ar.com.challenge.heroes;

import ar.com.challenge.heroes.controller.HeroeController;
import ar.com.challenge.heroes.entities.Heroe;
import ar.com.challenge.heroes.ex.HeroeNotFoundException;
import ar.com.challenge.heroes.loginflow.controllers.AuthController;
import ar.com.challenge.heroes.repository.HeroeRepository;
import ar.com.challenge.heroes.reqres.HeroeRequest;
import ar.com.challenge.heroes.services.HeroeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@SpringBootTest(classes = {SuperHeroesApplication.class, AuthController.class, AuthenticationManager.class},  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "security.basic.enabled=false"})
@AutoConfigureMockMvc(addFilters = false)
public class HeroeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private HeroeService heroeService;

  @Captor
  private ArgumentCaptor<HeroeRequest> heroeRequestArgumentCaptor;

  @Test
  public void postingANewHeroeShouldCreateANewHeroe() throws Exception {

    HeroeRequest heroeRequest = new HeroeRequest();
    heroeRequest.setNombre("X-Men");

    when(heroeService.createNewHeroe(heroeRequestArgumentCaptor.capture())).thenReturn(1L);


    this.mockMvc
      .perform(post("/api/heroes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(heroeRequest)))
      .andExpect(status().isCreated())
      .andExpect(header().exists("Location"))
      .andExpect(header().string("Location", "http://localhost/api/heroes/1"));

    assertThat(heroeRequestArgumentCaptor.getValue().getNombre(), is("X-Men"));

  }

  @Test
  public void allHeroesEndpointShouldReturnTwoHeroes() throws Exception {

    when(heroeService.getAllHeroes()).thenReturn(List.of(
      createHeroe(1L,  "X-Men"),
      createHeroe(2L,  "Hulk")));

    //this.mockMvc.perform(get("/api/heroes")).andExpect(content().string(containsString("Login")));

    this.mockMvc
      .perform(get("/api/heroes"))
      .andExpect(status().isOk())
      .andExpect(content().contentType("application/json"))
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$[0].nombre", is("X-Men")))
      .andExpect(jsonPath("$[0].id", is(1)));

  }

  @Test
  public void getHeroeWithIdOneShouldReturnAHeroe() throws Exception {

    when(heroeService.getHeroesById(1L)).thenReturn(createHeroe(1L, "X-Men"));

    this.mockMvc
      .perform(get("/api/heroes/1"))
      .andExpect(status().isOk())
      .andExpect(content().contentType("application/json"))
      .andExpect(jsonPath("$.nombre", is("X-Men")))
      .andExpect(jsonPath("$.id", is(1)));

  }

  @Test
  public void getHeroeWithUnknownIdShouldReturn404() throws Exception {

    when(heroeService.getHeroesById(1L)).thenThrow(new HeroeNotFoundException("Heroe with id '1' not found"));

    this.mockMvc
      .perform(get("/api/heroe/1"))
      .andExpect(status().isNotFound());

  }

  @Test
  public void updateHeroeWithKnownIdShouldUpdateTheHeroe() throws Exception {

    HeroeRequest heroeRequest = new HeroeRequest();
    heroeRequest.setNombre("X-Men");

    when(heroeService.updateHeroes(eq(1L), heroeRequestArgumentCaptor.capture()))
      .thenReturn(createHeroe(1L, "X-Men"));

    this.mockMvc
      .perform(put("/api/heroes/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(heroeRequest)))
      .andExpect(status().isOk())
      .andExpect(content().contentType("application/json"))
      .andExpect(jsonPath("$.nombre", is("X-Men")))
      .andExpect(jsonPath("$.id", is(1)));

    assertThat(heroeRequestArgumentCaptor.getValue().getNombre(), is("X-Men"));


  }

  @Test
  public void updateHeroeWithUnknownIdShouldReturn404() throws Exception {

    HeroeRequest heroeRequest = new HeroeRequest();
    heroeRequest.setNombre("X-Men");;

    when(heroeService.updateHeroes(eq(42L), heroeRequestArgumentCaptor.capture()))
      .thenThrow(new HeroeNotFoundException("The heroe with id '42' was not found"));

    this.mockMvc
      .perform(put("/api/heroes/42")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(heroeRequest)))
      .andExpect(status().isNotFound());

  }

  private Heroe createHeroe(Long id, String nombre) {
    Heroe heroe = new Heroe();
    heroe.setNombre(nombre);
    heroe.setId(id);
    return heroe;
  }

}
