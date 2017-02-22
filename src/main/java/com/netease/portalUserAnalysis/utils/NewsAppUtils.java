package com.netease.portalUserAnalysis.utils;

public class NewsAppUtils {
	private static String[] newsAppIds = {"2x1kfBk63z","Gj9YiT","2S5Wcx","S4zbL7","IC8kYG","h4H6BN","J0ylV8","8Bn4W5","2lR24f","tI2rfh","23RfG1","JBfjmI","0V0Gvx","vNjbl9"};

	public static boolean isNeteaseNewsApp(String appId){
		for(String s : newsAppIds){
			if(s.equals(appId)){
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isNotNeteaseNewsApp(String appId){
		return !isNeteaseNewsApp(appId);
	}
	
	public static String[] deviceAttrsInterested = {"installhour","ip","mac","ua","passport","valid_flag","device_model","imei","os","aid","app_version","province","city"};
	public static String[] eventsInterested = {"COLUMNX","COLUMND","_pvX","DU","LISTE","EV_P","_ivX","LISTR","_cvX","TABX","RT","RC","EV_nP","PATCH","LOCALY","_svX","_vvX","RELATESETX","_vvY","_vvZ","LISTY","RN","PUSH","PUSH_A","AUTOY","TIE_DING","JSPATCH","ENTRYX","UCX","PICTURES","PAGE_RELATE_CLK","LIVE","HOUSEY","SHARE_NEWS","SHARE","RELATESETY","TIE_POST","ABTEST","TIE_POSTX","LIST","PV","CC","PROFILEX","RFC","search","ads","VIDEO_TYPE_KPI","ENTRY","LOGINX","RECOPAGE_CLK","_rvX","RECO_CLKRE","TAB","EV_NP","wallet","SUBX","showbigpic","OPEN","_wv","PAGES","OPEN_PC","LISTZ","COOPX","OpenChannel","TIE","USER_LOCAL","openPush","LOCALX","WATCH","LOCAL_ENTRY","_qv"};
}
