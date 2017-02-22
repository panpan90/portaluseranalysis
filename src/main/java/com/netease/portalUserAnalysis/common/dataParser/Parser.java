package com.netease.portalUserAnalysis.common.dataParser;

import java.util.Map;

/**
 * 为解析数据文件提供统一接口
 * */
public interface Parser {
	public Map<String, String> parse(String line);
}
