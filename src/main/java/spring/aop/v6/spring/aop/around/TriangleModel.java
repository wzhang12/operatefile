package spring.aop.v6.spring.aop.around;

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
