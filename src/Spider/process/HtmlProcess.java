package Spider.process;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Spider.table.*;
import Spider.define.*;


public class HtmlProcess {
	//private static ArrayList <String> urlist = new ArrayList(); //html中存在的url列表
	public static void process(String context,int layer){
		
		Pattern pattern = Pattern.compile("((https?|ftp|file)://[-a-zA-Z0-9+&@#/%? =~_|!:,.;]"
				+ "*[-a-zA-Z0-9+&@ #\\/%=~_|])");
		Matcher matcher = pattern.matcher(context);
		String url = new String();
		while(matcher.find()){
			/*url 规范化*/			
			//去除空格
			url = matcher.group(0).replaceAll(" ", "");
			Pattern p = Pattern.compile("\\.jpg|\\.txt|\\.png|\\.pdf|\\.css|\\.gif|"
					+ "\\.ico|\\.js|\\.swf|\\.SWF|\\.JS|\\.JPG|\\.TXT|\\.PNG|\\.PDF|\\.ICO|\\.CSS|\\.GIF");
			Matcher m = p.matcher(url);
			if(m.find()) continue;
			/**
			 * 手工添加过滤列表
			 * 网易新闻过滤关键字列表：
			 * v.163.com
			 * photoview
			 */
			//System.out.println(url);
			UrlQueue.pushNode(new UrlNode(url,layer));
		}
	}
	/**
	 * 获取文章标题
	 * @param context html文本
	 * @return
	 */
	public static String getTitle(String context){
		Pattern p = Pattern.compile("<title>(.*?)</title>");
		Matcher m = p.matcher(context);
		String r = new String();
		if(m.find()) 
			r = m.group(1);
		return r;
	}
}
