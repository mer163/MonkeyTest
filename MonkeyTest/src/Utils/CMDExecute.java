package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public final class CMDExecute {
	/**
	 * 私有构造函数，防止被new
	 */
	private CMDExecute()
	{
		
	}
	
	/**
	* @Description 执行命令行;返回命令执行结果，每行数据以“\r\n”分隔 
	* @param  cmdString
	* @param    
	* @return String 
	* @throws
	 */
	public static String runCMD(String cmdString) {
		StringBuilder sb = new StringBuilder();
		String line = "";
		
		try {
			Process process = Runtime.getRuntime().exec(cmdString);	
			InputStream is = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					is));	
			while (((line = br.readLine())!= null)) {
				line = line.trim();
				if(line.equals(""))
				{
					continue;
				}
				sb.append(line);
				sb.append("\r\n");
			}
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("执行CMD wait操作失败");
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("执行CMD失败");
			System.out.println(e.getMessage());
			return null;
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(runCMD("adb devices"));
	}
}