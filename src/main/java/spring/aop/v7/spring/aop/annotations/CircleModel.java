package spring.aop.v7.spring.aop.annotations;

public class CircleModel {
	private String name;

	@Loggable
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
