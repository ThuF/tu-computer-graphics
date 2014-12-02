package tu.sofia.computer.graphics;

import tu.sofia.computer.graphics.renders.SolarSystemRenderer;

public class SolarSystemEntryPoint {

	public static void main(String[] args) {
		ApplicationFrame frame = EntryPoint.getApplicationFrame(new SolarSystemRenderer());
		frame.startRendering();
	}
}
