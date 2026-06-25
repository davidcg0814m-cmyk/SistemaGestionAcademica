package edu.universidad.sistema;

import java.sql.*;

public class UsuarioDAO {

    // Registrar usuario
    public void registrarUsuario(Usuario u) throws SQLException {
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "INSERT INTO usuario(username, password, rol) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRol());
            ps.executeUpdate();
        }
    }

    // Login
    public Usuario login(String username, String password) {
        Usuario usuario = null;
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "SELECT * FROM usuario WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("rol")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return usuario;
    }

    // Cambiar clave
    public boolean cambiarClave(String username, String claveActual, String nuevaClave) {
        try (Connection conn = ConexionBD.getConnection()) {
            String sql = "UPDATE usuario SET password=? WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nuevaClave);
            ps.setString(2, username);
            ps.setString(3, claveActual);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
