package vista;

import dao.FuncionarioDAO;
import dao.exception.DAOException;
import modelo.Funcionario;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FrmFuncionarioDialog extends JDialog {

    private final FuncionarioDAO dao;
    private final Funcionario funcionarioActual;
    private final Runnable callbackActualizar;

    private JTextField txtNombre, txtApellido, txtCedula, txtEmail, txtTelefono, txtDireccion, txtSalario;
    private JFormattedTextField txtFechaNacimiento, txtFechaIngreso;
    private JComboBox<String> cbEstado;

    public FrmFuncionarioDialog(JFrame parent, Funcionario funcionario, FuncionarioDAO dao, Runnable callback) {
        super(parent, true);
        this.dao = dao;
        this.funcionarioActual = funcionario;
        this.callbackActualizar = callback;

        setTitle(funcionario == null ? "Nuevo Funcionario" : "Editar Funcionario");
        setSize(480, 580);
        setLocationRelativeTo(parent);
        initComponents();
        if (funcionario != null) cargarDatos(funcionario);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(10, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        panel.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        panel.add(txtApellido);

        panel.add(new JLabel("Cédula:"));
        txtCedula = new JTextField();
        panel.add(txtCedula);

        panel.add(new JLabel("Fecha Nacimiento (yyyy-mm-dd):"));
        txtFechaNacimiento = new JFormattedTextField();
        panel.add(txtFechaNacimiento);

        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        panel.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panel.add(txtTelefono);

        panel.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panel.add(txtDireccion);

        panel.add(new JLabel("Fecha Ingreso (yyyy-mm-dd):"));
        txtFechaIngreso = new JFormattedTextField();
        panel.add(txtFechaIngreso);

        panel.add(new JLabel("Salario:"));
        txtSalario = new JTextField();
        panel.add(txtSalario);

        panel.add(new JLabel("Estado:"));
        cbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        panel.add(cbEstado);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardar());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JPanel botones = new JPanel();
        botones.add(btnGuardar);
        botones.add(btnCancelar);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    private void cargarDatos(Funcionario f) {
        txtNombre.setText(f.getNombre());
        txtApellido.setText(f.getApellido());
        txtCedula.setText(f.getCedula());
        txtFechaNacimiento.setText(f.getFechaNacimiento() != null ? f.getFechaNacimiento().toString() : "");
        txtEmail.setText(f.getEmail());
        txtTelefono.setText(f.getTelefono());
        txtDireccion.setText(f.getDireccion());
        txtFechaIngreso.setText(f.getFechaIngreso() != null ? f.getFechaIngreso().toString() : "");
        txtSalario.setText(f.getSalario() != null ? f.getSalario().toString() : "");
        cbEstado.setSelectedItem(f.getEstado());
    }

    private void guardar() {
        try {
            Funcionario f = funcionarioActual != null ? funcionarioActual : new Funcionario();

            f.setNombre(txtNombre.getText().trim());
            f.setApellido(txtApellido.getText().trim());
            f.setCedula(txtCedula.getText().trim());
            f.setEmail(txtEmail.getText().trim());
            f.setTelefono(txtTelefono.getText().trim());
            f.setDireccion(txtDireccion.getText().trim());
            f.setEstado((String) cbEstado.getSelectedItem());

            if (!txtFechaNacimiento.getText().trim().isEmpty()) {
                f.setFechaNacimiento(LocalDate.parse(txtFechaNacimiento.getText().trim()));
            }
            if (!txtFechaIngreso.getText().trim().isEmpty()) {
                f.setFechaIngreso(LocalDate.parse(txtFechaIngreso.getText().trim()));
            }
            if (!txtSalario.getText().trim().isEmpty()) {
                f.setSalario(new BigDecimal(txtSalario.getText().trim()));
            }

            if (funcionarioActual == null) {
                dao.insertar(f);
                JOptionPane.showMessageDialog(this, "Funcionario creado correctamente");
            } else {
                dao.actualizar(f);
                JOptionPane.showMessageDialog(this, "Funcionario actualizado correctamente");
            }

            if (callbackActualizar != null) callbackActualizar.run();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}