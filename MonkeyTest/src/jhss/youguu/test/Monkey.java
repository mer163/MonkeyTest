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
	public static void getCrash(String command,String pkgname,JTextArea text)
    {
        BufferedReader br = null;
        StringBuffer totalLog = new StringBuffer();
        StringBuffer crashLog = new StringBuffer();
//        String[] ret = new String[2];
//        String time = Outlog.log.time();
        String path = Monkey_Menu.path;
        Boolean flag = false;
        Boolean success = false;
        double testTime;
        
        if(!(new File(path).isDirectory())){
        	new File(path).mkdir();
       	}
        if(!(new File(path+"Monkey_log/").isDirectory())){
        	new File(path+"Monkey_log/").mkdir();
        }
        if(!(new File(path+"Crash_log/").isDirectory())){
        	new File(path+"Crash_log/").mkdir();
        }
        
        
        
        File file = new File(path + "Monkey_log/" + Outlog.log.time() + ".txt");
       
        File crashFile = new File(path+ "Crash_log/"+Outlog.log.time()+".txt");
        
//        File file = new File("data/local/tmp" + File.separator + time + ".txt");
        
        try {
//       	 String[] args = new String[]{"cmd","/c",command};
            Process p = Runtime.getRuntime().exec(command);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            Writer writer = new OutputStreamWriter(new FileOutputStream(file,
					true), "UTF-8");
            Writer writer1 = new OutputStreamWriter(new FileOutputStream(crashFile,
					true), "UTF-8");
            
            String line = null;
            while ((line = br.readLine()) != null) {
            if("".equals(line.trim())) continue;
            	int fisrt;
            	int end;
//                text.append(line+"\n");
                // 日志写入电脑磁盘中。
                
				writer.write(line);
				writer.write("\r\n");
				totalLog.append(line+"\n");
				//发现崩溃，将flag设置为true，				
                if (line.startsWith("// CRASH: " + pkgname)){
//               	while((line).contains("// ")){
                	text.append("应用："+pkgname+" crash发生，开始捕捉崩溃日志。\n");
                	text.append("--------------------------------------\n");
                	text.append(line+"\n");
               		crashLog.append(line+"\n");	//写入日志
               		
               		writer1.write("应用："+pkgname+" crash发生，开始捕捉崩溃日志。\n"+"--------------------------------------\n");
               		writer1.write(line);
    				writer1.write("\r\n");
               		flag = true;
                }
                else{
                	if(line.equalsIgnoreCase("// Monkey finished")){
                		success = true;
                	}
                }
                if (line.startsWith("## Network stats:")) {
                    fisrt = line.indexOf("=");
                    end = line.indexOf("ms");
                    testTime = Double.valueOf(line.substring(fisrt + 1, end)).doubleValue() / 1000.0D / 60.0D;
                    
                  }
                if(flag){
                	if(line.startsWith("// ")&&!line.equalsIgnoreCase("// Monkey finished")){
                		text.append(line+"\n");
                		crashLog.append(line+"\n");	//写入日志
                		writer1.write(line+"\r\n");
                	}else{
//                		flag = false;	//当日志不是以 "// "起始时则将flag设置为false。
                	}
                }	
                
            }
            
            if(success){
            	if(flag){
            		text.append("本次monkey结束，检测到应用崩溃，请查看日志。\n");
                	writer.write("本次monkey结束，检测到应用崩溃，请查看日志。\n");
            	}else{
            		text.append("本次monkey结束，未发生崩溃，请查看日志。\n");
                	writer.write("本次monkey结束，未发生崩溃，请查看日志。\n");
                	writer1.write("本次monkey结束，未发生崩溃，请查看日志。\n");
            	}
            }else{
            	text.append("monkey发生未知错误，执行失败。\n");
            }
                    
            writer.close();
            writer1.close();
//            ret[0]= totalLog.toString();
//            ret[1]= crashLog.toString();
           
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

  

