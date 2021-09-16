package com.google.ce.demos.DrugInteraction;

public class HealthcareNLPObject {
	
	private String body = "";
	
	public void setBody(String str) {
		
		body = str.replaceAll("[\\n\\t ]", "");
		
	}
	
	public String getBody() {
		
		return body;
		
	}

}
