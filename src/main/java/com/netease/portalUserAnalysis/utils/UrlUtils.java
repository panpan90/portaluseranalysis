package com.netease.portalUserAnalysis.utils;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.netease.weblogCommon.data.enums.NeteaseContentType;

public class UrlUtils {
	private static Pattern PATTERN_ARTICAL = Pattern.compile("http://[\\w.]+\\.163\\.com/([^\\s]+/)?\\d{2}/\\d{4}/\\d{2}/([0-9A-Z]{8}[0-9]{4}[0-9A-Z]{4})(_\\d+|_all)?.html.*");

	
	public static boolean is163Home(String url){
		if(StringUtils.isBlank(url)){
			return false;
		}
		
		String lowerCaseUrl = url.toLowerCase();
		
		return "http://www.163.com/".equals(lowerCaseUrl) || lowerCaseUrl.startsWith("http://www.163.com/#");
	}
	
	public static boolean isNeteaseHome(String url){
		if(StringUtils.isBlank(url)){
			return false;
		}
		
		String lowerCaseUrl = url.toLowerCase();
		
		return "http://www.netease.com".equals(lowerCaseUrl) || lowerCaseUrl.startsWith("http://www.netease.com#");
	}
	
	public static String urlGetDomain(String url, String def) {
		String domain = null;
		try {
			URL u = new URL(url);
			domain = u.getHost();
		} catch (Exception e) {
			domain = def;
		}

		return domain;
	}	
	
	public static String urlGetDomain(String url) {
		return urlGetDomain(url,"");
	}
	
//	public static String urlGetDomain(String url){
//		if(StringUtils.isBlank(url)){
//			return "";
//		}
//		
//		String temp = url.replace("http://", "").replaceAll("https://","");
//		int first = temp.indexOf("/");
//		if(first != -1){
//			return temp.substring(0, first);
//		}else{
//			return temp; 
//		}
//	}
	
	public static boolean isArticle(String url) {
		return PATTERN_ARTICAL.matcher(url).matches();
	}
	
	public static String  getArticleDocid(String url) {
		  
		    Matcher matcher = PATTERN_ARTICAL.matcher(url);
			if (matcher.find()) {
					return  matcher.group(2);
			}
			return null;	
	}
	

	
	public static String getOriginalUrl(String url){
		if(StringUtils.isBlank(url)){
			return url;
		}else{
			return removeAnchor(removeShareFlagAndFix(url.replace("#detail", "octothorpedetail"))).replace("octothorpedetail", "#detail");
		}
		
	}
	
	public static String removeShareFlagAndFix(String url) {
		if (url == null) {
			return null;
		}
		if (url.contains("http://snstj.")) {
			return url.replace("http://snstj.", "http://");
		} else if (url.contains("http//snstj.")) {
			return url.replace("http//snstj.", "http://");
		}
		return url;
	}
	
	public static String removeAnchor(String url) {
		if (url == null) {
			return null;
		}
		if (url.contains("?") || url.contains("#")) {
			return url.split("#|\\?")[0];
		} 
		return url;
	}

//	/**
//	 * 返回NeteaseChannel中的一个渠道名,若无匹配返回null
//	 * */
//	public static String getNeteaseChannel(String url){
//		if(url.contains("163.com")){
//			NeteaseChannelContent ncc = NeteaseChannelContent.getChannel(url);
//			if(null != ncc){
//				return ncc.getName();
//			}
//		}
//		
//		return null;
//	}
	
	public static String mergeSpecial(String url) {
		if (url == null) {
			return null;
		}
		Pattern ph = Pattern.compile(".*_\\d+.html$");
		Pattern ps = Pattern.compile(".*_\\d+/$");
		if (ph.matcher(url).matches()) {
			return url.split("_\\d+.html$")[0]+".html";
		} else if (ps.matcher(url).matches()) {
			return url.split("_\\d+/$")[0]+"/";
		} else {
			return url;
		}
	}
	
	public static String mergeArticle(String url) {
		if (url == null) {
			return null;
		}
		Pattern ph = Pattern.compile(".*.html.*");
		Pattern ps = Pattern.compile(".*_\\d+/$");
		if (ph.matcher(url).matches()) {
			String[] url_temp = url.split(".html");
			return url_temp[0].split("_(\\d+|all)$")[0]+".html";
		} else if (ps.matcher(url).matches()) {
			return url.split("_\\d+/$")[0]+"/";
		} else {
			return url;
		}
	}
	
	public static String getAticalCt(String url){
		String res = null;
		if(NeteaseContentType.artical.match(url)){
			try {
				String regex = "http://[\\w.]+\\.163\\.com/([^\\s]+/)?(\\d{2})/(\\d{4})/\\d{2}/[0-9A-Z]{8}00[0-9]{2}([0-9A-Z]{4}|sp|rt)(_\\d+|_all)?.html.*";
				Pattern pt = Pattern.compile(regex);
				Matcher m = pt.matcher(url); 
				if(m.matches()){
					res = "20" + m.group(2) + m.group(3);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	
	public static String mergeAticleMultiPage(String pureUrl){
		if(NeteaseContentType.getTypeName(pureUrl).equals("artical")&&pureUrl.contains("_")){
			String s[] = pureUrl.split("_");
			if (s.length==2){
				pureUrl= pureUrl.split("_")[0]+".html";
			}	
		}
		return pureUrl;
	}
}
