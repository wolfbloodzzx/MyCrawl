package Spider.process;

/**
 * Description:下载HTML到本地
 * @author wolfblood
 * @version 1.0
 * Create on 2014-10-3
 * */

import java.io.IOException;



import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class GetHtml {
	public GetHtml() {
	}

	public static byte[] getHtml(String url) {

		
		HttpClient httpClient = new DefaultHttpClient();
		try {
			
			//创建HttpGet
			HttpGet httpGet = new HttpGet(url);
			//执行get请求
			HttpResponse response = httpClient.execute(httpGet);
			//获取响应实体
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				byte[] content = EntityUtils.toByteArray(entity);
				//解决HttpClient获取中文乱码 ，用String对象进行转码
				return content;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			//关闭连接，释放资源
			httpClient.getConnectionManager().shutdown();
		}
		
		return null;
	}
	private static byte[] mergeByte(byte[] byte1,byte[] byte2){
		byte[] byte3 = new byte[byte1.length+byte2.length];
		System.arraycopy(byte1, 0, byte3, 0, byte1.length);
		System.arraycopy(byte2, 0, byte3, byte1.length,byte2.length);
		return byte3;
	}
}