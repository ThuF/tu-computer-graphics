package tu.sofia.computer.graphics.renders;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public class MyRenderer extends AbstractRenderer {

	@Override
	public void init(GLAutoDrawable drawable) {

	}

	private double startX = 0;
	private double startY = 0;
	private double endX = 0;
	private double endY = 0;
	private float angle = 0;
	
	private static final Random RANDOM = new Random();
	private float red = RANDOM.nextFloat();
	private float green = RANDOM.nextFloat();
	private float blue = RANDOM.nextFloat();
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();// load identity matrix

		gl.glLineWidth(width);

		gl.glColor3f(red, green, blue);
		gl.glTranslatef(translateX, translateY, translateZ);
		gl.glRotatef(angle, 0, 0, 0.5f);
		gl.glBegin(GL.GL_LINES);
		
		gl.glVertex2d(startX, startY);
		gl.glVertex2d(endX, endY);
		
		System.out.println(startX + " " + startY + " | " + endX + " " + endY);
		gl.glEnd();
//		drawLineLoop(gl);

	}

	private void drawLineLoop(GL2 gl) {
		gl.glBegin(GL.GL_LINE_LOOP);// start drawing a line loop

		gl.glVertex3f(-1.0f, 0.0f, 0.0f);// left of window
		gl.glVertex3f(0.0f, -1.0f, 0.0f);// bottom of window
		gl.glVertex3f(1.0f, 0.0f, 0.0f);// right of window
		gl.glVertex3f(0.0f, 1.0f, 0.0f);// top of window
		
		gl.glEnd();// end drawing of line loop
	}

	private float translateX = 0;
	private float translateY = 0;
	private float translateZ = 0;

	private static final float STEP = 0.05f;

	private int width = 0;
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {
		case '+':
			width ++;
			break;
		case '-':
			if (width > 0) {
				width --;
			}
			break;
		case 'w':
			translateY += STEP;
			break;
		case 's':
			translateY -= STEP;
			break;
		case 'a':
			translateX -= STEP;
			break;
		case 'd':
			translateX += STEP;
			break;
		case 'z':
			translateZ += STEP;
			break;
		case 'x':
			translateZ -= STEP;
			break;
		case 'r':
			angle ++;
			break;
		case 'e':
			angle --;
			break;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		startX  = (e.getX() - 250) / 250.0;
		startY = (250 - e.getY()) / 250.0;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		endX  = (e.getX() - 250) / 250.0;
		endY = (250 - e.getY()) / 250.0;
		
		red = RANDOM.nextFloat();
		green = RANDOM.nextFloat();
		blue = RANDOM.nextFloat();
	}
	
}
