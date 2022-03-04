import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class QueryQuatro extends JFrame {

	private JPanel contentPane;
	DefaultTableModel model = new DefaultTableModel();
	DefaultTableModel model2 = new DefaultTableModel();
	private JTable table;
	private JTable table_1;

	/**
	 * Create the frame.
	 */
	public QueryQuatro() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1071, 758);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		model.addColumn("Nome");
	    model.addColumn("Data de Fundação");
	    model.addColumn("Número de Funcionários");
	    model.addColumn("Localização");
	    model.addColumn("Estande");
	    model.addColumn("Jogo");
	    model.addColumn("Patrocinador");
		
	    model2.addColumn("Nome");
	    model2.addColumn("Data de Fundação");
	    model2.addColumn("Número de Funcionários");
	    model2.addColumn("Localização");
	    model2.addColumn("Estande");
	    model2.addColumn("Jogo");
	    model2.addColumn("Empresa");
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {	//Codigo da busca
			public void actionPerformed(ActionEvent e) {
				try {	//Comando para retornar informacao dos estudios de empresas
					PreparedStatement preparedStmt= GerarConexao.c.prepareStatement("SELECT DISTINCT EST.NOME,TO_CHAR(EST.DATA_FUNDACAO,'DD/MM/YYYY'),EST.NUM_FUNCIONARIOS,EST.LOCALIZACAO,EST.NUMERACAO,EMP.NOME FROM ESTUDIO EST,EMPRESA EMP WHERE EST.CNPJ=EMP.CNPJ " + 
							"ORDER BY EST.NOME");
					ResultSet rset = preparedStmt.executeQuery();
			
						
						//Comando SQL para buscar jogos de estudios de empresas
						PreparedStatement preparedStmtJ = GerarConexao.c.prepareStatement("SELECT EST.NOME,J1.TITULO FROM ESTUDIO EST,JOGO J1 WHERE J1.NOME=EST.NOME AND EST.INDIE='0' " + 
								"ORDER BY EST.NOME",ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
						ResultSet rsetJ = preparedStmtJ.executeQuery();
						
							
						// Concatenacao dos jogos na tupla estudio de empresas
						int qtd;
						int contador=1;
						String jogos="";
						
						if(rsetJ.last())
						{
							qtd=rsetJ.getRow(); //Tem jogos / qtd=quantidade de linhas recuperadas do rsetJ
							rsetJ.beforeFirst();
							rsetJ.next();
						}	
						else
						{
							qtd=0; //Nao tem plataformas
						}
						
						while(rset.next())
						{
							while(contador<=qtd)	
							{
								if(rset.getString(1).equals(rsetJ.getString(1)))	//enquanto as strings forem iguais, adiciona um novo jogo.
								{
									if(jogos.contentEquals(""))
									{
										jogos=jogos+rsetJ.getString(2);
									}
									else
									{
										jogos=jogos+"|"+rsetJ.getString(2);
									}
									rsetJ.next();
									contador++;
								}
								else
								{
									break;		//quando forem diferentes, sai do loop
								}
							}
							
							model2.addRow(new Object[]{rset.getString(1), rset.getString(2),rset.getString(3),rset.getString(4),rset.getString(5),jogos,rset.getString(6)});
							jogos="";
						}
						
						
						//Comando SQL para retornar informacao dos estudios indies
						PreparedStatement preparedStmt2= GerarConexao.c.prepareStatement("SELECT EST.NOME,TO_CHAR(EST.DATA_FUNDACAO,'DD/MM/YYYY'),EST.NUM_FUNCIONARIOS,EST.LOCALIZACAO,EST.NUMERACAO,J1.TITULO FROM ESTUDIO EST LEFT JOIN JOGO J1 ON EST.NOME=J1.NOME WHERE EST.INDIE='1' " + 
								"ORDER BY J1.TITULO");
						ResultSet rset2 = preparedStmt2.executeQuery();
						
						//Comando SQL para retornar jogos e respectivos patrocinadores.
						PreparedStatement preparedStmtP= GerarConexao.c.prepareStatement("SELECT J1.TITULO,PAT.NOME FROM JOGO J1, PATROCINADOR PAT, FINANCIAMENTO F WHERE F.CNPJ=PAT.CNPJ AND F.TITULO=J1.TITULO ORDER BY J1.TITULO",ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
						ResultSet rsetP = preparedStmtP.executeQuery();
						
						// Concatenacao dos patrocinadores por jogo
						int qtd2;
						int contador2=1;
						String pat="";
						
						if(rsetP.last())
						{
							qtd2=rsetP.getRow(); //Tem patrocinador / qtd=quantidade de linhas recuperadas do rsetP
							rsetP.beforeFirst();
							rsetP.next();
						}	
						else
						{
							qtd2=0; //Nao tem patrocinadores
						}
						
						while(rset2.next())
						{
							while(contador2<=qtd2)	
							{
								if(rset2.getString(6).equals(rsetP.getString(1)))	//enquanto as strings forem iguais, adiciona um novo patrocinador.
								{
									if(pat.contentEquals(""))
									{
										pat=pat+rsetP.getString(2);
									}
									else
									{
										pat=pat+"|"+rsetP.getString(2);
									}
									rsetP.next();
									contador2++;
								}
								else
								{
									break;		//quando forem diferentes, sai do loop
								}
							}
							
							model.addRow(new Object[]{rset2.getString(1), rset2.getString(2),rset2.getString(3),rset2.getString(4),rset2.getString(5),rset2.getString(6),pat});
							pat="";
						}
						
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Erro na busca.");
					//e1.printStackTrace();
				}
				
			}
		});
		btnBuscar.setBounds(10, 11, 89, 23);
		contentPane.add(btnBuscar);
		
		JLabel lblEstdiosIndies = new JLabel("Est\u00FAdios Indies:");
		lblEstdiosIndies.setBounds(10, 51, 114, 14);
		contentPane.add(lblEstdiosIndies);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 76, 1035, 301);
		contentPane.add(scrollPane);
		
		table = new JTable(model);
		scrollPane.setViewportView(table);
		
		JLabel lblEstdiosDeEmpresas = new JLabel("Est\u00FAdios de Empresas:");
		lblEstdiosDeEmpresas.setBounds(10, 401, 150, 14);
		contentPane.add(lblEstdiosDeEmpresas);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 426, 1035, 251);
		contentPane.add(scrollPane_1);
		
		table_1 = new JTable(model2);
		scrollPane_1.setViewportView(table_1);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPane.setVisible(false);
				dispose();
			}
		});
		btnVoltar.setBounds(485, 688, 89, 23);
		contentPane.add(btnVoltar);
	}
}
