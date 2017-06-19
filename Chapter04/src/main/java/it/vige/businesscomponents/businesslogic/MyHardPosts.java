package it.vige.businesscomponents.businesslogic;

import javax.ejb.Local;
import javax.ejb.Stateful;

@Stateful
@Local
@Hard
public class MyHardPosts extends MyPosts {
	
	public MyHardPosts() {
		
	}
}
