package edu.universidad.sistema;

import java.util.ArrayList;
import java.util.List;

public class DemoPolimorfismo {
    public static void main(String[] args) {
        // Lista genérica de tipo Persona
        List<Persona> personas = new ArrayList<>();

        // Agregamos objetos de distintas clases
        personas.add(new Estudiante(1, "Juan", "juan@correo.com", "Ingeniería"));
        personas.add(new Profesor(2, "Ana", "Matemáticas"));

        // Recorremos la lista
        for (Persona p : personas) {
            System.out.println(p.toString());
        }
    }
}
