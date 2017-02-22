
package com.netease.portalUserAnalysis.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.WritableComparable;

public class LongListWritable extends ArrayList<Long> implements WritableComparable<LongListWritable> {

	private static final long serialVersionUID = 1L;
	
	private static final String toStringSplitRegex = ",";
	
	public LongListWritable() {}

	public LongListWritable(LongListWritable one) {
		this.clear();
		for(long s : one){
			this.add(s);
		}
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.clear();
		int size = in.readInt();
		while(size-- > 0){
			this.add(in.readLong());
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(this.size());
		for(long s : this){
			out.writeLong(s);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null && !(o instanceof LongListWritable)) {
			return false;
		}
		LongListWritable that = (LongListWritable) o;
		
		if(this.size() != that.size()){
			return false;
		}
		
		for(int i = 0; i < this.size();++i){
			Long thisCur = this.get(i);
			if((thisCur != null && !this.get(i).equals(that.get(i))) 
					|| thisCur == null && that != null){
				return false;
			}
		}
		
		return true;
	}

	@Override
	public int compareTo(LongListWritable o) {
		LongListWritable that = (LongListWritable) o;
		
		int res = 0;
		
		for(int i = 0; i < this.size() && i < that.size(); ++i) {
			Long thisCur = this.get(i);
			Long thatCur = that.get(i);
			
			if(thisCur != null && thatCur != null){
				res = thisCur.compareTo(thatCur);
			}else if(thisCur == null && thatCur != null){
				res = -1;
			}else if(thisCur != null && thatCur == null){
				res = 1;
			}else{
				res = 0;
			}
			
			if(res != 0){
				return res;
			}
		}
		
		if(this.size() < that.size()){
			res = -1;
		}else if(this.size() > that.size()){
			res = 1;
		}

		return res;
	}

	@Override
	public int hashCode() {
		int res = 0;
		for(Long s : this){
			res = res ^ s.hashCode() + 1;
		}
		
		return res;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < this.size() - 1; ++i) {
			sb.append(this.get(i)).append(toStringSplitRegex);
		}
		sb.append(this.get(this.size() - 1));
		
		return sb.toString();
	}

}
