package edu.universidad.sistema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaCalificar extends JFrame {
    private JTable tablaEstudiantes;
    private DefaultTableModel modelo;
    private JButton btnAsignarNota, btnAceptarApelacion, btnRechazarApelacion, btnCerrar;
    private Curso curso;
    private EstudianteDAO estudianteDAO;

    public VentanaCalificar(Curso curso) {
        this.curso = curso;
        this.estudianteDAO = new EstudianteDAO();

        setTitle("Calificar - Curso: " + curso.getNombre());
        setSize(800, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tabla de estudiantes
        String[] columnas = {"ID", "Nombre", "Nota", "Observación", "Estado Apelación"};
        modelo = new DefaultTableModel(columnas, 0);
        tablaEstudiantes = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaEstudiantes);

        // Botones
        btnAsignarNota = new JButton("⭐ Asignar Nota");
        btnAceptarApelacion = new JButton("✅ Aceptar Apelación");
        btnRechazarApelacion = new JButton("❌ Rechazar Apelación");
        btnCerrar = new JButton("🚪 Cerrar");

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnAsignarNota);
        panelBotones.add(btnAceptarApelacion);
        panelBotones.add(btnRechazarApelacion);
        panelBotones.add(btnCerrar);

        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnAsignarNota.addActionListener(e -> asignarNota());
        btnAceptarApelacion.addActionListener(e -> actualizarApelacion("Aceptada"));
        btnRechazarApelacion.addActionListener(e -> actualizarApelacion("Rechazada"));
        btnCerrar.addActionListener(e -> dispose());

        cargarEstudiantes();

        setVisible(true);
    }

    private void cargarEstudiantes() {
        modelo.setRowCount(0);
        ArrayList<Estudiante> lista = estudianteDAO.consultarEstudiantesPorCurso(curso.getId());
        for (Estudiante est : lista) {
            modelo.addRow(new Object[]{
                    est.getId(),
                    est.getNombre(),
                    est.getNota() == null ? "Sin nota" : est.getNota(),
                    est.getObservacion() == null ? "" : est.getObservacion(),
                    est.getEstadoApelacion() == null ? "Pendiente" : est.getEstadoApelacion()
            });
        }
    }

    private void asignarNota() {
        int fila = tablaEstudiantes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante");
            return;
        }
        int estudianteId = (int) modelo.getValueAt(fila, 0);
        String notaStr = JOptionPane.showInputDialog(this, "Ingrese nota (0-10):");
        String observacion = JOptionPane.showInputDialog(this, "Ingrese observación:");
        try {
            double nota = Double.parseDouble(notaStr);
            if (nota < 0 || nota > 10) {
                JOptionPane.showMessageDialog(this, " La nota debe estar entre 0 y 10");
                return;
            }
            estudianteDAO.calificarEstudiante(curso.getId(), estudianteId, nota, observacion);
            JOptionPane.showMessageDialog(this, " Nota asignada");
            cargarEstudiantes();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, " Nota inválida");
        }
    }

    private void actualizarApelacion(String estado) {
        int fila = tablaEstudiantes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante");
            return;
        }
        int estudianteId = (int) modelo.getValueAt(fila, 0);
        estudianteDAO.actualizarApelacion(curso.getId(), estudianteId, estado);
        JOptionPane.showMessageDialog(this, " Apelación " + estado);
        cargarEstudiantes();
    }
}
