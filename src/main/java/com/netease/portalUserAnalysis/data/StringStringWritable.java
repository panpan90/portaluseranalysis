
package com.netease.portalUserAnalysis.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;


public class StringStringWritable implements WritableComparable<StringStringWritable> {

    private String first="";
    private String second="";


    public StringStringWritable() {    }

    public StringStringWritable(StringStringWritable one) {
        this.first = one.first;
        this.second = one.second;
    }

    /**
     * @param first
     * @param second
     */
    public StringStringWritable(String first, String second) {
        super();
        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
    
    public void readFields(DataInput in) throws IOException {
        first = in.readUTF();
        second = in.readUTF();
      }

    public void write(DataOutput out) throws IOException {

            out.writeUTF(first);
            out.writeUTF(second);
        
    }

    public boolean equals(Object o) {
        if (o == null && !(o instanceof StringStringWritable)){
            return false;
        }
        StringStringWritable other = (StringStringWritable)o;
        return this.first.equals(other.getFirst()) && this.second.equals(other.getSecond());
    }

    public int compareTo(StringStringWritable o) {
        StringStringWritable that = (StringStringWritable)o;
        if(this.first.compareTo(that.first)!=0){
            return this.first.compareTo(that.first);
        }

        return this.second.compareTo(that.getSecond());
    }
    
    @Override
    public int hashCode(){
        return this.first.hashCode() ^ this.second.hashCode() + 1;
    }

    @Override
    public String toString() {
        return first + "," + second;
    }

}
