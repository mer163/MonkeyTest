package jhss.youguu.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PushbackInputStream;

import javax.rmi.CORBA.Util;

public class test {
	
	public static String monkey_case= "./lib/case.sh";
	public static String app = "./lib/app.sh";
	public static String function = "./lib/function.sh";
	
	public static String localtmp = "/data/local/tmp/";
	
	public static void main(String[] args) {
//		System.out.println("result:"+adbPush(monkey_case));
//		System.out.println("adb root"+ Utils.CMDExecute.runCMD("adb root"));
		System.out.println("run:"+Utils.CMDExecute.runCMD("adb devices"));
		try {
			String command = "C:\\Users\\Administrator\\Desktop\\monkey压力执行脚本V2.1\\集成测试.bat";
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while((line= br.readLine())!=null){
				System.out.println(line);
				sb.append(line+"\r\n");
				
			}
			br.close();
		} catch (IOException e) {
			// TODO: handle exception
		}
	
    
	}
	
	
	
	public static String adbPush(String file){
		String command = "adb push "+file +" " +localtmp;
		
		return Utils.CMDExecute.runCMD(command);
	}
}


