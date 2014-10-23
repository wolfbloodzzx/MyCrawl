/**
 * Description: 爬虫主程序
 * @author wolfblood
 * @version 1.0
 * Create on 2014-10-3
 * */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;


public class Spider {
	//爬虫队列
	private MyQueue spiderQue = new MyQueue();
	private VisitedList visTable = new VisitedList();
	
	//爬虫深度
	private int N;
	private int setN = 3;
	
	//爬虫种子
	private String seed;
	
	public Spider(String _seed){seed = _seed;}
	
	public void setSeed(String _seed){seed = _seed;}
	
	public void setLayer(int _N){setN = _N;}
	
	public void crawl(){
		Pattern pattern = Pattern.compile("((https?|ftp|file)://[-a-zA-Z0-9+&@#/%? =~_|!:,.;]*[-a-zA-Z0-9+&@ #\\/%=~_|])");
		Matcher matcher = pattern.matcher(seed);
		if(!matcher.find()){
			System.out.println("wrong seed!");
			return;
		}
		spiderQue.enQueue(seed);
		while(!spiderQue.isQueueEmpty()){
			String url = (String)spiderQue.deQueue();
			System.out.println(url);
			HtmlParse hp = new HtmlParse();
			hp.parse(GetHtml.getHtml(url));
			//String content = hp.getContent();
			//System.out.println(hp.getTitle());
			ArrayList urlist = hp.getArray();
			for(int i =0;i<urlist.size();i++)
				spiderQue.enQueue(urlist.get(i));
			//System.out.print(content);
			if(setN++>1000) return;
		}
	}
}