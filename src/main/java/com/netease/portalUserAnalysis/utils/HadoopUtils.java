package com.netease.portalUserAnalysis.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class HadoopUtils {

	/**
	 * map阶段获取当前记录所在文件的Path
	 * */
	@SuppressWarnings("rawtypes")
	public static Path getInputFilePath(Context context) throws Exception {
		InputSplit split = context.getInputSplit();
		Class<? extends InputSplit> splitClass = split.getClass();

		FileSplit fileSplit = null;
		if (FileSplit.class.equals(splitClass)) {
			fileSplit = (FileSplit) split;
		} else if ("org.apache.hadoop.mapreduce.lib.input.TaggedInputSplit".equals(splitClass.getName())) {
			Method getInputSplitMethod = splitClass.getDeclaredMethod("getInputSplit");
			getInputSplitMethod.setAccessible(true);
			fileSplit = (FileSplit) getInputSplitMethod.invoke(split);
		}
		
		return fileSplit.getPath();
	}

	public static Job getJob(Class<?> exampleClass, String jobName)
			throws IOException {
		Job job = Job.getInstance();
		job.setJobName(jobName);
		job.setJarByClass(exampleClass);
		job.getConfiguration().set("mapred.job.queue.name", "weblog");

		return job;
	}

	public static void writeString(DataOutput out, String value)
			throws IOException {
		if (null == value) {
			out.writeUTF("");
		} else {
			out.writeUTF(value);
		}
	}

	public static byte[] getWritableBytes(Writable w) throws IOException {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(s);
		w.write(dos);
		dos.close();
		s.close();
		return s.toByteArray();
	}

	public void readFromBytes(Writable w, byte[] bytes) throws IOException {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
				bytes));
		w.readFields(dis);
		dis.close();
	}

}
