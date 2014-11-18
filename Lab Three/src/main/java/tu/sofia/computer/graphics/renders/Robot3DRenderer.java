package tu.sofia.computer.graphics.renders;

import java.awt.event.KeyEvent;
import java.util.Random;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import tu.sofia.computer.graphics.ApplicationFrame;

import com.jogamp.opengl.util.gl2.GLUT;

public class Robot3DRenderer extends AbstractRenderer {

	private static final float STEP = 5.0f;
	private static final Random RAND = new Random();
	private static final GLUT glut = new GLUT();

	private float worldWindowAspectRatio = 0.0f;

	private float rx = 0.0f;
	private float ry = 0.0f;
	private float legs = 0.0f;
	private float leftHand = 0.0f;
	private float rightHand = 0.0f;

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-3.0f, 3.0, -3.0f, 3.0f, -3.0, 3.0);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		worldWindowAspectRatio = ApplicationFrame.DEFAULT_SIZE.width
				/ ApplicationFrame.DEFAULT_SIZE.height;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glLoadIdentity();

		gl.glRotatef(rx, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(ry, 0.0f, 1.0f, 0.0f);

		drawCoordSystem(gl);

		drawBody(gl);
		drawHead(gl);

		drawLeftLeg(gl, legs);
		drawRightLeg(gl, legs);

		drawLeftHand(gl, leftHand);
		drawRightHand(gl, rightHand);
	}

	private void drawBody(GL2 gl) {
		gl.glLineWidth(3f);
		gl.glColor3f(bodyColor.getRed(), bodyColor.getGreen(), bodyColor.getBlue());
		gl.glPushMatrix();
		gl.glTranslatef(0.5f, 0.5f, 0.0f);
		gl.glScalef(1.0f, 2.0f, 1.0f);
		glut.glutWireCube(1.0f);
		gl.glPopMatrix();
		gl.glLineWidth(1f);
	}

	private void drawHead(GL2 gl) {
		gl.glColor3f(headColor.getRed(), headColor.getGreen(), headColor.getBlue());
		gl.glPushMatrix();
		gl.glTranslatef(0.5f, 2.0f, 0.0f);
		gl.glScalef(0.5f, 1.0f, 1.0f);
		glut.glutWireCube(1.0f);
		gl.glPopMatrix();
	}

	private void drawLeftHand(GL2 gl, float rotateAngle) {
		drawHand(gl, -0.5f, 1.0f, 0.0f, rotateAngle);
	}

	private void drawRightHand(GL2 gl, float rotateAngle) {
		drawHand(gl, 1.5f, 1.0f, 0.0f, rotateAngle);
	}

	private Color headColor = new Color(RAND.nextFloat(), RAND.nextFloat(), RAND.nextFloat());
	private Color bodyColor = new Color(RAND.nextFloat(), RAND.nextFloat(), RAND.nextFloat());
	private Color handsColor = new Color(RAND.nextFloat(), RAND.nextFloat(), RAND.nextFloat());
	private Color legsColor = new Color(RAND.nextFloat(), RAND.nextFloat(), RAND.nextFloat());

	private void drawLeftLeg(GL2 gl, float rotateAngle) {
		drawLeg(gl, -0.25f, -0.5f, 0.0f, rotateAngle);
	}

	private void drawRightLeg(GL2 gl, float rotateAngle) {
		drawLeg(gl, 1.25f, -0.5f, 0.0f, rotateAngle);
	}

	private void drawLeg(GL2 gl, float x, float y, float z, float rotateAngle) {
		gl.glLineWidth(3f);
		gl.glColor3f(legsColor.getRed(), legsColor.getGreen(), legsColor.getBlue());
		gl.glPushMatrix();
		gl.glTranslatef(x, y, z);
		gl.glScalef(0.5f, 1.0f, 1.0f);
		gl.glRotatef(rotateAngle, 1.0f, 0.0f, 0.0f);
		glut.glutWireCube(1.0f);
		gl.glPopMatrix();
		gl.glLineWidth(1f);
	}

	private void drawHand(GL2 gl, float x, float y, float z, float rotateAngle) {
		gl.glColor3f(handsColor.getRed(), handsColor.getGreen(), handsColor.getBlue());
		gl.glPushMatrix();
		gl.glTranslatef(x, y, z);
		gl.glScalef(1.0f, 0.5f, 1.0f);
		gl.glRotatef(rotateAngle, 1.0f, 0.0f, 0.0f);
		glut.glutWireCube(1.0f);
		gl.glPopMatrix();
	}

	private void drawCoordSystem(GL2 gl) {
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(2.0f, 0.0f, 0.0f);
		gl.glEnd();

		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 2.0f, 0.0f);
		gl.glEnd();

		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 0.0f, 2.0f);
		gl.glEnd();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 'w':
		case 'W':
			rx -= STEP;
			break;
		case 's':
		case 'S':
			rx += STEP;
			break;
		case 'a':
		case 'A':
			ry -= STEP;
			break;
		case 'd':
		case 'D':
			ry += STEP;
			break;
		case 'x':
		case 'X':
			legs += STEP;
			break;
		case 'q':
		case 'Q':
			leftHand -= STEP;
			break;
		case 'e':
		case 'E':
			rightHand -= STEP;
			break;
		default:
			return;
		}

		generateRandomColors();
	}

	private void generateRandomColors() {
		headColor.setRed(RAND.nextFloat());
		headColor.setGreen(RAND.nextFloat());
		headColor.setBlue(RAND.nextFloat());

		bodyColor.setRed(RAND.nextFloat());
		bodyColor.setGreen(RAND.nextFloat());
		bodyColor.setBlue(RAND.nextFloat());

		handsColor.setRed(RAND.nextFloat());
		handsColor.setGreen(RAND.nextFloat());
		handsColor.setBlue(RAND.nextFloat());

		legsColor.setRed(RAND.nextFloat());
		legsColor.setGreen(RAND.nextFloat());
		legsColor.setBlue(RAND.nextFloat());
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		if (worldWindowAspectRatio > (float) (width / height)) {
			gl.glViewport(0, 0, (int) width, (int) (width / worldWindowAspectRatio));
		} else {
			gl.glViewport(0, 0, (int) (height * worldWindowAspectRatio), (int) height);
		}
	}

	private static class Color {
		private float red;
		private float green;
		private float blue;

		public Color(float red, float green, float blue) {
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		public float getRed() {
			return red;
		}

		public float getGreen() {
			return green;
		}

		public float getBlue() {
			return blue;
		}

		public void setRed(float red) {
			this.red = red;
		}

		public void setGreen(float green) {
			this.green = green;
		}

		public void setBlue(float blue) {
			this.blue = blue;
		}
	}
}
