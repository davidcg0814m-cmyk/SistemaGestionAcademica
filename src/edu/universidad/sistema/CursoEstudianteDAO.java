package edu.universidad.sistema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CursoEstudianteDAO {

    // Método para matricular estudiante en curso
    public void matricularEstudiante(int cursoId, int estudianteId) {
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "INSERT INTO curso_estudiante (curso_id, estudiante_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cursoId);
            ps.setInt(2, estudianteId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // (Opcional) método para eliminar matrícula
    public void eliminarMatricula(int cursoId, int estudianteId) {
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "DELETE FROM curso_estudiante WHERE curso_id=? AND estudiante_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cursoId);
            ps.setInt(2, estudianteId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
