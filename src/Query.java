import java.util.*;
public class Query {

	boolean result;
	String predicate;
	List<Variable> variables;
	
	public Query(boolean result, String predicate, List<String> variables) {
		this.result = result;
		this.predicate = predicate;
		this.variables = new ArrayList<>();
		for(String variable : variables) {
			Variable v = new Variable(variable);
			this.variables.add(v);
		}
	}
	
	
	public List<String> updateVariable(Map<Variable, Variable> map) {
		List<String> result = new ArrayList<>();
		for(Variable v : variables) {
			if(map.containsKey(v)) {
				result.add(map.get(v).getName());
			} else {
				result.add(v.getName());
			}
		}
		return result;
	}
	
	public List<Variable> getVariable() {
		List<Variable> vList = new ArrayList<>();
		for(Variable v : variables) {
			Variable newv = new Variable(v.getName());
			vList.add(newv);
		}
		return vList;
	}
	
	public List<String> getVariableStr() {
		List<String> vList = new ArrayList<>();
		for(Variable v : variables) {
			Variable newv = new Variable(v.getName());
			vList.add(newv.getName());
		}
		return vList;
	}
	
	public boolean getResult() {
		boolean tempr = false;
		if(result) tempr = true;
		return tempr;
	}
	
	public String getPredicate() {
		String p = "";
		p += predicate;
		return p;
	}
	
	
	public void setVariable(List<Variable> list) {
		variables = new ArrayList<Variable>(list);
	}
	
	public String toString() {
		String s = "";
		s += this.getPredicate();
		s += "(";
		for(Variable v : this.getVariable()) {
			s += v.getName();
			s += ",";
		}
		s = s.substring(0, s.length()-1);
		s += ")";
		return s;
	}
}
