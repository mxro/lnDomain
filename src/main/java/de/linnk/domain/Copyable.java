package de.linnk.domain;

public interface Copyable {
	
	public void beforeToString();
	
	public void afterToString();
	
	public void afterInsert();
}
