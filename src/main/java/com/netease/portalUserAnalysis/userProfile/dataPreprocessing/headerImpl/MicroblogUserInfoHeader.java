package com.netease.portalUserAnalysis.userProfile.dataPreprocessing.headerImpl;

import java.util.Arrays;

import com.netease.portalUserAnalysis.common.dataParser.Header;

public class MicroblogUserInfoHeader extends Header {
	
	public static final String email = "email";
	public static final String idCard = "idCard";
	public static final String gender = "gender";
	public static final String birthday = "birthday";
	public static final String corporation = "corporation";
	public static final String school = "school";
	
	public static void main(String[] args) {
		Header o = new MicroblogUserInfoHeader();
		System.out.println(Arrays.toString(o.getHeader()));
	}
}
