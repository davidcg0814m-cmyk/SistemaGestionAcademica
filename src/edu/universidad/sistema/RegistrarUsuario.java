package edu.universidad.sistema;
public class RegistrarUsuario {

    private UsuarioDAO usuarioDAO;

    public RegistrarUsuario() {
        usuarioDAO = new UsuarioDAO();
    }

    // Método para registrar automáticamente según el rol
    public void registrar(String nombre, String clave, String rol) {
        try {
            Usuario u = new Usuario(0, nombre, clave, rol);
            usuarioDAO.registrarUsuario(u);
            System.out.println(" Usuario registrado: " + nombre + " (" + rol + ")");
        } catch (Exception ex) {
            System.err.println(" Error al registrar usuario: " + ex.getMessage());
        }
    }
}
