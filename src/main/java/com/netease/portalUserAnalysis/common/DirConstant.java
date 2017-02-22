package com.netease.portalUserAnalysis.common;

public class DirConstant {
	public static String DIR_PREFIX = "/ntes_weblog/";

	public static final String EVENT_DETAIL = "/user/mobilestat/formatlog/event_detail/";
	public static final String DEVICE = "/user/mobilestat/formatlog/device/";
	public static final String ADM_SDK_CHANNEL_DEVICE_STAT_DAY = "/user/portal/ADM/SDK/ADM_SDK_CHANNEL_DEVICE_STAT_DAY/";

	public static final String PORTAL_USER_ANALYSIS_BASE_DIR = "/ntes_weblog/portalUserAnalysis/";
	public static final String TEMP_DIR = PORTAL_USER_ANALYSIS_BASE_DIR + "temp/";

	public static final String MIDLAYER_APP = PORTAL_USER_ANALYSIS_BASE_DIR + "midLayer/app/";

	public static final String STATISTICS_DIR = PORTAL_USER_ANALYSIS_BASE_DIR + "statistics/";
	public static final String STATISTICS_RESULT_DIR = STATISTICS_DIR + "result/";

	public static final String CHANNEL_ATTRS = MIDLAYER_APP + "channelAttrs/";

	public static final String USER_DOCIDS = MIDLAYER_APP + "userDocids/";

	public static final String USER_EVENTS = MIDLAYER_APP + "userEvents/";
	public static final String USER_EVENTS_FORMAT = STATISTICS_DIR + "userEventsFormat/";
	public static final String USER_EVENTS_ATTRS_FORMAT = STATISTICS_DIR + "userEventsAttrsFormat/";

	public static final String USER_DAILY_VECTOR = STATISTICS_DIR + "userDailyVector/";

	// userProfile
	public static final String USER_PROFILE_BASE_DIR = STATISTICS_DIR + "userProfile/";
	public static final String USER_PROFILE_TEMP_DIR = USER_PROFILE_BASE_DIR + "temp/";

	// 原始日志层
	public static final String COMMON_DATA_DIR = DIR_PREFIX + "commonData/";
	// weblogFilter日志
	public static final String WEBLOG_FilterLOG = COMMON_DATA_DIR + "weblog/";
	// 跟帖原始数据
	// public static final String GEN_TIE_INFO = COMMON_DATA_DIR + "genTieLog/";
	public static final String GEN_TIE_INFO = COMMON_DATA_DIR + "genTieLogNew/";

	// userInfo
	public static final String TAGGED_USER_INFO = "/ntes_weblog/userProfile/commonData/userInfo_love/format/";

}
