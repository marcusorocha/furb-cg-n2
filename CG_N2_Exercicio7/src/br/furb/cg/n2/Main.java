package br.furb.cg.n2;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Main implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
	private float ortho2D_minX = -400.0f;
	private float ortho2D_maxX = 400.0f;
	private float ortho2D_minY = -400.0f;
	private float ortho2D_maxY = 400.0f;

	private float sruX = 200;
	private float sruY = 200;

	private GL gl;
	private GLU glu;

	private GLAutoDrawable glDrawable;

	private int raioCirculoMaior = 160;
	private int raioCirculoMenor = 50;

	private double quadradoX1, quadradoY1, quadradoX2, quadradoY2, quadradoX3,
			quadradoY3, quadradoX4, quadradoY4;

	private double xCentroCirculoMaior = 200d;
	private double yCentroCirculoMaior = 200d;

	private double xCentroCirculoMenor = 200d;
	private double yCentroCirculoMenor = 200d;

	private int xAnterior;
	private int yAnterior;
	private boolean dentroCirculo = false;
	

	public void init(GLAutoDrawable drawable) {
		System.out.println(" --- init ---");

		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glDrawable.setGL(new DebugGL(gl));
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		System.out.println("EspaÃ§o de desenho com tamanho: "
				+ drawable.getWidth() + " x " + drawable.getHeight());
	}

	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(ortho2D_minX, ortho2D_maxX, ortho2D_minY, ortho2D_maxY);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		desenharSRU();

		// Circulo Maior
		desenharCirculo(criarPontosCirculo(raioCirculoMaior, xCentroCirculoMaior,
				yCentroCirculoMaior));

		// BBox
		desenharBBox();

		 desenharCirculoMenor(criarPontosCirculo(raioCirculoMenor,
		 xCentroCirculoMenor, yCentroCirculoMenor));

		gl.glFlush();
	}

	public void desenharBBox() {
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glPointSize(2);
		gl.glBegin(GL.GL_LINE_LOOP);

		quadradoX1 = RetornaX(45, raioCirculoMaior) +xCentroCirculoMaior;
		quadradoY1 = RetornaY(45, raioCirculoMaior) +yCentroCirculoMaior;

		quadradoX2 = RetornaX(135, raioCirculoMaior) +xCentroCirculoMaior;
		quadradoY2 = RetornaY(135, raioCirculoMaior) +yCentroCirculoMaior;

		quadradoX3 = RetornaX(225, raioCirculoMaior) +xCentroCirculoMaior;
		quadradoY3 = RetornaY(225, raioCirculoMaior) +yCentroCirculoMaior;

		quadradoX4 = RetornaX(315, raioCirculoMaior) +xCentroCirculoMaior;
		quadradoY4 = RetornaY(315, raioCirculoMaior) +yCentroCirculoMaior;
		
		if(dentroCirculo){
			gl.glColor3f(1.0f, 0.0f, 0.0f);
		}

		gl.glVertex2d(quadradoX1, quadradoY1);
		gl.glVertex2d(quadradoX2, quadradoY2);
		gl.glVertex2d(quadradoX3, quadradoY3);
		gl.glVertex2d(quadradoX4, quadradoY4);
		gl.glEnd();

	}

	public boolean dentroQuadrado(double x, double y) {
		if (((quadradoX1 > x) && (quadradoX2 < x)) && ((quadradoY1 > y) && (quadradoY4 < y))) {
			xCentroCirculoMenor = x;
			yCentroCirculoMenor = y;

			dentroCirculo = false;
			return true;
		}
		return false;
	}

	public boolean dentroCirculo(double x, double y) {
		// distância euclidiana
		double dist = Math.pow(x - xCentroCirculoMaior, 2) + Math.pow(y - yCentroCirculoMaior, 2);
		if (dist < (raioCirculoMaior * raioCirculoMaior)) {
			xCentroCirculoMenor = x;
			yCentroCirculoMenor = y;

			dentroCirculo = true;
			return true;
		}
		
		return false;
	}

	public void desenharSRU() {
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

	public void desenharCirculo(List<Point> pontos) {
		gl.glPointSize(1.5f);
		gl.glColor3f(0.0f, 0.0f, 0.0f);

		gl.glBegin(GL.GL_POINTS);
		{
			for (Point p : pontos)
				gl.glVertex2d(p.getX(), p.getY());
		}
		gl.glEnd();
	}

	public void desenharCirculoMenor(List<Point> pontos) {
		gl.glPointSize(1.5f);
		gl.glColor3f(0.0f, 0.0f, 0.0f);

		gl.glBegin(GL.GL_POINTS);
		{
			for (Point p : pontos)
				gl.glVertex2d(p.getX(), p.getY());
		}
		gl.glEnd();

		// Desenha ponto central do circulo menor
		gl.glPointSize(4.0f);
		gl.glBegin(GL.GL_POINTS);
		gl.glVertex2d(xCentroCirculoMenor, yCentroCirculoMenor);
		gl.glEnd();
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		System.out.println(" --- reshape ---");
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		System.out.println(" --- displayChanged ---");
	}

	public double RetornaX(double angulo, double raio) {
		return (raio * Math.cos(Math.PI * angulo / 180.0));
	}

	public double RetornaY(double angulo, double raio) {
		return (raio * Math.sin(Math.PI * angulo / 180.0));
	}

	public Point criarPontoCirculo(double angulo, double raio, double posX,
			double posY) {
		int x = (int) (RetornaX(angulo, raio) + posX);
		int y = (int) (RetornaY(angulo, raio) + posY);

		return new Point(x, y);
	}

	public List<Point> criarPontosCirculo(double raio, double posX, double posY) {
		final int QTD_PONTOS_CIRCULO = 360;

		List<Point> pCirculo = new ArrayList<Point>(QTD_PONTOS_CIRCULO);

		double a = 0;

		for (int i = 0; i < QTD_PONTOS_CIRCULO; i++) {
			a = (360 * i) / QTD_PONTOS_CIRCULO;

			pCirculo.add(criarPontoCirculo(a, raio, posX, posY));
		}

		return pCirculo;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("key");
		switch (e.getKeyCode()) {
		case KeyEvent.VK_I:// aproximar
			System.out.println("minX " + ortho2D_minX + " maxX " + ortho2D_maxX
					+ " minY " + ortho2D_minY + " maxY " + ortho2D_maxY);
			if ((ortho2D_minX + 50) < -50 && (ortho2D_maxX - 50) > 50
					&& (ortho2D_minY + 50) < -50 && (ortho2D_maxY - 50) > 50) {
				ortho2D_minX += 50.0f;
				ortho2D_maxX -= 50.0f;
				ortho2D_minY += 50.0f;
				ortho2D_maxY -= 50.0f;
				glDrawable.display();
			}
			break;
		case KeyEvent.VK_O:// afastar
			System.out.println("minX " + ortho2D_minX + " maxX " + ortho2D_maxX
					+ " minY " + ortho2D_minY + " maxY " + ortho2D_maxY);
			if ((ortho2D_minX - 50) > -500 && (ortho2D_maxX + 50) < 500
					&& (ortho2D_minY - 50) > -500 && (ortho2D_maxY + 50) < 500) {
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
	public void keyReleased(KeyEvent e) {
		System.out.println(" --- keyReleased ---");
	}

	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println(" --- keyTyped ---");
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int deslocamentoX = e.getX() - xAnterior;
		int deslocamentoY = e.getY() - yAnterior;

		xAnterior = e.getX();
		yAnterior = e.getY();
		
		if(!dentroQuadrado(xCentroCirculoMenor + deslocamentoX, yCentroCirculoMenor - deslocamentoY)){
			dentroCirculo(xCentroCirculoMenor + deslocamentoX, yCentroCirculoMenor - deslocamentoY);
		}
		
		glDrawable.display(); // redesenhar ...
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		xAnterior = e.getX();
		yAnterior = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		glDrawable.display(); // redesenhar ...
	}

}
