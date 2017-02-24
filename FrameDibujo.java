/*
Instituto tecnologico de León
Topicos Avanzados de Programación
Equipo:
-Carlos Leonardo Luna Castillo
-Patricia Jazmín Ramírez Fonseca
 */
package miFigura;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FrameDibujo implements ActionListener, ItemListener {

    private PanelDibujo dw;
	private JButton botonBorrar;
	private JButton botonDeshacer;
	private JCheckBox relleno;
	private JComboBox listaFiguras;
        private JComboBox Colores;
	private JFrame ventana;
	private Color color;
	
	public FrameDibujo() {
		JLabel condicionLabel = new JLabel ();
		JPanel controlPanel = new JPanel ();
		controlPanel.setLayout (new GridLayout (2, 1, 5, 5));
		JPanel  principal = new JPanel ();
		JPanel fondo = new JPanel ();
		botonBorrar = new JButton ("Borrar");
		botonBorrar.addActionListener (this);
		botonDeshacer = new JButton ("Deshacer");
		botonDeshacer.addActionListener (this);
                ventana = new JFrame ("Dibujo");
                String[] nombresFiguras = {"Circulo", "Cuadrado", "Triangulo", "Linea"};
                String[] colores = {"Negro", "Verde", "Rojo","Amarillo","Naranja","Rosa","Azul"};
		listaFiguras = new JComboBox (nombresFiguras);
		listaFiguras.setMaximumRowCount (4);
		listaFiguras.addItemListener (this);
                Colores = new JComboBox (colores);
		Colores.setMaximumRowCount (7);
		Colores.addItemListener (this);
		color = Color.BLACK;
		dw = new PanelDibujo (condicionLabel);
		
		relleno = new JCheckBox ("Rellenado");
		relleno.addItemListener (this);

                principal.add (botonDeshacer);
                principal.add (botonBorrar);
                principal.add (listaFiguras);
                principal.add (Colores);
                principal.add (relleno);
		
		
		controlPanel.add (principal);
		controlPanel.add (fondo);
		ventana.add (controlPanel, BorderLayout.NORTH);
		ventana.add (dw);
		ventana.add (condicionLabel, BorderLayout.SOUTH);
		ventana.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		ventana.setSize (800, 500);
		ventana.setVisible (true);
	}

	public static void main (String args[]) {
		new FrameDibujo();
	}
	
    @Override
	public void actionPerformed (ActionEvent ae) {
		if (ae.getSource () == botonDeshacer)
			dw.deshacer();
		else if (ae.getSource () == botonBorrar){
			dw.limpiar();
                }
	}
	
	public void itemStateChanged (ItemEvent ie) {
		if (ie.getSource () == relleno)
			dw.setRellenable(relleno.isSelected ());
		if (ie.getStateChange () == ItemEvent.SELECTED)
			dw.setTipoFigura((byte) listaFiguras.getSelectedIndex ());  
                    if(Colores.getSelectedItem().toString().equals("Black")){
                           color = Color.BLACK;
                        }else if(Colores.getSelectedItem().toString().equals("Green")){
                           color = Color.green;
                        }else if(Colores.getSelectedItem().toString().equals("Red")){
                            color = Color.RED;
                        }else if(Colores.getSelectedItem().toString().equals("Yellow")){
                            color = Color.YELLOW;
                        }else if(Colores.getSelectedItem().toString().equals("Orange")){
                            color = Color.ORANGE;
                        }else if(Colores.getSelectedItem().toString().equals("Pink")){
                             color = Color.PINK;
                        }else if(Colores.getSelectedItem().toString().equals("Blue")){
                             color = Color.BLUE;
                        }
                        dw.setPintando(color);
	}
}
