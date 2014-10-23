/**
 * Description:下载HTML到本地
 * @author wolfblood
 * @version 1.0
 * Create on 2014-10-3
 * */

import java.io.IOException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class GetHtml {
	public GetHtml() {
	}

	public static byte[] getHtml(String url) {

		HttpClient httpClient = new HttpClient();
		// 设置HttpClient超时5s
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);
		// 使用get方式获取
		GetMethod getMethod = new GetMethod(url);
		// 设置get的超时为5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置重试请求
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());

		// 执行HTTP GET
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			// 处理返回的状态码
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method Failed:" + getMethod.getStatusLine()
						+ "by wolfblood");
				return null;
			}
			byte[] rBody = getMethod.getResponseBody();
			return rBody;
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		return null;
	}
}