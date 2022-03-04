//import java.awt.BorderLayout;
//import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class QueryUm extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldCNPJ;
	private JTextField textFieldData;
	private JTextField textFieldLocal;
	private JTextField textFieldNome;
	private JTextField textFieldTema;


	/**
	 * Create the frame.
	 */
	public QueryUm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 344, 419);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPane.setVisible(false);
				dispose();
			}
		});
		btnVoltar.setBounds(115, 333, 89, 23);
		contentPane.add(btnVoltar);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Palestra", "Publicidade"}));
		comboBox.setBounds(176, 34, 128, 20);
		contentPane.add(comboBox);
		
		JLabel lblTipoDeApresentao = new JLabel("Tipo de Apresenta\u00E7\u00E3o:");
		lblTipoDeApresentao.setBounds(10, 37, 138, 14);
		contentPane.add(lblTipoDeApresentao);
		
		textFieldCNPJ = new JTextField();
		textFieldCNPJ.setBounds(176, 77, 128, 20);
		contentPane.add(textFieldCNPJ);
		textFieldCNPJ.setColumns(10);
		
		JLabel lblEmpresacnpj = new JLabel("Empresa (CNPJ):");
		lblEmpresacnpj.setBounds(10, 80, 114, 14);
		contentPane.add(lblEmpresacnpj);
		
		textFieldData = new JTextField();
		textFieldData.setBounds(176, 118, 128, 20);
		contentPane.add(textFieldData);
		textFieldData.setColumns(10);
		
		JLabel lblDatahoraddHh = new JLabel("Data/Hora:");
		lblDatahoraddHh.setBounds(10, 121, 89, 14);
		contentPane.add(lblDatahoraddHh);
		
		textFieldLocal = new JTextField();
		textFieldLocal.setBounds(176, 160, 128, 20);
		contentPane.add(textFieldLocal);
		textFieldLocal.setColumns(10);
		
		JLabel lblLocal = new JLabel("Local:");
		lblLocal.setBounds(10, 163, 46, 14);
		contentPane.add(lblLocal);
		
		textFieldNome = new JTextField();
		textFieldNome.setColumns(10);
		textFieldNome.setBounds(176, 209, 128, 20);
		contentPane.add(textFieldNome);
		
		JLabel lblPalestranteapresentador = new JLabel("Palestrante/Apresentador:");
		lblPalestranteapresentador.setBounds(10, 212, 156, 14);
		contentPane.add(lblPalestranteapresentador);
		
		textFieldTema = new JTextField();
		textFieldTema.setColumns(10);
		textFieldTema.setBounds(85, 257, 219, 20);
		contentPane.add(textFieldTema);
		
		JLabel lblTemafoco = new JLabel("Tema/Foco:");
		lblTemafoco.setBounds(10, 260, 138, 14);
		contentPane.add(lblTemafoco);
		
		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		//Botão Cadastrar - Ação que executa as funções com o banco de dados.
				try {
					PreparedStatement preparedStmtCheck = GerarConexao.c.prepareStatement("SELECT DISTINCT DEV.CPF, TO_CHAR(DEV.DATA_HORA_INICIO,'DD/MM/YYYY HH24:MI:SS'),EMP.CNPJ FROM DESENVOLVEDOR DEV,ESTUDIO EST,EMPRESA EMP,ENTREVISTA ENT " + 
							"WHERE DEV.DATA_HORA_INICIO=? AND DEV.NOME_ESTUDIO=EST.NOME AND EST.CNPJ=?"); //checar data da apresentacao com data de entrevista desenvolvedor da empresa
					SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");	//parâmetros vindos da interface grafica(textField e comboBox)
					Date d = fmt.parse(textFieldData.getText());	//cria tipo data do java
					
					preparedStmtCheck.setDate(1, new java.sql.Date(d.getTime()));		//seta os parametros do PreparedStatement
					preparedStmtCheck.setString(2, textFieldCNPJ.getText());
					ResultSet rsetCheck = preparedStmtCheck.executeQuery(); 	//Result Set recebido
					
					if(rsetCheck.next())	//Checa se o result set está vazio.
					{
						//nao pode inserir, result set tem conteudo, ou seja há uma entrevista de desenvolvedor de empresa no horario requerido.
						JOptionPane.showMessageDialog(null, "Erro na inserção. Desenvolvedor da empresa faz entrevista no mesmo horário requerido.");
					}
					else
					{
						//pode inserir, nao a entrevista de desenvolvedor de empresa no horario requerido.
					PreparedStatement preparedStmt = GerarConexao.c.prepareStatement("INSERT INTO APRESENTACAO (DATA_HORA_INICIO,CATEGORIA,LOCALIZACAO,CNPJ) VALUES (?,?,?,?)"); //insere a apresentacao.
					
					if(textFieldTema.getText().length()>100)
					{
						JOptionPane.showMessageDialog(null, "Erro. Tema/Foco muito grande.");	//tratamentos de erros
					}
					else
					{
						if(textFieldNome.getText().length()>20)
						{
							JOptionPane.showMessageDialog(null, "Erro. Nome muito grande.");
						}
						else
						{
							preparedStmt.setDate(1, new java.sql.Date(d.getTime()));		//seta os parametros do PreparedStatement
						    preparedStmt.setString (2, comboBox.getSelectedItem().toString().toUpperCase());
						    preparedStmt.setString (3,textFieldLocal.getText());
						    preparedStmt.setString (4, textFieldCNPJ.getText());
							preparedStmt.execute();											//executa insercao em apresentacao
							
							if(comboBox.getSelectedItem().toString().equals("Palestra"))
							{
								PreparedStatement preparedStmt2 = GerarConexao.c.prepareStatement("INSERT INTO PALESTRA (DATA_HORA_INICIO,TEMA,PALESTRANTE) VALUES (?,?,?)");//insere a palestra.
								preparedStmt2.setDate(1, new java.sql.Date(d.getTime()));	//seta os parametros do PreparedStatement
								preparedStmt2.setString (2, textFieldTema.getText());
								preparedStmt2.setString (3, textFieldNome.getText());
								preparedStmt2.execute();								//executa insercao em palestra
							}
							else
							{
								PreparedStatement preparedStmt2 = GerarConexao.c.prepareStatement("INSERT INTO PUBLICIDADE (DATA_HORA_INICIO,FOCO,APRESENTADOR) VALUES (?,?,?)");//insere a publicidade.
								preparedStmt2.setDate(1, new java.sql.Date(d.getTime()));	//seta os parametros do PreparedStatement
								preparedStmt2.setString (2, textFieldTema.getText());
								preparedStmt2.setString (3, textFieldNome.getText());
								preparedStmt2.execute();								//executa insercao em publicidade
							}
							JOptionPane.showMessageDialog(null, "Inserido.");
						}
					}}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Erro na inserção. Restrição violada.");
					//e1.printStackTrace();
				} catch (ParseException e1) {
					JOptionPane.showMessageDialog(null, "Erro na data.");
					//e1.printStackTrace();
				}
			}
		});
		btnCadastrar.setBounds(103, 299, 108, 23);
		contentPane.add(btnCadastrar);
		
		JLabel lblddmmyyyyHhmiss = new JLabel(" (DD/MM/YYYY HH:MI:SS)");
		lblddmmyyyyHhmiss.setBounds(10, 138, 156, 14);
		contentPane.add(lblddmmyyyyHhmiss);
		
		JLabel lblXxaaaaaaaaaaaaa = new JLabel("xx.xxx.xxx/xxxx-xx");
		lblXxaaaaaaaaaaaaa.setBounds(10, 96, 138, 14);
		contentPane.add(lblXxaaaaaaaaaaaaa);
	}
}
