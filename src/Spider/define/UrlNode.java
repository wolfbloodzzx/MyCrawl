package Spider.define;

public class UrlNode {
	public UrlNode(String _url,int n){
		url = _url;
		urlmd5 = MD5.getMD5(_url.getBytes());
		layer = n;
	}
	//url字符串
	public String url;
	//url转换为md5值的字符串
	public String urlmd5;
	//爬虫层数
	public int layer;
}
