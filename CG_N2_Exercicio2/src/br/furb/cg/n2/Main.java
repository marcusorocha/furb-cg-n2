package br.furb.cg.n2;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main implements GLEventListener, KeyListener
{
	private float ortho2D_minX = -400.0f;
	private float ortho2D_maxX = 400.0f;
	private float ortho2D_minY = -400.0f;
	private float ortho2D_maxY = 400.0f;

	private float sruX = 200;
	private float sruY = 200;

	private float raio = 100;

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

		pontos = new ArrayList<Point>(72);

		/*
		 * 360 -- 72 x -- i
		 * 
		 * 72.x = 360.i x = 360.i / 72
		 */

		double a = 0;

		for (int i = 0; i < 72; i++)
		{
			a = (360 * i) / 72;

			pontos.add(criarPontoCirculo(a, raio));
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
		desenharCirculoPontilhado();

		gl.glFlush();
	}

	public void desenharSRU()
	{
		gl.glLineWidth(2.0f);

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

	public void desenharCirculoPontilhado()
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

	public Point criarPontoCirculo(double angulo, double raio)
	{
		int x = (int) RetornaX(angulo, raio);
		int y = (int) RetornaY(angulo, raio);

		return new Point(x, y);
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
}
