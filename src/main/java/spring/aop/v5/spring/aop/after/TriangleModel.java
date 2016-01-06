package spring.aop.v5.spring.aop.after;

public class TriangleModel {
	private String name;

	public String getName() {
		return name;
	}
	
	public String getNameWithException() throws Exception {
		
		throw new Exception();
	}

	public void setName(String name) {
		this.name = name;
	}
}
