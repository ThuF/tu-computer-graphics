package tu.sofia.computer.graphics;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tu.sofia.computer.graphics.renders.IRenderer;

import com.jogamp.opengl.util.FPSAnimator;

public class ApplicationFrame extends Frame {
	public static final Dimension DEFAULT_SIZE = new Dimension(500, 500);

	private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ApplicationFrame.class);

    private static final int TARGET_FRAME_RATE_NORMAL = 30;
    private static final int TARGET_FRAME_RATE_REDUCED = 10;
    private static final int FRAMERATE_COUNTER_UPDATE_INTERVAL_FRAMES = 120;
    private static final int TIME_TO_STOP_MS = 100;

    private final GLJPanel renderingSurface;
    private final FPSAnimator fpsAnimator;
    private IRenderer renderer;

	public ApplicationFrame() {
		this(DEFAULT_SIZE);
	}

    public ApplicationFrame(Dimension requiredSize) {
        super();
        renderingSurface = new GLJPanel(getGLCapabilities());
        renderingSurface.setSize(DEFAULT_SIZE);
        renderingSurface.setMinimumSize(DEFAULT_SIZE);
        renderingSurface.setMaximumSize(DEFAULT_SIZE);
        renderingSurface.setPreferredSize(DEFAULT_SIZE);

        fpsAnimator = new FPSAnimator(renderingSurface, TARGET_FRAME_RATE_NORMAL);
        fpsAnimator.setUpdateFPSFrames(FRAMERATE_COUNTER_UPDATE_INTERVAL_FRAMES, System.out);

        addWindowListener(new WindowAdapter() {
            private boolean isPausedBeforeIconification = false;
            private int originalRenderingSpeed = TARGET_FRAME_RATE_NORMAL;

            @Override
            public void windowClosing(WindowEvent e) {
                log.debug("Preparing to teminate the application.");
                log.debug("Average FPS for the lifetime of the application: {}", fpsAnimator.getTotalFPS());
                log.debug("Stoping the animator thread.");
                stopRendering();

                log.debug("Making the window invisible.");
                setVisible(false);

                try {
                    log.debug("Waiting {}ms for the animator thread to stop.", TIME_TO_STOP_MS);
                    Thread.sleep(TIME_TO_STOP_MS);
                } catch (InterruptedException exception) {
                    log.debug("Interrupted, while wainting for the animator to stop: ", exception);
                }

                if (!fpsAnimator.isStarted()) {
                    log.debug("The animator has been stopped successfully.");
                }

                log.debug("Disposing the window.");
                dispose();
            }

            @Override
            public void windowActivated(WindowEvent e) {
                log.debug("The window gained focus. Restoring the original rendering speed: {}", originalRenderingSpeed);
                changeAnimationSpeed(originalRenderingSpeed);
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                log.debug("The window lost focus. Reducing the rendering speed: {}", TARGET_FRAME_RATE_REDUCED);
                originalRenderingSpeed = fpsAnimator.getFPS();
                changeAnimationSpeed(TARGET_FRAME_RATE_REDUCED);
            }

            @Override
            public void windowIconified(WindowEvent e) {
                log.debug("The window is iconified.");
                isPausedBeforeIconification = fpsAnimator.isPaused();
                if (!isPausedBeforeIconification) {
                    log.debug("Pausing the animator.");
                    fpsAnimator.pause();
                }
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                log.debug("The window is deiconified.");
                if (!isPausedBeforeIconification) {
                    log.debug("Resuming the animator.");
                    fpsAnimator.resume();
                }
            }
        });

        add(renderingSurface);
        setMinimumSize(DEFAULT_SIZE);
        pack();
    }

    private static GLCapabilities getGLCapabilities() {
        GLProfile glProfile = GLProfile.getDefault();
        log.info("Is OpenGL v1.0-v3.0 capable: {}", glProfile.isGL2());
        log.info("Is OpenGL v3.1-v3.3 capable: {}", glProfile.isGL3());
        log.info("Is OpenGL v4.0-v4.3 capable: {}", glProfile.isGL4());
        log.info("GL implementation name: {}", glProfile.getImplName());
        log.info("Is hardware rasterizer: {}", glProfile.isHardwareRasterizer());

        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        log.debug("Requesting double buffering.");
        glCapabilities.setDoubleBuffered(true);

        log.debug("Requesting hardware acceleration.");
        glCapabilities.setHardwareAccelerated(true);

        /*
         * Disabled because this option causes ghost images.
         */
        log.debug("Requesting sampleBuffers.");
        glCapabilities.setSampleBuffers(false);
        // glCapabilities.setNumSamples(4);

        return glCapabilities;
    }

    public void startRendering() {
        if (null == renderer) {
            throw new IllegalStateException("You must set a renderer first.");
        }

        log.debug("Starting the animator.");
        fpsAnimator.start();
        fpsAnimator.resume();
    }

    public void stopRendering() {
        log.debug("FPSAnimator stop requested.");
        if (!fpsAnimator.isStarted()) {
            log.debug("The animator is not started. Nothing to do.");
        } else {
            log.debug("The animator is started. Stoping the animator.");
            fpsAnimator.stop();
        }
    }

    public void useRenderer(IRenderer renderer) {
        Objects.requireNonNull(renderer);
        if (fpsAnimator.isStarted()) {
            throw new IllegalStateException("You must first stop the animator: stopRendering();");
        }

        if (null != this.renderer) {
            renderingSurface.disposeGLEventListener(this.renderer, true);
            renderingSurface.removeGLEventListener(this.renderer);
            renderingSurface.removeKeyListener(this.renderer);
            renderingSurface.removeMouseListener(this.renderer);
            renderingSurface.removeMouseMotionListener(this.renderer);
        }

        this.renderer = renderer;
        renderingSurface.addGLEventListener(renderer);
        renderingSurface.addKeyListener(renderer);
        renderingSurface.addMouseListener(renderer);
        renderingSurface.addMouseMotionListener(renderer);
    }

    private void changeAnimationSpeed(int fps) {
        boolean isStarted = fpsAnimator.isStarted();
        boolean isPaused = fpsAnimator.isPaused();

        if (isStarted) {
            fpsAnimator.stop();
        }

        fpsAnimator.setFPS(fps);

        if (isStarted) {
            fpsAnimator.start();
        }
        if (isPaused) {
            fpsAnimator.pause();
        }
    }
}
