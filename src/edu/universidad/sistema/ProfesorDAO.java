package edu.universidad.sistema;

import java.sql.*;
import java.util.ArrayList;

public class ProfesorDAO {

    // Registrar profesor en la BD
    public void registrarProfesor(Profesor p) {
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "INSERT INTO profesor(nombre, especialidad) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getEspecialidad());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Consultar lista de profesores
    public ArrayList<Profesor> consultarProfesores() {
        ArrayList<Profesor> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, nombre, especialidad FROM profesor")) {

            while (rs.next()) {
                lista.add(new Profesor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("especialidad")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    // Actualizar profesor
    public void actualizarProfesor(Profesor p) {
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "UPDATE profesor SET nombre = ?, especialidad = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getEspecialidad());
            ps.setInt(3, p.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Eliminar profesor
    public void eliminarProfesor(int id) {
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "DELETE FROM profesor WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Buscar profesor por ID (para repintar datos en VentanaProfesor)
    public Profesor buscarProfesorPorId(int id) {
        Profesor prof = null;
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "SELECT id, nombre, especialidad FROM profesor WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                prof = new Profesor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("especialidad")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return prof;
    }
}


