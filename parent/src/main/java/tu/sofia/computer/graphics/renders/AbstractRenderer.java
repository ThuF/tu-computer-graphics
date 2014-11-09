package tu.sofia.computer.graphics.renders;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

public abstract class AbstractRenderer implements IRenderer {
	private double width;
	private double height;
	
	protected double getWindowWidth() {
		return width;
	}
	
	protected double getWindowHeight() {
		return height;
	}

	protected double getXPosition(MouseEvent e) {
		return (e.getX() - (getWindowWidth()/2)) / (getWindowWidth()/2);
	}

	protected double getYPosition(MouseEvent e) {
		return ((getWindowHeight()/2) - e.getY()) / (getWindowHeight()/2);
	}

	@Override
	public abstract void init(GLAutoDrawable drawable);

	@Override
	public abstract void display(GLAutoDrawable drawable);
	
	@Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, width, 0, height, -1.0, 1.0);
		this.width = width;
		this.height = height;
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // do nothing
    }
}
