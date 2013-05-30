package bladequest.graphics;

public interface Interpolatable {
	public Interpolatable interpolateAgainst(Interpolatable rhs, float t);
	void render();
}
