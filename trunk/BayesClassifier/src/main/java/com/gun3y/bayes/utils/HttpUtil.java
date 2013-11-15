package com.gun3y.bayes.utils;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.common.base.Strings;

public class HttpUtil {

    public static String httpGet(String url) {

	if (!Strings.isNullOrEmpty(url)) {

	    String responseBody = "";

	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    try {

		HttpGet httpget = new HttpGet(url);

		// Create a response handler
		ResponseHandler<String> responseHandler = new BasicResponseHandler();

		responseBody = httpclient.execute(httpget, responseHandler);

		return responseBody;

	    }
	    catch (ClientProtocolException e) {
		e.printStackTrace();
	    }
	    catch (IOException e) {
		e.printStackTrace();
	    }
	    finally {
		httpclient.getConnectionManager().shutdown();
	    }

	}
	return null;

    }

    public static void main(String[] args) {
	System.out.println(HttpUtil.httpGet("http://www.imdb.com/chart/top"));

    }

}
