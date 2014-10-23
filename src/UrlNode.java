/**Title: 爬虫元素
 * Description：用于描述爬取过的url元素
 * 
 * @author wolfblood
 * @version 1.0
 * Create on 2014-9-23
 */

public class UrlNode{
	//url地址
	private String url;
	//url地址的MD5特征值
	private String url_md5;
	//爬取的层数

	private int hitNums;
	//该链接的权重，根据权重进行链接爬取次序排名
	private int weight;
	//连接产生的文本内容
	private String content;
	//连接的标题
	private String title;

	public void setUrl(String _url){
		url = _url;
		url_md5= MD5(_url);
	}
	/**
	 * MD5特征值计算函数
	 * @return
	 *_ url字符串的特征值
	 */
	private String MD5(String _url){
		return MD5.getMD5(_url.getBytes());
	}
	
	public  void setHitNums(int _hitNums){
		hitNums=_hitNums;
	}
	public  void setWeight(int _weight){
		weight=_weight;
	}
	public void setTitle(String  _title){
		title=_title;
	}
	public void setContent(String  _content){
		content=_content;
	}
	public String getUrl(){return url;}
	public String getUrlMD5(){return url_md5;}
	
	public int getHitNums(){return hitNums;}
	public int getWeight(){return weight;}
	
}