package edu.universidad.sistema;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VentanaEstudiante extends JFrame {
    private JTextField txtId, txtNombre, txtCorreo, txtCarrera;
    private JButton btnRegistrar, btnListar, btnActualizar, btnEliminar, btnBuscar, btnCerrar, btnCursos;
    private JTextArea areaEstudiantes;

    public VentanaEstudiante() {
        setTitle("Gestión de Estudiantes");
        setSize(700, 520);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel superior con campos
        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 10, 10));
        txtId = new JTextField();
        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtCarrera = new JTextField();

        panelCampos.add(new JLabel("ID (buscar/actualizar/eliminar):"));
        panelCampos.add(txtId);
        panelCampos.add(new JLabel("Nombre:"));
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Correo:"));
        panelCampos.add(txtCorreo);
        panelCampos.add(new JLabel("Carrera:"));
        panelCampos.add(txtCarrera);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnRegistrar = new JButton("Registrar");
        btnListar = new JButton("Listar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnBuscar = new JButton("Buscar");
        btnCursos = new JButton("Cursos");   // 🔹 Nuevo botón
        btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnListar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnCursos);         // 🔹 Agregado al panel
        panelBotones.add(btnCerrar);

        // Área central para mostrar estudiantes
        areaEstudiantes = new JTextArea();
        areaEstudiantes.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaEstudiantes);

        // Agregar todo al frame
        add(panelCampos, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        EstudianteDAO dao = new EstudianteDAO();

        // Registrar estudiante con validación de duplicados
        btnRegistrar.addActionListener(e -> {
            try {
                ArrayList<Estudiante> lista = dao.consultarEstudiantes();
                boolean existe = lista.stream()
                        .anyMatch(est -> est.getCorreo().equalsIgnoreCase(txtCorreo.getText()));

                if (existe) {
                    JOptionPane.showMessageDialog(this,
                            "⚠️ Este estudiante ya está registrado.",
                            "Duplicado", JOptionPane.WARNING_MESSAGE);
                } else {
                    dao.registrarEstudiante(new Estudiante(
                            0,
                            txtNombre.getText(),
                            txtCorreo.getText(),
                            txtCarrera.getText()
                    ));
                    JOptionPane.showMessageDialog(this, " Estudiante registrado correctamente");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al registrar: " + ex.getMessage());
            }
        });

        // Listar estudiantes
        btnListar.addActionListener(e -> {
            try {
                ArrayList<Estudiante> lista = dao.consultarEstudiantes();
                areaEstudiantes.setText("");
                for (Estudiante est : lista) {
                    areaEstudiantes.append(est.toString() + "\n");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al listar: " + ex.getMessage());
            }
        });

        // Actualizar estudiante
        btnActualizar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Estudiante est = new Estudiante(id, txtNombre.getText(), txtCorreo.getText(), txtCarrera.getText());
                dao.actualizarEstudiante(est);
                JOptionPane.showMessageDialog(this, " Estudiante actualizado correctamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al actualizar: " + ex.getMessage());
            }
        });

        // Eliminar estudiante
        btnEliminar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                dao.eliminarEstudiante(id);
                JOptionPane.showMessageDialog(this, " Estudiante eliminado correctamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al eliminar: " + ex.getMessage());
            }
        });

        // Buscar estudiante por ID
        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Estudiante est = dao.buscarEstudiantePorId(id);
                if (est != null) {
                    txtNombre.setText(est.getNombre());
                    txtCorreo.setText(est.getCorreo());
                    txtCarrera.setText(est.getCarrera());
                    JOptionPane.showMessageDialog(this, " Estudiante encontrado y cargado.");
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ No se encontró estudiante con ese ID.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al buscar: " + ex.getMessage());
            }
        });

        // 🔹 Nuevo botón Cursos → abre VentanaMatricula
        btnCursos.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Estudiante est = dao.buscarEstudiantePorId(id);
                if (est != null) {
                    new VentanaMatricula(est).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ Debe ingresar un ID válido antes de matricular.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al abrir matrícula: " + ex.getMessage());
            }
        });

        // Repintar datos automáticamente al perder foco en ID
        txtId.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                try {
                    int id = Integer.parseInt(txtId.getText());
                    Estudiante est = dao.buscarEstudiantePorId(id);
                    if (est != null) {
                        txtNombre.setText(est.getNombre());
                        txtCorreo.setText(est.getCorreo());
                        txtCarrera.setText(est.getCarrera());
                    }
                } catch (Exception ex) {
                    // ignorar si está vacío o no es número
                }
            }
        });

        // Botón cerrar
        btnCerrar.addActionListener(e -> dispose());

        setVisible(true);
    }
}
