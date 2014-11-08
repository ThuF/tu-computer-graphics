package tu.sofia.computer.graphics.renders;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.GLEventListener;

public interface IRenderer extends GLEventListener, MouseListener, MouseMotionListener, KeyListener {

}
