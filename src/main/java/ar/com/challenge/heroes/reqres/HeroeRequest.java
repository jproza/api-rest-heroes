package ar.com.challenge.heroes.reqres;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class HeroeRequest {
  @NotEmpty
  @Size(max = 20)
  private String nombre;
}
