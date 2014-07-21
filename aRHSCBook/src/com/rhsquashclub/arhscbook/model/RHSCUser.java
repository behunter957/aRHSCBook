package com.rhsquashclub.arhscbook.model;

public class RHSCUser extends RHSCMember {
	
	private static RHSCUser user;
	
	private RHSCUser() {
		super();
	}
	
	public void validate(String userid,String password) {
		user.name = userid;
	}
	
	public static RHSCUser get() {
		if (user == null) {
			user = new RHSCUser();
		}
		return user;
	}

}
