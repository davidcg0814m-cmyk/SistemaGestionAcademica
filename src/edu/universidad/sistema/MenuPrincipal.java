package edu.universidad.sistema;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {
    private JButton btnEstudiantes, btnProfesores, btnCursos, btnAjustes, btnSalir;
    private Usuario usuario; // usuario logueado

    public MenuPrincipal(Usuario usuario) {
        this.usuario = usuario;
        String rol = usuario.getRol();

        setTitle("Menú Principal - Rol: " + rol);
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel con botones en columna
        JPanel panel = new JPanel(new GridLayout(5, 1, 15, 15));

        btnEstudiantes = new JButton("Gestión Estudiantes");
        btnProfesores = new JButton("Gestión Profesores");
        btnCursos = new JButton("Gestión Cursos");
        btnAjustes = new JButton("Ajustes");
        btnSalir = new JButton("Salir");

        //  Agregamos botones según el rol
        if (rol.equalsIgnoreCase("admin")) {
            panel.add(btnEstudiantes);
            panel.add(btnProfesores);
            panel.add(btnCursos);

            btnEstudiantes.addActionListener(e -> new VentanaEstudiante());
            btnProfesores.addActionListener(e -> new VentanaProfesor());
            btnCursos.addActionListener(e -> new VentanaCursoProfesor(usuario));

        } else if (rol.equalsIgnoreCase("profesor")) {
            panel.add(btnEstudiantes);
            panel.add(btnCursos);

            btnEstudiantes.addActionListener(e -> new VentanaEstudiante());
            btnCursos.addActionListener(e -> new VentanaCursoProfesor(usuario));

        } else if (rol.equalsIgnoreCase("estudiante")) {
            panel.add(btnCursos);

            btnCursos.addActionListener(e -> new VentanaCursoEstudiante(usuario));
        }

        // Ajustes y salir siempre presentes
        panel.add(btnAjustes);
        panel.add(btnSalir);

        add(panel, BorderLayout.CENTER);

        // Acción Ajustes → cambio de contraseña
        btnAjustes.addActionListener(e -> new VentanaAjustes(usuario));

        // Acción salir
        btnSalir.addActionListener(e -> dispose());

        setVisible(true);
    }
}
