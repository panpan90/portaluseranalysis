package com.netease.portalUserAnalysis.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toolbox.wordsegment.CSegmentor;
import toolbox.wordsegment.SegmentorPool;
import toolbox.wordsegment.TextTermInfo;

public class Segmentor {

    private static HashSet<String> removeWordsSet = null;

    private static final Pattern pattern = Pattern.compile("[\\u4E00-\\u9FFFa-zA-Z]+");
    
    private static SegmentorPool segmentorPool;
    
	public static void init(String path){

		if(StringUtils.isBlank(path)){
			return;
		}
		
		if(path.endsWith("/")){
			path = path.substring(0, path.length() - 1);
		}

        BufferedReader sBr = null;
        BufferedReader bBr = null;
        try {
            String libPath = path + "/wordsegment";
            String stopList = path + "/stoplist.txt";
            String blackList = path + "/blacklist.txt";
            
            segmentorPool = new SegmentorPool(12, 1024, libPath);

            String line = "";
            removeWordsSet = new HashSet<String>();
            // remove stoplist
            sBr = new BufferedReader(new FileReader(stopList));
            while ((line = sBr.readLine()) != null) {
                removeWordsSet.add(line.trim());
            }
            // remove blacklist
            bBr = new BufferedReader(new FileReader(blackList));
            while ((line = bBr.readLine()) != null) {
                removeWordsSet.add(line.trim());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sBr != null) {
                try {
                    sBr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bBr != null) {
                try {
                    bBr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String segmentString2(String inputString) {
        List<String> segsList = Segmentor.segmentStringList(inputString);

        StringBuilder contentSeg = new StringBuilder();

        for (String s: segsList) {
            contentSeg.append(s).append("\t");
        }

        return contentSeg.toString().trim();
    }

    public static String[] segmentString(String inputString) {
       return segmentStringList(inputString).toArray(new String[0]);
    }
    
    public static List<String> segmentStringList(String inputString) {
        List<String> list = new ArrayList<String>();
        
        if (inputString != null && !inputString.isEmpty()) {
            CSegmentor segmentor = segmentorPool.take();
            TextTermInfo[] terms = segmentor.doQuerySegment(inputString);
            segmentorPool.put(segmentor);            
            if (null != terms && 0 < terms.length) {
                Matcher matcher;
                for (TextTermInfo tti : terms) {
                    matcher = pattern.matcher(tti.getTerm());
                    if ((removeWordsSet.contains(tti.getTerm()) == false)
                            && tti.getTerm().length() > 1 
                            && matcher.find()) {
                        list.add(tti.getTerm());
                    }
                }
            }
        }
        
        return list;
    }
    
}
