package br.furb.cg.spline;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main implements GLEventListener
{
	private float ortho2D_minX = -200.0f;
	private float ortho2D_maxX = 200.0f;
	private float ortho2D_minY = -200.0f;
	private float ortho2D_maxY = 200.0f;
	
	private float sruX = 100;
	private float sruY = 100;

	private GL gl;
	private GLU glu;

	private GLAutoDrawable glDrawable;
	
	private List<Point> pontos;

	public void init(GLAutoDrawable drawable)
	{
		System.out.println(" --- init ---");

		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		System.out.println("Espaço de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
		
		pontos = new ArrayList<Point>(10);
		
		/*
		 *  Rx = P1x + (P2x - P1x) . t
		 *  Ry = P1y + (P2y - P1y) . t
		 *  
		 */
		
		double np = 10;
		double tt = 1 / np;
		
		// 2 Pontos de controle
		/*
		Point p1 = new Point(30, 20);
		Point p2 = new Point(70, 60);
		
		for (int i = 0; i < np; i++)
		{
			double t = tt * i;
			
			pontos.add(criarPontoParametrico(p1, p2, t));
		}
		*/
		
		// Pontos de controle
		Point p1 = new Point(30, 20);
		Point p2 = new Point(100, 20);
		Point p3 = new Point(70, 60); 
		
		for (int i = 0; i < np; i++)
		{
			double t = tt * i;
			
			pontos.add(criarPontoParametrico(p1, p2, p3, t));
		}
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
		desenharLinhaParametrica();		

		gl.glFlush();
	}

	public void desenharSRU()
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

	public void desenharLinhaParametrica()
	{
		gl.glPointSize(2.0f);	
		gl.glBegin(GL.GL_POINTS);
		{
			for (Point p : pontos)
				gl.glVertex2d(p.getX(), p.getY());
		}
		gl.glEnd();
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

	// Rx = P1x + (P2x - P1x) . t
	public double calcularParametrico(double pInicio, double pFim, double t) 
	{
		return pInicio + (pFim - pInicio) * t;
	}
	
	public Point criarPontoParametrico(Point p1, Point p2, Point p3, double t)
	{		
		double p12x = calcularParametrico(p1.getX(), p2.getX(), t);
		double p12y = calcularParametrico(p1.getY(), p2.getY(), t);
		
		double p23x = calcularParametrico(p2.getX(), p3.getX(), t);
		double p23y = calcularParametrico(p2.getY(), p3.getY(), t);
		
		double p123x = calcularParametrico(p12x, p23x, t);
		double p123y = calcularParametrico(p12y, p23y, t);
		
		Point p = new Point();
		p.setLocation(p123x, p123y);
		
		return p;
	}
}
