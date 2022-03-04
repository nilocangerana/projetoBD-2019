import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class GerarConexao {
	
	public static Connection c;
	public void Conectar()
	{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			c = DriverManager.getConnection("string_connection");
		} catch (ClassNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "Erro de driver.");
			//e1.printStackTrace();
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, "Erro na conexão com o banco de dados.");
			//e1.printStackTrace();
		}
	}
}
