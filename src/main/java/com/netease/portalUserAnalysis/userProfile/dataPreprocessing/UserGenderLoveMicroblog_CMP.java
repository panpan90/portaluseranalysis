package com.netease.portalUserAnalysis.userProfile.dataPreprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class UserGenderLoveMicroblog_CMP {
	public static void main(String[] args) {
		try {
			String loveUser = "C:/Users/Administrator/Downloads/userGender_love";
			String microblogUser = "C:/Users/Administrator/Downloads/userGender_microblog"; 
			
			BufferedReader loveBr = new BufferedReader(new FileReader(new File(loveUser)));
			BufferedReader microblogBr = new BufferedReader(new FileReader(new File(microblogUser)));
			
			String line = null;
			
			Map<String, Boolean> loveMaper = new HashMap<>();
			
			while((line = loveBr.readLine()) != null){
				String[] split = line.split("\\s+");
				boolean b = false;
				if("male".equals(split[1])){
					b = true;
				}
				loveMaper.put(split[0], b);
			}
			
			int c = 0;
			line = null;
			while((line = microblogBr.readLine()) != null){
				String[] split = line.split("\\s+");
				boolean b = false;
				if("male".equals(split[1])){
					b = true;
				}
				
				Boolean loveB = loveMaper.get(split[0]);
				if(loveB != null){
//					if(b == loveB){
//						System.out.println(split[0]);
						c++;
//					}
				}
			}
			
			System.out.println(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
