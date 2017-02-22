package com.netease.portalUserAnalysis.common.dataParser.parserImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.netease.portalUserAnalysis.common.dataParser.Parser;

public class TabReplaceParser implements Parser {
	
	private Parser parser;
	
	public TabReplaceParser(Parser parser) {
		this.parser = parser;
	}

	@Override
	public Map<String, String> parse(String line) {
		Map<String, String> res = new HashMap<>();
		for(Entry<String, String> entry : parser.parse(line).entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			res.put(key.replaceAll("\t", " "), value.replaceAll("\t", " "));
		}
		
		return res;
	}

}
