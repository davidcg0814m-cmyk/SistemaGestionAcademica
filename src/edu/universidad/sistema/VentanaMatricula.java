package edu.universidad.sistema;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VentanaMatricula extends JFrame {
    private JComboBox<Curso> comboCursos;
    private JButton btnMatricular, btnCerrar;
    private Estudiante estudiante;
    private CursoDAO cursoDAO;
    private CursoEstudianteDAO cursoEstudianteDAO;

    public VentanaMatricula(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.cursoDAO = new CursoDAO();
        this.cursoEstudianteDAO = new CursoEstudianteDAO();

        setTitle("Matricular Estudiante: " + estudiante.getNombre());
        setSize(400, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        comboCursos = new JComboBox<>();
        cargarCursos();

        btnMatricular = new JButton("📚 Matricular");
        btnCerrar = new JButton("🚪 Cerrar");

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.add(comboCursos);
        panel.add(btnMatricular);
        panel.add(btnCerrar);

        add(panel);

        // Acciones
        btnMatricular.addActionListener(e -> matricular());
        btnCerrar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void cargarCursos() {
        ArrayList<Curso> cursos = cursoDAO.consultarCursos();
        for (Curso c : cursos) {
            comboCursos.addItem(c);
        }
    }

    private void matricular() {
        Curso cursoSeleccionado = (Curso) comboCursos.getSelectedItem();
        if (cursoSeleccionado != null) {
            cursoEstudianteDAO.matricularEstudiante(cursoSeleccionado.getId(), estudiante.getId());
            JOptionPane.showMessageDialog(this, " Estudiante matriculado en " + cursoSeleccionado.getNombre());
        }
    }
}
