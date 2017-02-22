package com.netease.portalUserAnalysis.userProfile.dataPreprocessing.headerImpl;

import java.util.Arrays;

import com.netease.portalUserAnalysis.common.dataParser.Header;

public class LoveUserInfoHeader extends Header {
	public static final String email = "email";
	public static final String idCard = "idCard";
	public static final String gender = "gender";
	public static final String birthday = "birthday";
	public static final String constellation = "constellation";
	public static final String blood = "blood";
	public static final String height = "height";
	public static final String weight = "weight";
	public static final String education = "education";
	public static final String school = "school";
	public static final String industry = "industry";
	
	public static void main(String[] args) {
		Header o = new LoveUserInfoHeader();
		System.out.println(Arrays.toString(o.getHeader()));
		
		
	}
}
