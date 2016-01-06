package spring.aop.v6.spring.aop.around;

public class CircleModel {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setNameWithException(String name) throws Exception {
		throw( new Exception());	
	}
}
