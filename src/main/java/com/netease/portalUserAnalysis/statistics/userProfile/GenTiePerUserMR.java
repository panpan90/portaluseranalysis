package com.netease.portalUserAnalysis.statistics.userProfile;

import java.io.IOException;

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

public class GenTiePerUserMR extends MRJob {
	@Override
	public boolean init(String date) {
		inputList.add(DirConstant.GEN_TIE_INFO + date);
		outputList.add(DirConstant.USER_PROFILE_TEMP_DIR + "genTiePerUser/" + date);
		return true;
	}
	
    @Override
    public int run(String[] args) throws Exception {
    	int jobState = JobConstant.SUCCESSFUL;
        
    	Job job = HadoopUtils.getJob(this.getClass(), this.getClass().getName());

    	MultipleInputs.addInputPath(job, new Path(inputList.get(0)), TextInputFormat.class, GenTieInfoMapper.class);
        //mapper
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(StatisticResultWrapWritable.class);
   		// combiner
   		job.setCombinerClass(CountReducer.class);
        //reducer
        job.setReducerClass(CountReducer.class);
        job.setNumReduceTasks(8);
        FileOutputFormat.setOutputPath(job, new Path(outputList.get(0)));
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(StatisticResultWrapWritable.class);
        
        if(!job.waitForCompletion(true)){
            jobState = JobConstant.FAILED;
        }
        
        return jobState;
    }
    
    public static class GenTieInfoMapper extends Mapper<LongWritable, Text, Text, StatisticResultWrapWritable> {
    	
    	private Text outputKey = new Text();
    	private   StatisticResultWrapWritable srww = new StatisticResultWrapWritable();
    	
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        	try {
	//        	url,pdocid,docid,发帖用户id,跟帖时间，跟帖id
        		srww.getConf().clear();
    			srww.getStatistic().clear();
	        	String[] strs = value.toString().split(",");
	        	String url = strs[0];
	        	String uid = strs[3];
	
	      		
	        	outputKey.set(uid);
	    		srww.getConf().put("email", uid);
	    		srww.getStatistic().put("gentieUser_pv", 1);
	        	context.write(outputKey, srww);
			} catch (Exception e) {
				context.getCounter("GenTieInfoMapper", "mapException").increment(1);
			}
        }
    }
    
	public static class CountReducer extends Reducer<Text, StatisticResultWrapWritable, Text, StatisticResultWrapWritable> {
        private 	StatisticResultWrapWritable srww = new StatisticResultWrapWritable();
		@Override
		protected void reduce(Text key,
				Iterable<StatisticResultWrapWritable> values, Context context)
				throws IOException, InterruptedException {

      	
      		
     		srww.getConf().clear();
			srww.getStatistic().clear();
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
			context.write(key, srww);
		}
	}
	
}












