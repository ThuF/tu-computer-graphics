package tu.sofia.computer.graphics.renders;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import tu.sofia.computer.graphics.ApplicationFrame;
import tu.sofia.computer.graphics.material.PlanetMaterial;
import tu.sofia.computer.graphics.material.PlanetMaterialFactory;
import tu.sofia.computer.graphics.material.PlanetMaterialFactory.Planet;

import com.jogamp.opengl.util.gl2.GLUT;

public class SolarSystemWithShadesRenderer extends AbstractRenderer {
	// Dimensions, etc.
	private static final double RAD_TO_DEG = 180.0 / Math.PI;
	private static final double INNER_RADIUS = 90.0;
	private static final double TRACK_WIDTH = 20.0; //trace-a na kolata
	private static final double TRACK_MIDDLE = INNER_RADIUS + 0.5 * TRACK_WIDTH;

	// Colours
	private static final float fog[] = { 0.7f, 0.7f, 0.7f, 1.0f };

	private static final double  sun_body[] = { 1.0, 1.0, 0.2 };
	private static final double  mercury_body[] = { 0.55, 0.4, 0.25 };
	private static final double  venus_body[] = { 1.0, 0.8, 0.2 };
	private static final double  earth_body[] = { 0.2, 0.6, 0.6 };
	private static final double  mars_body[] = { 0.9, 0.2, 0.0 };
	private static final double  jupiter_body[] = { 0.7, 0.5, 0.5 };
	private static final double saturn_body[] = { 0.4, 0.2, 0.2 };
	private static final double  uranus_body[] = { 0.57, 0.7, 0.7 };
	private static final double neptune_body[] = { 0.0, 0.7, 1.0 };


	private enum CameraView {	// Constants for different views
		DISTANT, INSIDE, OUTSIDE, DRIVER, HOUSE,
		OTHER, BESIDE, BALLOON, HELICOPTER, AUTO
	};
	
	private CameraView view = CameraView.DISTANT;

	// Global variables.
	private double car_direction = 0.0;	// Variables for car.
	private double car_x_pos = 100.0;
	private double car_y_pos = 0.0;
	private double car_z_pos = 0.0;
	private double height = 5.0;			// Viewer's height
	private double zoom = 50.0;			// Camera zoom setting
	private double mouse_x = 0.0;			// Mouse coordinates
	private double mouse_y = 0.0;
	private int win_width = ApplicationFrame.DEFAULT_SIZE.width;			// Window dimensions
	private int win_height = ApplicationFrame.DEFAULT_SIZE.height;
	private boolean movie_mode = false;		// Change viewpoint periodically.
	private long clock = 0;
	private long next_switch_time = 0;
	private boolean fog_enabled = false;		// Fog data.
	private float fog_density = 0.01f;
	private double bumpiness = 0.0;		// Bumpy road.

	private boolean isLightEnabled = true;
	

	private GLUT glut = new GLUT();
	private GLU glu = new GLU();

	private void drawCircle(GL2 gl, double cx, double cy, double r, int num_segments) {
		gl.glBegin(GL2.GL_LINE_LOOP);
		for (int i = 0; i < num_segments; i++) {
			double theta = 2.0f * Math.PI * (float) i / (float) num_segments;// get
																				// the
																				// current
																				// angle

			float x = (float) (r * Math.cos(theta));// calculate the x component
			float y = (float) (r * Math.sin(theta));// calculate the y component

			gl.glVertex2d(x + cx, y + cy);// output vertex

		}
		gl.glEnd();
	}

	private void drawSaturnCircles (GL2 gl) {
		drawCircle(gl, -110.0, -200.0, 10.5, 50);
		drawCircle(gl, -110.0, -200.0, 11.0, 50);
		drawCircle(gl, -110.0, -200.0, 11.5, 50);
		drawCircle(gl, -110.0, -200.0, 12., 50);
		drawCircle(gl, -110.0, -200.0, 12.5, 50);
		drawCircle(gl, -110.0, -200.0, 13.0, 50);
	}

	private void drawUranusCircles(GL2 gl) {
		drawCircle(gl, -160.0, -65.0, 8.0, 50);
		drawCircle(gl, -160.0, -65.0, 8.5, 50);
		drawCircle(gl, -160.0, -65.0, 9.0, 50);
		drawCircle(gl, -160.0, -65.0, 9.5, 50);
		drawCircle(gl, -160.0, -65.0, 10.0, 50);
		drawCircle(gl, -160.0, -65.0, 10.5, 50);
		drawCircle(gl, -160.0, -65.0, 11.0, 50);
		drawCircle(gl, -160.0, -65.0, 11.5, 50);
		drawCircle(gl, -160.0, -65.0, 12.0, 50);
	}

	private void drawPlanet(GL2 gl, double x, double y, double z, double radius, PlanetMaterial planetMaterial) {
		// Draw the Planet. Planet is facing +X,
		// and up is +Z.  Save current transformation.
		gl.glPushMatrix();	
			gl.glTranslated(x, y, z);		
			gl.glRotated(95.0, 0.0, 1.0, 0.0);
		
			planetMaterial.drawMaterial(gl, isLightEnabled);
			
			glut.glutSolidSphere(radius, 100, 20);
			gl.glTranslated(0.0, 0.0, 12.0);
		gl.glPopMatrix();	
	}

	private void drawAllPlanets (GL2 gl) {
		drawPlanet(gl, 0.0, -70.0, 0.0, 1.25, PlanetMaterialFactory.getMaterial(Planet.MERCURY)); // Draw Mercury
		drawPlanet(gl, -15.0, -135.0, 0.0, 5.25, PlanetMaterialFactory.getMaterial(Planet.VENERA)); // Draw Venera
		drawPlanet(gl, -60.0, -95.0, 0.0, 5.25, PlanetMaterialFactory.getMaterial(Planet.EARTH)); // Draw Earth
		drawPlanet(gl, 70.0, -120.0, 0.0, 3.25, PlanetMaterialFactory.getMaterial(Planet.MARS)); // Draw Mars
		drawPlanet(gl, 50.0, 5.0, 0.0, 10.25, PlanetMaterialFactory.getMaterial(Planet.JUPITER)); // Draw Jupiter
		drawPlanet(gl, -110.0, -200.0, 0.0, 8.25, PlanetMaterialFactory.getMaterial(Planet.SATURN)); // Draw Saturn
		drawSaturnCircles(gl);
		drawPlanet(gl, -160.0, -65.0, 0.0, 4.0, PlanetMaterialFactory.getMaterial(Planet.URANUS)); // Draw Uranus
		drawUranusCircles(gl);
		drawPlanet(gl, 230.0, -100.0, 0.0, 4.25, PlanetMaterialFactory.getMaterial(Planet.NEPTUNE)); // Draw Neptune
	}

	private void drawScenery (GL2 gl) {
		 //Draw a patch of grass, a circular road, and some houses.
		initScenery(gl);
		drawPlanet(gl, 0.0, 0.0, 0.0, 21.25, PlanetMaterialFactory.getMaterial(Planet.SUN)); // Draw Sun
	}

	private void setViewpoint (GL2 gl) {
		// Use the current viewpoint to display.
		switch (view) {

		case DISTANT:
			// Static viewpoint and stationary camera.
			glu.gluLookAt( 
				250.0, 0.0, 20.0 * height,
				0.0, 0.0, 0.0, 
				0.0, 0.0, 1.0 );
			drawScenery(gl);	
		
			// Move to position of every planet.
			// Rotate so planet stays parallel to track.		
			gl.glTranslated(car_x_pos, car_y_pos, car_z_pos);
			gl.glRotated(RAD_TO_DEG * car_direction, 0.0, 0.0, -1.0);
			drawAllPlanets(gl);
			break;
		
		case INSIDE:
			// Static viewpoint inside the track; camera follows car.
			glu.gluLookAt(
				85.0, 0.0, height,
				car_x_pos, car_y_pos, 0.0,
				0.0, 0.0, 1.0 );
			drawScenery(gl);
			gl.glTranslated(car_x_pos, car_y_pos, car_z_pos);
			gl.glRotated(RAD_TO_DEG * car_direction, 0.0, 0.0, -1.0);
			drawAllPlanets(gl);
			break;
		
		case OUTSIDE:
			// Static viewpoint outside the track; camera follows car.
			glu.gluLookAt(
				115.0, 0.0, height,
				car_x_pos, car_y_pos, 0.0,
				0.0, 0.0, 1.0 );
			drawScenery(gl);
			gl.glTranslated(car_x_pos, car_y_pos, car_z_pos);
			gl.glRotated(RAD_TO_DEG * car_direction, 0.0, 0.0, -1.0);
			drawAllPlanets(gl);
			break;
		
		case DRIVER:
			// Driver's point of view.  gluLookAt() is defined in "car space".
			// After drawing the car, we use inverse transformations to show
			// the scenery.  The same idea is used for OTHER and BESIDE.
			glu.gluLookAt(
				2.0, 0.0, height,
				12.0, 0.0, 2.0,
				0.0, 0.0, 1.0 );
			drawAllPlanets(gl);
			drawScenery(gl);
			break;

		case HOUSE:
			// Drive around while looking at a house.  The first rotation
			// couteracts the rotation of the car.  gluLookAt() looks from
			// the driver's position to the house at (40,120).
			gl.glRotated(RAD_TO_DEG * car_direction, 0.0, -1.0, 0.0);
			glu.gluLookAt(
				2.0, 0.0, height,
				40.0 - car_x_pos, 120.0 - car_y_pos, car_z_pos,
				0.0, 0.0, 1.0 );
			drawAllPlanets(gl);
			gl.glRotated(RAD_TO_DEG * car_direction, 0.0, 0.0, 1.0);
			gl.glTranslated(- car_x_pos, - car_y_pos, car_z_pos);
			drawScenery(gl);
			break;

		case OTHER:
			// View looking backwards from another car.
			glu.gluLookAt(
				25.0, 5.0, height,
				0.0, 0.0, 3.0 + car_z_pos,
				0.0, 0.0, 1.0 );
			drawAllPlanets(gl);
			gl.glRotated(RAD_TO_DEG * car_direction, 0.0, 0.0, 1.0);
			gl.glTranslated(- car_x_pos, - car_y_pos, 0.0);
			drawScenery(gl);
			break;
		
		case BESIDE:
			// View from beside the car.
			glu.gluLookAt(
				5.0, 15.0, height,
				5.0, 0.0, 3.0 + car_z_pos,
				0.0, 0.0, 1.0 );
			drawAllPlanets(gl);
			gl.glRotated(RAD_TO_DEG * car_direction, 0.0, 0.0, 1.0);
			gl.glTranslated(- car_x_pos, - car_y_pos, 0.0);
			drawScenery(gl);
			break;
		
		case BALLOON:
			// View from a balloon.
			glu.gluLookAt(
				150.0, 75.0, 250.0,
				200.0 * mouse_x, 200.0 * mouse_y, 0.0,
				0.0, 0.0, 1.0 );
			drawScenery(gl);		
			gl.glTranslated(car_x_pos, car_y_pos, car_z_pos);
			gl.glRotated(RAD_TO_DEG * car_direction, 0.0, 0.0, -1.0);
			drawAllPlanets(gl);
			break;

		case HELICOPTER:
			// View from a helicopter.
			glu.gluLookAt( 
				200.0 * mouse_x, 200.0 * mouse_y, 200.0,
				0.0, 0.0, 0.0, 
				0.0, 0.0, 1.0 );
			drawScenery(gl);
			gl.glTranslated(car_x_pos, car_y_pos, car_z_pos);
			gl.glRotated(RAD_TO_DEG * car_direction, 0.0, 0.0, -1.0);
			drawAllPlanets(gl);
			break;
		case AUTO:
			break;
		default:
			break;
		}
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		setViewpoint(gl);
		drive();
	}

	private void set_projection (GL2 gl) {
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(zoom, win_width / win_height,  1.0, 500.0);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		win_width = width;
		win_height = height;
		gl.glViewport(0, 0, win_width, win_height);
		set_projection(gl);
	}

	private void drive() {
		// Idle callback function moves the car.  Since this function 
		// posts redisplay whenever there is nothing else to do, 
		// we do not need any other calls to glutPostRedisplay().
		car_direction += 0.07f;
		if (car_direction > (Math.PI * 2)) 
			car_direction -= Math.PI;
		car_x_pos = TRACK_MIDDLE * Math.sin(car_direction);
		car_y_pos = TRACK_MIDDLE * Math.cos(car_direction);
		car_z_pos = (bumpiness * Math.random()) / 10;	

		
		if (movie_mode) {
			clock++;
			if (clock > next_switch_time) {
				next_switch_time += 20 + Math.random() % 200;
				System.out.println("Time: " + clock + ".  Next change: " + next_switch_time +'.');
				switch (new Random().nextInt(7)) {
				case 0:
					view = CameraView.DISTANT;
					break;
				case 1:
					view = CameraView.INSIDE;
					break;
				case 2:
					view = CameraView.OUTSIDE;
					break;
				case 3:
					view = CameraView.DRIVER;
					break;
				case 4:
					view = CameraView.HOUSE;
					break;
				case 5:
					view = CameraView.OTHER;
					break;
				case 6:
					view = CameraView.BESIDE;
					break;
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mouse_x = (2.0 * e.getX()) / win_width - 1.0;
		mouse_y = (2.0 * e.getY()) / win_height - 1.0;
	}

	private void set_fog_density(GL2 gl, float density) {
		// Helper function for keyboard callback.
		fog_density = density;
		System.out.println("Fog density: " + fog_density);
		gl.glFogf(GL2.GL_FOG_DENSITY, fog_density);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 'b':
			// Bumpiness control.
			if (bumpiness == 0.0) {
				System.out.println("Bumpy road!  Use ',' and '>' to change bumpiness.");
				bumpiness = 0.1f;
			}
			else {
				System.out.println("Smooth road again!");
				bumpiness = 0.0;
			}
			break;
		case '<':
			// More bumpiness.
			bumpiness /= 2.0;
			break;
		case '>':
			// Less bumpiness.
			bumpiness *= 2.0;
			break;

		case 'l':
			isLightEnabled = !isLightEnabled;
			break;
		case 'y':
			PlanetMaterialFactory.setBodyOffsets(0.1f, 0.0f, 0.0f);
			break;
		case 'u':
			PlanetMaterialFactory.setBodyOffsets(0.0f, 0.1f, 0.0f);
			break;
		case 'i':
			PlanetMaterialFactory.setBodyOffsets(0.0f, 0.0f, 0.1f);
			break;
		case 'h':
			PlanetMaterialFactory.setBodyOffsets(-0.1f, 0.0f, 0.0f);
			break;
		case 'j':
			PlanetMaterialFactory.setBodyOffsets(0.0f, -0.1f, 0.0f);
			break;
		case 'k':
			PlanetMaterialFactory.setBodyOffsets(0.0f, 0.0f, -0.1f);
			break;
		case '+':
			// Increase fog density.
			// TODO
			// set_fog_density(2.0 * fog_density);
			break;
		case '-':
			// Decrease fog density.
			// TODO
			// set_fog_density(0.5f * fog_density);
			break;
		case 'a':
			zoom *= 1.2f;
			System.out.println("Zoom angle = " + zoom);
			// TODO
			// set_projection();
			break;
		case 'd':
			zoom /= 1.2f;
			System.out.println("Zoom angle = " + zoom);
			// TODO
			// set_projection();
			break;
		case 'w':
			height += 1.0;
			System.out.println("Height = " + height);
			break;
		case 's':
			height -= 1.0;
			System.out.println("Height = " + height);
			break;
		case '1':
			movie_mode = false;
			view = CameraView.DISTANT;
			System.out.println("Distant, fixed viewpoint.");
			break;
		case '2':
			movie_mode = false;
			System.out.println("View from a panning camera inside the track.");
			view = CameraView.INSIDE;
			break;
		case '3':
			movie_mode = false;
			System.out.println("View from a panning camera outside the track.");
			view = CameraView.OUTSIDE;
			break;
		case '4':
			movie_mode = false;
			view = CameraView.DRIVER;
			height = 6.0;
			zoom = 75.0;
			
			// TODO
			// set_projection();
			
			System.out.println("View from the driver's seat.");
			break;
		case '5':
			movie_mode = false;
			view = CameraView.HOUSE;
			System.out.println("Looking at a house while driving around.");
			break;
		case '6':
			movie_mode = false;
			view = CameraView.OTHER;
			System.out.println("Looking back from the car in front.");
			break;
		case '7':
			movie_mode = false;
			view = CameraView.BESIDE;
			System.out.println("View from another car.");
			break;
		case '8':
			movie_mode = false;
			view = CameraView.BALLOON;
			System.out.println("View from a balloon: use mouse to change viewing direction.");
			break;
		case '9':
			movie_mode = false;
			view = CameraView.HELICOPTER;
			System.out.println("View from a helicopter: move mouse to fly around.");
			break;
		case '0':
			movie_mode = true;
			clock = 0;
			next_switch_time = 0;
			System.out.println("Movie mode: views change at random intervals.");
			break;
		}
	}
	

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		initScenery(gl);
		
		// Select GL options.
		gl.glEnable(GL2.GL_DEPTH_TEST);
		GLUquadric p = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(p, GLU.GLU_FILL);
		gl.glFogi(GL2.GL_FOG_MODE, GL2.GL_EXP);
		gl.glFogfv(GL2.GL_FOG_COLOR, fog, 0);
		gl.glFogf(GL2.GL_FOG_DENSITY, fog_density);
		gl.glClearColor(fog[0], fog[1], fog[2], fog[3]);
		
		// Initialize projection.
		set_projection(gl);
	}

	private void initScenery(GL2 gl) {
		float ambient[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		float diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float position[] = { 0.0f, 0.0f, 1.0f, 1.0f };
		float lmodel_ambient[] = { 0.4f, 0.4f, 0.4f, 1.0f };
		float local_view[] = { 0.0f };

		gl.glShadeModel(GL2.GL_SMOOTH);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, local_view, 0);

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
	}

}
