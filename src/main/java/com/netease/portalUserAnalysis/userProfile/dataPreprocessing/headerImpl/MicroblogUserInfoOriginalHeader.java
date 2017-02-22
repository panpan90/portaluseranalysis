package com.netease.portalUserAnalysis.userProfile.dataPreprocessing.headerImpl;

import java.util.Arrays;

import com.netease.portalUserAnalysis.common.dataParser.Header;

public class MicroblogUserInfoOriginalHeader extends Header {
	public static final String ID = "ID";
	public static final String Email = "Email";
	public static final String ScreenName = "ScreenName";
	public static final String NickName = "NickName";
	public static final String Avatar = "Avatar";
	public static final String Gender = "Gender";
	public static final String Location = "Location";
	public static final String Mobile = "Mobile";
	public static final String Birthday = "Birthday";
	public static final String School = "School";
	public static final String Corporation = "Corporation";
	public static final String Url = "Url";
	public static final String PoPo = "PoPo";
	public static final String QQ = "QQ";
	public static final String MSN = "MSN";
	public static final String Description = "Description";
	public static final String CreatedTime = "CreatedTime";
	public static final String Verified = "Verified";
	public static final String InformMail = "InformMail";
	public static final String InformMobile = "InformMobile";
	public static final String TrueName = "TrueName";
	public static final String IdCard = "IdCard";
	public static final String UpdatedTime = "UpdatedTime";
	
	public static void main(String[] args) {
		Header o = new MicroblogUserInfoOriginalHeader();
		System.out.println(Arrays.toString(o.getHeader()));
	}
}
