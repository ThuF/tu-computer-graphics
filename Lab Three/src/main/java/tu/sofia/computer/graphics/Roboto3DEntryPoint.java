package tu.sofia.computer.graphics;

import tu.sofia.computer.graphics.renders.Robot3DRenderer;

public class Roboto3DEntryPoint {

	public static void main(String[] args) {
		ApplicationFrame frame = EntryPoint.getApplicationFrame(new Robot3DRenderer());
		frame.startRendering();
	}
}
