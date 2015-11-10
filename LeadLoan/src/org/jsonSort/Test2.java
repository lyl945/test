package org.jsonSort;

public class Test2 {
public static void main(String[] args) {
	try {
		 JSONStringer jj;
			jj = new JSONStringer();
			jj.object();
			jj.key("cinema");
		    jj.array();
		    
		    jj.object();
		    jj.key("id");
		    jj.value("1");
		    jj.endObject();
		    
		    jj.object();
		    jj.key("id");
		    jj.value("2");
		    jj.endObject();
		    
		    jj.endArray();
		    jj.endObject();
		    System.out.println(jj.toString());
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}
}
