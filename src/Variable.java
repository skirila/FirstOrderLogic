
public class Variable {
	String variableName;
	boolean isConstant;
	
	public Variable(String variableName) {
		this.variableName = variableName;
		char first = variableName.charAt(0);
		if(Character.isUpperCase(first)) {
			this.isConstant = true;
		} else {
			this.isConstant = false;
		}
	}
	
	public boolean equals(String s) {
		return variableName.equals(s);
	}
	
	public void update(String s) {
		variableName = s;
		isConstant = true;
	}
	
	public String getName() {
		String s = "";
		s += variableName;
		return s;
	}
	

}
