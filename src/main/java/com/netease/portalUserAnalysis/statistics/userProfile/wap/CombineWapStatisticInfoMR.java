package com.netease.portalUserAnalysis.statistics.userProfile.wap;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
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
import com.netease.portalUserAnalysis.data.StatisticResultWrapWritable;
import com.netease.portalUserAnalysis.utils.HadoopUtils;

public class CombineWapStatisticInfoMR extends MRJob {

	@Override
	public boolean init(String date) {
		inputList.add(DirConstant.USER_PROFILE_TEMP_DIR + "genTiePerUser/" + date);
		inputList.add(DirConstant.USER_PROFILE_TEMP_DIR +"actionPerUserWap/"+ date);
		outputList.add(DirConstant.USER_PROFILE_BASE_DIR + "combineWapStatisticInfo/" + date);
		return true;
	}

	@Override
	public int run(String[] args) throws Exception {
		int jobState = JobConstant.SUCCESSFUL;

		Job job = HadoopUtils.getJob(this.getClass(), this.getClass().getName() + "_step1");

		MultipleInputs.addInputPath(job, new Path(inputList.get(0)), SequenceFileInputFormat.class, gentieMapper.class);
		MultipleInputs.addInputPath(job, new Path(inputList.get(1)), SequenceFileInputFormat.class, pvShareBackEventMapper.class);
		
		
		// mapper
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(StatisticResultWrapWritable.class);

		// reducer
		job.setReducerClass(CombineReducer.class);
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

	public static class gentieMapper extends Mapper<Text, StatisticResultWrapWritable, Text, StatisticResultWrapWritable> {

		@Override
		public void map(Text key, StatisticResultWrapWritable value, Context context) throws IOException, InterruptedException {
			context.write(key, value);
		}
	}

	public static class pvShareBackEventMapper extends Mapper<Text, StatisticResultWrapWritable, Text, StatisticResultWrapWritable> {
		private Text outKey = new Text();
		@Override
		public void map(Text key, StatisticResultWrapWritable value, Context context) throws IOException, InterruptedException {
            if (value.getConf().get("email")!=null){
            	outKey.set(value.getConf().get("email"));
            	context.write(outKey, value);
            }else {
            	context.write(outKey, value);
			
		    }
	     }
	}


	public static class CombineReducer extends Reducer<Text, StatisticResultWrapWritable, Text, StatisticResultWrapWritable> {
		private StatisticResultWrapWritable outValue = new StatisticResultWrapWritable();
		private Text outKey = new Text();

		@Override
		protected void reduce(Text key, Iterable<StatisticResultWrapWritable> values, Context context) throws IOException, InterruptedException {
			outValue.getStatistic().clear();
			outValue.getConf().clear();
			
			for (StatisticResultWrapWritable val : values) {
				outValue.getConf().putAll(val.getConf());
				outValue.getStatistic().putAll(val.getStatistic());	
			}
			String uuid = outValue.getConf().get("uuid");
			String email = outValue.getConf().get("email");
			if (uuid!=null&&email!=null){
				outKey.set("uuid:"+uuid+" "+"email:"+email);
			}else if(uuid!=null){
				outKey.set("uuid:"+uuid);
			}else if(email!=null){
				outKey.set("email:"+email);
			}
			context.write(outKey, outValue);
			
	      }
        }
	}

	
