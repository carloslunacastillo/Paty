/*
Instituto tecnologico de León
Topicos Avanzados de Programación
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
