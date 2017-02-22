package com.netease.portalUserAnalysis.userProfile.dataPreprocessing;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.netease.portalUserAnalysis.common.dataParser.Parser;
import com.netease.portalUserAnalysis.common.dataParser.parserImpl.TabReplaceParser;
import com.netease.portalUserAnalysis.common.dataParser.parserImpl.TextCommonParser;
import com.netease.portalUserAnalysis.userProfile.dataPreprocessing.headerImpl.MicroblogUserInfoOriginalHeader;
import com.netease.portalUserAnalysis.utils.HadoopUtils;
import com.netease.weblogCommon.utils.DateUtils;

public class MicroblogUserInfoCleaningMR extends MRJob {

	@Override
	public boolean init(String date) {
		inputList.add("/ntes_weblog/userProfile/commonData/userInfo_microblog/20160322/");
		outputList.add("/ntes_weblog/userProfile/commonData/userInfo_microblog/format/");
		return true;
	}

	@Override
	public int run(String[] args) throws Exception {
		
		int jobState = JobConstant.SUCCESSFUL;
    	
    	Job job = HadoopUtils.getJob(this.getClass(), this.getClass().getName());
    	MultipleInputs.addInputPath(job, new Path(inputList.get(0)), TextInputFormat.class, MicroblogMapper.class);
    	
        //mapper
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        
        //reducer
        job.setReducerClass(CleaningReducer.class);
        job.setNumReduceTasks(16);
        FileOutputFormat.setOutputPath(job, new Path(outputList.get(0)));
        job.setOutputFormatClass(TextOutputFormat.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        
        if(!job.waitForCompletion(true)){
            jobState = JobConstant.FAILED;
        }
        
        return jobState;
	}
	
	private static String strProcess(String s){
		return StringUtils.isBlank(s) || "\\N".equals(s) ? "null" : s;
	}

	public static class MicroblogMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
		private Text outputKey = new Text();
		private Parser parser = new TabReplaceParser(new TextCommonParser(new MicroblogUserInfoOriginalHeader(), "#X_X#Y_Y#", "null"));
		private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			Map<String, String> map = parser.parse(value.toString());
			String email = strProcess(map.get(MicroblogUserInfoOriginalHeader.Email));
			if(!email.contains("@")){
				return;
			}
			
			String idCard = strProcess(map.get(MicroblogUserInfoOriginalHeader.IdCard));
			
			String gender = map.get(MicroblogUserInfoOriginalHeader.Gender);
			if("1".equals(gender)){
				gender = "male";
			}else if("2".equals(gender)){
				gender = "female";
			}else{
				gender = "null";
			}
			
			String birthday = strProcess(map.get(MicroblogUserInfoOriginalHeader.Birthday));
			try {
				long birthdayLong = Long.parseLong(birthday);
				if(birthdayLong < -639129600000l){//1949-10-01
					birthday = "null";
				}else{
					birthday = DateUtils.getTime(birthdayLong, dateFormat);
				}
			} catch (Exception e) {
				birthday = "null";
			} 
			
			String corporation = strProcess(map.get(MicroblogUserInfoOriginalHeader.Corporation));
			String school = strProcess(map.get(MicroblogUserInfoOriginalHeader.School));
			
			if("null".equals(idCard) && "null".equals(gender) && "null".equals(birthday) 
					&& "null".equals(corporation) && "null".equals(school)){
				return;
			}
			
			outputKey.set(email + "\t" + idCard + "\t" + gender + "\t" + birthday + "\t" + corporation + "\t" + school);
			context.write(outputKey, NullWritable.get());
		}
	}
	
	public static class CleaningReducer extends Reducer<Text, NullWritable, Text, NullWritable> {
		@Override
		protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
			for(NullWritable val : values){
				context.write(key, val);
			}
		}
	}
}













