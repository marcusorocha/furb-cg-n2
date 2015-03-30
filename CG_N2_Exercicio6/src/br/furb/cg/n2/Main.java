package br.furb.cg.n2;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.event.MouseInputAdapter;

public class Main extends MouseInputAdapter implements GLEventListener, KeyListener
{
	private float ortho2D_minX = -400.0f;
	private float ortho2D_maxX = 400.0f;
	private float ortho2D_minY = -400.0f;
	private float ortho2D_maxY = 400.0f;

	private float sruX = 200;
	private float sruY = 200;

	private GL gl;
	private GLU glu;

	private GLAutoDrawable glDrawable;

	//private List<Point> pontosControle;
	private List<Point> pontosSpline;
	private int numPontosSpline = 0;
	
	// Pontos de controle
	Point pc1;
	Point pc2;
	Point pc3;
	Point pc4;
	
	Point selecionado;	
	Point pressionado;

	public void init(GLAutoDrawable drawable)
	{
		System.out.println(" --- init ---");

		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		System.out.println("Espaço de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());

		numPontosSpline = 20;
		pontosSpline = new ArrayList<Point>(numPontosSpline);
		
		/*
		 *  Rx = P1x + (P2x - P1x) . t
		 *  Ry = P1y + (P2y - P1y) . t
		 *  
		 */
		
		// Pontos de controle
		pc1 = new Point(-100, -100);
		pc2 = new Point(-100, +100);
		pc3 = new Point(+100, +100);
		pc4 = new Point(+100, -100);
		
		selecionado = pc1;
	}

	public void display(GLAutoDrawable arg0)
	{
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(ortho2D_minX, ortho2D_maxX, ortho2D_minY, ortho2D_maxY);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		desenharSRU();
		desenharPontosControle();
		desenharSpline();
		desenharPontoSelecionado();

		gl.glFlush();
	}

	void desenharSRU()
	{
		gl.glLineWidth(1.0f);

		// Eixo X
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex2d(-sruX, 0.0);
			gl.glVertex2d(+sruX, 0.0);
		}
		gl.glEnd();

		// Eixo Y
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex2d(0.0, -sruY);
			gl.glVertex2d(0.0, +sruY);
		}
		gl.glEnd();
	}

	void desenharPontosControle()
	{
		gl.glLineWidth(2.0f);
		gl.glColor3f(0.0f, 1.0f, 1.0f); // Ciano
		gl.glBegin(GL.GL_LINE_STRIP);
		{
			gl.glVertex2d(pc1.getX(), pc1.getY());
			gl.glVertex2d(pc2.getX(), pc2.getY());
			gl.glVertex2d(pc3.getX(), pc3.getY());
			gl.glVertex2d(pc4.getX(), pc4.getY());
		}
		gl.glEnd();
	}
	
	void desenharPontoSelecionado()
	{
		gl.glPointSize(5.0f);
		gl.glColor3f(1.0f, 0.0f, 0.0f); // Vermelho
		gl.glBegin(GL.GL_POINTS);
		{
			gl.glVertex2d(selecionado.getX(), selecionado.getY());
		}
		gl.glEnd();
	}
	
	void desenharSpline()
	{		
		calcularPontosSpline();
		
		gl.glLineWidth(2.0f);
		gl.glPointSize(4.0f);
		gl.glColor3f(1.0f, 1.0f, 0.0f); // Amarelo
		gl.glBegin(GL.GL_LINE_STRIP);
		{
			for (Point p : pontosSpline)
				gl.glVertex2d(p.getX(), p.getY());
		}
		gl.glEnd();
	}
	
	void calcularPontosSpline()
	{
		pontosSpline.clear();
		
		double tt = 1 / (double)(numPontosSpline - 1);
		
		for (int i = 0; i < numPontosSpline; i++)
		{			
			double t = tt * i;
			
			pontosSpline.add(criarPontoParametrico(pc1, pc2, pc3, pc4, t));
		}
	}
	
	// Rx = P1x + (P2x - P1x) . t
	private double calcularParametrico(double pInicio, double pFim, double t) 
	{
		return pInicio + (pFim - pInicio) * t;
	}
	
	private Point criarPontoParametrico(Point p1, Point p2, Point p3, Point p4, double t)
	{		
		double p12x = calcularParametrico(p1.getX(), p2.getX(), t);
		double p12y = calcularParametrico(p1.getY(), p2.getY(), t);
		
		double p23x = calcularParametrico(p2.getX(), p3.getX(), t);
		double p23y = calcularParametrico(p2.getY(), p3.getY(), t);
		
		double p34x = calcularParametrico(p3.getX(), p4.getX(), t);
		double p34y = calcularParametrico(p3.getY(), p4.getY(), t);
		
		double p123x = calcularParametrico(p12x, p23x, t);
		double p123y = calcularParametrico(p12y, p23y, t);
		
		double p234x = calcularParametrico(p23x, p34x, t);
		double p234y = calcularParametrico(p23y, p34y, t);
		
		double p1234x = calcularParametrico(p123x, p234x, t);
		double p1234y = calcularParametrico(p123y, p234y, t);
		
		Point p = new Point();
		p.setLocation(p1234x, p1234y);
		
		return p;
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4)
	{
		System.out.println(" --- reshape ---");
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2)
	{
		System.out.println(" --- displayChanged ---");
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		System.out.println("key");
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_I:// aproximar
				System.out.println("minX " + ortho2D_minX + " maxX "
						+ ortho2D_maxX + " minY " + ortho2D_minY + " maxY "
						+ ortho2D_maxY);
				if ((ortho2D_minX + 50) < -50 && (ortho2D_maxX - 50) > 50
						&& (ortho2D_minY + 50) < -50
						&& (ortho2D_maxY - 50) > 50)
				{
					ortho2D_minX += 50.0f;
					ortho2D_maxX -= 50.0f;
					ortho2D_minY += 50.0f;
					ortho2D_maxY -= 50.0f;
					glDrawable.display();
				}
				break;
			case KeyEvent.VK_O:// afastar
				System.out.println("minX " + ortho2D_minX + " maxX "
						+ ortho2D_maxX + " minY " + ortho2D_minY + " maxY "
						+ ortho2D_maxY);
				if ((ortho2D_minX - 50) > -500 && (ortho2D_maxX + 50) < 500
						&& (ortho2D_minY - 50) > -500
						&& (ortho2D_maxY + 50) < 500)
				{
					ortho2D_minX -= 50.0f;
					ortho2D_maxX += 50.0f;
					ortho2D_minY -= 50.0f;
					ortho2D_maxY += 50.0f;
					glDrawable.display();
				}
				break;
			case KeyEvent.VK_E:// esquerda
				ortho2D_minX += 50.0f;
				ortho2D_maxX += 50.0f;
				glDrawable.display();
				break;
			case KeyEvent.VK_D:// direita
				ortho2D_minX -= 50.0f;
				ortho2D_maxX -= 50.0f;
				glDrawable.display();
				break;
			case KeyEvent.VK_C:// cima
				ortho2D_minY -= 50.0f;
				ortho2D_maxY -= 50.0f;
				glDrawable.display();
				break;
			case KeyEvent.VK_B:// baixo
				ortho2D_minY += 50.0f;
				ortho2D_maxY += 50.0f;
				glDrawable.display();
				break;
			case KeyEvent.VK_1:
				selecionado = pc1;
				glDrawable.display();
				break;
			case KeyEvent.VK_2:
				selecionado = pc2;
				glDrawable.display();
				break;
			case KeyEvent.VK_3:
				selecionado = pc3;
				glDrawable.display();
				break;	
			case KeyEvent.VK_4:
				selecionado = pc4;
				glDrawable.display();
				break;
			case KeyEvent.VK_A:
				if (numPontosSpline < 20) numPontosSpline += 1;
				glDrawable.display();
				break;
			case KeyEvent.VK_S:
				if (numPontosSpline > 0) numPontosSpline -= 1;
				glDrawable.display();
				break;
		}
	}
	

	@Override
	public void keyReleased(KeyEvent e)
	{
		System.out.println(" --- keyReleased ---");
	}
	

	@Override
	public void keyTyped(KeyEvent e)
	{
		System.out.println(" --- keyTyped ---");
	}
	

	@Override
	public void mousePressed(MouseEvent e)
	{
		pressionado = new Point(e.getPoint());
	}
	

	@Override
	public void mouseReleased(MouseEvent e)
	{
		pressionado = null;
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		atualizarPontoSelecionado(e.getPoint());
	}
	
	
	void atualizarPontoSelecionado(Point p)
	{
		if (pressionado != null && selecionado != null)
		{
			double mx = p.getX() - pressionado.getX();
			double my = p.getY() - pressionado.getY();
			
			double nx = selecionado.getX() + mx;
			double ny = selecionado.getY() - my;
			
			selecionado.setLocation(nx, ny);
			pressionado.setLocation(p);
			
			glDrawable.display();
		}
    }
}
