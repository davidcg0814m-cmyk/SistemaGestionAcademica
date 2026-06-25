package edu.universidad.sistema;

import java.sql.*;
import java.util.ArrayList;

public class EstudianteDAO {

    // Registrar estudiante
    public void registrarEstudiante(Estudiante e) {
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "INSERT INTO estudiante(nombre, correo, carrera) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCorreo());
            ps.setString(3, e.getCarrera());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Consultar lista de estudiantes
    public ArrayList<Estudiante> consultarEstudiantes() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, nombre, correo, carrera FROM estudiante")) {

            while (rs.next()) {
                lista.add(new Estudiante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("carrera")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // Actualizar estudiante
    public void actualizarEstudiante(Estudiante e) {
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "UPDATE estudiante SET nombre=?, correo=?, carrera=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCorreo());
            ps.setString(3, e.getCarrera());
            ps.setInt(4, e.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Eliminar estudiante
    public void eliminarEstudiante(int id) {
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "DELETE FROM estudiante WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Buscar estudiante por ID
    public Estudiante buscarEstudiantePorId(int id) {
        Estudiante est = null;
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "SELECT id, nombre, correo, carrera FROM estudiante WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                est = new Estudiante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("carrera")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return est;
    }

    // 🔹 Consultar estudiantes inscritos en un curso
    public ArrayList<Estudiante> consultarEstudiantesPorCurso(int cursoId) {
        ArrayList<Estudiante> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "SELECT e.id, e.nombre, e.correo, e.carrera, ce.nota, ce.observacion, ce.estado_apelacion " +
                    "FROM curso_estudiante ce " +
                    "JOIN estudiante e ON ce.estudiante_id = e.id " +
                    "WHERE ce.curso_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Estudiante est = new Estudiante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("carrera")
                );
                est.setNota(rs.getObject("nota") != null ? rs.getDouble("nota") : null);
                est.setObservacion(rs.getString("observacion"));
                est.setEstadoApelacion(rs.getString("estado_apelacion"));
                lista.add(est);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // 🔹 Calificar estudiante en curso
    public void calificarEstudiante(int cursoId, int estudianteId, double nota, String observacion) {
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "UPDATE curso_estudiante SET nota=?, observacion=?, estado_apelacion='Pendiente' " +
                    "WHERE curso_id=? AND estudiante_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, nota);
            ps.setString(2, observacion);
            ps.setInt(3, cursoId);
            ps.setInt(4, estudianteId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 🔹 Actualizar apelación
    public void actualizarApelacion(int cursoId, int estudianteId, String estado) {
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "UPDATE curso_estudiante SET estado_apelacion=? WHERE curso_id=? AND estudiante_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, cursoId);
            ps.setInt(3, estudianteId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 🔹 Consultar cursos de un estudiante (devuelve Estudiantes con su curso asociado)
    public ArrayList<Estudiante> consultarCursosPorEstudiante(int estudianteId) {
        ArrayList<Estudiante> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "SELECT c.id AS curso_id, c.nombre AS curso_nombre, " +
                    "p.id AS profesor_id, p.nombre AS profesor_nombre, " +
                    "ce.nota, ce.observacion, ce.estado_apelacion, " +
                    "e.id AS estudiante_id, e.nombre AS estudiante_nombre, e.correo, e.carrera " +
                    "FROM curso_estudiante ce " +
                    "JOIN curso c ON ce.curso_id = c.id " +
                    "JOIN profesor p ON c.profesor_id = p.id " +
                    "JOIN estudiante e ON ce.estudiante_id = e.id " +
                    "WHERE ce.estudiante_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, estudianteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Profesor prof = new Profesor(rs.getInt("profesor_id"), rs.getString("profesor_nombre"), "");
                Curso curso = new Curso(
                        rs.getInt("curso_id"),
                        rs.getString("curso_nombre"),
                        prof,
                        null, // notaMaxima
                        null, // fechaInicio
                        null  // fechaFin
                );

                Estudiante est = new Estudiante(
                        rs.getInt("estudiante_id"),
                        rs.getString("estudiante_nombre"),
                        rs.getString("correo"),
                        rs.getString("carrera")
                );
                est.setCurso(curso);
                est.setNota(rs.getObject("nota") != null ? rs.getDouble("nota") : null);
                est.setObservacion(rs.getString("observacion"));
                est.setEstadoApelacion(rs.getString("estado_apelacion"));

                lista.add(est);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // 🔹 Obtener ID de curso por nombre
    public int obtenerCursoIdPorNombre(String nombreCurso) {
        int id = -1;
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "SELECT id FROM curso WHERE nombre=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombreCurso);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }
}
