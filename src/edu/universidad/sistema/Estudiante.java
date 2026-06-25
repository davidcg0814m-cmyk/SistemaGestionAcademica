package edu.universidad.sistema;

public class Estudiante extends Persona {
    private String correo;
    private String carrera;

    // 🔹 Relación con Curso
    private Curso curso;

    // 🔹 Campos de calificación
    private Double nota;
    private String observacion;
    private String estadoApelacion;

    // Constructor principal
    public Estudiante(int id, String nombre, String correo, String carrera) {
        super(id, nombre);
        this.correo = correo;
        this.carrera = carrera;
    }

    // Constructor alternativo (solo id y nombre)
    public Estudiante(int id, String nombre) {
        super(id, nombre);
    }

    // Getters y Setters básicos
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    // 🔹 Relación con Curso
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    // 🔹 Calificación
    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getEstadoApelacion() { return estadoApelacion; }
    public void setEstadoApelacion(String estadoApelacion) { this.estadoApelacion = estadoApelacion; }

    @Override
    public String toString() {
        return id + " - " + nombre + " - " + correo + " - " + carrera;
    }
}
