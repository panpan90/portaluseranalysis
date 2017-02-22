package com.netease.portalUserAnalysis.common.dataParser.headerImpl;

import java.util.Arrays;

import com.netease.portalUserAnalysis.common.dataParser.Header;

public class ArticleContentAPIHeader extends Header {
	public static final String postid = "postid";
	public static final String body = "body";
	public static final String topicid = "topicid";
	public static final String other = "other";
	public static final String userid = "userid";
	public static final String dkeys = "dkeys";
	public static final String uip = "uip";
	public static final String docid = "docid";
	public static final String title = "title";
	public static final String statement_type = "statement_type";
	public static final String lmodify = "lmodify";
	public static final String boardid = "boardid";
	public static final String del = "del";
	public static final String ispic = "ispic";
	public static final String relatekey = "relatekey";
	public static final String status = "status";
	public static final String nickname = "nickname";
	public static final String extra = "extra";
	public static final String modelid = "modelid";
	public static final String originalflag = "originalflag";
	public static final String url = "url";
	public static final String navtopicid = "navtopicid";
	public static final String flag = "flag";
	public static final String source = "source";
	public static final String iscomment = "iscomment";
	public static final String sourceurl = "sourceurl";
	public static final String ptime = "ptime";
	
	public static void main(String[] args) {
		Header o = new ArticleContentAPIHeader();
		System.out.println(Arrays.toString(o.getHeader()));
	}
}
