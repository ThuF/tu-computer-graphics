package tu.sofia.computer.graphics;

import tu.sofia.computer.graphics.renders.DrawingRenderer;

public class DrawingEntryPoint {

	public static void main(String[] args) {
		ApplicationFrame frame = EntryPoint.getApplicationFrame(new DrawingRenderer());
		frame.startRendering();
	}
}
