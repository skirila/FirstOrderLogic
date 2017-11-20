import java.io.*;
import java.util.*;

public class homework {
	public static PriorityQueue<Sentence> sentences;
	public static List<Query> queries;
	public static Map<String, List<List<Variable>>> database;
	
	public static void main(String[] args) {
		sentences = new PriorityQueue<>((a,b) -> a.size-b.size);
		queries = new ArrayList<>();
		database = new HashMap<>();
		readFile();
		resolution();
		boolean[] result = new boolean[queries.size()];
		int k = 0;
		for(Query q: queries) {
			if(isInferred(q)) {
				result[k++] = true;
			} else {
				result[k++] = false;
			}
		}
		output(result);
	}
	
	public static boolean isInferred(Query query) {
		String predicate = query.getPredicate();
		if(!query.result) predicate = "-" + predicate;
		List<Variable> variables = query.variables;
		// 遍历database里面所有的variable可能，看有没有符合的
		if(!database.containsKey(predicate)) return false;
		List<List<Variable>> allVariableList = database.get(predicate);
		
		for(List<Variable> curList : allVariableList) {
			boolean flag = true;
			for(int i=0; i<curList.size(); i++) {
				Variable queryV = variables.get(i);
				Variable curV = curList.get(i);
				// currentVariable 是constant 但不等于query的variable，不成立，下一个list
				if(curV.isConstant && !curV.getName().equals(queryV.getName())) {
					flag = false;
					break;
				}
				else continue;
			}
			if(flag) return true;
			else continue;
		}
		return false;
	}
	
	// 从1个的开始消
	public static void resolution() {
		while(!sentences.isEmpty()) {
			Sentence curSentce = sentences.poll();
			if(curSentce.list.size() > 1) break;
			if(curSentce.list.size() == 0) continue;
			
			Query curQuery = curSentce.list.get(0);
			String flag = "";
			if(!curQuery.getResult()) flag = "-";
			if(isExist(flag, curQuery)) continue;
			if(database.containsKey(flag + curQuery.getPredicate())) {
				database.get(flag + curQuery.getPredicate()).add(curQuery.getVariable());
			}
			else {
				List<List<Variable>> list = new ArrayList<>();
				list.add(curQuery.variables);
				database.put(flag + curQuery.predicate, list);
			}
			//System.out.println("  ");
			//System.out.println("put into database:" + flag + curQuery.toString());
			Sentence[] restSentences = new Sentence[sentences.size()];
			Sentence[] restSentences2 = sentences.toArray(restSentences);
			/*while(itr.hasNext()) {
				Sentence sentence = itr.next();
				Sentence newSentence = new Sentence(sentence.list);
				if(newSentence.updatePredicate(curQuery)) {
					sentences.add(newSentence);
				}
			}*/
			
			for(Sentence sentence : restSentences2) {
				// 创建一个新的sentence
				Sentence newSentence = new Sentence(sentence.getList());
				// 这个句子里包含这个predicate，在pq里面放入新的一句话（删除原predicate，unify）
				if(newSentence.updatePredicate(curQuery)) {
					//System.out.print("origin sentce: ");
					//sentence.print();
					//System.out.print("new Sentence:     ");
					//newSentence.print();
					sentences.add(newSentence);
				}
			}
				
		}
		
	}
	
	// 比较判断是否已经在database里面出现过这个query，同样的predicate，同样的variables
	public static boolean isExist(String flag, Query query) {
		String predicate = flag + query.getPredicate();
		if(!database.containsKey(predicate)) return false;
		List<List<Variable>> list = database.get(predicate);
		List<Variable> originList = query.getVariable();
		for(List<Variable> curList : list) {
			boolean isEqual = true;
			for(int i=0; i<curList.size(); i++) {
				if(!curList.get(i).getName().equals(originList.get(i).getName())) isEqual = false;
			}
			if(isEqual) return true;
		}
		return false;
	}
	
	
	public static void readFile() {
		Scanner in = null;
		try {
			in = new Scanner(new FileReader("input16.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int querySize = Integer.valueOf(in.nextLine());
		String[] query = new String[querySize];
		for(int i = 0; i<querySize; i++){
			query[i] = in.nextLine().trim();
			Query q = convertQuery(query[i]);
			queries.add(q);
		}
		
		int sentenceSize = Integer.valueOf(in.nextLine());
		String[] sentence = new String[sentenceSize];
		for(int i = 0; i<sentenceSize; i++){
			sentence[i] = in.nextLine().trim();
			sentences.add(convertSentence(sentence[i]));
	    }
		
	}
	
	// Convert single string format to Query format
	public static Query convertQuery(String query) {
		if(query == null || query.length() == 0) return null;
		
		String predicate = "";
		boolean result = true;
		
		if(query.charAt(0) == '~') {
			result = false;
			// 去除首字符 非
			query = query.substring(1);
		}
		
		String[] predicateandconstant = query.split("\\("); 
		predicate = predicateandconstant[0];
		String constant  = predicateandconstant[1];
		// 去除最后字符)
		constant = constant.substring(0, constant.length()-1);
		String[] variables = constant.split(",");
		Query newq = new Query(result, predicate, Arrays.asList(variables));
		return newq;
	}
	
	// Convert combination of several queries to Sentence
	public static Sentence convertSentence(String sentence) {
		if(sentence == null || sentence.length() == 0) return null;
		List<Query> list = new ArrayList<>(); 
		String[] query = sentence.split(" \\| ");
		for(String q : query) {
			list.add(convertQuery(q));
		}
		Sentence s = new Sentence(list);
		return s;
	}
	
	public static void output(boolean[] result) {
		String fileName="output.txt";
		try
		{
			BufferedWriter out=new BufferedWriter(new FileWriter(fileName));
			for(boolean r : result) {
				if(r) {
					out.write("TRUE");
				} else {
					out.write("FALSE");
				}
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		 	e.printStackTrace();
		}
	}
	
}

