/*
Instituto tecnologico de León
Topicos Avanzados de Programación
Profesor: NG. LEVY ROJAS CARLOS RAFAEL
Debug Atom
Equipo:
-Carlos Leonardo Luna Castillo
-Patricia Jazmín Ramírez Fonseca
 */
 
 
 //Clase Figura
 
package miFigura;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Paint;
import java.awt.GradientPaint;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.geom.Point2D;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;

public class Figura implements Externalizable{
    protected Point puntoInicial;
    protected Point puntoFinal;
    private Paint pintar;
    private Stroke trazo;
    
    public Figura(){
        
    }
    
        public Figura (int x1, int y1, int x2, int y2, Paint pintar, Stroke trazo) {
		puntoInicial = new Point (x1, y1);
		puntoFinal = new Point (x2, y2);
		setPintar (pintar);
		setTrazo (trazo);
	}
        
        public final void setTrazo (Stroke newTrazo) {
		trazo = newTrazo == null ? new BasicStroke (2.0f) : newTrazo;
	}
	
	public final Stroke getTrazo () {
		return trazo;
	}
	
	public final void setPintar (Paint newPintar) {
		pintar = newPintar;
	}
	
	public final Paint getPintar () {
		return pintar;
	}
	
	public final void setPuntoFinal (int x, int y) {
		puntoFinal.x = x < 0 ? 0 : x;
		puntoFinal.y = y < 0 ? 0 : y;
	}
	
	public void dibujar (Graphics2D g2d) {
		g2d.setStroke(trazo);
		g2d.setPaint(getPintar ());
	}
	
	public void writeExternal (final ObjectOutput ou) throws IOException {
		ou.writeObject (puntoInicial);//it writes the initialPoint (Point)
		ou.writeObject (puntoFinal);//it writes the finalPoint
		ou.writeBoolean (pintar instanceof Color);//this boolean is to indicate that the paint object is a color
		
		if (pintar instanceof Color)
			ou.writeObject ((Color) pintar);//it writes the color
		else if (pintar instanceof GradientPaint) {
			//these instructions write each parameter of GradientPaint's constructor
			GradientPaint gp = (GradientPaint) pintar;
			
			ou.writeObject (gp.getPoint1 ());
			ou.writeObject (gp.getColor1 ());
			ou.writeObject (gp.getPoint2 ());
			ou.writeObject (gp.getColor2 ());
			ou.writeBoolean (gp.isCyclic ());
		} else
			System.err.println ("No se puede pintar el objeto que escogio");
		
		if (trazo instanceof BasicStroke) {
			//these instructions write each parameter of BasicStroke's constructor
			BasicStroke bs = (BasicStroke) trazo;
			
			ou.writeBoolean (bs.getDashArray () == null);//this sentence determinates if the basic stroke should be created with 3 or 6 parameters
			ou.writeFloat (bs.getLineWidth ());
			ou.writeInt (bs.getEndCap ());
			ou.writeInt (bs.getLineJoin ());
			
			if (bs.getDashArray () != null) {
				ou.writeFloat (bs.getMiterLimit());
				ou.writeObject (bs.getDashArray ());
				ou.writeFloat (bs.getDashPhase ());
			}
		} else
			System.err.println ("No se pudo hacer el trazo");
	}
        
        public void readExternal (final ObjectInput oi) throws IOException, ClassNotFoundException {
		puntoInicial = (Point) oi.readObject ();
		puntoFinal = (Point) oi.readObject ();
		
		if (oi.readBoolean ())
			setPintar ((Color) oi.readObject ());
		else
			setPintar (new GradientPaint ((Point2D) oi.readObject (),
			    (Color) oi.readObject (),
			    (Point2D) oi.readObject (),
			    (Color) oi.readObject (),
			    oi.readBoolean ()));
		
		if (oi.readBoolean ())
			setTrazo (new BasicStroke (oi.readFloat (),
			    oi.readInt (),
			    oi.readInt ()));
		else
			setTrazo (new BasicStroke (oi.readFloat (),
			    oi.readInt (),
			    oi.readInt (),
			    oi.readFloat (),
			    (float[]) oi.readObject (),
			    oi.readFloat ()));
	}
    
}



//Clase FiguraBidimensional
package miFigura;

import java.awt.Paint;
import java.awt.Stroke;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;

public class FiguraBidimensional extends Figura{
    private boolean estaRelleno;
	
	public FiguraBidimensional () {
	}
	
	public  FiguraBidimensional (int x1, int y1, int x2, int y2, boolean estaRelleno, Paint pintar, Stroke trazo) {
		super (x1, y1, x2, y2, pintar, trazo);
		setRellenable (estaRelleno);
	}
	
	public FiguraBidimensional (int x, int y, boolean estaRelleno, Paint pintar, Stroke trazo) {
		super (x, y, 0, 0, pintar, trazo);
		setRellenable (estaRelleno);
	}
	
	public final void setRellenable (boolean valor) {
		estaRelleno = valor;
	}
	
	public final boolean estaRelleno () {
		return estaRelleno;
	}
	
	@Override
	public void writeExternal (final ObjectOutput ou) throws IOException {
		super.writeExternal (ou);
		
		ou.writeBoolean (estaRelleno ());
	}
	
	@Override
	public void readExternal (final ObjectInput oi) throws IOException, ClassNotFoundException {
		super.readExternal (oi);
		
		setRellenable (oi.readBoolean ());
	}
    
}

//clase MiLinea

package miFigura;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Line2D;
import java.awt.Stroke;

public class MiLinea extends Figura{
    public MiLinea () {
	}

	public MiLinea (int x, int y, Paint pintar, Stroke trazo) {
		super (x, y, 0, 0, pintar, trazo);
	}
	
	public MiLinea (int x1, int y1, int x2, int y2, Paint pintar, Stroke trazo) {
		super (x1, y1, x2, y2, pintar, trazo);
	}
	
	public void dibujar (Graphics2D g2d) {
		super.dibujar(g2d);
		g2d.draw (new Line2D.Double (puntoInicial.x, puntoInicial.y, puntoFinal.x, puntoFinal.y));
	}
}

//clase MiCuadrado

package miFigura;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.awt.Stroke;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class MiCuadrado extends FiguraBidimensional{
    public MiCuadrado () {
	}

	public MiCuadrado (int x, int y, boolean estaRelleno, Paint pintar, Stroke trazo) {
		super (x, y, estaRelleno, pintar, trazo);
	}

	public MiCuadrado (int x1, int y1, int x2, int y2, boolean estaRelleno, Paint pintar, Stroke trazo) {
		super (min (x1, x2), min (y1, y2), max (x1, x2), max (y1, y2), estaRelleno, pintar, trazo);
	}
	
	public void dibujar (Graphics2D g2d) {
		super.dibujar(g2d);
		Rectangle2D recta = new Rectangle2D.Double (puntoInicial.x,
		    puntoInicial.y,
		    puntoFinal.x - puntoInicial.x,
		    puntoFinal.y - puntoInicial.y);
	
		if (estaRelleno ())
			g2d.fill (recta);
		else
			g2d.draw (recta);
	}
}

//Clase MiCirculo

package miFigura;

import java.awt.Graphics2D; import java.awt.Paint; import java.awt.geom.Ellipse2D; import java.awt.Stroke; import static java.lang.Math.max; import static java.lang.Math.min;

public class MiCirculo extends FiguraBidimensional{

    public MiCirculo () {
}

public MiCirculo (int x, int y, boolean estaRelleno, Paint pintar, Stroke trazo) {
    super (x, y, estaRelleno, pintar, trazo);
}

public MiCirculo (int x1, int y1, int x2, int y2, boolean estaRelleno, Paint pintar, Stroke trazo) {
    super (min (x1, x2), min (y1, y2), max (x1, x2), max (y1, y2), estaRelleno, pintar, trazo);
}

public void dibujar (Graphics2D g2d) {
    super.dibujar(g2d);
    Ellipse2D elipse = new Ellipse2D.Double (puntoInicial.x,
    puntoInicial.y, 
            puntoFinal.x -puntoInicial.x,
    puntoFinal.y - puntoInicial.y);

    if (estaRelleno ())
        g2d.fill (elipse);
    else
        g2d.draw (elipse);
}
}

//Clase MiTriangulo

package miFigura;

import java.awt.Graphics2D; import java.awt.Paint; import java.awt.Polygon; import java.awt.Stroke; import static java.lang.Math.max; import static java.lang.Math.min;

public class MiTriangulo extends FiguraBidimensional {

public MiTriangulo () {
}

public MiTriangulo (int x, int y, boolean estaRelleno, Paint pintar, Stroke trazo) {
    super (x, y, estaRelleno, pintar, trazo);
}

public MiTriangulo (int x1, int y1, int x2, int y2, boolean estaRelleno, Paint pintar, Stroke trazo) {
    super (min (x1, x2), min (y1, y2), max (x1, x2), max (y1, y2), estaRelleno, pintar, trazo);
}

public void dibujar (Graphics2D g2d) {
    super.dibujar (g2d);
    int[] puntosX = {(puntoInicial.x + puntoFinal.x) / 2, puntoFinal.x, puntoInicial.x};
    int[] puntosY = {puntoInicial.y, puntoFinal.y, puntoFinal.y};
    Polygon poligono = new Polygon (puntosX, puntosY, 3);

    if (estaRelleno ())
        g2d.fill (poligono);
    else
        g2d.draw (poligono);
}
}

//Clase DibujoPanel

package miFigura;

import java.awt.Color; import java.awt.Graphics; import java.awt.Graphics2D; import java.awt.Paint; import java.awt.Stroke; import java.awt.event.MouseEvent; import java.awt.event.MouseAdapter; import java.awt.event.MouseMotionListener; import javax.swing.JLabel; import javax.swing.JPanel;

public class PanelDibujo extends JPanel { public static final byte MICIRCULO = 0; public static final byte MICUADRADO = 1; public static final byte MITRIANGULO = 2; public static final byte MILINEA = 3;

private Figura[] figuras;
private Figura figuraActual;
private Stroke trazoActual;
private short numeroDeFiguras;
private byte tipoDeFigura;
private Paint pintando;
private boolean relleno = false;
private JLabel condicionLabel;

    public PanelDibujo (JLabel condicion) {
    condicionLabel = condicion;
    figuras = new Figura[100];
    numeroDeFiguras = 0;
    tipoDeFigura =  MICIRCULO;
    pintando= Color.BLACK;
    setBackground (Color.WHITE);
    MouseHandler mh = new MouseHandler ();
    addMouseListener (mh);
    addMouseMotionListener (mh);
}

@Override
public void paintComponent (Graphics g) {
    super.paintComponent (g);
    Graphics2D g2d = (Graphics2D) g;

    if (figuraActual != null)
        figuraActual.dibujar(g2d);

    for (byte contador = 0; figuras[contador] != null && contador < figuras.length; ++contador)
        figuras[contador].dibujar(g2d);
}

public void setTipoFigura (byte newTipoFigura) {
    tipoDeFigura = newTipoFigura >= 0 && newTipoFigura < 4 ? newTipoFigura : 0;
}

public void setPintando (Paint newPintando) {
    pintando = newPintando;
}

public void setTrazoActual (Stroke newTrazo) {
    trazoActual = newTrazo;
}

public void setRellenable (boolean valor) {
    relleno= valor;
}

public void deshacer () {
    if (--numeroDeFiguras < 0)
        numeroDeFiguras = 0;

    figuras[numeroDeFiguras] = null;

    repaint ();
}

public void limpiar () {
    numeroDeFiguras = 0;
        figuraActual = null;

    for (short contador = 0; figuras[contador] != null && contador < figuras.length; ++contador)
        figuras[contador] = null;

    repaint ();
}

    private class MouseHandler extends MouseAdapter implements MouseMotionListener {


    public void mousePressed(MouseEvent me) {
        switch (tipoDeFigura) {
            case MICIRCULO:
                figuraActual = new MiCirculo (me.getX (), me.getY (), relleno, pintando, trazoActual);
                break;

            case MICUADRADO:
                figuraActual = new MiCuadrado (me.getX (), me.getY (), relleno, pintando, trazoActual);
                break;

            case MITRIANGULO:
                figuraActual = new MiTriangulo (me.getX(), me.getY(), relleno, pintando, trazoActual);
                break;

            case MILINEA:
                figuraActual = new MiLinea (me.getX(), me.getY(), pintando, trazoActual);
        }
    }

    public void mouseReleased (MouseEvent me) {
        refreshCurrentShape (me.getX (), me.getY ());
        figuras[numeroDeFiguras++] = figuraActual;
        figuraActual = null;
        repaint ();
    }

    public void mouseMoved (MouseEvent me) {
        refreshStateLabel (me.getX (), me.getY ());
    }

    public void mouseDragged (MouseEvent me) {
        refreshStateLabel (me.getX (), me.getY ());
        refreshCurrentShape (me.getX (), me.getY ());
        repaint ();
    }

    private void refreshStateLabel (int x, int y) {
        condicionLabel.setText (String.format ("(%d, %d)", x, y));
    }

    private void refreshCurrentShape (int x, int y) {
        figuraActual.setPuntoFinal(x, y);
    }
}
}

//Main

package miFigura;

import java.awt.BorderLayout; import java.awt.GridLayout; import java.awt.Color; import java.awt.event.ActionListener; import java.awt.event.ActionEvent; import java.awt.event.ItemListener; import java.awt.event.ItemEvent; import javax.swing.JButton; import javax.swing.JCheckBox; import javax.swing.JComboBox; import javax.swing.JFrame; import javax.swing.JLabel; import javax.swing.JPanel;

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
