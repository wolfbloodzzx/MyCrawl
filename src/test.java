import org.apache.commons.httpclient.HttpClient;

public class test{
	public static void main(String [] args){
		Spider sp = new Spider("http://news.163.com/");
		sp.crawl();
		//HttpClient httpClient = new HttpClient();
		//System.out.print(GetHtml.getHtml("http://news.163.com/"));
			
	}
}