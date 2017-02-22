package com.netease.portalUserAnalysis.common.dataParser.parserImpl;

import java.util.HashMap;
import java.util.Map;

import com.netease.portalUserAnalysis.common.dataParser.Header;
import com.netease.portalUserAnalysis.common.dataParser.Parser;

public class TextCommonParser implements Parser {

	private String splitRegex = null;
	private String defVal = null;
	
	private String[] KEYS = null;
	
	public TextCommonParser(Header header, String splitRegex, String defVal) {
		this.KEYS = header.getHeader();
		this.splitRegex = splitRegex;
		this.defVal = defVal;
	}
	
	@Override
	public Map<String, String> parse(String line) {
		Map<String, String> res = new HashMap<String, String>();
		String values[] = line.split(splitRegex);
		
		for (int i = 0; i < KEYS.length; ++i) {
			String value = defVal;
			if (i < values.length) {
				value = values[i];
			}
			res.put(KEYS[i], value);
		}

		return res;
	}
}
