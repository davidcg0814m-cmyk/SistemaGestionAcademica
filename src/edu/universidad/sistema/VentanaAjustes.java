package edu.universidad.sistema;

import javax.swing.*;
import java.awt.*;

public class VentanaAjustes extends JFrame {
    private JPasswordField txtClaveActual;
    private JPasswordField txtNuevaClave;
    private JButton btnCambiar;

    private Usuario usuario; // usuario logueado

    public VentanaAjustes(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Ajustes de " + usuario.getUsername()); //  usar getUsername()
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Clave actual:"));
        txtClaveActual = new JPasswordField();
        panel.add(txtClaveActual);

        panel.add(new JLabel("Nueva clave:"));
        txtNuevaClave = new JPasswordField();
        panel.add(txtNuevaClave);

        btnCambiar = new JButton("Cambiar contraseña");
        panel.add(btnCambiar);

        JLabel lblAdvertencia = new JLabel("⚠️ Cuidado con olvidar la clave");
        panel.add(lblAdvertencia);

        add(panel);

        UsuarioDAO dao = new UsuarioDAO();

        btnCambiar.addActionListener(e -> {
            String claveActual = new String(txtClaveActual.getPassword());
            String nuevaClave = new String(txtNuevaClave.getPassword());

            //  usar getUsername() y setPassword()
            boolean ok = dao.cambiarClave(usuario.getUsername(), claveActual, nuevaClave);
            if (ok) {
                JOptionPane.showMessageDialog(this, " Clave cambiada correctamente");
                usuario.setPassword(nuevaClave); // actualizar en memoria
            } else {
                JOptionPane.showMessageDialog(this, " Clave actual incorrecta");
            }
        });

        setVisible(true);
    }
}
