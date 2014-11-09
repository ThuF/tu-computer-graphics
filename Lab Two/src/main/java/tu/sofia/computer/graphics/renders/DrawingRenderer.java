package tu.sofia.computer.graphics.renders;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class DrawingRenderer extends AbstractRenderer {

	private static final Random RANDOM = new Random();
	private static final float STEP = 0.05f;

	private double startX = 0;
	private double startY = 0;

	private double endX = 0;
	private double endY = 0;

	private float angle = 0;

	private int lineWidth = 0;

	private float translateX = 0;
	private float translateY = 0;
	private float translateZ = 0;

	private float red = 0;
	private float green = 0;
	private float blue = 0;

	private int polygonSides = 0;

	private boolean isDrawingLines = true;

	@Override
	public void init(GLAutoDrawable drawable) {
		//
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		gl.glColor3f(red, green, blue);
		gl.glTranslatef(translateX, translateY, translateZ);
		gl.glRotatef(angle, 0, 0, 0.5f);

		if (isDrawingLines) {
			drawLines(gl);
		} else {
			drawPolygons(gl, polygonSides);
		}
	}

	private void drawLines(GL2 gl) {
		gl.glLineWidth(lineWidth);
		gl.glBegin(GL.GL_LINES);

		gl.glVertex2d(startX, startY);
		gl.glVertex2d(endX, endY);

		gl.glEnd();
	}

	void drawPolygons(GL2 gl, int sides) {
		if (sides >= 3) {
			gl.glLineWidth(lineWidth);
			gl.glBegin(GL2.GL_LINES);
			double step = (2 * Math.PI) / (double) sides;
			double xFactor = getXFactor();
			double yFactor = getYFactor();
			for (int i = 0; i < sides; ++i) {
				int current = (i + 0) % sides;
				int next = (i + 1) % sides;
				gl.glVertex2d(xFactor * Math.cos(current * step), yFactor * Math.sin(current * step));
				gl.glVertex2d(xFactor * Math.cos(next * step), yFactor * Math.sin(next * step));
			}
			gl.glEnd();
		}
	}

	private double getYFactor() {
		return endY / startY;
	}

	private double getXFactor() {
		return endX / startX;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {
		case '+':
			lineWidth++;
			break;
		case '-':
			if (lineWidth > 0) {
				lineWidth--;
			}
			break;
		case 'w':
		case 'W':
			translateY += STEP;
			break;
		case 's':
		case 'S':
			translateY -= STEP;
			break;
		case 'a':
		case 'A':
			translateX -= STEP;
			break;
		case 'd':
		case 'D':
			translateX += STEP;
			break;
		case 'z':
		case 'Z':
			translateZ += STEP;
			break;
		case 'x':
		case 'X':
			translateZ -= STEP;
			break;
		case 'q':
		case 'Q':
			angle++;
			break;
		case 'e':
		case 'E':
			angle--;
			break;
		case 'c':
		case 'C':
			isDrawingLines = !isDrawingLines;
			break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		startX = getXPosition(e);
		startY = getYPosition(e);
		setPolygonProperties();
		resetDrawingProperties();
	}

	private void setPolygonProperties() {
		if (!isDrawingLines) {
			polygonSides = RANDOM.nextInt(15) + 3;
		} else {
			polygonSides = 0;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		endX = getXPosition(e);
		endY = getYPosition(e);
		generateRandomColor();
	}

	private void resetDrawingProperties() {
		angle = 0;
		translateX = 0;
		translateY = 0;
		translateZ = 0;
	}

	private void generateRandomColor() {
		red = RANDOM.nextFloat();
		green = RANDOM.nextFloat();
		blue = RANDOM.nextFloat();
	}
}
