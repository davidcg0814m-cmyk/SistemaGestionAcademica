package edu.universidad.sistema;

public class Profesor extends Persona {
    private String especialidad;

    public Profesor(int id, String nombre, String especialidad) {
        super(id, nombre);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() { return especialidad; }

    @Override
    public String toString() {
        return id + " - " + nombre + " - " + especialidad;
    }
}
