package com.netease.portalUserAnalysis.common.weblogfilter;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.netease.weblogCommon.data.enums.NeteaseChannel_CS;
import com.netease.weblogCommon.logparsers.WeblogParser;
import com.netease.weblogCommon.utils.TextUtils;
import com.netease.weblogCommon.utils.UrlUtils;

public class WeblogFilterUtils {
	
	
	public static void main(String[] args){
		String s = "183.194.12.161 [20/Aug/2015:00:01:02 +0800] GET /stat/?project=e_index&event=initialized&etype=process&cost=24378&uuid=e48653c23bdfa96a&unew=1&uctime=1440000037983&utime=1440000062660&ultime=0&uvcount=0&resolution=1920/1080&avlbsize=914/436&pagesize=1053/6304&pagescroll=0/0&url=http%3A%2F%2Fe.163.com%2F%23detail%2F99%2FATRTKCHK00162Q5T%3Fdxkl%3Fp%3D2796205&ref=http%3A%2F%2Fxfz163.200wan.net%2F2015%2Fxfz%2F163%2F12.html&cvar={\\x22trace_id\\x22:1440000037981,\\x22email\\x22:\\x22\\x22}&r=0.06439064957058571_0602&pgr=77aace0c96fd552a14400000379824357&entry=1&sid=e48653c23bdfa96a1440000037988 HTTP/1.1 \"-\" \"1440000062.852\" \"http://e.163.com/\" \"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)\"";
		WeblogParser p = new WeblogParser();
		HashMap<String, String> weblogParserWrapper = WeblogFilterUtils.weblogParserWrapper(p, s);
	
		System.out.println(weblogParserWrapper);
	}
	public static final String defNullStr = "(null)";
	private static final String[] columns = { "ip", "serverTime", "project", "event",
			"esource", "einfo", "cost", "uuid", "url", "pureurl",
			"contentchannel", "urldomain", "ref", "refdomain", "pver", "cdata",
			"pagescroll", "pagescrollx", "pagescrolly", "sid", "ptype",
			"entry", "pgr", "prev_pgr", "avlbsize", "avlbsizex", "avlbsizey",
			"resolution", "r", "ccount", "ctotalcount", "csource", "unew",
			"utime", "uctime", "ultime", "cvar_email", "cdata_pageX", "cdata_pageY",
			"cdata_time", "cdata_button", "cdata_tag", "cdata_href",
			"cdata_text", "cdata_img", "cdata_jcid", "cdata_page",
			"browserInfo", "extra_pageY", "extra_pageX" };
	
	//为了效率，这里没限制数组内容不能修改，为了程序的正确行，请不要修改获取的数组
	public static String[] getColumns(){
		return columns;
	}
	
	public static HashMap<String, String> buildKVMap(String line){
		if(StringUtils.isBlank(line)){
			return null;
		}
		
		HashMap<String, String> res = new HashMap<String, String>();
		
		String[] items = line.toString().split("\t");
		String[] columnsLocal = getColumns();
		if(items.length == columnsLocal.length){
			for(int i = 0; i < columnsLocal.length; i++){
				res.put(columnsLocal[i], items[i]);
			}
		}
		
		return res;
	}
	
	public static HashMap<String, String> weblogParserWrapper(WeblogParser weblogParser, String line){
		if(weblogParser == null || StringUtils.isBlank(line)){
			return null;
		}
		
		HashMap<String, String> resMap = new HashMap<String, String>();
		
		Map<String, String> weblogParserMap = weblogParser.parse(line);
//		for(Entry<String, String> entry : weblogParserMap.entrySet()){
//			resMap.put(entry.getKey(), TextUtils.notNullStr(entry.getValue(), defNullStr));
//		}
//		
		for(String  column: columns){
			resMap.put(column, TextUtils.notNullStr(weblogParserMap.get(column), defNullStr));
		}
		
		resMap.put("pureurl", TextUtils.notNullStr(UrlUtils.getOriginalUrl(resMap.get("url")), defNullStr));
		resMap.put("contentchannel", TextUtils.notNullStr(NeteaseChannel_CS.getChannelName(resMap.get("url")), defNullStr));
		resMap.put("urldomain", TextUtils.notNullStr(UrlUtils.urlGetDomain(resMap.get("url")), defNullStr));
		resMap.put("refdomain", TextUtils.notNullStr(UrlUtils.urlGetDomain(resMap.get("ref")), defNullStr));
		
		try {
			String[] pagescrollArr = resMap.get("pagescroll").split("/");
			resMap.put("pagescrollx", TextUtils.notNullStr(pagescrollArr[0]));
			resMap.put("pagescrolly", TextUtils.notNullStr(pagescrollArr[1]));
		} catch (Exception e) {
			resMap.put("pagescrollx", defNullStr);
			resMap.put("pagescrolly", defNullStr);
		}
		
		try {
			String[] avlbsizeArr = resMap.get("avlbsize").split("/");
			resMap.put("avlbsizex", TextUtils.notNullStr(avlbsizeArr[0]));
			resMap.put("avlbsizey", TextUtils.notNullStr(avlbsizeArr[1]));
		} catch (Exception e) {
			resMap.put("avlbsizex", defNullStr);
			resMap.put("avlbsizey", defNullStr);
		}
		
		try {
			String[] cdataArr = resMap.get("cdata").split(",");
			for (String s : cdataArr) {
				if (s.replaceAll("\"", "").startsWith("pageY")) {
					resMap.put("extra_pageY", TextUtils.notNullStr(s.split(":")[1]));
				}else if (s.replaceAll("\"", "").startsWith("{pageX")) {
					resMap.put("extra_pageX", TextUtils.notNullStr(s.split(":")[1]));
				}
			}
		} catch (Exception e) {
			resMap.put("extra_pageX", defNullStr);
			resMap.put("extra_pageY", defNullStr);
		}
		
		return resMap;
	}
}
