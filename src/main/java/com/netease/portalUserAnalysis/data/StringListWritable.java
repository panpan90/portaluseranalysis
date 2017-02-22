
package com.netease.portalUserAnalysis.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.WritableComparable;

import com.netease.portalUserAnalysis.utils.HadoopUtils;
import com.netease.portalUserAnalysis.utils.StringUtils;

public class StringListWritable extends ArrayList<String> implements WritableComparable<StringListWritable> {

	private static final long serialVersionUID = 1L;
	
	private static final String toStringSplitRegex = "\t";
	
	public StringListWritable() {}

	public StringListWritable(StringListWritable one) {
		this.clear();
		for(String s : one){
			this.add(s);
		}
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.clear();
		int size = in.readInt();
		while(size-- > 0){
			this.add(in.readUTF());
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(this.size());
		for(String s : this){
			HadoopUtils.writeString(out, s);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null && !(o instanceof StringListWritable)) {
			return false;
		}
		StringListWritable other = (StringListWritable) o;
		
		if(this.size() != other.size()){
			return false;
		}
		
		for(int i = 0; i < this.size();++i){
			if(!StringUtils.equals(this.get(i), other.get(i))){
				return false;
			}
		}
		
		return true;
	}

	@Override
	public int compareTo(StringListWritable o) {
		StringListWritable that = (StringListWritable) o;
		
		int res = 0;
		
		for(int i = 0; i < this.size() && i < that.size(); ++i) {
			String thisCur = this.get(i);
			String thatCur = that.get(i);
			
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
		for(String s : this){
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
