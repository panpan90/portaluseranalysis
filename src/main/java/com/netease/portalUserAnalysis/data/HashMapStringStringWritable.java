package com.netease.portalUserAnalysis.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.Writable;

public class HashMapStringStringWritable  implements Writable {

	
	private HashMap<String,String> hm = new HashMap<String,String>();

	public HashMapStringStringWritable(){
		
	}



	public HashMapStringStringWritable( HashMap<String, String> hm) {

		this.hm = hm;
	}



	public HashMap<String, String> getHm() {
		return hm;
	}



	public void setHm(HashMap<String, String> hm) {
		this.hm = hm;
	}



	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
	  out.writeInt(this.hm.size());
	  
	  for (String s :hm.keySet()){
		  out.writeUTF(s);
		  out.writeUTF(hm.get(s));
	   }
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		HashMap<String,String> hm2 = new HashMap<String,String>();
		int size =in.readInt();
		for (int i=0;i<size;i++){
			String key = in.readUTF();
			String value = in.readUTF();
			hm2.put(key, value);
		}
		this.hm = hm2;
	}
	  public int length() {
		    return this.hm.size();
		  }
		  
		  @Override
		  public String toString() {
		    return hm.toString();
		  }
		  
		  @Override
		  public int hashCode() {
		    return hm.hashCode();
		  }

}
