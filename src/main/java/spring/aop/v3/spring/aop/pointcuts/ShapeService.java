package spring.aop.v3.spring.aop.pointcuts;

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
