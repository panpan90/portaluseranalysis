package com.netease.portalUserAnalysis.userProfile.dataPreprocessing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.netease.jurassic.hadoopjob.MRJob;
import com.netease.jurassic.hadoopjob.data.JobConstant;
import com.netease.portalUserAnalysis.common.dataParser.Header;
import com.netease.portalUserAnalysis.common.dataParser.Parser;
import com.netease.portalUserAnalysis.common.dataParser.headerImpl.TaggedUserInfoHeader;
import com.netease.portalUserAnalysis.common.dataParser.parserImpl.TextCommonParser;
import com.netease.portalUserAnalysis.data.LongStringWritable;
import com.netease.portalUserAnalysis.userProfile.dataPreprocessing.headerImpl.LoveUserInfoHeader;
import com.netease.portalUserAnalysis.userProfile.dataPreprocessing.headerImpl.MicroblogUserInfoHeader;
import com.netease.portalUserAnalysis.utils.HadoopUtils;
import com.netease.portalUserAnalysis.utils.StringUtils;

public class TaggedUserInfoMergeMR extends MRJob {

	@Override
	public boolean init(String date) {
		inputList.add("/ntes_weblog/userProfile/commonData/userInfo_love/format/");
		inputList.add("/ntes_weblog/userProfile/commonData/userInfo_microblog/format/");
		outputList.add("/ntes_weblog/userProfile/commonData/userInfoAll/");
		return true;
	}

	@Override
	public int run(String[] args) throws Exception {
		
		int jobState = JobConstant.SUCCESSFUL;
    	
    	Job job = HadoopUtils.getJob(this.getClass(), this.getClass().getName());
    	MultipleInputs.addInputPath(job, new Path(inputList.get(0)), TextInputFormat.class, LoveMapper.class);
    	MultipleInputs.addInputPath(job, new Path(inputList.get(1)), TextInputFormat.class, MicroblogMapper.class);
    	
        //mapper
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongStringWritable.class);
        
        //reducer
        job.setReducerClass(MergeReducer.class);
        job.setNumReduceTasks(1);
        FileOutputFormat.setOutputPath(job, new Path(outputList.get(0)));
        job.setOutputFormatClass(TextOutputFormat.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        
        if(!job.waitForCompletion(true)){
            jobState = JobConstant.FAILED;
        }
        
        return jobState;
	}
	
	public static class LoveMapper extends Mapper<LongWritable, Text, Text, LongStringWritable> {
		
		private Text outputKey = new Text();
		private LongStringWritable outputValue = new LongStringWritable();
		private Parser loveParser = new TextCommonParser(new LoveUserInfoHeader(), "\t", StringUtils.defNullStr);
		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			//email,idCard,gender,birthday,constellation,blood,height,weight,education,school,industry
			String line = value.toString();
			String email = loveParser.parse(line).get(LoveUserInfoHeader.email);
			outputKey.set(email);
			outputValue.setFirst(1);
			outputValue.setSecond(line);
			
			context.write(outputKey, outputValue);
		}
	}
	
	public static class MicroblogMapper extends Mapper<LongWritable, Text, Text, LongStringWritable> {
		
		private Text outputKey = new Text();
		private LongStringWritable outputValue = new LongStringWritable();
		private Parser mircoblogParser = new TextCommonParser(new MicroblogUserInfoHeader(), "\t", StringUtils.defNullStr);
		
		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			//email,idCard,gender,birthday,corporation,school
			String line = value.toString();
			String email = mircoblogParser.parse(line).get(MicroblogUserInfoHeader.email);
			outputKey.set(email);
			outputValue.setFirst(2);
			outputValue.setSecond(line);
			
			context.write(outputKey, outputValue);
		}
	}
	
	public static class MergeReducer extends Reducer<Text, LongStringWritable, Text, NullWritable> {
		
		private List<LongStringWritable> loveList = new ArrayList<>();
		private List<LongStringWritable> microblogList = new ArrayList<>();
		
		private Map<String, String> vaildOutputMap = new HashMap<>();
		
		private Text outputKey = new Text();
		private NullWritable outputVal = NullWritable.get();
		
		private Parser loveParser = new TextCommonParser(new LoveUserInfoHeader(), "\t", StringUtils.defNullStr);
		private Parser mircoblogParser = new TextCommonParser(new MicroblogUserInfoHeader(), "\t", StringUtils.defNullStr);
		
		private Header taggedUserInfoHeader = new TaggedUserInfoHeader();
		
		@Override
		protected void reduce(Text key, Iterable<LongStringWritable> values, Context context) throws IOException, InterruptedException {
			loveList.clear();
			microblogList.clear();
			vaildOutputMap.clear();
			
			for(LongStringWritable val : values){
				if(val.getFirst() == 1){
					loveList.add(new LongStringWritable(val));
				}else if(val.getFirst() == 2){
					microblogList.add(new LongStringWritable(val));
				}
			}
			
			if(loveList.size() == 1 && microblogList.size() != 1){//输出花田
				vaildOutputMap.putAll(loveParser.parse(loveList.get(0).getSecond()));
				context.getCounter("MergeReducer", "use_love_only").increment(1);
			}else if(loveList.size() != 1 && microblogList.size() == 1){//输出微博
				vaildOutputMap.putAll(mircoblogParser.parse(microblogList.get(0).getSecond()));
				context.getCounter("MergeReducer", "use_microblog_only").increment(1);
			}else if(loveList.size() == 1 && microblogList.size() == 1){
				Map<String, String> loveMap = loveParser.parse(loveList.get(0).getSecond());
				Map<String, String> mircoblogMap = mircoblogParser.parse(microblogList.get(0).getSecond());
				vaildOutputMap.putAll(mircoblogMap);
				vaildOutputMap.putAll(loveMap);
				if(!loveMap.get(LoveUserInfoHeader.gender).equals(mircoblogMap.get(MicroblogUserInfoHeader.gender))){
					vaildOutputMap.put(TaggedUserInfoHeader.gender, StringUtils.defNullStr);
					context.getCounter("MergeReducer", "gender_conflict").increment(1);
				}
				context.getCounter("MergeReducer", "both_love_microblog").increment(1);
			}else{
				if(loveList.size() > 1){
					context.getCounter("MergeReducer", "repeated_love").increment(1);
				}
				if(microblogList.size() > 1){
					context.getCounter("MergeReducer", "repeated_microblog").increment(1);
				}
				return;
			}
			
			StringBuilder sb = new StringBuilder();
			for(String header : taggedUserInfoHeader.getHeader()){
				sb.append(StringUtils.notNullStr(vaildOutputMap.get(header), StringUtils.defNullStr)).append("\t");
			}
			
			outputKey.set(sb.toString().trim());
			context.write(outputKey, outputVal);
		}
		
	}
}













