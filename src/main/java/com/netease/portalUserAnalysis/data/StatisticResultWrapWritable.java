package com.netease.portalUserAnalysis.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.Writable;

import com.netease.portalUserAnalysis.utils.HadoopUtils;

public class StatisticResultWrapWritable implements Writable {
	
	private Map<String, Integer> statistic = new HashMap<>();
	
	private Map<String, String> conf = new HashMap<>();
	
	@Override
	public void readFields(DataInput in) throws IOException {
		int statisticSize = in.readInt();
		statistic = new HashMap<>();
		while(statisticSize-- > 0){
			String key = in.readUTF().toString();
			Integer value = in.readInt();
			statistic.put(key, value);
		}
		
		int confSize = in.readInt();
		conf = new HashMap<>();
		while(confSize-- > 0){
			String key = in.readUTF().toString();
			String value = in.readUTF().toString();
			conf.put(key, value);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(statistic.size());
		for(Entry<String, Integer> entry : statistic.entrySet()){
			HadoopUtils.writeString(out, entry.getKey());
			out.writeInt(entry.getValue()); 
		}
		out.writeInt(conf.size());
		for(Entry<String, String> entry : conf.entrySet()){
			HadoopUtils.writeString(out, entry.getKey());
			HadoopUtils.writeString(out, entry.getValue());
		}
	}
	
	@Override
	public String toString(){
		StringBuilder sb= new StringBuilder();
		
		for(Entry<String, String> entry : conf.entrySet()){
			sb.append(entry.getKey().toString()).append(":").append(entry.getValue()).append(";");
		}
		sb.append("||");
		for(Entry<String, Integer> entry : statistic.entrySet()){
			sb.append(entry.getKey().toString()).append(":").append(entry.getValue()).append(";");
		}
		return sb.toString();
	}
	
	
	public Map<String, String> getConf() {
		return conf;
	}

	public void setConf(Map<String, String> conf) {
		this.conf = conf;
	}
	
	public Map<String, Integer> getStatistic() {
		return statistic;
	}

	public void setStatistic(Map<String, Integer> statistic) {
		this.statistic = statistic;
	}

	public void add(Map<String, String> conf){
		for(Entry<String, String> entry : conf.entrySet()){
			this.conf.put(new String(entry.getKey()), new String(entry.getValue()));
		}
	}
}
