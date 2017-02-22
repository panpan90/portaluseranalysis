package com.netease.portalUserAnalysis.resultData;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Random;

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

public class MonthWebInfoRandomResultPercentMR extends MRJob {

	@Override
	public boolean init(String date) {
        	inputList.add(DirConstant.USER_PROFILE_BASE_DIR + "monthwebInfoResultPercent/" + date);
		    outputList.add(DirConstant.USER_PROFILE_BASE_DIR + "monthwebInfoRandomResultPercent/" + date);
		return true;
	}

	@Override
	public int run(String[] args) throws Exception {
		int jobState = JobConstant.SUCCESSFUL;

		Job job = HadoopUtils.getJob(this.getClass(), this.getClass().getName() + "_step1");
	
	    MultipleInputs.addInputPath(job, new Path(inputList.get(0)), SequenceFileInputFormat.class, webStatisticInfoPercentMapper.class);

		// mapper
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		// reducer

		job.setNumReduceTasks(0);
		FileOutputFormat.setOutputPath(job, new Path(outputList.get(0)));
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		if (!job.waitForCompletion(true)) {
			jobState = JobConstant.FAILED;
		}
		
		return jobState;
	}

	public static class webStatisticInfoPercentMapper extends Mapper<Text, Text, Text, Text> {
		 Random r = new Random(10);
		@Override
		public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
			   String sex = key.toString().split("\t")[1];
			   
               if(r.nextInt(100)<12 && sex.equals("male")){
            	   context.write(key, value);
               }else if (sex.equals("female")){
            	   context.write(key, value);
               }
				
			}
	
		}
}

	
