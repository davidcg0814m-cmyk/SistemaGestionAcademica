package edu.universidad.sistema;

import javax.swing.*;
import java.awt.*;

public class VentanaProfesorMenu extends JFrame {
    private final Usuario usuario;

    public VentanaProfesorMenu(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Menú Profesor - " + usuario.getUsername());
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnEstudiantes = new JButton("Ver Estudiantes");
        JButton btnNotas = new JButton("Notas");
        JButton btnAjustes = new JButton("Ajustes");
        JButton btnSalir = new JButton("Salir");

        JPanel panel = new JPanel();
        panel.add(btnEstudiantes);
        panel.add(btnNotas);
        panel.add(btnAjustes);
        panel.add(btnSalir);

        add(panel);

        // Acciones
        btnEstudiantes.addActionListener(e -> new VentanaCursoProfesor(usuario).setVisible(true));

        btnNotas.addActionListener(e -> {
            // Abrimos la nueva ventana de calificación
            CursoDAO dao = new CursoDAO();
            // 🔹 Aquí puedes decidir cómo obtener el curso del profesor
            // Ejemplo: buscar el primer curso asignado al profesor
            Curso curso = dao.buscarCursoPorProfesor(usuario.getId());
            if (curso != null) {
                new VentanaCalificar(curso).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "No hay cursos asignados al profesor.");
            }
        });

        btnAjustes.addActionListener(e -> new VentanaAjustes(usuario).setVisible(true));
        btnSalir.addActionListener(e -> dispose());
    }
}
