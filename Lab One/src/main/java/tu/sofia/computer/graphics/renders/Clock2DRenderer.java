package tu.sofia.computer.graphics.renders;

import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilitiesImmutable;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Clock2DRenderer extends AbstractRenderer {
    private static final Logger log = LoggerFactory.getLogger(Clock2DRenderer.class);
    private static final int CIRCLE_POINTS = 120;
    private static final double CLOCK_BORDER_SIZE_FACTOR = 0.92;
    private static final double MARK_TOP_SIZE_FACTOR = 0.96;
    private static final double MARK_WIDTH_SIZE_FACTOR = 0.006;

    private static final double HOUR_MARKS_BOTTOM_SIZE_FACTOR = 0.85;
    private static final double HOUR_MARK_BOTTOM = CLOCK_BORDER_SIZE_FACTOR * HOUR_MARKS_BOTTOM_SIZE_FACTOR;
    private static final double HOUR_MARK_TOP = CLOCK_BORDER_SIZE_FACTOR * MARK_TOP_SIZE_FACTOR + 0.006;
    private static final double HOUR_MARK_WIDTH = CLOCK_BORDER_SIZE_FACTOR * MARK_WIDTH_SIZE_FACTOR * 2;

    private static final double MINUTE_MARKS_BOTTOM_SIZE_FACTOR = 0.90;
    private static final double MINUTE_MARK_BOTTOM = CLOCK_BORDER_SIZE_FACTOR * MINUTE_MARKS_BOTTOM_SIZE_FACTOR;
    private static final double MINUTE_MARK_TOP = CLOCK_BORDER_SIZE_FACTOR * MARK_TOP_SIZE_FACTOR;
    private static final double MINUTE_MARK_WIDTH = CLOCK_BORDER_SIZE_FACTOR * MARK_WIDTH_SIZE_FACTOR;

    private final Calendar now = GregorianCalendar.getInstance();
    private final Random random = new Random();
    private double r = random.nextDouble(), g = random.nextDouble(), b = random.nextDouble();

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        log.debug("Enabling GL_BLEND: GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA");
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        log.debug("Enabling antialiasing features.");
        gl.glEnable(GL.GL_LINE_SMOOTH);
        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
        gl.glEnable(GL2GL3.GL_POLYGON_SMOOTH);
        gl.glHint(GL2GL3.GL_POLYGON_SMOOTH_HINT, GL.GL_NICEST);
        gl.glEnable(GL2ES1.GL_POINT_SMOOTH);
        gl.glHint(GL2ES1.GL_POINT_SMOOTH_HINT, GL.GL_NICEST);

        GLCapabilitiesImmutable glCapabilities = drawable.getChosenGLCapabilities();
        if (glCapabilities.getSampleBuffers()) {
            log.debug("Sample buffers are enabled. Number of buffers: {}", glCapabilities.getNumSamples());
            if (gl.isExtensionAvailable("GL_ARB_multisample")) {
                log.debug("GL_ARB_multisample is available. Enabling GL_MULTISAMPLE.");
                gl.glEnable(GL.GL_MULTISAMPLE);
            }
        }

        log.debug("Clearing the screen on init()");
        clearScreen(gl);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        log.debug("Reshape(x={},y={},width={},height={}); called.", x, y, width, height);

        /*
         * Preserve aspect ratio on window resize
         */
        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        double aspect = (double) width / height;
        if (aspect >= 1) {
            gl.glOrtho(-aspect, aspect, -1, 1, -1, 1);
        } else {
            gl.glOrtho(-1, 1, -1 / aspect, 1 / aspect, -1, 1);
        }
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();

        log.debug("Clearing the screen on reshape();");
        clearScreen(gl);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        now.setTimeInMillis(System.currentTimeMillis());
        GL2 gl = drawable.getGL().getGL2();
        clearScreen(gl);

        gl.glColor3d(r, g, b);
        drawClockBorder(gl);
        drawMarks(gl);
        drawPointers(gl);
    }

    private void clearScreen(GL2 gl) {
        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    }

    private void drawClockBorder(GL2 gl) {
        gl.glLineWidth(2.5f);
        gl.glBegin(GL.GL_LINE_LOOP);
        for (int i = 0; i < CIRCLE_POINTS; i++) {
            double theta = 2.0 * Math.PI * i / CIRCLE_POINTS;
            double x = CLOCK_BORDER_SIZE_FACTOR * Math.cos(theta);
            double y = CLOCK_BORDER_SIZE_FACTOR * Math.sin(theta);

            gl.glVertex2d(x, y);
        }
        gl.glEnd();
    }

    private void drawMarks(GL2 gl) {
        gl.glPushMatrix();

        double angle;
        for (int i = 0; i < 60; i++) {
            gl.glRotated(6, 0, 0, i);
            angle = i * 6;
            if (angle % 30 == 0) {
                drawHourMark(gl);
            } else {
                drawMinuteMark(gl);
            }
        }
        gl.glPopMatrix();
    }

    private void drawHourMark(GL2 gl) {
        gl.glBegin(GL.GL_TRIANGLES);

        gl.glVertex2d(-HOUR_MARK_WIDTH, HOUR_MARK_BOTTOM);
        gl.glVertex2d(HOUR_MARK_WIDTH, HOUR_MARK_BOTTOM);
        gl.glVertex2d(0, HOUR_MARK_TOP);

        gl.glEnd();
    }

    private void drawMinuteMark(GL2 gl) {
        gl.glBegin(GL.GL_TRIANGLES);

        gl.glVertex2d(-MINUTE_MARK_WIDTH, MINUTE_MARK_BOTTOM);
        gl.glVertex2d(MINUTE_MARK_WIDTH, MINUTE_MARK_BOTTOM);
        gl.glVertex2d(0, MINUTE_MARK_TOP);

        gl.glEnd();
    }

    private void drawPointers(GL2 gl) {
        drawHoursPointer(gl);
        drawMinutesPointer(gl);
        drawSecondsPointer(gl);
    }

    private void drawHoursPointer(GL2 gl) {
        double hourAngle = -(60 * now.get(Calendar.HOUR_OF_DAY) + now.get(Calendar.MINUTE)) / 2;

        gl.glPushMatrix();
        gl.glRotated(hourAngle, 0, 0, 1);
        gl.glBegin(GL.GL_TRIANGLE_STRIP);

        gl.glVertex2d(-0.02, 0);// left
        gl.glVertex2d(0, 0.60);// top
        gl.glVertex2d(0.02, 0);// right
        gl.glVertex2d(0, -0.03);// bottom

        gl.glEnd();
        gl.glPopMatrix();
    }

    private void drawMinutesPointer(GL2 gl) {
        double minutesAngle = -(6 * now.get(Calendar.MINUTE) + now.get(Calendar.SECOND) / 10);

        gl.glPushMatrix();
        gl.glRotated(minutesAngle, 0, 0, 1);
        gl.glBegin(GL.GL_TRIANGLE_STRIP);

        gl.glVertex2d(-0.02, 0);
        gl.glVertex2d(0, 0.75);
        gl.glVertex2d(0.02, 0);
        gl.glVertex2d(0, -0.05);

        gl.glEnd();
        gl.glPopMatrix();
    }

    private void drawSecondsPointer(GL2 gl) {
        double secondsAngle = -(6 * now.get(Calendar.SECOND) + 6 * now.get(Calendar.MILLISECOND) / 1000);

        gl.glPushMatrix();
        gl.glRotated(secondsAngle, 0, 0, 1);
        gl.glBegin(GL.GL_TRIANGLE_STRIP);

        gl.glVertex2d(-0.02, 0);
        gl.glVertex2d(0, 0.80);
        gl.glVertex2d(0.02, 0);
        gl.glVertex2d(0, -0.05);

        gl.glEnd();
        gl.glPopMatrix();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (!e.isActionKey()) {
            switch (e.getKeyChar()) {
                case 'R':
                case 'r': {
                    r = 1;
                    g = 0;
                    b = 0;
                    break;
                }

                case 'G':
                case 'g': {
                    r = 0;
                    g = 1;
                    b = 0;
                    break;
                }

                case 'B':
                case 'b': {
                    r = 0;
                    g = 0;
                    b = 1;
                    break;
                }
                default: {
                    r = random.nextDouble();
                    g = random.nextDouble();
                    b = random.nextDouble();
                    break;
                }
            }
        }
    }
}
