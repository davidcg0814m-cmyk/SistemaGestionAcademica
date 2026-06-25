package edu.universidad.sistema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaCursoEstudiante extends JFrame {
    private JTable tablaCursos;
    private DefaultTableModel modelo;
    private JButton btnApelar, btnCerrar;
    private Usuario usuario; // estudiante logueado
    private EstudianteDAO estudianteDAO;

    public VentanaCursoEstudiante(Usuario usuario) {
        this.usuario = usuario;
        this.estudianteDAO = new EstudianteDAO();

        setTitle("Mis Cursos - Estudiante: " + usuario.getUsername());
        setSize(800, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tabla
        String[] columnas = {"Curso", "Profesor", "Nota", "Observación", "Estado Apelación"};
        modelo = new DefaultTableModel(columnas, 0);
        tablaCursos = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaCursos);

        // Botones
        btnApelar = new JButton("📌 Apelar Nota");
        btnCerrar = new JButton("🚪 Salir");

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnApelar);
        panelBotones.add(btnCerrar);

        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnApelar.addActionListener(e -> apelarNota());
        btnCerrar.addActionListener(e -> dispose());

        cargarCursos();

        setVisible(true);
    }

    private void cargarCursos() {
        modelo.setRowCount(0);
        // 🔹 Ahora consultamos estudiantes con sus cursos
        ArrayList<Estudiante> lista = estudianteDAO.consultarCursosPorEstudiante(usuario.getId());
        for (Estudiante est : lista) {
            Curso curso = est.getCurso(); // suponiendo que Estudiante tiene referencia al curso
            modelo.addRow(new Object[]{
                    curso.getNombre(),
                    curso.getProfesor().getNombre(),
                    est.getNota() == null ? "Sin nota" : est.getNota(),
                    est.getObservacion() == null ? "" : est.getObservacion(),
                    est.getEstadoApelacion() == null ? "Pendiente" : est.getEstadoApelacion()
            });
        }
    }

    private void apelarNota() {
        int fila = tablaCursos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso para apelar");
            return;
        }
        String motivo = JOptionPane.showInputDialog(this, "Ingrese motivo de apelación:");
        if (motivo != null && !motivo.trim().isEmpty()) {
            String nombreCurso = (String) modelo.getValueAt(fila, 0);
            int cursoId = estudianteDAO.obtenerCursoIdPorNombre(nombreCurso);
            estudianteDAO.actualizarApelacion(cursoId, usuario.getId(), "Pendiente");
            JOptionPane.showMessageDialog(this, " Apelación enviada");
            cargarCursos();
        }
    }
}
