import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class QuerySeis extends JFrame {

	private JPanel contentPane;
	private JTable table;
	DefaultTableModel model = new DefaultTableModel();
	

	/**
	 * Create the frame.
	 */
	public QuerySeis() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 637, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel labelJV = new JLabel("-");
		labelJV.setBounds(418, 26, 193, 14);
		contentPane.add(labelJV);
		
		model.addColumn("Patrocinador");
	    model.addColumn("Quantidade de Jogos Patrocinados");
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPane.setVisible(false);
				dispose();
			}
		});
		btnVoltar.setBounds(244, 407, 89, 23);
		contentPane.add(btnVoltar);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {	//Busca patrocinadores e conta quantidade de jogos patrocinados
					PreparedStatement preparedStmt = GerarConexao.c.prepareStatement("SELECT P.NOME, COUNT(J.TITULO) " + 
							"FROM PATROCINADOR P JOIN FINANCIAMENTO F " + 
							"                    ON P.CNPJ = F.CNPJ " + 
							"                    JOIN JOGO J " + 
							"                    ON J.TITULO = F.TITULO " + 
							"GROUP BY P.NOME " + 
							"ORDER BY P.NOME");
					ResultSet rset = preparedStmt.executeQuery();
					
					while(rset.next())
					{
						model.addRow(new Object[]{rset.getString(1), ""+rset.getInt(2)});
					}
					//Busca jogo mais patrocinado e quantidade de patrocinios
					PreparedStatement preparedStmt2 = GerarConexao.c.prepareStatement("SELECT * " + 
							"FROM( " + 
							"SELECT J.TITULO, COUNT(P.NOME) " + 
							"FROM JOGO J JOIN FINANCIAMENTO F " + 
							"              ON J.TITULO = F.TITULO " + 
							"            JOIN PATROCINADOR P " + 
							"              ON P.CNPJ = F.CNPJ " + 
							"GROUP BY J.TITULO " + 
							"HAVING COUNT(P.NOME) > 0 " + 
							"ORDER BY COUNT(P.NOME) DESC) " + 
							"WHERE ROWNUM = 1");
					
					ResultSet rset2 = preparedStmt2.executeQuery();
					
					rset2.next();
					labelJV.setText(rset2.getString(1)+" - "+rset2.getInt(2)+" Patrocinadores");
					
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Erro na busca.");
					//e1.printStackTrace();
				}
				
				
			}
		});
		btnBuscar.setBounds(21, 22, 89, 23);
		contentPane.add(btnBuscar);
		
		JLabel lblJogoMP = new JLabel("Jogo mais patrocinado:");
		lblJogoMP.setBounds(259, 26, 150, 14);
		contentPane.add(lblJogoMP);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 100, 578, 280);
		contentPane.add(scrollPane);
		
		table = new JTable(model);
		scrollPane.setViewportView(table);
	
	}

}
