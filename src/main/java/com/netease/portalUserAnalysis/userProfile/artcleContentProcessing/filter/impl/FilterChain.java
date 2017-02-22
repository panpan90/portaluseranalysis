package com.netease.portalUserAnalysis.userProfile.artcleContentProcessing.filter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.netease.portalUserAnalysis.userProfile.artcleContentProcessing.filter.ContentCleanFilter;

public class FilterChain implements ContentCleanFilter {
	
	private List<ContentCleanFilter> filters;
	
	public FilterChain() {
		this.filters = new ArrayList<ContentCleanFilter>();
	}
	
	public FilterChain addFilter(ContentCleanFilter filter){
		this.filters.add(filter);
		return this;
	}
	
	public void clear(){
		this.filters.clear();
	}

	@Override
	public String doFilter(String s) {
		String result = s;
		
		if (null == result) {
			return result;
		}
		
		
		for(ContentCleanFilter filter : filters){
			result = filter.doFilter(result);
		}
		
		return result;
	}

}


