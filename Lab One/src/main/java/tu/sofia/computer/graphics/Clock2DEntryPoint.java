package tu.sofia.computer.graphics;

import tu.sofia.computer.graphics.renders.Clock2DRenderer;

public class Clock2DEntryPoint {

	public static void main(String[] args) {
		ApplicationFrame frame = EntryPoint.getApplicationFrame(new Clock2DRenderer());
		frame.startRendering();
	}
}
