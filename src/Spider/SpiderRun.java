package Spider;

import Spider.define.*;
import Spider.process.*;
import Spider.table.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SpiderRun {
	// 爬虫爬取的文件保存路径
	private String path;
	// 爬虫线程个数
	private int N = 8;
	// 允许爬取的层数
	private int maxlayer = 5;
	// 允许爬取的最大个数
	private int maxHtmlNum = 1000;

	private int cnt = 0;

	public SpiderRun(String p) {
		path = p;
	}

	public SpiderRun(String url, String p) {
		UrlQueue.pushNode(new UrlNode(url, 0));
//		System.out.println(UrlQueue.size());
		path = p;
	}

	/**
	 * 添加初始爬虫队列
	 * 
	 * @param url
	 */
	public void addInitUrl(String url) {
		UrlQueue.pushNode(new UrlNode(url, 0));
	}

	public void setPath(String p) {
		path = p;
	}

	public void setMaxThreadNum(int n) {
		N = n;
	}

	public void setMaxHtmlNum(int n) {
		maxHtmlNum = n;
	}

	public void setMaxLayer(int n) {
		maxlayer = n;
	}

	public void sleep() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.out.println("debug信息：线程错误" + e.getMessage());
		}
	}

	/**
	 * 启动爬虫线程
	 */
	public void crawl() {
		AtomicInteger number = new AtomicInteger();
		ThreadPoolExecutor threadpool = new ThreadPoolExecutor(N, N, 3,
				TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()); // 创建一个线程池
		UrlNode node;
		try {
			while (!UrlQueue.isEmpty()) {
				node = UrlQueue.popNode();
				if (node.layer + 1 <= maxlayer) {
					threadpool.execute(new Spider(node.url, path, number,
							node.layer + 1));
//					System.out.println(number.get());
					while ((UrlQueue.isEmpty() && number.get() != 0)
							|| (number.get()) == N)
						try {
							//System.out.println("sleep");
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							System.out.println("debug信息：线程错误" + e.getMessage());
						}
					if (cnt++ > maxHtmlNum)
						break;
				}
			}
		} finally {
			threadpool.shutdown();
		}
		System.out.println("爬虫线程退出，爬取链接数为：" + cnt);
		System.out.println("未爬取的连接数为：" + UrlQueue.size());
	}

}