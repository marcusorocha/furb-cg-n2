package br.furb.cg.n2;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
	private float angulo = 45;
	
	private Point p1;
	private Point p2;

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
		
		p1 = new Point(0, 0);
		p2 = new Point(0, 0);

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
		desenharSR();

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

	public void desenharSR()
	{
		double xp2 = RetornaX(angulo, raio) + p2.getX();
		double yp2 = RetornaY(angulo, raio) + p2.getY();
		
		gl.glLineWidth(2);
		gl.glBegin(GL.GL_LINES);
		{
			gl.glVertex2d(p1.getX(), p1.getY());
			gl.glVertex2d(xp2, yp2);
		}
		gl.glEnd();
		
		gl.glPointSize(5);
		gl.glBegin(GL.GL_POINTS);
		{
			gl.glVertex2d(p1.getX(), p1.getY());
			gl.glVertex2d(xp2, yp2);
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

	public double RetornaX(double angulo, double raio)
	{
		return (raio * Math.cos(Math.PI * angulo / 180.0));
	}

	public double RetornaY(double angulo, double raio)
	{
		return (raio * Math.sin(Math.PI * angulo / 180.0));
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
			case KeyEvent.VK_W:// SR para direita
				p1.setLocation(p1.getX() + 10, p1.getY());
				p2.setLocation(p2.getX() + 10, p2.getY());
				glDrawable.display();
				break;
			case KeyEvent.VK_Q:// SR para esquerda
				p1.setLocation(p1.getX() - 10, p1.getY());
				p2.setLocation(p2.getX() - 10, p2.getY());
				glDrawable.display();
				break;
			case KeyEvent.VK_A:// SR diminuir raio
				raio -= 5;	
				glDrawable.display();
				break;
			case KeyEvent.VK_S:// SR aumentar raio
				raio += 5;
				glDrawable.display();
				break;
			case KeyEvent.VK_Z:// SR diminuir angulo
				angulo -= 5;	
				glDrawable.display();
				break;
			case KeyEvent.VK_X:// SR aumentar angulo
				angulo += 5;
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
