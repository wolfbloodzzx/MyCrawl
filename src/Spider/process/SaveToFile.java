package Spider.process;

import java.io.*;
import java.util.*;
import java.text.*;

import Spider.define.*;

public class SaveToFile {
	
	/**
	 * 拼接两个byte[]数组
	 * @param byte1
	 * @param byte2
	 * @return
	 */
	private static byte[] mergeByte(byte[] byte1,byte[] byte2){
		byte[] byte3 = new byte[byte1.length+byte2.length];
		System.arraycopy(byte1, 0, byte3, 0, byte1.length);
		System.arraycopy(byte2, 0, byte3, byte1.length,byte2.length);
		return byte3;
	}

	/**
	 * 获取格式为yyyymmddhhmimi日期字符串
	 * 
	 * @return
	 */
	private static String getDateString() {
		// 获取当天日期
		Date today = new Date();
		// 设置日期格式
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmm");
		// 转换为字符串
		String dateS = f.format(today);
		return dateS;
	}

	/**
	 * 把字节流html写入文件
	 * 
	 * @param path
	 *            文件路径
	 * @param url
	 *            爬取的链接
	 * @param html
	 *            写入字节
	 */
	public static void write(String path, String url, byte[] html) {

		// 文件名格式 yyyyMMddHHmm(url的MD5).txt
		String name = getDateString() + MD5.getMD5(url.getBytes()) + ".txt";
		// 根据日期确定路径
		String ppath = path + getDateString() + File.separator;

		File fp = new File(path + name);
		try {
			fp.createNewFile();
			System.out.println("debug信息：创建文件成功");
		} catch (IOException e) {
			System.out.println("debug信息：创建文件失败" + e.getMessage());
			return;
		}
		try {
			url = url + '\n';
			byte[] _html = mergeByte(url.getBytes(),html);
			FileOutputStream fos = new FileOutputStream(fp);
			fos.write(_html);
			fos.close();
			System.out.println("debug信息：写入文件成功");
		} catch (FileNotFoundException e) {
			System.out.println("debug信息：找不到文件" + e.getMessage());
		} catch (IOException e) {
			System.out.println("debug信息：IO错误" + e.getMessage());
		}

	}

	/**
	 * 把context文本写入文件
	 * 
	 * @param path
	 *            文件路径
	 * @param url
	 *            爬取的链接
	 * @param context
	 *            写入文本
	 */
	public static void write(String path, String url, String context) {

		// 文件名格式 yyyyMMddHHmm(url的MD5).txt
		String name = getDateString() + MD5.getMD5(url.getBytes()) + ".txt";
		// 根据日期确定路径
		String ppath = path + getDateString() + File.separator;

		File fp = new File(path + name);
		try {
			fp.createNewFile();
			System.out.println("debug信息：创建文件成功");
		} catch (IOException e) {
			System.out.println("debug信息：创建文件失败" + e.getMessage());
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(fp);
			context = url + '\n' +context;
			byte[] html = context.getBytes();
			fos.write(html);
			fos.close();
			System.out.println("debug信息：写入文件成功");
		} catch (FileNotFoundException e) {
			System.out.println("debug信息：找不到文件" + e.getMessage());
		} catch (IOException e) {
			System.out.println("debug信息：IO错误" + e.getMessage());
		}

	}

	/**
	 * 把字节流写入文件，文件格式为：title\n url\n context
	 * 
	 * @param path
	 *            文件路径
	 * @param title
	 *            文章标题
	 * @param url
	 *            文本链接
	 * @param context
	 *            文本正文
	 */
	public static void write(String path, String title, String url,
			String context) {
		// 文件名格式 yyyyMMddHHmm(url的MD5).txt
		String name = getDateString() + MD5.getMD5(url.getBytes()) + ".txt";
		// 根据日期确定路径
		String ppath = path + getDateString() + File.separator;

		File fp = new File(path + name);
		try {
			fp.createNewFile();
			System.out.println("debug信息：创建文件成功");
		} catch (IOException e) {
			System.out.println("debug信息：创建文件失败" + e.getMessage());
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(fp);
			context = title + '\n' + url + '\n' + context;
			byte[] html = context.getBytes();
			fos.write(html);
			fos.close();
			System.out.println("debug信息：写入文件成功");
		} catch (FileNotFoundException e) {
			System.out.println("debug信息：找不到文件" + e.getMessage());
		} catch (IOException e) {
			System.out.println("debug信息：IO错误" + e.getMessage());
		}
	}

}
