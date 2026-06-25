package edu.universidad.sistema;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {
    private final JTextField txtUsuario;
    private final JPasswordField txtClave;
    private final JButton btnIngresar, btnSalir;

    public Login() {
        setTitle("Login - Sistema Universidad");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel de campos
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 10));
        txtUsuario = new JTextField();
        txtClave = new JPasswordField();

        panelCampos.add(new JLabel("Usuario:"));
        panelCampos.add(txtUsuario);
        panelCampos.add(new JLabel("Clave:"));
        panelCampos.add(txtClave);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnIngresar = new JButton("Ingresar");
        btnSalir = new JButton("Salir");
        panelBotones.add(btnIngresar);
        panelBotones.add(btnSalir);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        UsuarioDAO dao = new UsuarioDAO();

        // Acción ingresar
        btnIngresar.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String clave = String.valueOf(txtClave.getPassword());

            Usuario u = dao.login(usuario, clave);

            if (u != null) {
                JOptionPane.showMessageDialog(this,
                        " Bienvenido " + u.getUsername() + " (" + u.getRol() + ")");

                switch (u.getRol()) {
                    case "profesor":
                        new MenuPrincipal(u).setVisible(true);   // menú profesor
                        break;
                    case "estudiante":
                        new MenuPrincipal(u).setVisible(true);   // menú estudiante
                        break;
                    case "admin":
                        new MenuPrincipal(u).setVisible(true);   // menú admin
                        break;
                }

                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        " Usuario o clave incorrectos");
            }
        });

        // Acción salir
        btnSalir.addActionListener(e -> dispose());

        setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }
}

