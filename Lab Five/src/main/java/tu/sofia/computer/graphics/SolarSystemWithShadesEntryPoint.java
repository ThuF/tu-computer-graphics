package tu.sofia.computer.graphics;

import tu.sofia.computer.graphics.renders.SolarSystemWithShadesRenderer;

public class SolarSystemWithShadesEntryPoint {

	public static void main(String[] args) {
		ApplicationFrame frame = EntryPoint.getApplicationFrame(new SolarSystemWithShadesRenderer());
		frame.startRendering();
	}
}
