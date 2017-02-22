/**
 * @(#)LongLongWritable.java, 2012-11-20. 
 * 
 * Copyright 2012 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.portalUserAnalysis.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;


public class LongStringWritable implements WritableComparable<LongStringWritable> {

    private long first;
    private String second;
    
    
    public LongStringWritable() {    }
    
    public LongStringWritable(LongStringWritable one) {
        this.first = one.first;
        this.second = one.second;
    }
    
    /**
     * @param first
     * @param second
     */
    public LongStringWritable(long first, String second) {
        super();
        this.first = first;
        this.second = second;
    }

    public long getFirst() {
        return first;
    }

    public void setFirst(long first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
    
    public void readFields(DataInput in) throws IOException {
        first = in.readLong();
        second = in.readUTF();
      }

    public void write(DataOutput out) throws IOException {
        out.writeLong(first);
        if (second == null) {
            out.writeUTF("");
        } else {
            out.writeUTF(second);
        }
    }

    public boolean equals(Object o) {
        if (o == null && !(o instanceof LongStringWritable)){
            return false;
        }
        LongStringWritable other = (LongStringWritable)o;
        return this.first == other.getFirst() && this.second.equals(other.getSecond());
    }

    public int compareTo(LongStringWritable o) {
        LongStringWritable that = (LongStringWritable)o;
        if(this.first > that.first){
            return 1;
        }
        if(this.first < that.first){
            return -1;
        }
        
        return this.second.compareTo(that.getSecond());
    }
    
    @Override
    public int hashCode(){
        return Long.toString(this.first).hashCode() ^ this.second.hashCode() + 1;
    }

    @Override
    public String toString() {
        return first + "," + second;
    }

}
