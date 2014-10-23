/**
 * Description: HTML处理模块
 * @author wolfblood
 * @version 1.0
 * Create on 2014-10-3
 * */

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParse{

	//网页中爬取的url连接列表
	private ArrayList <String> URLList = new ArrayList();
	//网页编码方式
	private String charset;
	//网页标题
	private String title;
	//网页正文文本
	private String content;
	
	private void parseTitle(String str){
		Pattern pattern = Pattern.compile("<title>(.*)</title>");
		Matcher matcher = pattern.matcher(str);
		if(matcher.find()) title = matcher.group(1);
	}
	private void parseContent(String str){
		TextExtract te = new TextExtract();
		content = te.parse(str);
	}
	/**
	 * @author wolfblood
	 * @param str 网页文本
	 *
	 */
	private void getCharsetByStr(String str){
		Pattern pattern = Pattern.compile("<meta[^>]*?charset=(\\w+)[\\W]*?>");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find())
			charset=matcher.group(1).replaceAll(" ","");
	}
	
	/**
	 * 获取网页中的URL
	 * @author wolfblood
	 * @param str 网页文本
	 */
	private void getURL(String str){
		Pattern pattern = Pattern.compile("((https?|ftp|file)://[-a-zA-Z0-9+&@#/%? =~_|!:,.;]*[-a-zA-Z0-9+&@ #\\/%=~_|])");
		Matcher matcher = pattern.matcher(str);
		String url = new String();
		while(matcher.find()){
			/*url 规范化*/			
			//去除空格
			url = matcher.group(0).replaceAll(" ", "");
//			
//			//判断url字符串是否为空
//			if(url.length()==0) continue;
//			
//			//判断url是否为#
//			if(url.charAt(0)=='#') continue;
//			
//			//判断url是否以特殊字符结尾
//			String specialChar = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
//			Pattern pSpecial = Pattern.compile(specialChar);
//			Matcher m = pSpecial.matcher(url.charAt(url.length()-1)+"");
//			if(m.find()) continue;
//			
//			//判断url是否以特殊字符开头
//			m = pSpecial.matcher(url.charAt(0)+"");
//			if(m.find()) continue;
//			
//			//判断url是否以http://开头，如不是则添加
//			if(url.charAt(0)!='h') url="http://"+url;
//			
//			//判断规范化的url是否合法
//			Pattern urlPat = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
//			m = urlPat.matcher(url);
//			if(!m.find()) continue;
			
			Pattern p = Pattern.compile("\\.jpg|\\.txt|\\.png|\\.pdf|\\.css|\\.gif|\\.ico|\\.js|\\.JS|\\.JPG|\\.TXT|\\.PNG|\\.PDF|\\.ICO|\\.CSS|\\.GIF");
			Matcher m = p.matcher(url);
			if(m.find()) continue;
			URLList.add(url);
		}
	}
	
	/**
	 * @author wolfblood
	 * @param html 待分析的html源码
	 */
	public void parse(byte[] html){
		String context = new String(html);
		getCharsetByStr(context);
		getURL(context);
		try{String context_chs = new String(html,charset);
		parseTitle(context_chs);}
		catch(Exception e){}
		
		//parseContent(context);
	}

	public ArrayList getArray(){return URLList;}
	public String getCharset(){return charset;}
	public String getTitle(){return title;}
	public String getContent(){return content;}
	
	public void parse(String str){
		getCharsetByStr(str);
		getURL(str);
	}
	
}
