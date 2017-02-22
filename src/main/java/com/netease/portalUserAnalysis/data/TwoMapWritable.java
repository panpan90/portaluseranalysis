package com.netease.portalUserAnalysis.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.Writable;

import com.netease.portalUserAnalysis.utils.HadoopUtils;

public class TwoMapWritable implements Writable {
	
	private Map<String, Long> slMap = new HashMap<>();
	private Map<String, String> ssMap = new HashMap<>();
	
	
	public TwoMapWritable() {}
	
	public TwoMapWritable(TwoMapWritable other) {
		this.slMap.clear();
		this.slMap.putAll(other.getSlMap());
		this.ssMap.clear();
		this.ssMap.putAll(other.getSsMap());
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		slMap.clear();
		int siMapSize = in.readInt();
		while(siMapSize-- > 0){
			String key = in.readUTF();
			Long value = in.readLong();
			slMap.put(key, value);
		}

		ssMap.clear();
		int ssMapSize = in.readInt();
		while(ssMapSize-- > 0){
			String key = in.readUTF();
			String value = in.readUTF();
			ssMap.put(key, value);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(slMap.size());
		for(Entry<String, Long> entry : slMap.entrySet()){
			HadoopUtils.writeString(out, entry.getKey());
			out.writeLong(entry.getValue()); 
		}
		
		out.writeInt(ssMap.size());
		for(Entry<String, String> entry : ssMap.entrySet()){
			HadoopUtils.writeString(out, entry.getKey());
			HadoopUtils.writeString(out, entry.getValue());
		}
	}
	
	@Override
	public String toString(){
		StringBuilder sb= new StringBuilder();
		
		for(Entry<String, Long> entry : slMap.entrySet()){
			sb.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
		}
		
		for(Entry<String, String> entry : ssMap.entrySet()){
			sb.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
		}
		
		return sb.toString();
	}
	
	public void clear(){
		this.slMap.clear();
		this.ssMap.clear();
	}
	
	public void put(String k, long v){
		this.slMap.put(k, v);
	}
	
	public void put(String k, String v){
		this.ssMap.put(k, v);
	}
	
	public String getString(String key){
		return this.ssMap.get(key);
	}
	
	public String getString(String key, String defVal){
		String res = this.ssMap.get(key);
		if(res == null){
			res = defVal;
		}
		
		return res;
	}
	
	public Long getLong(String key){
		return this.slMap.get(key);
	}
	
	public Long getLong(String key, long defVal){
		Long res = this.slMap.get(key);
		if(res == null){
			res = defVal;
		}
		
		return res;
	}

	public Map<String, Long> getSlMap() {
		return this.slMap;
	}

	public Map<String, String> getSsMap() {
		return this.ssMap;
	}
}
