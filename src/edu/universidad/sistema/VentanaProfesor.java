package edu.universidad.sistema;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VentanaProfesor extends JFrame {
    private JTextField txtId, txtNombre, txtEspecialidad;
    private JButton btnRegistrar, btnListar, btnActualizar, btnEliminar, btnBuscar, btnCerrar;
    private JTextArea areaProfesores;

    public VentanaProfesor() {
        setTitle("Gestión de Profesores");
        setSize(600, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel superior con campos
        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 10, 10));
        txtId = new JTextField();
        txtNombre = new JTextField();
        txtEspecialidad = new JTextField();

        panelCampos.add(new JLabel("ID (buscar/actualizar/eliminar):"));
        panelCampos.add(txtId);
        panelCampos.add(new JLabel("Nombre:"));
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Especialidad:"));
        panelCampos.add(txtEspecialidad);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnRegistrar = new JButton("Registrar");
        btnListar = new JButton("Listar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnBuscar = new JButton("Buscar");
        btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnListar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnCerrar);

        // Área central para mostrar profesores
        areaProfesores = new JTextArea();
        areaProfesores.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaProfesores);

        // Agregar todo al frame
        add(panelCampos, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        ProfesorDAO dao = new ProfesorDAO();

        // Registrar profesor con validación de duplicados
        btnRegistrar.addActionListener(e -> {
            try {
                ArrayList<Profesor> lista = dao.consultarProfesores();
                boolean existe = lista.stream()
                        .anyMatch(prof -> prof.getNombre().equalsIgnoreCase(txtNombre.getText()));

                if (existe) {
                    JOptionPane.showMessageDialog(this,
                            "⚠️ Este profesor ya está registrado.",
                            "Duplicado", JOptionPane.WARNING_MESSAGE);
                } else {
                    dao.registrarProfesor(new Profesor(
                            0,
                            txtNombre.getText(),
                            txtEspecialidad.getText()
                    ));
                    JOptionPane.showMessageDialog(this, " Profesor registrado correctamente");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al registrar: " + ex.getMessage());
            }
        });

        // Listar profesores
        btnListar.addActionListener(e -> {
            try {
                ArrayList<Profesor> lista = dao.consultarProfesores();
                areaProfesores.setText("");
                for (Profesor prof : lista) {
                    areaProfesores.append(prof.toString() + "\n");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al listar: " + ex.getMessage());
            }
        });

        // Actualizar profesor
        btnActualizar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Profesor prof = new Profesor(id, txtNombre.getText(), txtEspecialidad.getText());
                dao.actualizarProfesor(prof);
                JOptionPane.showMessageDialog(this, " Profesor actualizado correctamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al actualizar: " + ex.getMessage());
            }
        });

        // Eliminar profesor
        btnEliminar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                dao.eliminarProfesor(id);
                JOptionPane.showMessageDialog(this, " Profesor eliminado correctamente");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al eliminar: " + ex.getMessage());
            }
        });

        // Buscar profesor por ID
        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Profesor prof = dao.buscarProfesorPorId(id);
                if (prof != null) {
                    txtNombre.setText(prof.getNombre());
                    txtEspecialidad.setText(prof.getEspecialidad());
                    JOptionPane.showMessageDialog(this, " Profesor encontrado y cargado.");
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ No se encontró profesor con ese ID.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Error al buscar: " + ex.getMessage());
            }
        });

        // Repintar datos automáticamente al perder foco en ID
        txtId.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                try {
                    int id = Integer.parseInt(txtId.getText());
                    Profesor prof = dao.buscarProfesorPorId(id);
                    if (prof != null) {
                        txtNombre.setText(prof.getNombre());
                        txtEspecialidad.setText(prof.getEspecialidad());
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


