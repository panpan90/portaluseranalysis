package com.netease.portalUserAnalysis.statistics.userProfile.wap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import com.netease.jurassic.hadoopjob.MRJob;
import com.netease.jurassic.hadoopjob.data.JobConstant;
import com.netease.portalUserAnalysis.common.DirConstant;
import com.netease.portalUserAnalysis.data.StatisticResultWrapWritable;
import com.netease.portalUserAnalysis.utils.HadoopUtils;
import com.netease.portalUserAnalysis.utils.StringUtils;
import com.netease.weblogCommon.data.enums.NeteaseChannel_CS;
import com.netease.weblogCommon.data.enums.ShareBackChannel_CS;
public class ActionPerUserWapMR extends MRJob {

	@Override
	public boolean init(String date) {
	   inputList.add(DirConstant.USER_PROFILE_TEMP_DIR +"needColumnFormZy4Wap/"+date);
	   outputList.add(DirConstant.USER_PROFILE_TEMP_DIR +"actionPerUserWap/"+ date);
	   return true;
	}

	@Override
	public int run(String[] args) throws Exception {
		int jobState = JobConstant.SUCCESSFUL;

        	   
 
        	Job job = HadoopUtils.getJob(this.getClass(), this.getClass()
       				.getName() + "_step1");
               
       		// weblog
       		MultipleInputs.addInputPath(job, new Path(inputList.get(0)),
       				TextInputFormat.class, LogMapper.class);

       		// mapper
       		job.setMapOutputKeyClass(Text.class);
       		job.setMapOutputValueClass(StatisticResultWrapWritable.class);

       		// reducer
       		job.setReducerClass(PvShareBackReducer.class);
       		job.setNumReduceTasks(16);
       		FileOutputFormat.setOutputPath(job, new Path(outputList.get(0)));
       		job.setOutputFormatClass(SequenceFileOutputFormat.class);

       		job.setOutputKeyClass(Text.class);
       		job.setOutputValueClass(StatisticResultWrapWritable.class);

       		if (!job.waitForCompletion(true)) {
       			jobState = JobConstant.FAILED;
       		}

       		
       		
       	   return jobState;
       
	}

	  public static class LogMapper extends Mapper<LongWritable, Text, Text, StatisticResultWrapWritable> {
	    	
	    	private Text outputkey = new Text();
	    	private  StatisticResultWrapWritable srww = new StatisticResultWrapWritable();
	    	
	        @Override
	        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	        	try {
	        		srww.getConf().clear();
	    			srww.getStatistic().clear();
	    			HashMap<String, String> lineMap = NeedColumnUtils.buildKVMapOfZyOnput(value.toString());
	    			//分频道统计
	    			String url =lineMap.get("url");
					NeteaseChannel_CS nce = NeteaseChannel_CS.getChannel(url);
					if  (nce!=null){
						srww.getStatistic().put(nce.getName(), 1);
					}
	   
							
					String uuid = lineMap.get("uid");
					srww.getStatistic().put("uuid_pv", 1);
					srww.getConf().put("uuid", uuid);
					
					String sid = lineMap.get("sid");
					String  logTime = lineMap.get("logTime");
					if (!sid.equals("(null)")&&!StringUtils.isBlank(sid)){
						srww.getStatistic().put("sid_count"+sid, 1);
						srww.getStatistic().put("sid_time"+sid+"#Time#"+logTime, 1);
					}
	
					
					String email = lineMap.get("email");
					if(!StringUtils.isBlank(email)&&!email.equals("(null)")){
						srww.getConf().put("email", email);
					}
					
					String suffix = null;                    
					if (null != (suffix = ShareBackChannel_CS.getShareChannel(url))) {
						srww.getStatistic().put("s_"+suffix, 1);
					} else if (null != (suffix = ShareBackChannel_CS
							.getBackChannel(url))) {
						srww.getStatistic().put("b_"+suffix, 1);
					}
					
					outputkey.set(uuid);
					context.write(outputkey, srww);
					
				} catch (Exception e) {
					context.getCounter("LogMapper", "parseError").increment(1);
				}
	        }
	    }


	public static class PvShareBackReducer
			extends
			Reducer<Text, StatisticResultWrapWritable, Text, StatisticResultWrapWritable> {
  		private  StatisticResultWrapWritable srww = new StatisticResultWrapWritable();
  		private  HashMap<String,ArrayList<Long>> sid_Time = new  HashMap<String,ArrayList<Long>>();
		@Override
		protected void reduce(Text key,
				Iterable<StatisticResultWrapWritable> values, Context context)
				throws IOException, InterruptedException {
			srww.getConf().clear();
			srww.getStatistic().clear();
			sid_Time.clear();

			for (StatisticResultWrapWritable val : values) {
				srww.getConf().putAll(val.getConf());
				for (String s :val.getStatistic().keySet()){
				    if (srww.getStatistic().get(s)!=null){
				    	srww.getStatistic().put(s,srww.getStatistic().get(s)+val.getStatistic().get(s));				
				    }else {
				    	srww.getStatistic().put(s,val.getStatistic().get(s));				
				    }
				}		
					
			}
			
			int sid_count = 0; 
			long sid_time =0;
			ArrayList<String> del = new ArrayList<>();
			for (Entry<String, Integer> s:srww.getStatistic().entrySet()){
				if (s.getKey().startsWith("sid_count")){
					sid_count++;
					del.add(s.getKey());
				
				}
			
				if(s.getKey().startsWith("sid_time")){
					String sid = s.getKey().split("#Time#")[0];
					try{
						long time = Long.parseLong(s.getKey().split("#Time#")[1]);
						if (sid_Time.get(sid)!=null){	
						 sid_Time.get(sid).add(time);
						}else {
							ArrayList<Long> al = new ArrayList<>();
							al.add(time);
							sid_Time.put(sid, al);
						}
						del.add(s.getKey());
					}catch(Exception e){
						context.getCounter("PvShareBackReducer","can not convert to time").increment(1);
					}
				}
			}
			for (String s :del){
				srww.getStatistic().remove(s);
			}
			srww.getStatistic().put("sid_count", sid_count);
			for (Entry<String, ArrayList<Long>> s:sid_Time.entrySet()){
				 Collections.sort(s.getValue());
				 long l = s.getValue().get(s.getValue().size()-1)-s.getValue().get(0);
				 sid_time +=l;
			}
			srww.getStatistic().put("sid_time", (int) sid_time);
		
			context.write(key, srww);
		}
	}

      
}
