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



/**********ʹ��ʱ��ֱ�ӵ���parse��String��������StringΪ��������htmlԭ�룬
  ��������ʱ��Ҫ�������Htmlԭ���txt��htm��html���ļ��ı��ص�ַ
  129�У�����ͼʱ�����õ����������п鳤��******************/
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
		/* ������ȡ����ҳ�����������ɿ�����ű���δ�޳�ʱ��ֻҪ�������ֵ���ɡ�*/
		/* ��ֵ����׼ȷ���������ٻ����½���ֵ��С��������󣬵����Ա�֤�鵽ֻ��һ�仰������ */
		threshold	= -1;   
	}
	

	/**
	 * ��ȡ��ҳ���ģ����жϸ���ҳ�Ƿ���Ŀ¼�͡�����֪����Ŀ϶��ǿ��Գ�ȡ���ĵ���������ҳ��
	 * 
	 * @param _html ��ҳHTML�ַ���
	 * 
	 * @return ��ҳ����string
	 */
	public String parse(String _html) {
		//System.out.println(parse(_html, false));			�������ȡ��ϵ��ı�
		return parse(_html, false);
	}
	
	/**
	 * �жϴ���HTML��������������ҳ�����ȡ���ģ��������<b>"unkown"</b>��
	 * 
	 * @param _html ��ҳHTML�ַ���
	 * @param _flag true�����������ж�, ʡ�Դ˲�����Ĭ��Ϊfalse
	 * 
	 * @return ��ҳ����string
	 */
	public String parse(String _html, boolean _flag) {
		flag = _flag;
		html = _html;
		html = preProcess(html);
		//System.out.println(html); 			//�����������ǩû���������е��ı�
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
		
		//�޳�������Ƭ�ĳ������ı�����Ϊ�ǣ�����������,�����Ӷ����span��
		source = source.replaceAll("<[sS][pP][aA][nN].*?>", "");
		source = source.replaceAll("</[sS][pP][aA][nN]>", "");

		int len = source.length();
		while ((source = links.matcher(source).replaceAll("")).length() != len)
		{
			len = source.length();
		}
			;//continue;
		
		//source = links.matcher(source).replaceAll("");
		
		//��ֹhtml����<>�а������ںŵ��ж�
		source = source.replaceAll("<[^>'\"]*['\"].*['\"].*?>", "");

		source = source.replaceAll("<[//s//S]*?>", "");
		source = source.replaceAll("<[//s//S]*?>", "");
		source = source.replaceAll("\r\n", "\n");

		return source;
	
	}
	
	private String getText() {
		lines = Arrays.asList(html.split("\n"));
		indexDistribution.clear();
		
		int empty = 0;//���е�����
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
			//System.out.printf("%d:%d\n",i,wordsNum);   //	����кź��п鳤�ȣ���������ͼ��ʹ�õ�X��Y
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
		boolean firstMatch = true;//ǰ��ı���������Ƚ�С��Ӧ�ü�С����ƥ�����ֵ
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
				if (str.contains("Copyright")  || str.contains("��Ȩ����") ) continue; 
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
			if ((!str.contains("Copyright"))  || (!str.contains("��Ȩ����")) ) 
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
