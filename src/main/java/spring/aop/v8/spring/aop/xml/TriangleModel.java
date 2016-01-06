package spring.aop.v8.spring.aop.xml;

public class TriangleModel {
	private String name;

	public String getName() {
		System.out.println("TriangleModel.getName");
		return name;
	}
	
	public String getNameWithException() throws Exception {
		
		throw new Exception();
	}

	public void setName(String name) {
		this.name = name;
	}
}
