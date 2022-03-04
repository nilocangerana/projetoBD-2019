import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import java.awt.Font;

public class MainFrame {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new GerarConexao().Conectar();
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Projeto de Banco de Dados");
		frame.setBounds(100, 100, 604, 432);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnInserir = new JButton("Inserir Nova Apresenta\u00E7\u00E3o");
		
		btnInserir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				QueryUm q1 = new QueryUm();
				q1.setVisible(true);
			}
		});
		btnInserir.setBounds(350, 24, 187, 49);
		frame.getContentPane().add(btnInserir);
		
		JButton btnBuscaInformaoDe = new JButton("Busca Informa\u00E7\u00E3o de Jogo");
		btnBuscaInformaoDe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QueryDois q2 = new QueryDois();
				q2.setVisible(true);
			}
		});
		btnBuscaInformaoDe.setBounds(350, 84, 187, 49);
		frame.getContentPane().add(btnBuscaInformaoDe);
		
		JButton btnBuscarEventosDo = new JButton("Buscar Empresa");
		btnBuscarEventosDo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Query3 q3 = new Query3();
				q3.setVisible(true);
			}
		});
		btnBuscarEventosDo.setBounds(350, 144, 187, 49);
		frame.getContentPane().add(btnBuscarEventosDo);
		
		JButton btnBuscarEstdios = new JButton("Buscar Est\u00FAdios");
		btnBuscarEstdios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QueryQuatro q4 = new QueryQuatro();
				q4.setVisible(true);
			}
		});
		btnBuscarEstdios.setBounds(350, 204, 187, 49);
		frame.getContentPane().add(btnBuscarEstdios);
		
		JButton btnContarPblicoPor = new JButton("Contar P\u00FAblico-Apresenta\u00E7\u00E3o \r\n");
		btnContarPblicoPor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				QueryCinco q5 = new QueryCinco();
				q5.setVisible(true);
			}
		});
		btnContarPblicoPor.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnContarPblicoPor.setBounds(350, 264, 187, 49);
		frame.getContentPane().add(btnContarPblicoPor);
		
		JButton btnContarPatrocinadorjogo = new JButton("Contar Patrocinador-Jogo");
		btnContarPatrocinadorjogo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuerySeis q6 = new QuerySeis();
				q6.setVisible(true);
			}
		});
		btnContarPatrocinadorjogo.setBounds(350, 324, 187, 49);
		frame.getContentPane().add(btnContarPatrocinadorjogo);
		
		JLabel lblNewLabel = new JLabel("Insere uma nova apresenta\u00E7\u00E3o na base de dados (1)");
		lblNewLabel.setBounds(10, 41, 301, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblAParti = new JLabel("Buscar informa\u00E7\u00F5es sobre determinado jogo (3)");
		lblAParti.setBounds(10, 101, 286, 14);
		frame.getContentPane().add(lblAParti);
		
		JLabel lblNewLabel_1 = new JLabel("Buscar informa\u00E7\u00E3o sobre empresa (9)");
		lblNewLabel_1.setBounds(10, 161, 301, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblBuscarInfromaesSobre = new JLabel("Buscar infroma\u00E7\u00F5es sobre est\u00FAdios (10)");
		lblBuscarInfromaesSobre.setBounds(10, 221, 250, 14);
		frame.getContentPane().add(lblBuscarInfromaesSobre);
		
		JLabel lblContaOPblico = new JLabel("Conta o P\u00FAblico por Apresenta\u00E7\u00E3o de cada Empresa (11)");
		lblContaOPblico.setBounds(10, 281, 314, 14);
		frame.getContentPane().add(lblContaOPblico);
		
		JLabel lblIndicaAQuantidade = new JLabel("Indica a quantidade de jogo por patrocinador e jogo");
		lblIndicaAQuantidade.setBounds(10, 341, 301, 14);
		frame.getContentPane().add(lblIndicaAQuantidade);
		
		JLabel lblMaisPatrocinado = new JLabel("mais patrocinado (12)");
		lblMaisPatrocinado.setBounds(10, 359, 148, 14);
		frame.getContentPane().add(lblMaisPatrocinado);
	}
}
