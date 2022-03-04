import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class QueryCinco extends JFrame {

	private JPanel contentPane;
	private JTable table;
	DefaultTableModel model = new DefaultTableModel();

	/**
	 * Create the frame.
	 */
	public QueryCinco() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 674, 503);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		model.addColumn("Nome da Empresa");
	    model.addColumn("Quantidade de Público");
		
		JButton btnContar = new JButton("Contar");
		btnContar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Comando SQL para realizar a contagem de publico que assiste a apresentacao por empresa.
					try {
						PreparedStatement preparedStmt = GerarConexao.c.prepareStatement("SELECT E.NOME , COUNT(P.NUM_CRACHA) AS CONTAGEM " + 
								"FROM EMPRESA E JOIN APRESENTACAO A " + 
								"                 ON E.CNPJ = A.CNPJ " + 
								"               JOIN ASSISTE_APRESENTACAO A_A " + 
								"                 ON A_A.DATA_HORA_INICIO = A.DATA_HORA_INICIO " + 
								"                                JOIN PUBLICO P " + 
								"                 ON p.num_cracha = a_a.num_cracha " + 
								"GROUP BY E.NOME " + 
								"ORDER BY CONTAGEM DESC,E.NOME");

						ResultSet rset = preparedStmt.executeQuery();
						
						while(rset.next())
						{
							model.addRow(new Object[]{rset.getString(1), ""+rset.getInt(2)});
						}
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, "Erro na busca.");
						//e1.printStackTrace();
					}

			}
		});
		btnContar.setBounds(22, 21, 89, 23);
		contentPane.add(btnContar);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPane.setVisible(false);
				dispose();
			}
		});
		btnVoltar.setBounds(263, 430, 89, 23);
		contentPane.add(btnVoltar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(36, 94, 581, 301);
		contentPane.add(scrollPane);
		
		table = new JTable(model);
		scrollPane.setViewportView(table);
	}
}
