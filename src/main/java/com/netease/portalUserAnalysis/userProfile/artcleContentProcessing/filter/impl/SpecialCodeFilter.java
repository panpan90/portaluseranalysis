package com.netease.portalUserAnalysis.userProfile.artcleContentProcessing.filter.impl;

import com.netease.portalUserAnalysis.userProfile.artcleContentProcessing.filter.ContentCleanFilter;

public class SpecialCodeFilter implements ContentCleanFilter{

	private String[] specialCode = new String[] {"(u[0-9a-fA-F]{4})","(&#*[A-Za-z0-9]{0,20};)"};
	
	@Override
	public String doFilter(String s) {
		String res = s;

		for(String sc : specialCode){
			res = res.replaceAll(sc, "");
		}
		
		return res;
	}
	
	public static void main(String[] args) {
		
		String s = "<p><strong></p>u000au000a<p>《意见》明确，要推动专业化消费金融组织发展，鼓励有条件的银行业金融机构围绕新消费领域设立特色专营机构"
				+ "、完善配套机制，推进消费金融公司设立常态化，鼓励消费金融公司针对细分市场提供特色服务。</p>u000au000a<p>“消费金融公司”这个名字可能已不陌"
				+ "生。2009年起，消费金融公司试点从4个城市逐步扩展到16个城市，去年6月，国务院常务会议提出将消费金融公司试点推广至全国，增强消费对经济的拉动力。资料显示"
				+ "，发达国家均为消费驱动型经济，居民最终消费占GDP比重在70%左右，而我国消费拉动水平仅占40%，可见我国消费的后劲和潜力仍有待挖掘。目前，银行、电商在内的多类"
				+ "机构已纷纷进入消费金融市场。</p>u000au000a<p>在消费金融公司发展得到推动的同时，消费金融业务也不断拓展。捷信母公司PPF集团中国区总裁卢米尔&#00183;马龙"
				+ "表示，很多企业进入到这个行业中，产品的创新会比以前更多，消费者也可以从中得到更多好处。此次《意见》进一步提出，要加快推进消费信贷产品创新，鼓励银行业金融机构创新消费"
				+ "信贷抵质押模式，开发不同首付比例、期限和还款方式的信贷产品。</p>u000au000a<p>除了产品创新外，《意见》还鼓励金融公司加大对新消费重点领域的金融支持，其中绿"
				+ "色消费领域成为一大亮点。“经银监会批准经营个人汽车贷款业务的金融机构办理新能源汽车和二手车贷款的首付款比例，可分别在15%和30%最低要求基础上，根据自愿、审慎和风险"
				+ "可控原则自主决定。”据了解，此前汽车金融公司对新能源汽车首付款要求在20%上下。</p>u000au000a<p>对此，网贷之家首席分析师马骏表示，这一条款其实是鼓励消费者在购"
				+ "车时使用更多的金融工具。现在消费者买房时有使用按揭服务的习惯，但是在买车时，对于金融服务的使用习惯培养还不是很完善。易观智库互联网金融行业分析师来妍进一步分析，目前我"
				+ "国新能源汽车技术、市场推广等方面都不太成熟，这一条款将在一定程度上加快新能源汽车的推广力度，同时推动汽车消费金融的发展。</p>u000au000a<p>业内普遍认为，新政策的出"
				+ "台将助推新消费金融迎来一轮发展热潮，但热潮背后隐藏的风险不能忽视。京东金融消费金融事业部总经理许凌表示，新消费金融在政策上迎来利好，同时整个市场也一窝蜂地在做，当热点成为风口"
				+ "的时候，可能就会伴随着风险。像互联网金融行业，容易一波热点所有人都蜂拥而至，但过一轮就会“死”得差不多了，所以底层风控很重要，消费金融风控为王。</p>u000au000a<p>众所周知"
				+ "，近年来风控严&#00184;格的银行业信贷不良率都在不断攀升，卢米尔&#00183;马龙表示，“目前消费金融公司服务的客户跟一般的银行客户不一样，往往没有信用记录或是信用记录比较少，而这些"
				+ "新加入消费金融行业的企业，有可能对风控风险的理解或准备不足，而风控对行业的健康发展至关重要”。</p>u000au000a<p>作者：崔启斌 刘双霞 程维妙</p>";
		
		SpecialCodeFilter obj = new SpecialCodeFilter();
		long b = System.currentTimeMillis();
		System.out.println(s);
		System.out.println(obj.doFilter(s));
		long e = System.currentTimeMillis();
		System.out.println("cost: " + (e - b) + "ms");
	}

}
