/**
 * @(#)HttpClientFactory.java, 2012-12-21. 
 * 
 * Copyright 2012 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.portalUserAnalysis.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.io.IOUtils;

/**
*
* @author mengyan
*
*/

public class HttpClientFactory {

    protected static final int MAX_CONNECTION = 50;

    protected static final int TIMEOUT_CONNECTION = 10000;

    protected static final int TIMEOUT_SOCKET = 5000;

    protected static final int MAX_CONTENT_SIZE = 2 * 1024 * 1024;

    protected static final int MAX_CONNECTION_PER_IP = 5;

    protected static final int CONNECTION_MANAGER_TIMEOUT = 10000;

    public static HttpClient getNewInstance() {
        return getNewInstance(TIMEOUT_SOCKET, TIMEOUT_CONNECTION);
    }

    public static HttpClient getNewInstance(int soTimeout, int connTimeout, int connectionManagerTimeout) {
        return getNewInstance(TIMEOUT_SOCKET, TIMEOUT_CONNECTION, connectionManagerTimeout);
    }

    public static HttpClient getNewInstance(int soTimeout, int connTimeout) {
        return getNewInstance(soTimeout, connTimeout, MAX_CONNECTION, MAX_CONNECTION_PER_IP, MAX_CONTENT_SIZE,
                CONNECTION_MANAGER_TIMEOUT);
    }

    public static HttpClient getNewInstance(int soTimeout, int connTimeout, int maxConn, int maxConnPerIp, int maxContentSize) {
        return getNewInstance(soTimeout, connTimeout, maxConn, maxConnPerIp, maxContentSize, CONNECTION_MANAGER_TIMEOUT);
    }

    public static HttpClient getNewInstance(int soTimeout, int connTimeout, int maxConn, int maxConnPerIp,
            int maxContentSize, int connectionManagerTimeout) {
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setMaxTotalConnections(maxConn);
        connectionManager.getParams().setConnectionTimeout(connTimeout);
        connectionManager.getParams().setSoTimeout(soTimeout);
        connectionManager.getParams().setSendBufferSize(maxContentSize);
        connectionManager.getParams().setReceiveBufferSize(maxContentSize);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnPerIp);

        HttpClientParams params = new HttpClientParams();
        params.setConnectionManagerTimeout(connectionManagerTimeout);
        return new HttpClient(params, connectionManager);
    }
    
    public static GetMethod getMethod(String url){
        GetMethod getmethod = new GetMethod(url);
        return getmethod;
    }
    public static PostMethod postMethod(String url){
        PostMethod postMethod = new PostMethod(url);
        return postMethod;
    }
    public static int executeMethod(HttpClient client,HttpMethod method) throws HttpException, IOException{
        try {
            return client.executeMethod(method);
        }finally{
            method.releaseConnection();
        }
    }
    
    public static List<String> runGetMethod(String url) throws HttpException{
        GetMethod getMethod = getMethod(url);
        try {
            int statusCode = getNewInstance().executeMethod(getMethod);
            if (statusCode == HttpURLConnection.HTTP_OK) {
                InputStream is = getMethod.getResponseBodyAsStream();
                return IOUtils.readLines(is, "UTF-8");
            }
            throw new HttpException(url+" Get方式访问失败，返回状态:"+statusCode);
        } catch (Exception e) {
            if (e instanceof HttpException) {
                throw (HttpException)e;
            }
            throw new HttpException(url+" Get方式访问失败", e);
        }finally{
            getMethod.releaseConnection();
        }
    }
}

