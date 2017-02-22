package com.netease.portalUserAnalysis.userProfile.artcleContentProcessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import com.netease.portalUserAnalysis.common.dataParser.headerImpl.ArticleContentAPIHeader;
import com.netease.portalUserAnalysis.utils.HttpClientFactory;
import com.netease.weblogCommon.utils.JsonUtils;

public class GetArticleContent {
	
	private static final String url = "http://cmssync.netease.com/article/getarticle.htm";
	private static final int BUFFER_SIZE = 1024;
	private HttpClient httpClient = HttpClientFactory.getNewInstance();
	
	private String docidsF = null;
	private String titleF = null;
	private String contentF = null;
	private String tagF = null;//记录已经完成docid
	
	public static void main(String[] args) {
		if(args.length != 1){
			System.out.println("args error");
			return; 
		}
		
		long b = System.currentTimeMillis(); 
		
		GetArticleContent obj = new GetArticleContent();
		
		String baseDir = args[0];
		
		obj.docidsF = baseDir + "/docids";
		obj.titleF = baseDir + "/title";
		obj.contentF = baseDir + "/content";
		obj.tagF = baseDir + "/tag";
		
		BufferedWriter titleWriter = null;
		BufferedWriter contentWriter = null;
		BufferedWriter tagWriter = null;
		BufferedReader docidReader = null;
		
		List<String> finishDocids = obj.getFinishDocids();
		
		String docid = null;
		
		//拉取文章内容
		try {
			titleWriter = new BufferedWriter(new FileWriter(new File(obj.titleF), true));
			contentWriter = new BufferedWriter(new FileWriter(new File(obj.contentF), true));
			tagWriter =  new BufferedWriter(new FileWriter(new File(obj.tagF), true));
			
			docidReader = new BufferedReader(new FileReader(new File(obj.docidsF)));
			
			while((docid = docidReader.readLine()) != null){
				try {
					if(finishDocids.contains(docid)){
						continue;
					}
					
					Map<String, String> response = obj.getResponse(docid.trim());
					
					String title = response.get(ArticleContentAPIHeader.title);
					String content = response.get(ArticleContentAPIHeader.body);
					
					titleWriter.write(docid + "@" + title.replace("\n", ""));titleWriter.newLine();titleWriter.flush();
					contentWriter.write(docid + "@" + content.replace("\n", ""));contentWriter.newLine();contentWriter.flush();
					tagWriter.write(docid);tagWriter.newLine();tagWriter.flush();
					
					Thread.sleep(200);
				} catch (Exception e1) {
					System.out.println("error docid: " + docid);
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				titleWriter.flush();
				titleWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				contentWriter.flush();
				contentWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				tagWriter.flush();
				tagWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				docidReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		long e = System.currentTimeMillis();
		System.out.println("GetArticleContent finish, cost " + (e - b)/1000 + "s");
	}
	
	
    private String InputStreamTOString(InputStream in) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] data = new byte[BUFFER_SIZE];  
        int count = -1;  
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)  
            outStream.write(data, 0, count);  
          
        data = null;  
        return new String(outStream.toByteArray(),"UTF-8");  
    }
    
    private Map<String, String> getResponse(String docid) throws Exception{
    	PostMethod post = HttpClientFactory.postMethod(url);
		post.addParameter("docid", docid);
		
		httpClient.executeMethod(post);

		InputStream responseBodyAsStream = post.getResponseBodyAsStream();

		String response = InputStreamTOString(responseBodyAsStream);

		return JsonUtils.json2Map(response);
    }
	
	private List<String> getFinishDocids(){
		List<String> res = new ArrayList<>();
		BufferedReader tagReader = null;
		try {
			File tagFile = new File(tagF);
			if(tagFile.exists()){
				tagReader = new BufferedReader(new FileReader(tagFile));
				String line = null;
				while((line = tagReader.readLine()) != null){
					res.add(line.trim());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(tagReader != null){
				try {
					tagReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return res;
	}
}






