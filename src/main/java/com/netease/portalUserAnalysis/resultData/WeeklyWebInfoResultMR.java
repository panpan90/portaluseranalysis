package com.netease.portalUserAnalysis.resultData;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import com.netease.jurassic.hadoopjob.MRJob;
import com.netease.jurassic.hadoopjob.data.JobConstant;
import com.netease.portalUserAnalysis.common.DirConstant;
import com.netease.portalUserAnalysis.utils.HadoopUtils;
import com.netease.weblogCommon.utils.DateUtils;

public class WeeklyWebInfoResultMR extends MRJob {

	@Override
	public boolean init(String date) {
    	List<String> dateList;
        try {
            String firstDay = DateUtils.getTheDayBefore(date, 6);
            dateList = DateUtils.getDateList(firstDay, date);
        } catch (ParseException e) {
            return false;
        }
        for (String d : dateList) {
        	inputList.add(DirConstant.USER_PROFILE_BASE_DIR + "webInfoResult/"+d);
        }
		outputList.add(DirConstant.USER_PROFILE_BASE_DIR + "weeklyWebInfoResult/" + date);
		return true;
	}

	@Override
	public int run(String[] args) throws Exception {
		int jobState = JobConstant.SUCCESSFUL;

		Job job = HadoopUtils.getJob(this.getClass(), this.getClass().getName() + "_step1");
		
		  for (String input : inputList) {
	            Path path = new Path(input);
	            if (getHDFS().exists(path)) {
	                MultipleInputs.addInputPath(job, path, SequenceFileInputFormat.class, weeklyWebStatisticInfoMapper.class);
	            }
		  }

	
		// mapper
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		// reducer
		job.setReducerClass(weeklyWebInfoResultReducer.class);
		job.setNumReduceTasks(16);
		FileOutputFormat.setOutputPath(job, new Path(outputList.get(0)));
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		if (!job.waitForCompletion(true)) {
			jobState = JobConstant.FAILED;
		}
		
		return jobState;
	}

	public static class weeklyWebStatisticInfoMapper extends Mapper<Text, NullWritable, Text, Text> {

		private Text outKey = new Text();
		private Text outValue = new Text();
		
		@Override
		public void map(Text key, NullWritable value, Context context) throws IOException, InterruptedException {
			   String s[] = key.toString().split("\t");
				outKey.set(s[0]+"\t"+s[1]);
				StringBuilder sb = new StringBuilder();
				for (int i = 2;i<s.length;i++){
					sb.append(s[i]).append("\t");
				}
				outValue.set(sb.toString().trim());
				context.write(outKey, outValue);
				context.getCounter("webStatisticInfoMapper", "hiveEmail").increment(1);
			}
	
		}


	public static class weeklyWebInfoResultReducer extends Reducer<Text, Text, Text, Text> {
		private Text outValue = new Text();

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            
			Integer[] statistic = null;
			for (Text val : values) {
		       if (statistic==null){
		    	   String[] s =val.toString().split("\t");
		    	   statistic = new Integer[s.length];
		    	   for (int i =0;i<s.length;i++){
		    		   statistic[i] = Integer.parseInt(s[i]); 
		    	   }
		       }else {
		    	   String[] s =val.toString().split("\t");
		    	   for (int i =0;i<s.length;i++){
		    	   statistic[i] +=  Integer.parseInt(s[i]);
		    	   }
		       }
			}
	
			int sum =0;
			   for (int i:statistic){
				   sum+= i;
			   }
			   if(sum!=0){
				   StringBuffer sb = new StringBuffer();
				   for (int j :statistic){
					   sb.append(j).append("\t");
				   }
				   outValue.set(sb.toString().trim());
				   context.write(key, outValue);
			   }
		
	      }
        }
	}

	
