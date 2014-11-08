package tu.sofia.computer.graphics;

import java.awt.Dimension;

import javax.media.opengl.GLProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tu.sofia.computer.graphics.renders.IRenderer;

public class EntryPoint {
	private static final Logger log = LoggerFactory.getLogger(EntryPoint.class);

	private EntryPoint() {
	}

	public static ApplicationFrame getApplicationFrame(IRenderer render) {
		return getApplicationFrame(render, ApplicationFrame.DEFAULT_SIZE);
	}

	public static ApplicationFrame getApplicationFrame(IRenderer render, Dimension dimension) {
		log.debug("Initializing OpenGL.");
		GLProfile.initSingleton();

		ApplicationFrame applicationFrame = new ApplicationFrame(dimension);
		log.debug("Starting the EDT.");
		applicationFrame.useRenderer(render);
		applicationFrame.setVisible(true);

		return applicationFrame;
	}
}
