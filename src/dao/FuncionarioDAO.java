package dao;

import dao.exception.DAOException;
import modelo.Funcionario;
import util.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    // ====================== CREATE ======================
    public void insertar(Funcionario f) throws DAOException {
        String sql = "INSERT INTO funcionarios (nombre, apellido, cedula, fecha_nacimiento, email, telefono, direccion, fecha_ingreso, salario, estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setParameters(ps, f);

            ps.executeUpdate();

            // Obtener el ID generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    f.setIdFuncionario(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al insertar el funcionario", e);
        }
    }

    // ====================== READ ======================
    public List<Funcionario> listarTodos() throws DAOException {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionarios ORDER BY apellido, nombre";

        try (Connection conn = ConexionBD.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar funcionarios", e);
        }
        return lista;
    }

    public Funcionario buscarPorId(int id) throws DAOException {
        String sql = "SELECT * FROM funcionarios WHERE id_funcionario = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar funcionario por ID", e);
        }
        return null;
    }

    // ====================== UPDATE ======================
    public boolean actualizar(Funcionario f) throws DAOException {
        String sql = "UPDATE funcionarios SET nombre=?, apellido=?, cedula=?, fecha_nacimiento=?, "
                   + "email=?, telefono=?, direccion=?, fecha_ingreso=?, salario=?, estado=? "
                   + "WHERE id_funcionario=?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setParameters(ps, f);
            ps.setInt(11, f.getIdFuncionario());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar el funcionario", e);
        }
    }

    // ====================== DELETE ======================
    public boolean eliminar(int id) throws DAOException {
        String sql = "DELETE FROM funcionarios WHERE id_funcionario = ?";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar el funcionario", e);
        }
    }

    // ====================== MÉTODOS AUXILIARES ======================
    private void setParameters(PreparedStatement ps, Funcionario f) throws SQLException {
        ps.setString(1, f.getNombre());
        ps.setString(2, f.getApellido());
        ps.setString(3, f.getCedula());
        ps.setDate(4, f.getFechaNacimiento() != null ? Date.valueOf(f.getFechaNacimiento()) : null);
        ps.setString(5, f.getEmail());
        ps.setString(6, f.getTelefono());
        ps.setString(7, f.getDireccion());
        ps.setDate(8, Date.valueOf(f.getFechaIngreso()));
        ps.setBigDecimal(9, f.getSalario());
        ps.setString(10, f.getEstado());
    }

    private Funcionario mapear(ResultSet rs) throws SQLException {
        Funcionario f = new Funcionario();
        f.setIdFuncionario(rs.getInt("id_funcionario"));
        f.setNombre(rs.getString("nombre"));
        f.setApellido(rs.getString("apellido"));
        f.setCedula(rs.getString("cedula"));
        if (rs.getDate("fecha_nacimiento") != null) {
            f.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        }
        f.setEmail(rs.getString("email"));
        f.setTelefono(rs.getString("telefono"));
        f.setDireccion(rs.getString("direccion"));
        if (rs.getDate("fecha_ingreso") != null) {
            f.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
        }
        f.setSalario(rs.getBigDecimal("salario"));
        f.setEstado(rs.getString("estado"));
        return f;
    }
}