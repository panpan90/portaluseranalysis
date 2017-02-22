package com.netease.portalUserAnalysis.utils;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
	public static final String defNullStr = "null";
	
	public static String notNullStr(String s, String def) {
		return StringUtils.isBlank(s) ? def : s.trim();
	}
	
	public static String notNullStr(String s) {
		return notNullStr(s, "");
	}
	
    /**
     * 加强版 first.compareTo(second)
     * */
    public static int strCompare(String first, String second){
    	if(null != first && null != second){
    		return first.compareTo(second);
    	}else if (null == first && null != second) {
    		return -1;
    	}else if (null != first && null == second ){
    		return 1;
    	}else{
    		return 0;
    	}
    }
    
    public static String quickUnescape(String src) {
		if(StringUtils.isBlank(src)){
			return src;	
		}
		
        try {
        	src = src.replaceAll("\"", "%22");//日志预处理
        	src = src.replace("\\x", "%");
        	
			StringBuffer tmp = new StringBuffer();  
			tmp.ensureCapacity(src.length());  
			int lastPos = 0, pos = 0;  
			char ch;  
			while (lastPos < src.length()) {
			    pos = src.indexOf("%", lastPos);  
			    if (pos == lastPos && pos < src.length()) {
			    	int intFrom;
			    	int intTo;
			    	int len;
			    	
			        if (pos + 1 < src.length() && src.charAt(pos + 1) == 'u') {
			        	intFrom = pos + 2;
			        	intTo =  pos + 6;
			        	len = 6;
			        } else {  
			        	intFrom = pos + 1;
			        	intTo =  pos + 3;
			        	len = 3;
			        }  
			        
			        if(intFrom < src.length() && intTo <= src.length()){
			        	String intS = src.substring(intFrom, intTo);
			        	try {
							ch = (char) Integer.parseInt(intS, 16);
							tmp.append(ch);
						} catch (Exception e) {
							tmp.append("%");
							len = 1;
						}  
			        }else{
			        	tmp.append("%" + src.substring(pos, src.length()));
			        }
			        
		            lastPos = pos + len; 
			    } else {
			        if (pos == -1) {  
			            tmp.append(src.substring(lastPos));  
			            lastPos = src.length();  
			        } else {  
			            tmp.append(src.substring(lastPos, pos));  
			            lastPos = pos;  
			        }  
			    }  
			}  

			return tmp.toString();
		} catch (Exception e) {
//			e.printStackTrace();
			return src;
		}  
    }  

}
