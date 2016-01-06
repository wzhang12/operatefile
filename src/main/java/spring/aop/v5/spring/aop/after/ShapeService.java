package spring.aop.v5.spring.aop.after;

public class ShapeService {
	private CircleModel circle;
	private TriangleModel triangle;
	
	public CircleModel getCircle() {
		return circle;
	}
	public void setCircle(CircleModel circle) {
		this.circle = circle;
	}
	public TriangleModel getTriangle() {
		return triangle;
	}
	public void setTriangle(TriangleModel triangle) {
		this.triangle = triangle;
	}
}
