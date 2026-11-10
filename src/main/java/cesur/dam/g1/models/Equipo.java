package cesur.dam.g1.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Equipo {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fecha;
    private String categoria;
    private Boolean disponible;
}
