import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Query3 extends JFrame {

	private JPanel contentPane;
	DefaultTableModel model = new DefaultTableModel();
	DefaultTableModel model2 = new DefaultTableModel();
	private JTextField textFieldCnpj;
	private JTable table;
	private JTable table_1;

	/**
	 * Create the frame.
	 */
	public Query3() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 883, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblEmpresacnpj = new JLabel("Empresa(CNPJ):");
		lblEmpresacnpj.setBounds(10, 11, 107, 14);
		contentPane.add(lblEmpresacnpj);
		
		textFieldCnpj = new JTextField();
		textFieldCnpj.setBounds(105, 8, 117, 20);
		contentPane.add(textFieldCnpj);
		textFieldCnpj.setColumns(10);
		
	    model.addColumn("Data/Hora");
	    model.addColumn("Categoria");
	    model.addColumn("Palestrante/Apresentador");
	    model.addColumn("Tema/Foco");
	    model.addColumn("Local");
		table = new JTable(model);
		
		model2.addColumn("Título");
		model2.addColumn("Data de Lançamento");
	    model2.addColumn("Preço");
	    model2.addColumn("DLC");
	    model2.addColumn("Plataformas");
		
		JLabel lblEstande = new JLabel("Estande:");
		lblEstande.setBounds(362, 11, 59, 14);
		contentPane.add(lblEstande);
		
		JLabel labelEstnd = new JLabel("0");
		labelEstnd.setBounds(445, 11, 46, 14);
		contentPane.add(labelEstnd);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//comando SQL para buscar estande da empresa
				try {
					PreparedStatement preparedStmt= GerarConexao.c.prepareStatement("SELECT EST.NUMERACAO FROM EMPRESA EMP,ESTANDE EST WHERE EMP.NUMERACAO=EST.NUMERACAO AND EMP.CNPJ=?");
					preparedStmt.setString (1,textFieldCnpj.getText());
					ResultSet rset = preparedStmt.executeQuery();
					rset.next();
						labelEstnd.setText(""+rset.getInt(1));
						
						//Comando SQL para buscar informacoes de apresentacao
						PreparedStatement preparedStmt2 = GerarConexao.c.prepareStatement("SELECT TO_CHAR(APR.DATA_HORA_INICIO,'DD/MM/YYYY HH24:MI:SS') AS DATA1, APR.CATEGORIA, PA_APR.PALESTRANTE,PA_APR.TEMA, APR.LOCALIZACAO FROM EMPRESA EMP,APRESENTACAO APR,PALESTRA PA_APR,ESTUDIO EST,JOGO J1 " + 
								"WHERE APR.DATA_HORA_INICIO=PA_APR.DATA_HORA_INICIO AND APR.CNPJ=EMP.CNPJ AND EST.CNPJ=EMP.CNPJ AND EMP.CNPJ=? " + 
								"UNION " + 
								"SELECT TO_CHAR(APR.DATA_HORA_INICIO,'DD/MM/YYYY HH24:MI:SS') AS DATA1, APR.CATEGORIA, PU_APR.APRESENTADOR,PU_APR.FOCO, APR.LOCALIZACAO FROM EMPRESA EMP,APRESENTACAO APR,PUBLICIDADE PU_APR,ESTUDIO EST,JOGO J1 " + 
								"WHERE APR.DATA_HORA_INICIO=PU_APR.DATA_HORA_INICIO AND APR.CNPJ=EMP.CNPJ AND EST.CNPJ=EMP.CNPJ AND EMP.CNPJ=? " + 
								"ORDER BY DATA1");
						preparedStmt2.setString (1,textFieldCnpj.getText());
						preparedStmt2.setString (2,textFieldCnpj.getText());
						ResultSet rset2 = preparedStmt2.executeQuery();
						
						while(rset2.next())
						{
							model.addRow(new Object[]{rset2.getString(1), rset2.getString(2),rset2.getString(3),rset2.getString(4),rset2.getString(5)});
						}
						
					
							
						//Comando SQL para buscar jogos
							PreparedStatement preparedStmt3 = GerarConexao.c.prepareStatement("SELECT J1.TITULO,TO_CHAR(J1.DATA_DE_LANCAMENTO,'DD/MM/YYYY'),J1.PRECO,J1.DLC FROM JOGO J1,ESTUDIO EST, EMPRESA EMP WHERE J1.NOME=EST.NOME AND EST.CNPJ = EMP.CNPJ AND EMP.CNPJ=? ORDER BY J1.TITULO");
							preparedStmt3.setString (1,textFieldCnpj.getText());
							ResultSet rset3 = preparedStmt3.executeQuery();
							//Comando SQL para buscar plataformas dos jogos
							PreparedStatement preparedStmtP = GerarConexao.c.prepareStatement("SELECT J1.TITULO,P1.NOME_DA_PLATAFORMA FROM JOGO J1,ESTUDIO EST, EMPRESA EMP,PLATAFORMA P1 WHERE J1.NOME=EST.NOME AND EST.CNPJ = EMP.CNPJ AND EMP.CNPJ=? AND P1.TITULO=J1.TITULO ORDER BY J1.TITULO",ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
							preparedStmtP.setString (1,textFieldCnpj.getText());
							ResultSet rsetP = preparedStmtP.executeQuery();
							
							// Concatenacao das plataformas dos jogos
							int qtd;
							int contador=1;
							String plat="";
							
							if(rsetP.last())
							{
								qtd=rsetP.getRow(); //Tem platafomas / qtd=quantidade de linhas recuperadas do rsetP
								rsetP.beforeFirst();
								rsetP.next();
							}	
							else
							{
								qtd=0; //Nao tem plataformas
							}
							
							while(rset3.next())
							{
								while(contador<=qtd)	
								{
									if(rset3.getString(1).equals(rsetP.getString(1)))	//enquanto as strings forem iguais, adiciona um nova plataforma.
									{
										if(plat.contentEquals(""))
										{
											plat=plat+rsetP.getString(2);
										}
										else
										{
											plat=plat+"|"+rsetP.getString(2);
										}
										rsetP.next();
										contador++;
									}
									else
									{
										break;		//quando forem diferentes, sai do loop
									}
								}
								
								if(rset3.getString(4).equals("1"))
								{
									model2.addRow(new Object[]{rset3.getString(1), rset3.getString(2),"$ "+ rset3.getString(3),"Sim",plat});
								}
								else
								{
									model2.addRow(new Object[]{rset3.getString(1), rset3.getString(2),"$ "+ rset3.getString(3),"Não",plat});
								}
								plat="";
							}
							
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Erro na busca.");
					//e.printStackTrace();
				}
				
			}
		});
		btnBuscar.setBounds(232, 7, 89, 23);
		contentPane.add(btnBuscar);
		
		JLabel lblApresentaes = new JLabel("Apresenta\u00E7\u00F5es:");
		lblApresentaes.setBounds(10, 36, 125, 14);
		contentPane.add(lblApresentaes);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 63, 847, 320);
		contentPane.add(scrollPane);
		
		table = new JTable(model);
		scrollPane.setViewportView(table);
		
		JLabel lblJogos = new JLabel("Jogos:");
		lblJogos.setBounds(10, 392, 70, 14);
		contentPane.add(lblJogos);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 417, 847, 207);
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
		btnVoltar.setBounds(362, 627, 89, 23);
		contentPane.add(btnVoltar);
	}
}
