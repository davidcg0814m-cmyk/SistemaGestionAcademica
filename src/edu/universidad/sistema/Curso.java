package edu.universidad.sistema;

public class Curso {
    private int id;
    private String nombre;
    private Profesor profesor;
    private Double notaMaxima;   // 🔹 nuevo campo
    private String fechaInicio;  // 🔹 nuevo campo
    private String fechaFin;     // 🔹 nuevo campo

    // Constructor principal
    public Curso(int id, String nombre, Profesor profesor, Double notaMaxima, String fechaInicio, String fechaFin) {
        this.id = id;
        this.nombre = nombre;
        this.profesor = profesor;
        this.notaMaxima = notaMaxima;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Profesor getProfesor() { return profesor; }
    public void setProfesor(Profesor profesor) { this.profesor = profesor; }

    public Double getNotaMaxima() { return notaMaxima; }
    public void setNotaMaxima(Double notaMaxima) { this.notaMaxima = notaMaxima; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    @Override
    public String toString() {
        return nombre; // 🔹 así se mostrará solo el nombre del curso en combos
    }
}
