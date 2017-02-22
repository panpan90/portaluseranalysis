package com.netease.portalUserAnalysis.resultData;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
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
import com.netease.portalUserAnalysis.common.weblogfilter.WeblogFilterUtils;
import com.netease.portalUserAnalysis.data.StatisticResultWrapWritable;
import com.netease.portalUserAnalysis.resultData.MonthWebInfoResultMR.weeklyWebStatisticInfoMapper;
import com.netease.portalUserAnalysis.utils.HadoopUtils;
import com.netease.portalUserAnalysis.utils.StringUtils;
import com.netease.portalUserAnalysis.utils.UrlUtils;
import com.netease.weblogCommon.utils.DateUtils;
public class MonthColumnCountWebMR extends MRJob {

	@Override
	public boolean init(String date) {
	 	List<String> dateList;
        try {
            String firstDay = DateUtils.getTheDayBefore(date, 29);
            dateList = DateUtils.getDateList(firstDay, date);
        } catch (ParseException e) {
            return false;
        }
        for (String d : dateList) {
        	inputList.add(DirConstant.WEBLOG_FilterLOG +d);
        }
	   outputList.add(DirConstant.USER_PROFILE_TEMP_DIR +"columnCountWeb");
	   return true;
	}

	@Override
	public int run(String[] args) throws Exception {
		int jobState = JobConstant.SUCCESSFUL;

        	   
 
        	Job job = HadoopUtils.getJob(this.getClass(), this.getClass()
       				.getName() + "_step1");
               
       		// weblog
        	
  		  for (String input : inputList) {
	            Path path = new Path(input);
	            if (getHDFS().exists(path)) {
	            	MultipleInputs.addInputPath(job, path,
	           				SequenceFileInputFormat.class, LogMapper.class);
	            }
		  }
       		

       		// mapper
       		job.setMapOutputKeyClass(Text.class);
       		job.setMapOutputValueClass(IntWritable.class);

       		// combiner
      		job.setCombinerClass(ColumnCountWebReducer.class);

       		// reducer
       		job.setReducerClass(ColumnCountWebReducer.class);
       		job.setNumReduceTasks(16);
       		FileOutputFormat.setOutputPath(job, new Path(outputList.get(0)));
       		job.setOutputFormatClass(SequenceFileOutputFormat.class);

       		job.setOutputKeyClass(Text.class);
       		job.setOutputValueClass(IntWritable.class);

       		if (!job.waitForCompletion(true)) {
       			jobState = JobConstant.FAILED;
       		}

       		
       		
       	   return jobState;
       
	}

	  public static class LogMapper extends Mapper<NullWritable, Text, Text, IntWritable> {
	    	
	    	private Text outputkey = new Text();
	    	private  IntWritable outputValue = new IntWritable(1);
	    	
	        @Override
	        public void map(NullWritable key, Text value, Context context) throws IOException, InterruptedException {
	        	try {
	    			HashMap<String,String> lineMap = WeblogFilterUtils.buildKVMap(value.toString());
	    			//分频道统计
	    			String url = lineMap.get("url");
	    			String docid = UrlUtils.getArticleDocid(url);
					if  (docid!=null){
						String column = docid.substring(8,16);
						outputkey.set(column);
						context.write(outputkey, outputValue);
					}
					
				} catch (Exception e) {
					context.getCounter("LogMapper", "parseError").increment(1);
				}
	        }
	    }


	public static class ColumnCountWebReducer
			extends
			Reducer<Text, IntWritable, Text, IntWritable> {
    	private  IntWritable outputValue = new IntWritable();
		@Override
		protected void reduce(Text key,
				Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
            int sum =0;
			
			for (IntWritable val : values) {
				
					sum+= val.get();
			}
			
			outputValue.set(sum);
			context.write(key, outputValue);
		}
	}

      
}
