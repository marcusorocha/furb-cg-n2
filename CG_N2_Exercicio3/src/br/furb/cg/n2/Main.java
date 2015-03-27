package br.furb.cg.n2;

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
	private float ortho2D_minX = -400.0f;
	private float ortho2D_maxX = 400.0f;
	private float ortho2D_minY = -400.0f;
	private float ortho2D_maxY = 400.0f;

	private float sruX = 200;
	private float sruY = 200;

	private GL gl;
	private GLU glu;

	private GLAutoDrawable glDrawable;

	public void init(GLAutoDrawable drawable)
	{
		System.out.println(" --- init ---");

		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		System.out.println("Espaço de desenho com tamanho: " + drawable.getWidth() + " x " + drawable.getHeight());
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
		desenharTriangulo();
		
		desenharCirculo(criarPontosCirculo(100, -101.5, +101.5));
		desenharCirculo(criarPontosCirculo(100, +101.5, +101.5));
		desenharCirculo(criarPontosCirculo(100, -1, -101.5));

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

	public void desenharCirculo(List<Point> pontos)
	{
		gl.glPointSize(1.5f);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		
		gl.glBegin(GL.GL_POINTS);
		{
			for (Point p : pontos)
				gl.glVertex2d(p.getX(), p.getY());
		}
		gl.glEnd();
	}

	public void desenharTriangulo()
	{
		gl.glColor3f(0.0f, 1.0f, 1.0f);
		
		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex2d(-100, +100);
			gl.glVertex2d(+100, +100);
			
			gl.glVertex2d(+100, +100);
			gl.glVertex2d(0, -100);
			
			gl.glVertex2d(0, -100);
			gl.glVertex2d(-100, +100);
		}
		gl.glEnd();
	}
	
	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4)
	{
		System.out.println(" --- reshape ---");
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2)
	{
		System.out.println(" --- displayChanged ---");
	}

	public double RetornaX(double angulo, double raio)
	{
		return (raio * Math.cos(Math.PI * angulo / 180.0));
	}

	public double RetornaY(double angulo, double raio)
	{
		return (raio * Math.sin(Math.PI * angulo / 180.0));
	}

	public Point criarPontoCirculo(double angulo, double raio, double posX, double posY)
	{
		int x = (int) (RetornaX(angulo, raio) + posX);
		int y = (int) (RetornaY(angulo, raio) + posY);

		return new Point(x, y);
	}

	public List<Point> criarPontosCirculo(double raio, double posX, double posY)
	{
		final int QTD_PONTOS_CIRCULO = 360;
		
		List<Point> pCirculo = new ArrayList<Point>(QTD_PONTOS_CIRCULO);

		double a = 0;

		for (int i = 0; i < QTD_PONTOS_CIRCULO; i++)
		{
			a = (360 * i) / QTD_PONTOS_CIRCULO;

			pCirculo.add(criarPontoCirculo(a, raio, posX, posY));
		}
		
		return pCirculo;
	}
}
