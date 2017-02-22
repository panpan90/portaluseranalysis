package com.netease.portalUserAnalysis.userProfile.artcleContentProcessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.netease.portalUserAnalysis.userProfile.artcleContentProcessing.filter.impl.FilterChain;
import com.netease.portalUserAnalysis.userProfile.artcleContentProcessing.filter.impl.HtmlTagFilter;
import com.netease.portalUserAnalysis.userProfile.artcleContentProcessing.filter.impl.SpecialCodeFilter;
import com.netease.portalUserAnalysis.utils.Segmentor;

public class ArticleSegmentor {
	
	private String titleF = null;
	private String titleSegmentF = null;
	private String contentF = null;
	private String contentSegmentF = null;
	
	private FilterChain filterChain = new FilterChain();
	
	private void init(){
		filterChain.addFilter(new HtmlTagFilter());
		filterChain.addFilter(new SpecialCodeFilter());
	}
	
	private void titleSegment(){
		segment(titleF, titleSegmentF);
		
	}
	private void contentSegment(){
		segment(contentF, contentSegmentF);
	}
	
	private void segment(String src, String dest){
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(new File(src)));
			bw = new BufferedWriter(new FileWriter(new File(dest)));
			
			String line = null;
			while((line = br.readLine()) != null){
				int splitRegexIndex = line.indexOf("@");
				if(splitRegexIndex <= 0){
					continue;
				}
				
				String doicd = line.substring(0, splitRegexIndex);
				String content = line.substring(splitRegexIndex + 1);
				String pureContent = filterChain.doFilter(content);
				String segmentString2 = Segmentor.segmentString2(pureContent);
				
				bw.write(doicd + "@" + segmentString2);bw.newLine();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(bw != null){
				try {
					bw.flush();
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(br != null){
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		if(args.length != 2){
			System.out.println("args error");
			return; 
		}
		
		long b = System.currentTimeMillis(); 
		
		ArticleSegmentor obj = new ArticleSegmentor();
		
		String baseDir = args[0].endsWith("/") ? args[0] : args[0] + "/";
		String libDir =  args[1];
		
		obj.titleF = baseDir + "title";
		obj.titleSegmentF = baseDir + "titleSegment";
		obj.contentF = baseDir + "content";
		obj.contentSegmentF = baseDir + "contentSegment";
		
		Segmentor.init(libDir);
		
		obj.init();
		
		obj.titleSegment();
		obj.contentSegment();
		
		long e = System.currentTimeMillis(); 
		
		System.out.println("ArticleSegmentor finish, cost " + (e - b)/1000 + "s");
	}

}
