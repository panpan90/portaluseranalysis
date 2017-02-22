package com.netease.portalUserAnalysis.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

public class PrefixPathFilter implements PathFilter, FileFilter {
	
	private Set<String> prefixSet = new HashSet<String>();
	
	public PrefixPathFilter(String prefix){
		this.prefixSet.add(prefix);
	}
	
	public PrefixPathFilter(Collection<String> prefixs){
		this.prefixSet.addAll(prefixs);
	}
	
	public PrefixPathFilter(String[] prefixs){
		for(String prefix : prefixs){
			this.prefixSet.add(prefix);
		}
	}
	
	@Override
	public boolean accept(Path paramPath) {
		for(String prefix : prefixSet){
			if(paramPath.getName().startsWith(prefix)){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean accept(File pathname) {
		for(String prefix : prefixSet){
			if(pathname.getName().startsWith(prefix)){
				return true;
			}
		}
		
		return false;
	}

}
