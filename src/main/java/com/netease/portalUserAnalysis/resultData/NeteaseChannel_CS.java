package com.netease.portalUserAnalysis.resultData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.netease.weblogCommon.utils.UrlUtils;

public enum NeteaseChannel_CS{
	news("news", "http://news\\.163\\.com",
			"^http://(history|news[\\d]*|discovery?|focus|media|bj|beijing|bluecross|lovegongyi|expo|jiuzai|data|world|view|attitude)\\.163\\.com.*,"
			+ "^http://[^\\.]+\\.(bj|beijing|news|history)\\.163\\.com.*,"
			+ "^http://(shehui|domestic|world)\\.firefox\\.163\\.com.*,"
			+ "^http://tag\\.163\\.com/news.*"),
			
			
	money("money", "http://money\\.163\\.com", 
			"^http://(money|stock|fund|stocksms|stock2|media|finance|stocksms|hq|emarketing|biz|biz2|biz3|biz4|minisite)\\.163\\.com.*,"
			+ "^http://[^/]*\\.(finance|fund|biz|biz2|biz3|biz4|money)\\.163\\.com.*,"
			+ "^http://money\\.firefox\\.163\\.com.*,"
			+ "^http://tag\\.163\\.com/money.*"),
			
			
	digi("digi", "http://digi\\.163\\.com", 
			"^http://(digi|buy|pc|av|dp|dpshow|wm|jvc|minisite)\\.163\\.com.*,"
			+ "^http://[^/]*\\.(digi|buy|jvc)\\.163\\.com.*,"
			+ "^http://hea\\.163\\.com.*,"
			+ "^http://digibbs\\.tech\\.163\\.com.*"),
			
	sport("sport", "http://sports\\.163\\.com",
			"^http://([^/]+\\.)?(2012|2014)\\.163\\.com.*,"
			+ "^http://([^/]+\\.)?(sports|sport|f1|winnerway|2008|2010|2012|cbachina|2010worldcup|worldcup2010|fibawc2010)\\.163\\.com.*,"
			+ "^http://(gpcfootball|euro2008|sz2011|euro2012|2014)\\.163\\.com.*,"
			+ "^http://(goal|v)\\.euro2012\\.163\\.com.*,"
			+ "^http://(golf|psg|sefutbol|cbf|iwf)\\.163\\.com.*,"
			+ "^http://sports\\.firefox\\.163\\.com.*"
			+ "^http://tag\\.163\\.com/sports.*"),
	ent("ent", "http://ent\\.163\\.com",
			"^http://(ent|orz|vod|superfans|worldcup|music|pepsi|fs|xinli|philips|movie|tcl|sexy)\\.163\\.com.*,"
			+ "^http://[^\\.]*\\.(ent|orz|music|pepsi|superfans|fs|tcl|sexy)\\.163\\.com.*,"
			+ "^http://tag\\.163\\.com/ent.*"),
	lady("lady", "http://lady\\.163\\.com",
			"^http://(lady|astro|minisite|qipai|morning|koreastyle|fashion|fushi)\\.163\\.com.*,"
			+ "^http://[^/]*.(lady|koreastyle|fushi)\\.163\\.com.*,"
			+ "^http://data\\.art\\.163\\.com.*"),
			
	mobile("mobile", "http://mobile\\.163\\.com", 
					"^http://[^/]*\\.mobile\\.163\\.com.*,"
					+ "^http://(mobile|minisite)\\.163\\.com.*,"
			//	    + "^http://m\\.163\\.com.*,"
					+"^http://club\\.tech\\.163\\.com.*"),
	
	auto("auto", "http://auto\\.163\\.com",
			"^http://[^/]*\\.auto\\.163\\.com.*,"
			+ "^http://auto[0-9]*\\.163\\.com.*,"
			+ "^http://tag\\.163\\.com/auto.*"),
	edu("edu", "http://edu\\.163\\.com", 
			"^http://([^/]+\\.)?(education|edu|kids|daxue|campus)\\.163\\.com.*"),
	travel("travel", "http://travel\\.163\\.com",
			"^http://travel\\d*\\.163\\.com.*,"
			+ "^http://[^\\.]*\\.travel\\.163\\.com.*"),
			
	tech("tech", "http://tech\\.163\\.com",
					"^http://(tech\\d*|it)\\.163\\.com.*,"
					+ "^http://[^/]*\\.tech\\d*\\.163\\.com.*,"
					+ "^http://tech\\.firefox\\.163\\.com.*"),	

	men("men", "http://men\\.163\\.com", 
			"^http://men\\.163\\.com.*,"
			+ "^http://[^/]*\\.men\\.163\\.com.*"),
	baby("baby", "http://baby\\.163\\.com", 
			"^http://baby\\.163\\.com/(?!\\?channel=).*,"
			+ "^http://[^/]*.baby\\.163\\.com.*"),
	jiankang("jiankang", "http://jiankang\\.163\\.com",
			"^http://jiankang\\.163\\.com.*,"
			+ "^http://[^/]*\\.jiankang\\.163\\.com.*"),
	
	war("war","http://war\\.163\\.com","^http://war\\.163\\.com.*,"
			+ "^http://[^\\.]+\\.war\\.163\\.com.*,"
			+ "^http://war\\.news\\.163.com.*");
			
//	military("military", "http://war\\.163\\.com",
//			"^http://war\\.163\\.com.*"
//			);
	
	public static final String OTHER = "other";
	
	public static NeteaseChannel_CS[] vaildChannelForScore = new NeteaseChannel_CS[] {
			news, money, digi, sport, ent, lady, mobile, auto, edu, travel, tech, men, baby, jiankang,war
	};
	
	private String name;
	private Pattern homePattern;
	private List<Pattern> channelPatterns = new ArrayList<Pattern>();
	
	private NeteaseChannel_CS(String name, String homeRegex, String channelRegexs) {
		this.name = name;
		homePattern = Pattern.compile(homeRegex);
		String[] channelRegexArr = channelRegexs.split(",");
		for(String cr : channelRegexArr){
			channelPatterns.add(Pattern.compile(cr));
		}
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isChannel(String url){
		if(StringUtils.isBlank(url)){
			return false;
		}
		
		for(Pattern cp : channelPatterns){
			if(cp.matcher(url).matches()){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isHome(String url){
		if(StringUtils.isBlank(url)){
			return false;
		}
		
		String pureUrl = UrlUtils.removeAnchor(url);
		if(pureUrl.endsWith("/")){
			pureUrl = pureUrl.substring(0, pureUrl.length() - 1);
		}
		return homePattern.matcher(pureUrl).matches();
	}
	
	public static List<String> getVaildChannelNameForScore(){
		List<String> list = new ArrayList<String>();
		for(NeteaseChannel_CS ncc : vaildChannelForScore){
			list.add(ncc.getName());
		}
		
		Collections.sort(list);
		
		return list;
	}
	
	public static boolean isVaildChannelForScore(String channelName){
		if(StringUtils.isBlank(channelName)){
			return false;
		}
		for(NeteaseChannel_CS ncc : vaildChannelForScore){
			if(ncc.name.equals(channelName)){
				return true;
			}
		}
		return false;
	}
	
	public static NeteaseChannel_CS getChannel(String url){
		if(StringUtils.isBlank(url)){
			return null;
		}
		for(NeteaseChannel_CS val : NeteaseChannel_CS.values()){
			if(val.isChannel(url)){
				return val;
			}
		}
		
		return null;
	}
	
	public static String getChannelName(String url){
		if(StringUtils.isBlank(url)){
			return OTHER;
		}
		for(NeteaseChannel_CS val : NeteaseChannel_CS.values()){
			if(val.isChannel(url)){
				return val.getName();
			}
		}
		
		return OTHER;
	}
}