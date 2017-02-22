package com.netease.portalUserAnalysis.common.dataParser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class Header {
	
	private String[] header = null;
	
	public final String[] getHeader(){
		if(this.header == null){
			synchronized (this.getClass()) {
				if(this.header == null){
					List<String> lst = new ArrayList<>();
					Field[] fields = this.getClass().getFields();
					for(Field f : fields){
						try {
							lst.add(f.get(null).toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					header = lst.toArray(new String[0]);
				}
			}
		}
		
		return header;
	}
}
