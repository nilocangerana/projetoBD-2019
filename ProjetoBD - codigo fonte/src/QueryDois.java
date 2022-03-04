import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;

public class QueryDois extends JFrame {

	DefaultTableModel model = new DefaultTableModel();
	DefaultTableModel model2 = new DefaultTableModel();
	DefaultTableModel model3 = new DefaultTableModel();
	private JPanel contentPane;
	private JTextField textFieldJogo;
	private JTable table;
	private JTable table_1;
	private JTable table_2;
	private JTable table_3;

	/**
	 * Create the frame.
	 */
	public QueryDois() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 956, 873);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNomeDoJogo = new JLabel("Nome do Jogo:");
		lblNomeDoJogo.setBounds(10, 11, 128, 14);
		contentPane.add(lblNomeDoJogo);
		
		textFieldJogo = new JTextField();
		textFieldJogo.setBounds(114, 8, 147, 20);
		contentPane.add(textFieldJogo);
		textFieldJogo.setColumns(10);
	    model.addColumn("Empresa(Nome)");
	    model.addColumn("Data/Hora");
	    model.addColumn("Categoria");
	    model.addColumn("Palestrante/Apresentador");
	    model.addColumn("Tema/Foco");
	    model.addColumn("Local");
		table = new JTable(model);
		
		model2.addColumn("Nome Artístico/Nome");
		model2.addColumn("Especialidade/Formacao");
	    model2.addColumn("Data/Hora");
	    model2.addColumn("Tema");
	    model2.addColumn("Local");
	    
	    //DefaultTableModel model2 = (DefaultTableModel) table_2.getModel();
	    
	    model3.addColumn("CNPJ");
		model3.addColumn("Nome");
	    model3.addColumn("Anuncio");
	    model3.addColumn("Midia Fisica");
	    model3.addColumn("Midia Digital");
		
		JLabel lblApresentaes = new JLabel("Apresenta\u00E7\u00F5es:");
		lblApresentaes.setBounds(10, 44, 100, 14);
		contentPane.add(lblApresentaes);
		
		JLabel lblEntrevistas = new JLabel("Entrevistas:");
		lblEntrevistas.setBounds(10, 312, 100, 14);
		contentPane.add(lblEntrevistas);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPane.setVisible(false);
				dispose();
			}
		});
		btnVoltar.setBounds(422, 800, 89, 23);
		contentPane.add(btnVoltar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 66, 920, 235);
		contentPane.add(scrollPane);
		
		table_1 = new JTable(model);
		scrollPane.setViewportView(table_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 328, 920, 240);
		contentPane.add(scrollPane_1);
		
		table_2 = new JTable(model2);
		scrollPane_1.setViewportView(table_2);
		
		JLabel lblEstande = new JLabel("Estande:");
		lblEstande.setBounds(463, 11, 60, 14);
		contentPane.add(lblEstande);
		
		JLabel labelNumber = new JLabel("0");
		labelNumber.setBounds(533, 11, 46, 14);
		contentPane.add(labelNumber);
		
		JLabel labelTeste = new JLabel("0");
		labelTeste.setBounds(560, 36, 80, 14);
		contentPane.add(labelTeste);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {		//Inicio do código de busca
			public void actionPerformed(ActionEvent arg0) {
				try {		//comando SQL para buscar estande do jogo, tempo de teste e condicao do jogo(indie ou não)
					PreparedStatement preparedStmt = GerarConexao.c.prepareStatement("SELECT J1.TEMPO_DE_TESTE,EST.INDIE,EST.NUMERACAO FROM JOGO J1,ESTUDIO EST WHERE J1.NOME=EST.NOME AND J1.TITULO=?");
					preparedStmt.setString (1,textFieldJogo.getText().toUpperCase());
					ResultSet rset = preparedStmt.executeQuery();
					rset.next();
						labelTeste.setText(""+rset.getInt(1)+" minutos");
						labelNumber.setText(""+rset.getInt(3));
						
						//Recuperar Entrevistas e Temas	de jogos
						//Comando SQL para buscar nome artistico ou nome de desenvolvedor/especialidade ou formacao/dataInicio da entrevista/localizacao da entrevista
						PreparedStatement preparedStmt2 = GerarConexao.c.prepareStatement("SELECT DISTINCT NA.NOME_ARTISTICO , NA.ESPECIALIDADE , TO_CHAR(ENT.DATA_HORA_INICIO,'DD/MM/YYYY HH24:MI:SS') AS DATA1, ENT.LOCALIZACAO "
								+ "FROM PERSONALIDADE NA,ENTREVISTA ENT,JOGO J1,CONTRATO T1 "
								+ "WHERE (ENT.DATA_HORA_INICIO=NA.DATA_HORA_INICIO) AND (T1.NOME_ARTISTICO=NA.NOME_ARTISTICO) AND (T1.TITULO=J1.TITULO) AND (J1.TITULO=?) "
								+ "UNION "
								+ "SELECT DISTINCT DEV.NOME, DEV.FORMACAO,TO_CHAR(ENT2.DATA_HORA_INICIO,'DD/MM/YYYY HH24:MI:SS') AS DATA1, ENT2.LOCALIZACAO FROM DESENVOLVEDOR DEV,ENTREVISTA ENT2,JOGO J2,ESTUDIO EST "
								+ "WHERE ENT2.DATA_HORA_INICIO=DEV.DATA_HORA_INICIO AND DEV.NOME_ESTUDIO=EST.NOME AND J2.NOME=EST.NOME AND J2.TITULO=? "
								+ "ORDER BY DATA1");
							preparedStmt2.setString (1,textFieldJogo.getText().toUpperCase());
							preparedStmt2.setString (2,textFieldJogo.getText().toUpperCase());
							ResultSet rset2 = preparedStmt2.executeQuery();
							
							//Comando SQL para buscar os temas das entrevistas
							PreparedStatement preparedStmtTema = GerarConexao.c.prepareStatement("SELECT DISTINCT TO_CHAR(ENT.DATA_HORA_INICIO,'DD/MM/YYYY HH24:MI:SS') AS DATA1 , T_ENT.TEMA FROM PERSONALIDADE NA,ENTREVISTA ENT,TEMA_ENTREVISTA T_ENT,JOGO J1,CONTRATO T1 " + 
									"WHERE (ENT.DATA_HORA_INICIO=NA.DATA_HORA_INICIO) AND (T1.NOME_ARTISTICO=NA.NOME_ARTISTICO) AND (T1.TITULO=J1.TITULO) AND (J1.TITULO=?) AND T_ENT.DATA_HORA_INICIO=ENT.DATA_HORA_INICIO " + 
									"UNION " + 
									"SELECT DISTINCT TO_CHAR(ENT2.DATA_HORA_INICIO,'DD/MM/YYYY HH24:MI:SS') AS DATA1,T_ENT2.TEMA FROM DESENVOLVEDOR DEV,ENTREVISTA ENT2,TEMA_ENTREVISTA T_ENT2,JOGO J2,ESTUDIO EST " + 
									"WHERE ENT2.DATA_HORA_INICIO=DEV.DATA_HORA_INICIO AND DEV.NOME_ESTUDIO=EST.NOME AND J2.NOME=EST.NOME AND J2.TITULO=? AND T_ENT2.DATA_HORA_INICIO=ENT2.DATA_HORA_INICIO " + 
									"ORDER BY DATA1,TEMA",ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
							
									preparedStmtTema.setString (1,textFieldJogo.getText().toUpperCase());
									preparedStmtTema.setString (2,textFieldJogo.getText().toUpperCase());
									ResultSet rsetTema = preparedStmtTema.executeQuery();
									int qtd;
									int contador=1;
									String tema="";
									
									//Concatena os temas na mesma tupla
									if(rsetTema.last())
									{
										qtd=rsetTema.getRow(); //Tem temas / qtd=quantidade de linhas recuperadas do rsetTema
										rsetTema.beforeFirst();
										rsetTema.next();
									}	
									else
									{
										qtd=0; //Nao tem temas
									}
									
									while(rset2.next())
									{
										while(contador<=qtd)	
										{
											if(rset2.getString(3).equals(rsetTema.getString(1)))	//enquanto as strings forem iguais, adiciona um novo tema.
											{
												if(tema.contentEquals(""))
												{
													tema=tema+rsetTema.getString(2);
												}
												else
												{
													tema=tema+"|"+rsetTema.getString(2);
												}
												rsetTema.next();
												contador++;
											}
											else
											{
												break;		//quando forem diferentes, sai do loop
											}
										}
										model2.addRow(new Object[]{rset2.getString(1), rset2.getString(2),rset2.getString(3),tema,rset2.getString(4)});
										tema="";
									}

						if(rset.getString(2).equals("1"))	//Checa se o jogo é Indie
						{		
								//Recuperar Patrocinadores dos jogos indies
								//Comando SQL para buscar CNPJ/Nome/Anuncio/Midia Digital/Midia Fisica de patrocinador
								PreparedStatement preparedStmt3 = GerarConexao.c.prepareStatement("SELECT PTR.CNPJ,PTR.NOME,FIN.ANUNCIO,FIN.MIDIA_DIGITAL,FIN.MIDIA_FISICA FROM PATROCINADOR PTR,FINANCIAMENTO FIN,JOGO J1 " + 
										"WHERE PTR.CNPJ=FIN.CNPJ AND FIN.TITULO=J1.TITULO AND J1.TITULO=?");
								preparedStmt3.setString (1,textFieldJogo.getText().toUpperCase());
								ResultSet rset3 = preparedStmt3.executeQuery();
								while(rset3.next())
								{
									model3.addRow(new Object[]{rset3.getString(1), rset3.getString(2),rset3.getString(3),rset3.getString(4),rset3.getString(5)});
								}
							
						}
						else
						{	//Caso o jogo não seja indie
							
							//Recuperar Apresentacoes e Temas de Empresas
							//Comando SQL para buscar informações das apresentacoes
							PreparedStatement preparedStmt3 = GerarConexao.c.prepareStatement("SELECT EMP.NOME,TO_CHAR(APR.DATA_HORA_INICIO,'DD/MM/YYYY HH24:MI:SS') AS DATA1, APR.CATEGORIA, PA_APR.PALESTRANTE,PA_APR.TEMA, APR.LOCALIZACAO FROM EMPRESA EMP,APRESENTACAO APR,PALESTRA PA_APR,ESTUDIO EST,JOGO J1 " + 
									"WHERE APR.DATA_HORA_INICIO=PA_APR.DATA_HORA_INICIO AND APR.CNPJ=EMP.CNPJ AND EST.CNPJ=EMP.CNPJ AND J1.NOME=EST.NOME AND J1.TITULO=? " + 
									"UNION " + 
									"SELECT EMP.NOME,TO_CHAR(APR.DATA_HORA_INICIO,'DD/MM/YYYY HH24:MI:SS') AS DATA1, APR.CATEGORIA, PU_APR.APRESENTADOR,PU_APR.FOCO, APR.LOCALIZACAO FROM EMPRESA EMP,APRESENTACAO APR,PUBLICIDADE PU_APR,ESTUDIO EST,JOGO J1 " + 
									"WHERE APR.DATA_HORA_INICIO=PU_APR.DATA_HORA_INICIO AND APR.CNPJ=EMP.CNPJ AND EST.CNPJ=EMP.CNPJ AND J1.NOME=EST.NOME AND J1.TITULO=? " + 
									"ORDER BY DATA1");
							preparedStmt3.setString (1,textFieldJogo.getText().toUpperCase());
							preparedStmt3.setString (2,textFieldJogo.getText().toUpperCase());
							ResultSet rset3 = preparedStmt3.executeQuery();
							
							while(rset3.next())
							{
								model.addRow(new Object[]{rset3.getString(1), rset3.getString(2),rset3.getString(3),rset3.getString(4),rset3.getString(5),rset3.getString(6)});
							}

						}
						
					

				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Erro na busca.");
					//e1.printStackTrace();
				}
			}
		});
		btnBuscar.setBounds(271, 7, 89, 23);
		contentPane.add(btnBuscar);
		
		JLabel lblTempoDeTeste = new JLabel("Tempo de Teste:");
		lblTempoDeTeste.setBounds(463, 36, 100, 14);
		contentPane.add(lblTempoDeTeste);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 593, 920, 196);
		contentPane.add(scrollPane_2);
		
		table_3 = new JTable(model3);
		scrollPane_2.setViewportView(table_3);
		
		JLabel lblPatrocinadores = new JLabel("Patrocinadores:");
		lblPatrocinadores.setBounds(10, 579, 100, 14);
		contentPane.add(lblPatrocinadores);
	}
}
