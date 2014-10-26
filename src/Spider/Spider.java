package Spider;

import Spider.process.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Spider implements Runnable{
	
	private AtomicInteger number;
	private String url;
	private String path;
	private int layer;
	
	public Spider(String _url,String _path,AtomicInteger n,int _layer){
		url = _url;
		path = _path;
		number = n;
		layer = _layer+1;
		number.incrementAndGet();
	}
	
	public void run(){
		try{
//			System.out.println("线程"+number.get()+"正在运行...");
			byte[] html = null;
			html = GetHtml.getHtml(url);
			HtmlProcess.process(new String(html),layer);
			System.out.println("正在访问"+url);
			/************************
			 * 特殊处理
			 * 根据特定网站处理:
			 * 网易163新闻后缀		html
			 * qq新闻后缀			htm
			 * 新浪新闻后缀		shtml
			 * 凤凰新闻网			shtml
			 * 中国新闻网			shtml
			 * 
			 * 
			 */
			Pattern p = Pattern.compile("\\.shtml|\\.SHTML|"
					+ "\\.html|\\.HTML|\\.htm|\\.HTM");
			Matcher m = p.matcher(url);
			if(!m.find()) return;				
			/**
			 * 处理结束
			 **************************/
			TextExtract te = new TextExtract();
			String context,title;
			try{
				String charset = te.getCharset(html);
				context = te.parse(new String(html,charset));
				title = HtmlProcess.getTitle(context);
				if(context.length()<100) return;
				SaveToFile.write(path, title, url, context);
				//System.out.println(title+"保存成功......");
			}catch(UnsupportedEncodingException e){
				System.out.println("debug信息：未知文件类型"+e.getMessage());
			}
		}finally{
			number.decrementAndGet();
			
		}
	}
	
}