package com.netease.portalUserAnalysis.userProfile.artcleContentProcessing.filter.impl;

import com.netease.portalUserAnalysis.userProfile.artcleContentProcessing.filter.ContentCleanFilter;

public class HtmlTagFilter implements ContentCleanFilter{

	private String htmlTagRegex = "(<[^>]*>)";

	@Override
	public String doFilter(String s) {
		return s.replaceAll(htmlTagRegex, "");
	}
	
	public static void main(String[] args) {
		
		String s = "<p>南方农村报讯　2016年全国两会即将在北京召开。作为全面建成小康社会决胜阶段的第一次全国两会,网民最关心什么?有哪些期待?从2月15日开始,人民网第十五次推出两会热点问题调查。"
				+ "</p>u000au000a<p>截至3月1日24时投票结束,参与网民超388万人次。其中,社会保障居排行榜首位,居民收入、医疗改革、打虎拍蝇和教育公平分列第二至第五。</p>u000au000a<p>值得一提的是,在"
				+ "人民网每年一次的调查中,社会保障在最近五年内四次成为最热选项,显现出网民对这一问题的持续高关注度。</p>u000au000a<p>从投票情况看,在社会保障方面,“企业退休人员基本养老金迎11连涨”是近年来最令网民满"
				+ "意的社保领域改革进展；有27.64%的网民期待适时调整职工和城乡居民基本养老保险待遇；网民认为,孤寡老人、空巢老人是目前最欠缺保障的人群。</p>u000au000a<p>在居民收入方面,有超过六成网民不满意目前个人收入水平；如果考虑"
				+ "到物价上涨因素,有51.97%的网民认为自己2015年实际收入比上年有所减少；超过四成的网民认为工资薪金所得税免征额设定在5000元最合适；行业差距、普通员工和管理者间收入差距以及区域差距被选为当前收入差距最主要的表现。</p>u000au000a<p>"
				+ "在医疗改革方面,网民认为在就医过程中遇到的最突出问题是药品贵、检查项目多和医疗水平不高；网民认为引发医患关系紧张最主要的原因是医疗资源缺乏；药价高、医疗资源紧张和医疗服务费高,被网民选为医改最该解决的问题；有超过五成的网民对目前社区医院总体服"
				+ "务水平评价为一般。</p>u000au000a<p>在打虎拍蝇方面,对于2015年的反腐工作,“解决发生在群众身边的腐败问题”最受网民认可；34.67%的网民认为,最该治理“四风”中“办事推诿、扯皮”现象。网民期待2016年反腐能够“力度不减、节奏不变、尺度不"
				+ "松”。</p>u000au000a<p>在教育公平方面,网民认为教育不公在中学阶段最突出；有39.07%的网民最关心城乡教育不公问题；35.85%的网民认为地区间招生人数及录取分数差异导致的不公是目前高考存在的最严重的问题；超过五成网民认为,居住地区外来人员子女"
				+ "无法与本地子女达到上学平等。</p>u000au000a<p>在本次调查中,热度位于第六位至第十位的选项分别是:住房、环境保护、司法改革、金融风险和一带一路。</p>u000au000a<p>(人民网)</p>";
		
		HtmlTagFilter obj = new HtmlTagFilter();
		System.out.println(obj.doFilter(s));
	}

}
