package com.netease.portalUserAnalysis.data;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class NewAppConstant_tmp {
	private static Logger logger = Logger.getLogger(NewAppConstant_tmp.class);

	public static final int NETMONITOR_LENGTH = 13;

	public static final String DECRYPT_KEY = "neteasemobiledat";

	public static final String HADOOPJOBQUEEN = "mobilestat";

	public static final String IOS7MAC = "020000000000"; //ios7的Mac固定值

	/**
	 * 数据库 列名 之前加前缀，用于判断列的类型：目前主要是string 和 int 类型
	 * @author songzhang
	 *
	 */
	public interface DbInfos {
		int COLUMNPREFIXLENGTH = 2;
		String INTPREFIX = "i_";
		String STRINGPREFIX = "s_";
	}

	//hive查询结果本地目录
	public static final String HIVERESULTPATH = "/home/hadoop_mobilestat/workspace/hiveresulttemp/";
	//device--channel文件
	public static final String DEVICECHANNELLOCALPATH = "/home/hadoop_mobilestat/workspace/DEVICECHANNEL";
	
	public static final String EVENTINFOLOCALPATH = "/home/hadoop_mobilestat/workspace/EVENTINFO";

	public enum HiveSqlType {
		PUSHCOUNT, SECONDACTIVEUSER, ALLACTIVEUSER, NEWMACUVUAHOUR, LLWEEKACTIVE, USERLEFTDAY, USERLEFTWEEK, USERLEFTMONTH, USERLEFTNEWDAY, USERLEFTNEWWEEK, USERLEFTNEWMONTH, DURATIONTIME, VERSIONCHANGE, UVWEEK, UVNEWWEEK, UVMONTH,UVNEWMONTH,UV, MONTHPV, MONTHPVCHANNEL, HOURPVCOUNT, OSSYSTEM, FAULT, DEVICENEW, ACTIVEUSERTOTAL, ACTIVEUSERTODAY, ACTIVEUSEROTHER, EVENTHOUR, EVENTDAILY, EVENTBATCH,EVENTALLUV,EVENTTAGUV, FIRSTUSERTIME, PATHDAILY, PVTIMES, AVGUSETIME, PV, VERSION, DEVICEMODEL, PROVINCE, RESULOTION, OPERATOR, NETTYPE, EVENTDETAIL, COLUMNPVREALTIMEHOUR,COLUMNPVREALTIMEDAY
	}

	public static String getHiveResultPath(String dayString, HiveSqlType fileType) {
		//确保hiveday是 yyyyMMdd格式
		String hiveday = dayString.replaceAll("\\D", "");
		hiveday = hiveday.substring(0, 8);
		return HIVERESULTPATH + hiveday + "/" + fileType + "/";
	}

	public static interface HadoopPaths {
		String HIVE_MOBILELOG_PATH = "/user/hive/warehouse/mobilelog.db/"; //hive mobilelog的hdfs路径

		String RAWLOGDAYPATH = "/user/mobilestat/rawlog/"; //原始日志路径  year/month/day/
		String RAWLOGHOURPATH = "/user/mobilestat/rawlog/tmp/"; //原始日志小时路劲 {year}{month}{day}{hour}/
		String FORMATLOGDAYPATH = "/user/mobilestat/formatlog/"; //format后日志路径  formatkey/yearmonth/yearmonthday/
		String FORMATLOGHOURPATH = "/user/mobilestat/formatlog/tmp/"; //format后小时路径 formatkey/{year}{month}{day}{hour}/

		//mac 相关
		Path MACNEWPATH = new Path(FORMATLOGDAYPATH + "macdevice/newmac");
		Path MACCURRENTPATH = new Path(FORMATLOGDAYPATH + "macdevice/currentdata");

		//保存 device-channel对应关系
		String UUIDTOCHANNLEPATH = "/user/mobilestat/resource/appidchannel.dat";
		
		String EVENTINFO = "/user/mobilestat/resource/eventinfo.dat";

		//IP库路径
		String QQWAYFILE = "/user/mobilestat/resource/qqwry_utf8.dat";
	}

	/**
	 * 统计协议中各个字段，及其意义
	 * @author songzhang
	 *
	 */
	public static interface OriginalLogKeys {
		String JSON_IP_TAG = "ip:";
		int JSON_IP_TAG_LENGTH = JSON_IP_TAG.length();
		String JSON_MESSAGE_TAG = "message:";
		int JSON_MESSAGE_TAG_LENGTH = JSON_MESSAGE_TAG.length();

		String JSON_SEPARATOR_TAG = " ";
		int JSON_SEPARATOR_LENGTH = JSON_SEPARATOR_TAG.length();
		String JSON_EMPTY_TAG = "";

		String JSON_MOBILE_SESSION = "s"; //sessionID, 用户打开一次客户端的唯一标示，用来统计每天客户端打开次数

		String JSON_DEVICE = "i"; //设备信息 deviceinfo
		String JSON_DEVICE_APPID = "id"; //客户端类型 appid
		String JSON_DEVICE_DEVICEUUID = "u"; //android客户端的唯一标示	
		String JSON_DEVICE_SYSTEM_VERSION = "o"; //客户端操作系统版本
		String JSON_DEVICE_APP_VERSION = "v"; //应用程序的版本
		String JSON_DEVICE_CHANNEL = "mid"; //推广渠道
		String JSON_DEVICE_DEVICE_MODEL = "m"; //设备型号
		String JSON_DEVICE_RESOLUTION = "r"; //设备分辨率
		String JSON_DEVICE_NETTYPE = "nt"; //联网类型
		String JSON_DEVICE_OPERATOR = "op"; //运营商
		String JSON_DEVICE_MACUX = "ux"; //设备mac地址, ios客户端唯一标识
		String JSON_DEVICE_VERDORIDUV = "uv"; //ios客户端唯一标示
		String JSON_DEVICE_ADIDUA = "ua"; //用于对接广告推广商
		//增加6个属性 20140724
		String JSON_DEVICE_LANG ="l";   //language 语言，中文
		String JSON_DEVICE_TIMEZONE ="tz ";  //time zone 时区
		String JSON_DEVICE_COUNTRY ="cn ";  // country国家-中国，取设备语言对应国家
		String JSON_DEVICE_SDK_V ="sv";   //sdk version SDK版本
		String JSON_DEVICE_BUILD ="bd";  //Build号
		String JSON_DEVICE_VALID ="act"; //用于验证首次安装激活的加密字段
		
		
		

		String JSON_EVENT = "e"; //客户端事件标示
		String JSON_EVENT_DURA = "du";//持续时长
		String JSON_EVENT_NAME = "n"; //自定义事件名称
		String JSON_EVENT_TC = "t";//事件发生次数
		String JSON_EVENT_TAG = "g"; //自定义事件标签
		
		String JSON_EVENT_TIME = "ts"; //事件发生时间
		String JSON_EVENT_TIME_OLD = "t"; //事件发生时间
		String JSON_EVENT_START_TAG = "^"; //自定义事件开始标识
		String JSON_EVENT_START_TAG2 = "session_start";
		String JSON_EVENT_END_TAG = "$"; //自定义事件结束标示
		String JSON_EVENT_END_TAG2 = "session_end";

		String JSON_FAULT = "f"; //错误信息
		String JSON_FAULT_TIME = "t"; //错误发生时间
		String JSON_FAULT_CONTENT = "c"; //错误内容

		String JSON_PRIVATE = "ui"; //加密后的敏感信息 
		String JSON_PRIVATE_PASSPORT = "i"; //通行证账号
		String JSON_PRIVATE_SW = "sw";
		String JSON_PRIVATE_QQ = "qq";
		String JSON_PRIVATE_ID = "bid";//第三方平台绑定id,绑定新浪微博后传输
		String JSON_PRIVATE_TOKEN = "btk";// 第三方平台登录token,绑定新浪微博后传输
		String JSON_PRIVATE_PUSHTOKEN = "pt"; //用于push的token，或push开关
		String JSON_PRIVATE_CITY = "ct"; //本地新闻中选择的城市
		//增加6个属性 20140724
		String JSON_PRIVATE_LLNG ="llng";//:235.21, //地理位置经度  
		String JSON_PRIVATE_LLAT ="llat";//:123.32, //地理位置维度  
		String JSON_PRIVATE_LASL ="lasl";//:23.32, //地理位置海拔(选传
		String JSON_PRIVATE_LPRO ="lpro";//:河北,  //省(选传)      
		String JSON_PRIVATE_LCT ="lct";// :衡水,   /市(选传)     
		String JSON_PRIVATE_LDT ="ldt";// :景县XXX街道,  //详细地址(


		//String JSON_PRIVATE_OPENDOCID = "op"; //打开Push对应的docid
		//String JSON_PRIVATE_ACCESSTOKEN = "sw"; //绑定的微博token

	}

	public static String concat_ws(String... sa)
    {
		StringBuilder sb_concat_ws = new StringBuilder();
        sb_concat_ws.setLength(0);
        for (int i = 1; i < sa.length; i++)
        {
            sb_concat_ws.append(sa[i].replaceAll(sa[0], "") + sa[0]);
        }

        sb_concat_ws.delete(sb_concat_ws.length() - 1, sb_concat_ws.length());

        return sb_concat_ws.toString();
    }
	/**
	 * 接口中的所有字段，event合并时需要保存session信息，即不进行合并
	 * @author songzhang
	 *
	 */
	public interface EventDetailNames {
		String KEYTAG = "EVENTDETAIL";
		String HUATIANEVNET = "REG"; //需要与eventset中的对应

		Set<Integer> EVENTSET = new HashSet<Integer>() {
			private static final long serialVersionUID = 1L;

			{
				add(632);
			}
		};
	}

	/**
	 * 按小时获取 路径
	 * @param keyEnum	
	 * @param dateHour  yyyyMMddHH
	 * @return  rawlog-->rawlog/tmp/hourStr   other-->log/tmp/hourStr/key
	 */
	public static String getHourlyPath(FormatOutPutEnum keyEnum, String dateHour) {
		if (dateHour.indexOf("-") >= 0) {
			dateHour = dateHour.replaceAll("-", "");
		}
		switch (keyEnum) {
		case RAWLOG:
			return HadoopPaths.RAWLOGHOURPATH + dateHour + "/";
		default:
			return HadoopPaths.FORMATLOGHOURPATH + dateHour + "/" + keyEnum + "/";
		}
	}

	/**
	 * 按天获取路径
	 * @param keyEnum
	 * @param dateHour	yyyyMMddHH 或 yyyyMMdd
	 * @return
	 */
	public static String getDailyPath(FormatOutPutEnum keyEnum, String dateHour) {
		if (dateHour.indexOf("-") >= 0) {
			dateHour = dateHour.replaceAll("-", "");
		}
		String yearmonth = dateHour.substring(0, 6);
		String date = dateHour.substring(0, 8);
		switch (keyEnum) {
		case RAWLOG:
			return HadoopPaths.RAWLOGDAYPATH + "month=" + yearmonth + "/day=" + date + "/";
		case EVENT_NOAPPID:
			return HadoopPaths.FORMATLOGDAYPATH + keyEnum + "/";
		default:
			return HadoopPaths.FORMATLOGDAYPATH + keyEnum + "/month=" + yearmonth + "/day=" + date + "/";
		}
	}

	/**
	 * 获取hive中指定表的HDFS路径
	 * @param keyEnum
	 * @param dayString	yyyyMMddHH 或 yyyyMMdd
	 * @return
	 */
	public static String getHivePath(FormatOutPutEnum keyEnum, String dayString) {
		return getDailyPath(keyEnum, dayString);
	}

	/**
	 * 获取Hive中 没有partition的表的路径
	 * @param keyEnum
	 * @return
	 */
	public static String getHivePath(FormatOutPutEnum keyEnum) {
		switch (keyEnum) {
		case RAWLOG:
			return HadoopPaths.RAWLOGDAYPATH + keyEnum;
		default:
			return HadoopPaths.FORMATLOGDAYPATH + keyEnum;
		}
	}

	public enum FormatOutPutEnum {
		RAWLOG("rawlog"), SESSIONBEGIN("sessionbegin"), SESSIONEND("sessionend"), FAULT("fault"), PATH("path"), EVENT(
				"event"), EVENT_NOAPPID("event_noappid"), MACDEVICE("macdevice"), MACUVUA("macuvua"), SESSION("session"), SESSIONTMP(
				"sessiontmp"), SESSION2APPID("session2appid"), DEVICEHOUR("devicehour"), PRIVATEINFOTMP(
				"privateinfotmp"),EVENTPV("eventpv"),DOCATT("docatt");

		private String outPutStr;
		private Text outputText;

		private FormatOutPutEnum(String outPutStr) {
			this.outPutStr = outPutStr;
			this.outputText = new Text(outPutStr);
		}

		public String getOutPutStr() {
			return outPutStr;
		}

		public void setOutPutStr(String outPutStr) {
			this.outPutStr = outPutStr;
		}

		public Text getOutputText() {
			return outputText;
		}

		public void setOutputText(Text outputText) {
			this.outputText = outputText;
		}

		@Override
		public String toString() {
			return getOutPutStr();
		}
	}

	public interface FormatLengths {
		int MACDEVICELENGTH = 12; //mac地址为12位;
	}

	public interface Seperators {
		String START = "#";
		String MIDDLE = "#,#";
		String END = "#";
		
		int STARTLENGTH = START.length();
		int MIDDLELENGTH = MIDDLE.length();
		int ENDLENGTH = END.length();
		
		String HIVE_SEP = "\001";
	}

	public interface AllAppIds {
		String NEWIPHONESTR = "2S5Wcx"; //iphone新闻客户端
		String NEWIPADSTR = "IC8kYG"; //ipad新闻客户端
		String NEWANDROIDPHONESTR = "2x1kfBk63z"; //android手机客户端
		String NEWANDROIDPADSTR = "JBfjmI"; //android平板客户端
		String NEWANDROIDICECREAMSTR = "8Bn4W5"; //android冰淇淋版
		String NEWANDROIDEXPSTR = "tI2rfh"; //android体验版
		String NEWSYMBIANV3STR = "Gj9YiT"; //symbianV3
		String NEWSYMBIANV5STR = "S4zbL7"; //symbianV5
		String NEWWP7STR = "h4H6BN"; //wp7
		String NEWMTKSTR = "J0ylV8"; //MTK
		String NEWWP8STR = "2lR24f"; //wp8
		String NEWWINDOW8STR = "23RfG1"; //window8
		String NEWBLACKBERRYSTR = "0V0Gvx"; //黑莓

		String OTHERS = "otherappids";

		int INEWIPHONE = 27;
		int INEWIPAD = 57;
		int INEWANDROIDPHONE = 24;
		int INEWANDROIDPAD = 82;
		int INEWANDROIDICECREAM = 66;
		int INEWANDROIDEXP = 78;
		int iNEWSYMBIANV3 = 25; //symbianV3
		int iNEWSYMBIANV5 = 29; //symbianv5
		int iNEWWP7 = 58; //wp7        
		int iNEWMTK = 65; //MTK        
		int iNEWWP8 = 74; //wp8        
		int iNEWWINDOW8 = 80; //window8
		int iNEWBLACKBERRY = 83; //黑莓

		String NEWPARENTCHANNEL = "2345_news"; //此渠道下面子渠道 2345_news_*,需要汇总计算
	}

	/**
	 * 设备数据库中的表名
	 * @author songzhang
	 *
	 */
	public interface DeviceTableNames {
		String IOSNEWSTABLE = "3_mobile_device_news_ios";
		String ANDROIDNEWSTABLE = "3_mobile_device_news_android";
		String OTHERNEWSTABLE = "3_mobile_device_news_other";
		String OTHERTABLE = "3_mobile_device_other";
	}

	public static final String netease_name = "netease";

	public static boolean isNull(String input) {
		return null == input || input.toLowerCase().equals("null");
	}

	public static boolean isBlank(String input) {
		return null == input || "".equals(input.trim());
	}

	public static String decodeURL(String url) {
		try {
			return URLDecoder.decode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return url;
		}
	}

	public static String transformDateType(String dateInput, SimpleDateFormat oldFormat, SimpleDateFormat targetFormat) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(oldFormat.parse(dateInput));
			return targetFormat.format(calendar.getTime());
		} catch (ParseException e) {
			logger.info("tranform date format error:::" + dateInput + "::" + oldFormat.toPattern() + "**"
					+ targetFormat.toPattern());
			return null;
		}
	}

	public static Text transformTextToUTF8(Text text, String encoding) {
		String value = null;
		try {
			value = new String(text.getBytes(), 0, text.getLength(), encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new Text(value);
	}

	public static String GetDocAttFromIdInput(String date) {
		// TODO Auto-generated method stub
		return NewAppConstant_tmp.getDailyPath(FormatOutPutEnum.EVENTPV, date);
	}

	public static String GetDocAttFromIdOutput(String date) {
		// TODO Auto-generated method stub
		return NewAppConstant_tmp.getDailyPath(FormatOutPutEnum.DOCATT, date);
	}
}
