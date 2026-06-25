package edu.universidad.sistema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaCurso extends JFrame {
    private JTextField txtId, txtNombre, txtNotaMaxima, txtFechaInicio, txtFechaFin;
    private JComboBox<Profesor> comboProfesor;
    private JButton btnCrear, btnListar, btnEditar, btnEliminar, btnBuscar, btnSalir;
    private JTable tablaCursos;
    private DefaultTableModel modelo;

    public VentanaCurso() {
        setTitle("Gestión de Cursos");
        setSize(850, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel superior con campos
        JPanel panelCampos = new JPanel(new GridLayout(6, 2, 10, 10));
        txtId = new JTextField();
        txtNombre = new JTextField();
        txtNotaMaxima = new JTextField();
        txtFechaInicio = new JTextField();
        txtFechaFin = new JTextField();
        comboProfesor = new JComboBox<>();

        // Cargar profesores en combo
        ProfesorDAO pdao = new ProfesorDAO();
        ArrayList<Profesor> profesores = pdao.consultarProfesores();
        for (Profesor p : profesores) {
            comboProfesor.addItem(p);
        }

        panelCampos.add(new JLabel("ID (buscar/editar/eliminar):"));
        panelCampos.add(txtId);
        panelCampos.add(new JLabel("Nombre del curso:"));
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Profesor:"));
        panelCampos.add(comboProfesor);
        panelCampos.add(new JLabel("Nota máxima:"));
        panelCampos.add(txtNotaMaxima);
        panelCampos.add(new JLabel("Fecha inicio (YYYY-MM-DD):"));
        panelCampos.add(txtFechaInicio);
        panelCampos.add(new JLabel("Fecha fin (YYYY-MM-DD):"));
        panelCampos.add(txtFechaFin);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnCrear = new JButton("➕ Crear");
        btnListar = new JButton("📋 Listar");
        btnEditar = new JButton("✏️ Editar");
        btnEliminar = new JButton("🗑️ Eliminar");
        btnBuscar = new JButton("🔍 Buscar");
        btnSalir = new JButton("❌ Salir");

        panelBotones.add(btnCrear);
        panelBotones.add(btnListar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnSalir);

        // Tabla para mostrar cursos
        String[] columnas = {"ID", "Nombre", "Profesor", "Nota Máxima", "Inicio", "Fin", "Estudiantes"};
        modelo = new DefaultTableModel(columnas, 0);
        tablaCursos = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tablaCursos);

        // Agregar todo al frame
        add(panelCampos, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        CursoDAO dao = new CursoDAO();

        // Crear curso
        btnCrear.addActionListener(e -> {
            try {
                Profesor prof = (Profesor) comboProfesor.getSelectedItem();
                Curso curso = new Curso(
                        0,
                        txtNombre.getText(),
                        prof,
                        Double.parseDouble(txtNotaMaxima.getText()),
                        txtFechaInicio.getText(),
                        txtFechaFin.getText()
                );
                dao.registrarCurso(curso);
                JOptionPane.showMessageDialog(this, " Curso creado correctamente");
                listarCursos(dao);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al crear: " + ex.getMessage());
            }
        });

        // Listar cursos
        btnListar.addActionListener(e -> listarCursos(dao));

        // Editar curso
        btnEditar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Profesor prof = (Profesor) comboProfesor.getSelectedItem();
                Curso curso = new Curso(
                        id,
                        txtNombre.getText(),
                        prof,
                        Double.parseDouble(txtNotaMaxima.getText()),
                        txtFechaInicio.getText(),
                        txtFechaFin.getText()
                );
                dao.actualizarCurso(curso);
                JOptionPane.showMessageDialog(this, " Curso editado correctamente");
                listarCursos(dao);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al editar: " + ex.getMessage());
            }
        });

        // Eliminar curso
        btnEliminar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                dao.eliminarCurso(id);
                JOptionPane.showMessageDialog(this, " Curso eliminado correctamente");
                listarCursos(dao);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al eliminar: " + ex.getMessage());
            }
        });

        // Buscar curso
        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Curso c = dao.buscarCursoPorId(id);
                if (c != null) {
                    txtNombre.setText(c.getNombre());
                    comboProfesor.setSelectedItem(c.getProfesor());
                    txtNotaMaxima.setText(String.valueOf(c.getNotaMaxima()));
                    txtFechaInicio.setText(c.getFechaInicio());
                    txtFechaFin.setText(c.getFechaFin());
                    JOptionPane.showMessageDialog(this, " Curso encontrado.");
                } else {
                    JOptionPane.showMessageDialog(this, " No se encontró curso con ese ID.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al buscar: " + ex.getMessage());
            }
        });

        // Cerrar ventana
        btnSalir.addActionListener(e -> dispose());

        setVisible(true);
    }

    // Método para refrescar la tabla
    private void listarCursos(CursoDAO dao) {
        modelo.setRowCount(0);
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
    }
}
