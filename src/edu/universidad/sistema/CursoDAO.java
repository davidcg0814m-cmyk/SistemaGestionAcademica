package edu.universidad.sistema;

import java.sql.*;
import java.util.ArrayList;

public class CursoDAO {

    // Registrar curso
    public void registrarCurso(Curso c) {
        if (c.getNombre() == null || c.getNombre().trim().isEmpty()) {
            System.out.println("⚠️ Error: El nombre del curso no puede estar vacío.");
            return;
        }
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "INSERT INTO curso(nombre, profesor_id, nota_maxima, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getProfesor().getId());
            ps.setDouble(3, c.getNotaMaxima());
            ps.setString(4, c.getFechaInicio());
            ps.setString(5, c.getFechaFin());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Consultar todos los cursos
    public ArrayList<Curso> consultarCursos() {
        ArrayList<Curso> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, nombre, profesor_id, nota_maxima, fecha_inicio, fecha_fin FROM curso")) {

            while (rs.next()) {
                ProfesorDAO pdao = new ProfesorDAO();
                Profesor prof = pdao.buscarProfesorPorId(rs.getInt("profesor_id"));
                Curso curso = new Curso(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        prof,
                        rs.getDouble("nota_maxima"),
                        rs.getString("fecha_inicio"),
                        rs.getString("fecha_fin")
                );
                lista.add(curso);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // Actualizar curso
    public void actualizarCurso(Curso c) {
        if (c.getNombre() == null || c.getNombre().trim().isEmpty()) {
            System.out.println("⚠️ Error: El nombre del curso no puede estar vacío.");
            return;
        }
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "UPDATE curso SET nombre=?, profesor_id=?, nota_maxima=?, fecha_inicio=?, fecha_fin=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getProfesor().getId());
            ps.setDouble(3, c.getNotaMaxima());
            ps.setString(4, c.getFechaInicio());
            ps.setString(5, c.getFechaFin());
            ps.setInt(6, c.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Eliminar curso
    public void eliminarCurso(int id) {
        if (id <= 0) {
            System.out.println("⚠️ Error: ID inválido para eliminar curso.");
            return;
        }
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "DELETE FROM curso WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Buscar curso por ID
    public Curso buscarCursoPorId(int id) {
        if (id <= 0) {
            System.out.println("⚠️ Error: ID inválido para buscar curso.");
            return null;
        }
        Curso curso = null;
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "SELECT id, nombre, profesor_id, nota_maxima, fecha_inicio, fecha_fin FROM curso WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ProfesorDAO pdao = new ProfesorDAO();
                Profesor prof = pdao.buscarProfesorPorId(rs.getInt("profesor_id"));
                curso = new Curso(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        prof,
                        rs.getDouble("nota_maxima"),
                        rs.getString("fecha_inicio"),
                        rs.getString("fecha_fin")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return curso;
    }

    // 🔹 Contar estudiantes matriculados en un curso
    public int contarEstudiantesMatriculados(int cursoId) {
        int cantidad = 0;
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "SELECT COUNT(*) AS total FROM curso_estudiante WHERE curso_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cursoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cantidad = rs.getInt("total");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cantidad;
    }

    // 🔹 Buscar curso por profesor
    public Curso buscarCursoPorProfesor(int profesorId) {
        Curso curso = null;
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "SELECT id, nombre, profesor_id, nota_maxima, fecha_inicio, fecha_fin " +
                    "FROM curso WHERE profesor_id=? LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, profesorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ProfesorDAO pdao = new ProfesorDAO();
                Profesor prof = pdao.buscarProfesorPorId(rs.getInt("profesor_id"));
                curso = new Curso(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        prof,
                        rs.getDouble("nota_maxima"),
                        rs.getString("fecha_inicio"),
                        rs.getString("fecha_fin")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return curso;
    }

    // 🔹 Consultar todos los cursos de un profesor
    public ArrayList<Curso> consultarCursosPorProfesor(int profesorId) {
        ArrayList<Curso> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "SELECT id, nombre, profesor_id, nota_maxima, fecha_inicio, fecha_fin " +
                    "FROM curso WHERE profesor_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, profesorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProfesorDAO pdao = new ProfesorDAO();
                Profesor prof = pdao.buscarProfesorPorId(rs.getInt("profesor_id"));
                Curso curso = new Curso(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        prof,
                        rs.getDouble("nota_maxima"),
                        rs.getString("fecha_inicio"),
                        rs.getString("fecha_fin")
                );
                lista.add(curso);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }
}
