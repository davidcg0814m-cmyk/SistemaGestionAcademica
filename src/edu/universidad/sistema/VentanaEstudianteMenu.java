package edu.universidad.sistema;

import javax.swing.*;
import java.awt.*;

public class VentanaEstudianteMenu extends JFrame {
    private final Usuario usuario;

    public VentanaEstudianteMenu(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Menú Estudiante - " + usuario.getUsername());
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnCursos = new JButton("Cursos");
        JButton btnAjustes = new JButton("Ajustes");
        JButton btnSalir = new JButton("Salir");

        JPanel panel = new JPanel();
        panel.add(btnCursos);
        panel.add(btnAjustes);
        panel.add(btnSalir);

        add(panel);

        btnCursos.addActionListener(e -> new VentanaCursoEstudiante(usuario).setVisible(true));
        btnAjustes.addActionListener(e -> new VentanaAjustes(usuario).setVisible(true));
        btnSalir.addActionListener(e -> dispose());
    }
}
