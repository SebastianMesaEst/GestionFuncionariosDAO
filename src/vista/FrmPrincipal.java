package vista;

import dao.FuncionarioDAO;
import dao.exception.DAOException;
import modelo.Funcionario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class FrmPrincipal extends javax.swing.JFrame {

    private final FuncionarioDAO funcionarioDAO;
    private DefaultTableModel modeloTabla;

    public FrmPrincipal() {
        initComponents();
        funcionarioDAO = new FuncionarioDAO();
        configurarTabla();
        cargarDatosTabla();
    }

    private void initComponents() {
        setTitle("Gestión de Funcionarios");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Crear componentes
        btnNuevo = new JButton("Nuevo");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar");

        tblFuncionarios = new JTable();
        jScrollPane1 = new JScrollPane(tblFuncionarios);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);

        // Layout principal
        setLayout(new java.awt.BorderLayout());
        add(panelBotones, java.awt.BorderLayout.NORTH);
        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        // Agregar listeners
        btnNuevo.addActionListener(this::btnNuevoActionPerformed);
        btnEditar.addActionListener(this::btnEditarActionPerformed);
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido");
        modeloTabla.addColumn("Cédula");
        modeloTabla.addColumn("Email");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Fecha Ingreso");
        modeloTabla.addColumn("Salario");
        modeloTabla.addColumn("Estado");

        tblFuncionarios.setModel(modeloTabla);
    }

    private void cargarDatosTabla() {
        try {
            modeloTabla.setRowCount(0);
            List<Funcionario> lista = funcionarioDAO.listarTodos();

            for (Funcionario f : lista) {
                Object[] fila = {
                    f.getIdFuncionario(),
                    f.getNombre(),
                    f.getApellido(),
                    f.getCedula(),
                    f.getEmail(),
                    f.getTelefono(),
                    f.getFechaIngreso() != null ? f.getFechaIngreso().toString() : "",
                    f.getSalario(),
                    f.getEstado()
                };
                modeloTabla.addRow(fila);
            }
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {
        FrmFuncionarioDialog dialog = new FrmFuncionarioDialog(this, null, funcionarioDAO, this::cargarDatosTabla);
        dialog.setVisible(true);
    }

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = tblFuncionarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un funcionario para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        try {
            Funcionario f = funcionarioDAO.buscarPorId(id);
            if (f != null) {
                FrmFuncionarioDialog dialog = new FrmFuncionarioDialog(this, f, funcionarioDAO, this::cargarDatosTabla);
                dialog.setVisible(true);
            }
        } catch (DAOException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = tblFuncionarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un funcionario para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        String apellido = (String) modeloTabla.getValueAt(fila, 2);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar a " + nombre + " " + apellido + "?", 
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (funcionarioDAO.eliminar(id)) {
                    JOptionPane.showMessageDialog(this, "Funcionario eliminado correctamente");
                    cargarDatosTabla();
                }
            } catch (DAOException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {
        cargarDatosTabla();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new FrmPrincipal().setVisible(true));
    }

    // Variables
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblFuncionarios;
}