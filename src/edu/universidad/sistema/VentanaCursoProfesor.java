package edu.universidad.sistema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VentanaCursoProfesor extends JFrame {
    private JTextField txtId, txtNombre, txtNotaMaxima;
    private JSpinner spinnerInicio, spinnerFin;
    private JComboBox<Profesor> comboProfesor;
    private JButton btnCrear, btnEditar, btnEliminar, btnListar, btnCalificar, btnCerrar;
    private JTable tablaCursos;
    private DefaultTableModel modelo;

    private Usuario usuario; // profesor logueado
    private CursoDAO dao;
    private ProfesorDAO pdao;

    public VentanaCursoProfesor(Usuario usuario) {
        this.usuario = usuario;
        this.dao = new CursoDAO();
        this.pdao = new ProfesorDAO();

        setTitle("Gestión de Cursos - Profesor: " + usuario.getUsername());
        setSize(950, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel de campos con GridLayout
        JPanel panelCampos = new JPanel(new GridLayout(6, 2, 10, 10));
        txtId = new JTextField();
        txtNombre = new JTextField();
        txtNotaMaxima = new JTextField();
        comboProfesor = new JComboBox<>();

        for (Profesor p : pdao.consultarProfesores()) {
            comboProfesor.addItem(p);
        }

        // Spinners de fecha
        SpinnerDateModel modeloInicio = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        spinnerInicio = new JSpinner(modeloInicio);
        spinnerInicio.setEditor(new JSpinner.DateEditor(spinnerInicio, "yyyy-MM-dd"));

        SpinnerDateModel modeloFin = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        spinnerFin = new JSpinner(modeloFin);
        spinnerFin.setEditor(new JSpinner.DateEditor(spinnerFin, "yyyy-MM-dd"));

        // Agregar campos al panel
        panelCampos.add(new JLabel("ID Curso:"));
        panelCampos.add(txtId);
        panelCampos.add(new JLabel("Nombre Curso:"));
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Profesor:"));
        panelCampos.add(comboProfesor);
        panelCampos.add(new JLabel("Nota Máxima:"));
        panelCampos.add(txtNotaMaxima);
        panelCampos.add(new JLabel("Fecha Inicio:"));
        panelCampos.add(spinnerInicio);
        panelCampos.add(new JLabel("Fecha Fin:"));
        panelCampos.add(spinnerFin);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnCrear = new JButton("➕ Crear");
        btnEditar = new JButton("✏️ Editar");
        btnEliminar = new JButton("🗑️ Eliminar");
        btnListar = new JButton("📋 Listar");
        btnCalificar = new JButton("⭐ Calificar");
        btnCerrar = new JButton("🚪 Cerrar");

        panelBotones.add(btnCrear);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnListar);
        panelBotones.add(btnCalificar);
        panelBotones.add(btnCerrar);

        // Tabla
        String[] columnas = {"ID", "Nombre", "Profesor", "Nota Máxima", "Inicio", "Fin", "Estudiantes"};
        modelo = new DefaultTableModel(columnas, 0);
        tablaCursos = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaCursos);

        add(panelCampos, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Acciones
        btnCrear.addActionListener(e -> crearCurso());
        btnEditar.addActionListener(e -> editarCurso());
        btnEliminar.addActionListener(e -> eliminarCurso());
        btnListar.addActionListener(e -> listarCursos());
        btnCalificar.addActionListener(e -> abrirVentanaCalificar());
        btnCerrar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void crearCurso() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText();
            Profesor profesor = (Profesor) comboProfesor.getSelectedItem();
            Double notaMaxima = Double.parseDouble(txtNotaMaxima.getText());
            String fechaInicio = new SimpleDateFormat("yyyy-MM-dd").format(spinnerInicio.getValue());
            String fechaFin = new SimpleDateFormat("yyyy-MM-dd").format(spinnerFin.getValue());

            Curso nuevo = new Curso(id, nombre, profesor, notaMaxima, fechaInicio, fechaFin);
            dao.registrarCurso(nuevo);
            JOptionPane.showMessageDialog(this, " Curso creado exitosamente");
            listarCursos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, " Error al crear curso: " + ex.getMessage());
        }
    }

    private void editarCurso() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText();
            Profesor profesor = (Profesor) comboProfesor.getSelectedItem();
            Double notaMaxima = Double.parseDouble(txtNotaMaxima.getText());
            String fechaInicio = new SimpleDateFormat("yyyy-MM-dd").format(spinnerInicio.getValue());
            String fechaFin = new SimpleDateFormat("yyyy-MM-dd").format(spinnerFin.getValue());

            Curso curso = new Curso(id, nombre, profesor, notaMaxima, fechaInicio, fechaFin);
            dao.actualizarCurso(curso);
            JOptionPane.showMessageDialog(this, "️ Curso actualizado");
            listarCursos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, " Error al editar curso: " + ex.getMessage());
        }
    }

    private void eliminarCurso() {
        try {
            int id = Integer.parseInt(txtId.getText());
            dao.eliminarCurso(id);
            JOptionPane.showMessageDialog(this, " Curso eliminado");
            listarCursos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, " Error al eliminar curso: " + ex.getMessage());
        }
    }

    private void abrirVentanaCalificar() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un ID de curso válido");
                return;
            }
            int id = Integer.parseInt(txtId.getText());
            Curso curso = dao.buscarCursoPorId(id);
            if (curso != null) {
                new VentanaCalificar(curso);
            } else {
                JOptionPane.showMessageDialog(this, " Curso no encontrado");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, " Error al abrir ventana de calificación: " + ex.getMessage());
        }
    }

    private void listarCursos() {
        modelo.setRowCount(0);
        try {
            ArrayList<Curso> lista = dao.consultarCursos();
            for (Curso c : lista) {
                int cantidad = dao.contarEstudiantesMatriculados(c.getId());
                modelo.addRow(new Object[]{
                        c.getId(),
                        c.getNombre(),
                        c.getProfesor().getNombre(),
                        c.getNotaMaxima(),
                        c.getFechaInicio(),
                        c.getFechaFin(),
                        cantidad
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, " Error al listar: " + ex.getMessage());
        }
    }
}
