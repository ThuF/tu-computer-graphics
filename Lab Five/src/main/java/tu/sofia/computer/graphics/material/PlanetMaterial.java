package tu.sofia.computer.graphics.material;

import javax.media.opengl.GL2;

public class PlanetMaterial {

	private static final float NO_MAT[] = { 0.0f, 0.0f, 0.0f, 1.0f };
	private static final float NO_SHININESS[] = { 0.0f };

	private float[] ambient;
	private float[] diffuse;
	private float[] specular;
	private float[] shininess;
	private float[] emission;

	PlanetMaterial() {
	}
	
	public float[] getAmbient() {
		return ambient;
	}

	public void setAmbient(float[] ambient) {
		this.ambient = ambient;
	}

	public float[] getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(float[] diffuse) {
		this.diffuse = diffuse;
	}

	public float[] getSpecular() {
		return specular;
	}

	public void setSpecular(float[] specular) {
		this.specular = specular;
	}

	public float[] getShininess() {
		return shininess;
	}

	public void setShininess(float[] shininess) {
		this.shininess = shininess;
	}

	public float[] getEmission() {
		return emission;
	}

	public void setEmission(float[] emission) {
		this.emission = emission;
	}

	public void drawMaterial(GL2 gl, boolean isLightEnabled) {
		if(isLightEnabled) {
			drawMaterial(gl, getAmbient(), getDiffuse(), getSpecular(), getShininess(), getEmission());
		} else {
			drawMaterial(gl, this.NO_MAT, this.NO_MAT, this.NO_MAT, this.NO_SHININESS, this.NO_MAT);			
		}
	}

	private void drawMaterial(GL2 gl, float[] ambient, float[] diffuse, float[] specular, float[] shininess, float[] emission) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, shininess, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emission, 0);
	}
}
