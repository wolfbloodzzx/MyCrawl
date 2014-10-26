package Spider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;
import Spider.define.*;
import Spider.process.*;
import Spider.table.*;
import Spider.*;

public class Main{
	public static void main(String[] args){
		Date today = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		String dateS = f.format(today);
		
		File file = new File("./"+dateS+"网易新闻"+"/");
		file.mkdir();
		System.out.println("开始爬取网易新闻：");
		SpiderRun s = new SpiderRun("http://news.163.com","./"+dateS+"网易新闻"+"/");
		s.crawl();
		
		file = new File("./"+dateS+"腾讯新闻"+"/");
		file.mkdir();
		System.out.println("开始爬取腾讯新闻：");
		s = new SpiderRun("http://news.qq.com/","./"+dateS+"腾讯新闻"+"/");
		s.crawl();
		
		file = new File("./"+dateS+"sina新闻"+"/");
		file.mkdir();
		System.out.println("开始爬取新浪新闻：");
		s = new SpiderRun("http://news.sina.com.cn/","./"+dateS+"sina新闻"+"/");
		s.crawl();
		
		file = new File("./"+dateS+"凤凰新闻"+"/");
		file.mkdir();
		System.out.println("开始爬取凤凰新闻：");
		s = new SpiderRun("http://news.ifeng.com/","./"+dateS+"凤凰新闻"+"/");
		s.crawl();
		
		file = new File("./"+dateS+"中国新闻网"+"/");
		file.mkdir();
		System.out.println("开始爬取中国新闻网：");
		s = new SpiderRun("http://www.chinanews.com/","./"+dateS+"中国新闻"+"/");
		s.crawl();
	}
}