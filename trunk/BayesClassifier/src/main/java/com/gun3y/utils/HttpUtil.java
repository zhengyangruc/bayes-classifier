package com.gun3y.utils;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.common.base.Strings;

public class HttpUtil {

	private static final String proxyHost = "proxyus3.huawei.com";
	private static final int proxyPort = 8080;
	private static final String username = "CHINA\\M00741555";
	private static final String password = "Alone5198*";

	public static String httpGet(String url) {

		if (!Strings.isNullOrEmpty(url)) {

			String responseBody = "";

			DefaultHttpClient httpclient = new DefaultHttpClient();

			// // log.info("use proxy " + proxyHost);
			// httpclient.getCredentialsProvider().setCredentials(
			// new AuthScope(proxyHost, proxyPort),
			// new UsernamePasswordCredentials(username, password));
			//
			// HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			//
			// httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
			// proxy);

			try {

				HttpGet httpget = new HttpGet(url);

				// Create a response handler
				ResponseHandler<String> responseHandler = new BasicResponseHandler();

				responseBody = httpclient.execute(httpget, responseHandler);

				return responseBody;

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// When HttpClient instance is no longer needed,
				// shut down the connection manager to ensure
				// immediate deallocation of all system resources
				httpclient.getConnectionManager().shutdown();
			}

		}
		return null;

	}

	public static void main(String[] args) {
		System.out.println(HttpUtil.httpGet("http://www.imdb.com/chart/top"));

	}

}
