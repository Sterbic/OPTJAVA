package hr.fer.zemris.optjava.ant.visual;

import hr.fer.zemris.optjava.ant.AntSimulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 * Klasa koja sadrzi glavni prozor za prikaz rada simuliranje kretanja mrava
 * @author Luka Sterbic
 * @version 0.2
 */
public class AntWorldFrame extends JFrame {
	
	private static final long serialVersionUID = 2213673539834783396L;
	
	private AntSimulator simulator;
	private AntWorldDrawer drawer;
	
	/**
	 * Konstruktor za AntWorldFrame
	 * @param simulator simulator kretanja mrava
	 */
	public AntWorldFrame(AntSimulator simulator) {
		this.simulator = simulator;
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(100, 100);
		setTitle("Ant Trail - GP");
		
		initGui();
		pack();
	}
	
	/**
	 * Inicijalizacija grafickog sucelja
	 */
	private void initGui() {
		getContentPane().setLayout(new BorderLayout());
		
		drawer = new AntWorldDrawer(simulator);
		
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new GridBagLayout());
		centralPanel.add(drawer);
		centralPanel.setBackground(Color.white);
		
		centralPanel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				simulator.next();
			}
			
		});
		
		JScrollPane centralPane = new JScrollPane(centralPanel);
		getContentPane().add(centralPane, BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(2, 2, 5, 5));
		southPanel.setBorder(BorderFactory.createTitledBorder("Commands"));
		
		final JTextField foodTextField = new JTextField("0");
		foodTextField.setEditable(false);
		foodTextField.setHorizontalAlignment(JTextField.RIGHT);
		
		southPanel.add(new JLabel(" Food collected:"));
		southPanel.add(foodTextField);		
		southPanel.add(new JButton(new NextMoveAction(simulator)));
		southPanel.add(new JButton(new ResetAction(simulator)));
		
		simulator.addListener(new IAntSimulatorListener() {
			
			@Override
			public void update(AntSimulator simulator) {
				foodTextField.setText(Integer.toString(simulator.getAnt().getScore()));
			}
			
		});
		
		getContentPane().add(southPanel, BorderLayout.SOUTH);
	}

}
