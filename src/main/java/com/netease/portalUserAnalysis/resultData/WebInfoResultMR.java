package com.netease.portalUserAnalysis.resultData;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import com.netease.jurassic.hadoopjob.MRJob;
import com.netease.jurassic.hadoopjob.data.JobConstant;
import com.netease.portalUserAnalysis.common.DirConstant;
import com.netease.portalUserAnalysis.common.dataParser.Parser;
import com.netease.portalUserAnalysis.common.dataParser.headerImpl.TaggedUserInfoHeader;
import com.netease.portalUserAnalysis.common.dataParser.parserImpl.TextCommonParser;
import com.netease.portalUserAnalysis.data.StatisticResultWrapWritable;
import com.netease.portalUserAnalysis.data.StringStringWritable;
import com.netease.portalUserAnalysis.userProfile.dataPreprocessing.headerImpl.LoveUserInfoHeader;
import com.netease.portalUserAnalysis.utils.HadoopUtils;
import com.netease.portalUserAnalysis.utils.StringUtils;

public class WebInfoResultMR extends MRJob {

	@Override
	public boolean init(String date) {
		inputList.add(DirConstant.USER_PROFILE_BASE_DIR + "combineWebStatisticInfo/" + date);
		inputList.add(DirConstant.TAGGED_USER_INFO);
		outputList.add(DirConstant.USER_PROFILE_BASE_DIR + "webInfoResult/" + date);
		return true;
	}

	@Override
	public int run(String[] args) throws Exception {
		int jobState = JobConstant.SUCCESSFUL;

		Job job = HadoopUtils.getJob(this.getClass(), this.getClass().getName() + "_step1");

		MultipleInputs.addInputPath(job, new Path(inputList.get(0)), SequenceFileInputFormat.class, webStatisticInfoMapper.class);
		MultipleInputs.addInputPath(job, new Path(inputList.get(1)), TextInputFormat.class, LoveMapper.class);
		
		
		// mapper
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(StringStringWritable.class);

		// reducer
		job.setReducerClass(webInfoResultReducer.class);
		job.setNumReduceTasks(16);
		FileOutputFormat.setOutputPath(job, new Path(outputList.get(0)));
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		if (!job.waitForCompletion(true)) {
			jobState = JobConstant.FAILED;
		}
		
		return jobState;
	}

	public static class webStatisticInfoMapper extends Mapper<Text, StatisticResultWrapWritable, Text, StringStringWritable> {

		private Text outKey = new Text();
		private StringStringWritable outValue = new StringStringWritable();
		
		@Override
		public void map(Text key, StatisticResultWrapWritable value, Context context) throws IOException, InterruptedException {
			
			String email = value.getConf().get("email");
			if(email!=null){
			StringBuffer sb = new StringBuffer();
			for (NeteaseChannel_CS ncs :NeteaseChannel_CS.vaildChannelForScore){
			int channelValue =	value.getStatistic().get(ncs.getName())==null?0:value.getStatistic().get(ncs.getName());
				sb.append(channelValue).append("\t");
			}
		
				outKey.set(email);
				outValue.setFirst("info");
				outValue.setSecond(sb.toString().trim());
				context.write(outKey, outValue);
				context.getCounter("webStatisticInfoMapper", "hiveEmail").increment(1);
			}
	
		}
	}

	public static class LoveMapper extends Mapper<LongWritable, Text, Text, StringStringWritable> {
		
		private Text outputKey = new Text();
		private StringStringWritable outputValue = new StringStringWritable();
		private Parser taggedUserParser = new TextCommonParser(new TaggedUserInfoHeader(), "\t", StringUtils.defNullStr);
		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			//email,idCard,gender,birthday,constellation,blood,height,weight,education,school,industry
			String line = value.toString();
			
			String email = taggedUserParser.parse(line).get(LoveUserInfoHeader.email);
			String gender = taggedUserParser.parse(line).get(LoveUserInfoHeader.gender);
			if(!gender.equals(StringUtils.defNullStr)){
				outputKey.set(email);
				outputValue.setFirst("gender");
				outputValue.setSecond(gender);
				context.write(outputKey, outputValue);
			}else {
				context.getCounter("LoveMapper", "null").increment(1);
			}
			
		}
	}


	public static class webInfoResultReducer extends Reducer<Text, StringStringWritable, Text, NullWritable> {
		private Text outKey = new Text();

		@Override
		protected void reduce(Text key, Iterable<StringStringWritable> values, Context context) throws IOException, InterruptedException {
            String info = null;
            String gender = null;
			
			for (StringStringWritable val : values) {
		       if (val.getFirst().equals("info")){
		    	   info =  val.getSecond();
		       }else if (val.getFirst().equals("gender")){
		    	   gender = val.getSecond();
		       }
			}
		   if (info!=null&&gender!=null){
			   outKey.set(key.toString()+"\t"+gender+"\t"+info);
			   context.write(outKey, NullWritable.get());
		   }
	      }
        }
	}

	
