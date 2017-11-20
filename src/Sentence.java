import java.util.*;

public class Sentence {
	List<Query> list;
	int size;
	
	public Sentence(List<Query> list) {
		this.list = list;
		size = list.size();
	}
	
	public List<Query> getList() {
		List<Query> qList = new ArrayList<>();
		for(Query q : list) {
			qList.add(new Query(q.getResult(), q.getPredicate(), q.getVariableStr()));
		}
		return qList;
	}
	
	// 更新这个sentence，如果这个predicate存在，删除，如果不存在，不动
	public boolean updatePredicate(Query curQuery) {
		Map<String, String> convertMap = new HashMap<>();
		for(int i=0; i<list.size(); i++) {
			Query query = list.get(i);
			// 比较query名
			if(query.predicate.equals(curQuery.predicate)) {
				// 比较符号，异或， 相同false，不同true，两个要不同才能resolution
				if(query.result ^ curQuery.result) {
					
					// 比较variable
					List<Variable> queryVariables = query.variables;
					List<Variable> curQueryVariables = curQuery.variables;
					for(int j=0; j<queryVariables.size(); j++) {
						Variable curVariable = curQueryVariables.get(j);
						Variable variable = queryVariables.get(j);
						// 当前curQuery的不是constant，就不用unify，continue
						if(!curVariable.isConstant) continue;
						// 如果是constant，就要unify，先比较对应的query的是不是constant
						if(!variable.isConstant) {
							// 如果不是constant 那么就是 x->Joe, 加到convertmap里面
							convertMap.put(variable.variableName, curVariable.variableName);
						} else {
							// 如果是constant，比较两个constant是否相等，不相等，那么false，相等，不用动
							if(!curVariable.getName().equals(variable.getName())) return false;
						}
					}
					
					list.remove(i);
					// 都比较完一遍，有了convertMap，并且成立没有return false的时候，这个时候就要unify了
					for(Query q : list) {
						//Query newq = new Query(q.result, q.predicate, q.updateVariable(convertMap));
						//list.remove(q);
						//list.add(newq);
						for(Variable v : q.variables) {
							if(convertMap.containsKey(v.variableName)) {
								v.update(convertMap.get(v.variableName));
							}
						}
						
					}
					
					
					size = list.size();
					return true;
				}
			}
		}
		return false;
	}
	
	public void print() {
		List<Query> qList = this.getList();
		String s = "";
		for(Query q : qList) {
			s += q.toString();
			s += " | ";
		}
		s = s.substring(0, s.length()-2);
		System.out.println(s);
	}
	
	
}
