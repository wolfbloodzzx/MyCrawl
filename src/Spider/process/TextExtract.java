package Spider.process;

import java.io.BufferedInputStream;
import java.io.FileInputStream;     
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;



/**********使用时，直接调用parse（String）函数，String为爬下来的html原码，
  单独运行时需要输入存有Html原码的txt或htm、html等文件的本地地址
  129行，有作图时可以用到的行数和行块长度******************/
public class TextExtract {
	
	private List<String> lines;
	private final static int blocksWidth=3;
	private int threshold;
	private String html;
	private boolean flag;
	private int start;
	private int end;
	private StringBuilder text;
	private ArrayList<Integer> indexDistribution;
	
	public TextExtract() {
		lines = new ArrayList<String>();
		indexDistribution = new ArrayList<Integer>();
		text = new StringBuilder();
		flag = false;
		/* 当待抽取的网页正文中遇到成块的新闻标题未剔除时，只要增大此阈值即可。*/
		/* 阈值增大，准确率提升，召回率下降；值变小，噪声会大，但可以保证抽到只有一句话的正文 */
		threshold	= -1;   
	}
	

	/**
	 * 抽取网页正文，不判断该网页是否是目录型。即已知传入的肯定是可以抽取正文的主题类网页。
	 * 
	 * @param _html 网页HTML字符串
	 * 
	 * @return 网页正文string
	 */
	public String parse(String _html) {
		//System.out.println(parse(_html, false));			输出已提取完毕的文本
		return parse(_html, false);
	}
	
	/**
	 * 判断传入HTML，若是主题类网页，则抽取正文；否则输出<b>"unkown"</b>。
	 * 
	 * @param _html 网页HTML字符串
	 * @param _flag true进行主题类判断, 省略此参数则默认为false
	 * 
	 * @return 网页正文string
	 */
	public String parse(String _html, boolean _flag) {
		flag = _flag;
		html = _html;
		html = preProcess(html);
		//System.out.println(html); 			//输出已消除标签没有消除空行的文本
		return getText();
	}
	private static int FREQUENT_URL = 30;
	private static Pattern links = Pattern.compile("<[aA]\\s+[Hh][Rr][Ee][Ff]=[\"|\']?([^>\"\' ]+)[\"|\']?\\s*[^>]*>([^>]+)</a>(\\s*.{0,"+FREQUENT_URL+"}\\s*<a\\s+href=[\"|\']?([^>\"\' ]+)[\"|\']?\\s*[^>]*>([^>]+)</[aA]>){2,100}", Pattern.DOTALL);
	private static String preProcess(String source) {
		
		source = source.replaceAll("(?is)<!DOCTYPE.*?>", "");
		source = source.replaceAll("(?is)<!--.*?-->", "");				// remove html comment
		source = source.replaceAll("(?is)<script.*?>.*?</script>", ""); // remove javascript
		source = source.replaceAll("(?is)<style.*?>.*?</style>", "");   // remove css
		source = source.replaceAll("&.{2,5};|&#.{2,5};", " ");			// remove special char
		
		//剔除连续成片的超链接文本（认为是，广告或噪音）,超链接多藏于span中
		source = source.replaceAll("<[sS][pP][aA][nN].*?>", "");
		source = source.replaceAll("</[sS][pP][aA][nN]>", "");

		int len = source.length();
		while ((source = links.matcher(source).replaceAll("")).length() != len)
		{
			len = source.length();
		}
			;//continue;
		
		//source = links.matcher(source).replaceAll("");
		
		//防止html中在<>中包括大于号的判断
		source = source.replaceAll("<[^>'\"]*['\"].*['\"].*?>", "");

		source = source.replaceAll("<[//s//S]*?>", "");
		source = source.replaceAll("<[//s//S]*?>", "");
		source = source.replaceAll("\r\n", "\n");

		return source;
	
	}
	
	private String getText() {
		lines = Arrays.asList(html.split("\n"));
		indexDistribution.clear();
		
		int empty = 0;//空行的数量
		for (int i = 0; i < lines.size() - blocksWidth; i++) {
			
			if (lines.get(i).length() == 0)
			{
				empty++;
			}
			
			int wordsNum = 0;
			for (int j = i; j < i + blocksWidth; j++) { 
				lines.set(j, lines.get(j).replaceAll("\\s+", ""));
				wordsNum += lines.get(j).length();
			}
			indexDistribution.add(wordsNum);
			//System.out.printf("%d:%d\n",i,wordsNum);   //	输出行号和行块长度，就是制作图表使用的X，Y
		}
		int sum = 0;

		for (int i=0; i< indexDistribution.size(); i++)
		{
			sum += indexDistribution.get(i);
		}
		
		threshold = Math.min(100, (sum/indexDistribution.size())<<(empty/(lines.size()-empty)>>>1));
		threshold = Math.max(50, threshold);
		
		start = -1; end = -1;
		boolean boolstart = false, boolend = false;
		boolean firstMatch = true;//前面的标题块往往比较小，应该减小与它匹配的阈值
		text.setLength(0);
		
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < indexDistribution.size() - 1; i++) {
			
			if(firstMatch && ! boolstart)
			{
				if (indexDistribution.get(i) > (threshold/2) && ! boolstart) {
					if (indexDistribution.get(i+1).intValue() != 0 
						|| indexDistribution.get(i+2).intValue() != 0) {
						firstMatch = false;
						boolstart = true;
						start = i;
						continue;
					}
				}
				
			}
			if (indexDistribution.get(i) > threshold && ! boolstart) {
				if (indexDistribution.get(i+1).intValue() != 0 
					|| indexDistribution.get(i+2).intValue() != 0
					|| indexDistribution.get(i+3).intValue() != 0) {
					boolstart = true;
					start = i;
					continue;
				}
			}
			if (boolstart) {
				if (indexDistribution.get(i).intValue() == 0 
					|| indexDistribution.get(i+1).intValue() == 0) {
					end = i;
					boolend = true;
				}
			}
		
			if (boolend) {
				buffer.setLength(0);
				//System.out.println(start+1 + "\t\t" + end+1);
				for (int ii = start; ii <= end; ii++) {
					if (lines.get(ii).length() < 5) continue;
					buffer.append(lines.get(ii) + "\n");
				}
				String str = buffer.toString();
				//System.out.println(str);
				if (str.contains("Copyright")  || str.contains("版权所有") ) continue; 
				text.append(str);
				boolstart = boolend = false;
			}
		}
		
		if (start > end)
		{
			buffer.setLength(0);
			int size_1 = lines.size()-1;
			for (int ii = start; ii <= size_1; ii++) {
				if (lines.get(ii).length() < 5) continue;												
				buffer.append(lines.get(ii) + "\n");
			}
			String str = buffer.toString();
			//System.out.println(str);
			if ((!str.contains("Copyright"))  || (!str.contains("版权所有")) ) 
			{	
				text.append(str);
			}
		}
		
		return text.toString();
	}
	
	
	private String detCharset = null;

	private Pattern pGB2312 = Pattern.compile("GB2312", Pattern.CASE_INSENSITIVE);
	private Pattern pUTF8 = Pattern.compile("(UTF8)|(UTF-8)", Pattern.CASE_INSENSITIVE);
	
	public String getCharset(byte[] content){
		detectCharset(content);
		return detCharset;
	}
	public void detectCharset(byte[] content)
	{
		String html = new String(content); 
		Matcher m = pGB2312.matcher(html);
		if (m.find())
		{
			detCharset = "gb2312";
			return ;
		}
		m = pUTF8.matcher(html);
		if (m.find())
		{
			detCharset = "utf-8";
			return;
		}
		
		int lang = nsPSMDetector.ALL;
		nsDetector det = new nsDetector(lang);
		det.Init(new nsICharsetDetectionObserver() {
			public void Notify(String charset) {
				detCharset = charset;
			} 
		});
		boolean isAscii = true;

		if (isAscii)
			isAscii = det.isAscii(content, content.length);

		if (!isAscii)
			det.DoIt(content, content.length, false);

		det.DataEnd();

		boolean found = false;
		if (isAscii) {
			this.detCharset = "US-ASCII";
			found = true;
		}

		if (!found && detCharset == null) {
			detCharset = det.getProbableCharsets()[0];
		}
	}
}
