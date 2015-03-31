package br.furb.cg.n2;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main extends KeyAdapter implements GLEventListener
{
	private float ortho2D_minX = -400.0f;
	private float ortho2D_maxX = 400.0f;
	private float ortho2D_minY = -400.0f;
	private float ortho2D_maxY = 400.0f;

	private float sruX = 200;
	private float sruY = 200;
	
	private int[] primitivas;
	private int idxPrimitiva = 0;

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
		
		primitivas = new int[10];
		primitivas[0] = GL.GL_POINTS;
		primitivas[1] = GL.GL_LINES;
		primitivas[2] = GL.GL_LINE_LOOP;
		primitivas[3] = GL.GL_LINE_STRIP;
		primitivas[4] = GL.GL_TRIANGLES;
		primitivas[5] = GL.GL_TRIANGLE_FAN;
		primitivas[6] = GL.GL_TRIANGLE_STRIP;
		primitivas[7] = GL.GL_QUADS;
		primitivas[8] = GL.GL_QUAD_STRIP;
		primitivas[9] = GL.GL_POLYGON;

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
		desenharPrimitiva();

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

	public void desenharPrimitiva()
	{		
		gl.glLineWidth(2);	
		gl.glPointSize(5);
		gl.glBegin(primitivas[idxPrimitiva]);
		{
			switch (idxPrimitiva)
			{
				case 8:
																									
					// Azul
					gl.glColor3f(0.0f, 0.0f, 1.0f);
					gl.glVertex2d(-200, +200);
					
					// Roxo
					gl.glColor3f(1.0f, 0.0f, 1.0f);
					gl.glVertex2d(-200, -200);
					
					// Vermelho
					gl.glColor3f(1.0f, 0.0f, 0.0f);			
					gl.glVertex2d(+200, -200);
					
					// Verde
					gl.glColor3f(0.0f, 1.0f, 0.0f);
					gl.glVertex2d(+200, +200);
					
					break;
					
				default:
			
					// Vermelho
					gl.glColor3f(1.0f, 0.0f, 0.0f);			
					gl.glVertex2d(+200, -200);
					
					// Verde
					gl.glColor3f(0.0f, 1.0f, 0.0f);
					gl.glVertex2d(+200, +200);
					
					// Azul
					gl.glColor3f(0.0f, 0.0f, 1.0f);
					gl.glVertex2d(-200, +200);
					
					// Roxo
					gl.glColor3f(1.0f, 0.0f, 1.0f);
					gl.glVertex2d(-200, -200);
			}
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

	@Override
	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_SPACE: // alternar primitiva
				idxPrimitiva = (idxPrimitiva + 1) % 10;
				
				System.out.println("Primitiva: " + idxPrimitiva);
				
				glDrawable.display();
				break;				
		}
	}
}
