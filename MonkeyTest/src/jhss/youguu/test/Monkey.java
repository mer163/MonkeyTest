package jhss.youguu.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import jhss.youguu.test.Monkey_Menu;
import Outlog.log;

public class Monkey {

	
	public static void main(String []args) throws IOException, InterruptedException
	{
		
		//Monkey(" ", "com.taobao.taobao", "500", "", "", "", "", "", "", "500","D:/log8.txt");
//		System.out.println(getDeviceName());
		System.out.println(getDevices());
		System.out.println(getDevices().size());
		
//		killMonkey("192.168.56.101:5555", getMonkeyPid("192.168.56.101:5555"));
			
	}

	public static void killMonkey(String DevicesName,String pid) throws IOException{
		
		Runtime runtime1 = Runtime.getRuntime();
		if(pid!=null){
			Process proc = runtime1.exec("adb -s "+DevicesName+" shell kill "+pid);
//		    Thread.sleep(5000);
		    try {
		        if (proc.waitFor() != 0) {
		            System.err.println("exit value = " + proc.exitValue());
		        }
		        BufferedReader in = new BufferedReader(new InputStreamReader(
		                proc.getInputStream()));
		        StringBuffer stringBuffer = new StringBuffer();
		        String line = null;
		        while ((line = in.readLine()) != null) {
		            stringBuffer.append(line+" ");
		            
		            
		        }
		     //   System.out.println(stringBuffer);
		    String str1=stringBuffer.toString();
			if(str1.length()==0){
				
				System.out.println("结束monkey成功");
			}
			else{
				System.out.println("Monkey 没有在运行中，结束monkey失败");
				
			}

		    } catch (InterruptedException e) {
		        System.err.println(e);
		    }finally{
		        try {
		            proc.destroy();
		        } catch (Exception e2) {
		        }
		    }
		   
		}else System.out.println("monkey没在运行中，无法关闭。");
		
	}
	
	
	
	public static String getMonkeyPid(String DevicesName) throws IOException{
		
		Runtime runtime1 = Runtime.getRuntime();
		Process proc = runtime1.exec("adb -s "+DevicesName+" shell ps |grep monkey");
//	    Thread.sleep(5000);
	    try {
	        if (proc.waitFor() != 0) {
	            System.err.println("exit value = " + proc.exitValue());
	        }
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	                proc.getInputStream()));
	        StringBuffer stringBuffer = new StringBuffer();
	        String line = null;
	        while ((line = in.readLine()) != null) {
	            stringBuffer.append(line+" ");
	            
	            
	        }
	     //   System.out.println(stringBuffer);
	    String str1=stringBuffer.toString();
	    		
		if(str1.length()==0){
			
			System.out.println("monkey is not running...");
		}
		else{
			Pattern pattern = Pattern.compile("([\" \"][0-9]+)");
			ArrayList<Integer> num = matchInteger(pattern,
					str1);
			
			
			
			// root      9392  62    469220 23924 ffffffff b75e3d69 S com.android.commands.monkey
//			String str2 = str1.substring(str1.indexOf("root")+4,str1.indexOf("root")+16).trim();
//			System.out.println("str2:"+str2);
			
			return num.get(0).toString();
		}

	    } catch (InterruptedException e) {
	        System.err.println(e);
	    }finally{
	        try {
	            proc.destroy();
	        } catch (Exception e2) {
	        }
	    }
	    return null;
	}
	
	// 
	public static ArrayList<Integer> matchInteger(Pattern pattern, String str) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		Matcher mc = pattern.matcher(str);
		
		while (mc.find()) {
			result.add(new Integer(mc.group().trim()));
		}
		
		return result;
	}
	
	
	public static Boolean isMonkeyRunning( String DevicesName) throws IOException, InterruptedException{
		Boolean ismonkeyrunning=false;
		Runtime runtime1 = Runtime.getRuntime();
		Process proc = runtime1.exec("adb -s "+DevicesName+" shell ps |grep monkey");
//	    Thread.sleep(5000);
	    try {
	        if (proc.waitFor() != 0) {
	            System.err.println("exit value = " + proc.exitValue());
	        }
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	                proc.getInputStream()));
	        StringBuffer stringBuffer = new StringBuffer();
	        String line = null;
	        while ((line = in.readLine()) != null) {
	            stringBuffer.append(line+" ");
	            
	            
	        }
	     //   System.out.println(stringBuffer);
	    String str1=stringBuffer.toString();
		
		if(str1.length()==0){
			ismonkeyrunning=false;
//			System.out.println("monkey is not running...");
		}
		else{
			ismonkeyrunning =true;
//			System.out.println("monkey is running...");
		}
//		 String monkeypidd = str1.substring(10, 16);
//       String monkeypidd = ""+getPid("com.jhss.yoguu");
//      	System.out.println("monkey:"+monkeypidd);
//       if (!(monkeypidd==null)){
//       	ismonkeyrunning = true;
//       	return ismonkeyrunning;
//       }              
//       else{
//       	ismonkeyrunning =false;
//       	
//       }	
	    } catch (InterruptedException e) {
	        System.err.println(e);
	    }finally{
	        try {
	            proc.destroy();
	        } catch (Exception e2) {
	        }
	    }
	    return ismonkeyrunning;
	}
	
	/**
	 * 		run monkey and get the crash log..
	 * @param command
	 * @return 返回一个String数组， 第一位代表完整内容，第二位为crash内容。
	 */
	public static String[] getCrash(String command,String pkgname,JTextArea text)
    {
        BufferedReader br = null;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer1 = new StringBuffer();
        String[] ret = new String[2];	//保存返回结果。
        String str2 = null;
        String time = Outlog.log.time();
        String log = Monkey_Menu.path;
        
        //目录不存在则创建。
        if(!(new File(log).isDirectory())){
        	new File(log).mkdir();
        }
        
        //创建crash日志文件。
        File monkeyLog = new File(log + File.separator + time + ".txt");
        File crashLog = new File(log+"Crash_log/"+Outlog.log.time()+".txt");

        try {
//       	 String[] args = new String[]{"cmd","/c",command};
            Process p = Runtime.getRuntime().exec(command);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            Writer writer = new OutputStreamWriter(new FileOutputStream(monkeyLog,
					true), "UTF-8");
            Writer writer1 = new OutputStreamWriter(new FileOutputStream(crashLog,
					true), "UTF-8");
            
            String line = null;
            while ((line = br.readLine()) != null) {
            	if("".equals(line.trim())) continue;
                text.append(line+"\n");
                // 日志写入电脑磁盘中。
                writer.write(line);
				writer.write("\r\n");
				stringBuffer1.append(line+"\n");
								
                if (line.startsWith("// ")){
//               	 while((line).contains("// ")){
               		 stringBuffer.append(line+"\n");
                }
                
    
            }
            writer.close();
            
            ret[0] = stringBuffer1.toString(); //保存完整内容。
            str2 = stringBuffer.toString();		//保存 包含"// "的内容。
            String str3[] = str2.split("\r\n");
            Boolean find = false;
            for (int i=0;i<str3.length;i++){
            	
            	if(str3[i].startsWith("// CRASH: "+ pkgname)){
//               	ret[1] = str2.substring(str2.indexOf("// CRASH: "+pkgname)); //截取后翻入字符串数组中。
                	ret[1]+=str3[i];
                	find = true;
            	}
                else{
                	if(find){
                		ret[1]+=str3[i];
                		
                		if(str3[i].equals("// "+"\n")){
                			find = false;
                		}
                		
                	}
                }
            	 
                }
            //写文件，
           	if(ret[1]!=null){
           		writer1.write(ret[1]);
           	}
           	writer1.close();  
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (br != null) {
                try {
                    br.close();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }
	
	

	
	
	public static String Monkey( String DevicesName,String PackageName ,String zscount,String throttle,String anyevent,String touch,String motion,String trackball,String syskeys,String appswitch,String count,String path) throws InterruptedException{
		Runtime runtime1 = Runtime.getRuntime();
		String errorMSG = "";
		 BufferedReader br = null;
         StringBuffer stringBuffer = new StringBuffer();
         String str2 = null;
	    String cmd="adb -s "+DevicesName+" shell monkey -p "+PackageName+  " --ignore-crashes --ignore-timeouts" +" --monitor-native-crashes" +" -s "+zscount+" "+throttle+" "+anyevent+" "
	    		+ touch+" "+motion +" "+trackball +" "+syskeys+" " +appswitch+ " -v-v-v "+count+" > "+path+log.time()+".txt";
	    
//	    System.out.println(cmd);
		try {
			String[] args = new String[]{"cmd","/c",cmd};
			//String[] args = new String[]{"sh","-?/c",command};
			
			Process pro1 = runtime1.exec(args);
			//Process pro = runtime.exec("c://///////.exe");
//			
			br = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
	             if("".equals(line.trim())) continue;

//	                 stringBuffer.append(line+"\n");
	                 System.out.println(line);
	                 if (line.contains("// ")){
//	                	 while((line).contains("// ")){
	                		 stringBuffer.append(line+"\n");
	                		 
//	                	 }
	                	
	                 }
	                 String str1 = stringBuffer.toString();
	                 if(str1.contains("// CRASH: com.jhss.youguu")){
	                	 str2 = str1.substring(str1.indexOf("// CRASH: com.jhss.youguu"));
	                     
	                 }
	             }
			
			
			
			if (pro1.waitFor() != 0) {
	            System.err.println("exit value = " + pro1.exitValue());
	        }
	       
			//检查命令是否失败
			try {
				if(pro1.waitFor()!=0){
					System.err.println("exit value:" + pro1.exitValue());
					JOptionPane.showMessageDialog(new JFrame(), "哥们抱歉，好像出问题了！关掉重试吧！");
				}
			} catch (InterruptedException e) {
				System.err.println();
				e.printStackTrace();
			
				
			}
			
		} catch (IOException e) {
			System.out.println("error Message:"+e.getMessage());
			e.printStackTrace();
		} finally{
			if (str2!=null){
				return str2;
			}
			else return null;
		}
		
		
	  }

	
	public static String getDeviceName() throws IOException{
		String name=null;
	    Runtime runtime = Runtime.getRuntime();
	    Process proc = runtime.exec("adb get-serialno");
	    try {
	        if (proc.waitFor() != 0) {
	            System.err.println("exit value = " + proc.exitValue());
	        }
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	                proc.getInputStream()));
	        StringBuffer stringBuffer = new StringBuffer();
	        String line = null;
	        while ((line = in.readLine()) != null) {
	            stringBuffer.append(line+" ");
	            
	            
	        }
	     //   System.out.println(stringBuffer);
	    String str1=stringBuffer.toString();
//	    System.out.println(str1);
	    name=str1.trim();
	    } catch (InterruptedException e) {
	        System.err.println(e);
	    }finally{
	        try {
	            proc.destroy();
	        } catch (Exception e2) {
	        }
	    }

		return name;
		
	  }
	
	public static ArrayList<String> getDevices(){
		ArrayList<String> devices = new ArrayList<String>();
//		System.out.println(devices.size());
	
		try{
			   
		    Runtime runtime = Runtime.getRuntime();
		    Process proc = runtime.exec("adb devices");
		    try {
		        if (proc.waitFor() != 0) {
		            System.err.println("exit value = " + proc.exitValue());
		        }
		        BufferedReader in = new BufferedReader(new InputStreamReader(
		                proc.getInputStream()));
		        StringBuffer stringBuffer = new StringBuffer();
		        String line = null;
		        while ((line = in.readLine()) != null && line.contains("device")) {
		            stringBuffer.append(line+" ");
		        }
		    String str1=stringBuffer.toString();  
		    
		    //判断是否有设备连接。
		    if(str1.contains("device ")){
//		    	System.out.println("有设备链接");
		    	 String a[] =str1.split("attached ");
				    String b[] = a[1].split("device");
				    //用循环来切割
				    for(int i=0;i<b.length-1;i++){
				    	b[i]=b[i].trim();
				    	devices.add(b[i]);
				    }
		    }
		    else{
		    	
		    	return devices;
		    }
		
		    } catch (InterruptedException e) {
		        System.err.println(e);
		    }finally{
		        try {
		            proc.destroy();
		        } catch (Exception e2) {
		        }
		    }
			}
			catch (Exception ArrayIndexOutOfBoundsException)
			{
				
				System.out.print("请检查设备是否连接");
				
				return devices;
			}
		return devices;
		
	}


}

  

