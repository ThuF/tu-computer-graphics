package tu.sofia.computer.graphics.material;

import java.util.Random;

public class PlanetMaterialFactory {
	
	private static final int NUMBER_OF_BODIES = 9;
	private static final float[] sun_body = { 1.0f, 1.0f, 0.2f };
	private static final float[] mercury_body = { 0.55f, 0.4f, 0.25f };
	private static final float[] venus_body = { 1.0f, 0.8f, 0.2f };
	private static final float[] earth_body = { 0.2f, 0.6f, 0.6f };
	private static final float[] mars_body = { 0.9f, 0.2f, 0.0f };
	private static final float[] jupiter_body = { 0.7f, 0.5f, 0.5f };
	private static final float[] saturn_body = { 0.4f, 0.2f, 0.2f };
	private static final float[] uranus_body = { 0.57f, 0.7f, 0.7f };
	private static final float[] neptune_body = { 0.0f, 0.7f, 1.0f };
	
	private static final float[] no_mat = { 0.0f, 0.0f, 0.0f, 1.0f };
	
	private static final float[] mat_ambient = { 0.7f, 0.7f, 0.7f, 1.0f };
	private static final float[] mat_diffuse = { 0.1f, 0.5f, 0.8f, 1.0f };
	private static final float[] mat_specular = { 1.0f, 1.0f, 1.0f, 1.0f };
	private static final float[] no_shininess = { 0.0f };
	private static final float[] low_shininess = { 5.0f };
	private static final float[] high_shininess = { 100.0f };
	private static final float[] mat_emission = { 0.3f, 0.2f, 0.2f, 0.0f };

	public enum Planet {
		SUN, MERCURY, VENERA, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE
	}

	private PlanetMaterialFactory() {
	}

	private static float[] getBody(float[] body) {
		if(PlanetMaterialFactory.change) {
			body[0] += PlanetMaterialFactory.redOffset;
			body[1] += PlanetMaterialFactory.greenOffset;
			body[2] += PlanetMaterialFactory.blueOffset;
			PlanetMaterialFactory.counter++;
			if (PlanetMaterialFactory.counter % NUMBER_OF_BODIES == 0) {
				PlanetMaterialFactory.counter = 0;
				PlanetMaterialFactory.change = false;
			}
		}
		return body;
	}

	private static float redOffset, greenOffset, blueOffset;
	private static int counter;
	private static boolean change;

	public static void setBodyOffsets(float red, float green, float blue) {
		boolean changed = false;
		if((PlanetMaterialFactory.redOffset + red) >= 0 && (PlanetMaterialFactory.redOffset + red) <= 255){
			PlanetMaterialFactory.redOffset = red;
			changed = true;
		}
		if((PlanetMaterialFactory.greenOffset + green) >= 0 && (PlanetMaterialFactory.greenOffset + green) <= 255){
			PlanetMaterialFactory.greenOffset = green;
			changed = true;
		}
		if((PlanetMaterialFactory.blueOffset + blue) >= 0 && (PlanetMaterialFactory.blueOffset + blue) <= 255){
			PlanetMaterialFactory.blueOffset = blue;
			changed = true;
		}

		PlanetMaterialFactory.change = changed;
	}

	public static PlanetMaterial getMaterial(Planet planet) {
		PlanetMaterial material = new PlanetMaterial();
		switch (planet) {
		case SUN:
			setProperties(material, getBody(sun_body), mat_diffuse, no_mat, no_shininess, mat_emission);
			break;
		case MERCURY:
			setProperties(material, getBody(mercury_body), mat_diffuse, mat_specular, low_shininess, no_mat);
			break;
		case VENERA:
			setProperties(material, getBody(venus_body),  mat_diffuse, mat_specular, low_shininess, no_mat);
			break;
		case EARTH:
			setProperties(material, getBody(earth_body), mat_diffuse, mat_specular, low_shininess, no_mat);
			break;
		case MARS:
			setProperties(material, getBody(mars_body), mat_diffuse, no_mat, high_shininess, mat_emission);
			break;
		case JUPITER:
			setProperties(material, getBody(jupiter_body), mat_diffuse, no_mat, high_shininess, no_mat);
			break;
		case SATURN:
			setProperties(material, getBody(saturn_body), mat_diffuse, no_mat, high_shininess, no_mat);
			break;
		case URANUS:
			setProperties(material, getBody(uranus_body), mat_diffuse, no_mat, no_shininess, mat_emission);
			break;
		case NEPTUNE:
			setProperties(material, getBody(neptune_body), mat_diffuse, no_mat, no_shininess, mat_emission);
			break;
		}
		return material;
	}

	private static void setProperties(PlanetMaterial material, float[] ambient, float[] diffuse, float[] specular, float[] shininess, float[] emission ) {
		material.setAmbient(ambient);
		material.setDiffuse(diffuse);
		material.setSpecular(specular);
		material.setShininess(shininess);
		material.setEmission(emission);
	}
}
