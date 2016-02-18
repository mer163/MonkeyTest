package jhss.youguu.test;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Utils.ReUtils;
import Utils.ShellUtils;
import jhss.youguu.test.Monkey;
import jhss.youguu.test.Monkey_Menu;

public class Monkey_Menu extends JFrame
implements ActionListener{
	/**
 * 
 */
private static final long serialVersionUID = 1L;
	public static String text;
	public static String[] monkeyResult;
	public int exitCode = 1;  	// 退出码，1为执行monkey，0为不执行。
	public JPanel frame1 = new JPanel();
	ArrayList<String> devices = Monkey.getDevices();
	String DevicesName;
	static String path;
	static String time =Outlog.log.time();
	public static ArrayList<String> thirdApps;
	
	public String monkey_case= "./lib/case.sh";
	public String app = "./lib/app.sh";
	public String function = "./lib/function.sh";
	
	static Boolean screenLocked = false;  //屏幕是否锁定，false为点亮状态，true为屏幕已锁定。
   
	public Monkey_Menu() throws IOException{
		
	
		frame1.setLayout(null);
		
		thirdApps = Monkey.getDevices();
	    JLabel lable0 = new JLabel("设备名称:");
	    frame1.add(lable0);
	    lable0.setBounds(30, 30, 75, 25);
 
//	    final JTextField JText0 = new JTextField("");   
//	    JText0.setText(new Monkey().getDeviceName());
//	    frame1.add(JText0);
//	    JText0.setBounds(90, 30, 180, 30);
	  
	    final JComboBox<String> box0 = new JComboBox(); 
//	    System.out.println(devices.size());
	    if(devices.size()==0){
	    	box0.addItem("设备未连接，请连接设备！");
	    }
	    else{
	    for(int i=0;i<devices.size();i++){
	    	box0.addItem(devices.get(i));
	    }
	    }
//        box0.addItem("0");  
//        box0.addItem("0.1秒");  
//        box0.addItem("0.5秒"); 
//        box0.addItem("1秒");
//        box0.addItem("2秒"); 
        box0.setBounds(90, 30, 180, 30);  
        frame1.add(box0); 
	    
	    
	    

	    JLabel lable = new JLabel("应用:");
	    frame1.add(lable);
	    lable.setBounds(400, 30, 45, 25);
	    
	    final JComboBox<String> apps = new JComboBox<String>();
	   
    	apps.addItem("优顾理财");  
    	apps.addItem("优顾炒股");
	   
	    apps.setBounds(460, 30, 180, 30);
	    frame1.add(apps);
	    
	    //注释掉以前的JtextField
//	    final JTextField JText = new JTextField("com.jhss.youguu");
//	    frame1.add(JText);
////	    JText.setBounds(360, 30, 180, 30);
//	    JText.setBounds(460, 30, 180, 30);
	    
		 JLabel lable1 = new JLabel("事件数量:");
		 frame1.add(lable1);
		 lable1.setBounds(30, 80, 75, 25);
		    
	     final JTextField JText1 = new JTextField("500000");
		 frame1.add(JText1);
		 JText1.setBounds(90, 80, 180, 30);
		 

		 JLabel lable2 = new JLabel("种子数量:");
		 frame1.add(lable2);
		 lable2.setBounds(400, 80,75, 25);
		    
	     final JTextField JText2 = new JTextField("500");
		 frame1.add(JText2);
		 JText2.setBounds(460, 80, 180, 30);
		 
		 
		 
		 JLabel lable6 = new JLabel("随机事件：");
		 frame1.add(lable6);
		 lable6.setBounds(30,150, 75, 25); 
	     final JTextField JText6 = new JTextField("15");
		 frame1.add(JText6);
		 JText6.setBounds(100, 150, 100, 30);
		 
		 

		 JLabel lable4 = new JLabel("点击事件：");
		 frame1.add(lable4);
		 lable4.setBounds(240,150, 75, 25); 
	     final JTextField JText4 = new JTextField("15");
		 frame1.add(JText4);
		 JText4.setBounds(310, 150, 100, 30);
		 

		 JLabel lable5 = new JLabel("滑动事件：");
		 frame1.add(lable5);
		 lable5.setBounds(450,150, 75, 25);
		    
	     final JTextField JText5 = new JTextField("15");
		 frame1.add(JText5);
		 JText5.setBounds(540, 150, 100, 30);
		 
		 
		 JLabel lable7 = new JLabel("轨迹球事件：");
		 frame1.add(lable7);
		 lable7.setBounds(20,220, 85, 25); 
	     final JTextField JText7 = new JTextField("15");
		 frame1.add(JText7);
		 JText7.setBounds(100, 220, 100, 30);
		 
		 

		 JLabel lable8 = new JLabel("导航事件：");
		 frame1.add(lable8);
		 lable8.setBounds(240,220, 75, 25); 
	     final JTextField JText8 = new JTextField("15");
		 frame1.add(JText8);
		 JText8.setBounds(310, 220, 100, 30);
		 

		 JLabel lable9 = new JLabel("页面切换事件：");
		 frame1.add(lable9);
		 lable9.setBounds(440,220, 105, 25);
		    
	     final JTextField JText9 = new JTextField("15");
		 frame1.add(JText9);
		 JText9.setBounds(540, 220, 100, 30);
		 
		  
			

		 JLabel lable11 = new JLabel("日志路径：");
		 frame1.add(lable11);
		 lable11.setBounds(20, 325, 200, 35);   
	     final JTextField JText11 = new JTextField("D:/log/Monkey_log/");
		 frame1.add(JText11);
		 JText11.setBounds(90, 325, 200, 35); 
		 
		final JButton button1 = new JButton("开始测试");		
//		button1.setBounds(400, 325, 200, 35);     //原位置
		button1.setBounds(440, 350, 200, 35);
	    frame1.add(button1);
	
	    JLabel counts = new JLabel("循环次数:");
		frame1.add(counts);
		counts.setBounds(250,372, 75, 25); 
	    final JTextField count_num = new JTextField("1");
		frame1.add(count_num);
		
		count_num.setBounds(320, 372, 100, 30);
	    
        //初始化下拉列表框  
      /*  final JComboBox<String> box = new JComboBox();  
        box.addItem("1次");  
        box.addItem("5次"); 
        box.addItem("10次");
        box.addItem("20次"); 
        box.addItem("50次"); 
        box.setBounds(320, 135, 55, 25);  
        frame1.add(box); */
	    JLabel lable13 = new JLabel("延时:");
		frame1.add(lable13);
	    lable13.setBounds(300, 325, 200, 35);   
        //初始化下拉列表框  
        final JComboBox<String> box = new JComboBox();  
        box.addItem("0");  
        box.addItem("0.1秒");  
        box.addItem("0.5秒"); 
        box.addItem("1秒");
        box.addItem("2秒"); 
        box.setBounds(340, 325, 75, 35);  
        frame1.add(box); 
        box.setSelectedIndex(2);;
        
        //显示
        final JTextArea txt = new JTextArea ("monkey运行日志："+"\n");
        JScrollPane scroll = new JScrollPane(txt); 
        scroll.setHorizontalScrollBarPolicy( 
        		JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy( 
        		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(30,420,640,452);     
        //textArea.setColumns(1);
        //textArea.setRows(10);
       // txt.setTabSize (30);
        txt.setLineWrap(true);	
        txt.setWrapStyleWord(true); 
        txt.setBounds(30,420,640,452);
        txt.setEditable(false);
        txt.setCaretPosition(8);
        
//        add(txt);
//        frame.add(new JScrollPane(txt));
//        frame.add(txt);
       frame1.add(scroll);
        
//      
//        box0.addItemListener(new ItemListener() {
//			
//			@Override
//			public void itemStateChanged(ItemEvent e) {
//				devices = Monkey.getDevices(); //重新获取设备列表
//				if(devices.size()==0){
//					box0.addItem("设备未连接，请连接设备！");
//				}
//				else{
//					for(int i=0;i<devices.size();i++){
//				    	box0.addItem(devices.get(i));
//				    }
//				}
//				
//			}
//		});
	    
	    
		button1.addActionListener(new ActionListener(){//匿名类实现ActionListener接口
			public void actionPerformed(ActionEvent e){	
				new Thread(new Runnable(){
		            public void run() {
		        		try {
		        			
							String anyevent;
							String touch;
							String motion;
							String trackball;
							String syskeys;
							String appswitch;
							String throttle = null ;
			//				JText0.setText(new Monkey().getDeviceName());
			//				String DevicesName=JText0.getText();
							DevicesName =box0.getSelectedItem().toString();  //当前选择的设备名称
							
							String packageName = null;
							if(apps.getSelectedItem().toString()=="优顾炒股"){
								packageName = "com.jhss.youguu";
							}else if (apps.getSelectedItem().toString()=="优顾理财") {
								packageName = "jhss.youguu.finance";
							}
							String zscount=JText2.getText();
							if(JText6.getText().isEmpty())
							{
							   anyevent=JText6.getText();
							}
							else
							{
								 anyevent=" --pct-anyevent "+JText6.getText();
							}
							
							if(JText4.getText().isEmpty())
							{
								touch=JText4.getText();
							}
							else
							{
								 touch=" --pct-touch "+JText4.getText();
							}
					
							if(JText5.getText().isEmpty())
							{
								motion=JText5.getText();
							}
							else
							{
								motion=" --pct-motion "+JText5.getText();
							}
			
							if(JText7.getText().isEmpty())
							{
								trackball=JText7.getText();
							}
							else
							{
								trackball=" --pct-trackball "+JText7.getText();
							}
					
							if(JText8.getText().isEmpty())
							{
								syskeys=JText8.getText();
							}
							else
							{
								syskeys=" --pct-syskeys "+JText8.getText();
							}
							if(JText9.getText().isEmpty())
							{
								appswitch=JText9.getText();
							}
							else
							{
								appswitch=" --pct-appswitch "+JText9.getText();
							}
							
							if(box.getSelectedItem().equals("0"))
							{
								throttle="";
							}
							if(box.getSelectedItem().equals("0.1秒"))
							{
								throttle="--throttle "+100;
							}
							if(box.getSelectedItem().equals("0.5秒"))
							{
								throttle="--throttle "+500;
							}
							if(box.getSelectedItem().equals("1秒"))
							{
								throttle="--throttle "+1000;
							}
							if(box.getSelectedItem().equals("2秒"))
							{
								throttle="--throttle "+2000;
							}
							//设备数为0时弹出提示框。
							if(devices.size()==0){
								JOptionPane.showMessageDialog(frame1.getRootPane(), "请选择设备", "系统信息", JOptionPane.WARNING_MESSAGE);
							}	
							else if (Monkey.isMonkeyRunning(DevicesName)){	//检查当前所选择设备中monkey是否运行中，如果已运行则弹出提示框。
			    				JOptionPane.showMessageDialog(frame1.getRootPane(),
			    						"monkey已经开始运行了!", "系统信息", JOptionPane.WARNING_MESSAGE);		//警告提示框。
			    			}
							// monkey未运行，则执行下列内容。
							else{
								String count=JText1.getText();
							    path =JText11.getText();
							    int counts = Integer.parseInt(count_num.getText());	//循环次数
							    txt.setText("monkey运行日志：\n启动时间："+Outlog.log.time()+"\n");		//默认显示内容。
							    startListen(); //监听屏幕是否锁定，弱锁定则启动解锁屏幕程序。
							
							   //根据循环次数执行monkey循环。
								for(int i=1;i<=counts;i++){
			//					    	text = Monkey.Monkey(DevicesName, PackageName, zscount,throttle,anyevent, touch, motion, trackball, syskeys,appswitch, count,path);
									if(exitCode==1){
										String cmd="adb -s "+DevicesName+" shell monkey -p "+packageName+ 
								    			" --ignore-crashes --ignore-timeouts" +" --monitor-native-crashes"
								    			+ " -s "+zscount+" "+throttle+" "+anyevent+" "+touch+" "
								    			+motion +" "+trackball +" "+syskeys+" " +appswitch+" -v-v-v "+count;
			//					    			+" > "+path+log.time()+".txt";
									
								    	txt.append("---------------------------\n总共循环"+counts+"次，第"+i+"次循环monkey\n");
								    	System.out.println("start---------------------");
								    	
								    	monkeyResult = Monkey.getCrash(cmd,packageName,txt);
								    	goback();
			//					    	Outlog.log.writelogs("D:/log/Monkey_log",time,"monkey运行日志：\n总共循环"+counts+"次，第"+i+"次循环monkey\n"+monkey[0]);
								    	if(monkeyResult[1]!=null){
								    		Outlog.log.writelogs("D:/log/Crash_log",DevicesName+time,"应用crash发生：\n发生时间：\n"+time+"设备名称："+DevicesName+"\n"+monkeyResult[1]); //crash发生后写入日志文件。
								    	}else{
								    		System.out.println("执行完成，请在日志中查看详情.");
								    		}
									}
								    
							    }
							    
								
								button1.setBackground(Color.LIGHT_GRAY);
							}
							
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        }).start();
			}});
		
	
		setTitle("Monkey-----jhss_peter");
		setAutoRequestFocus(true);
//		setBounds(0, 150, 635, 408);
		setBounds(0, 150, 740, 930);
		add(frame1);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        try {
					exit();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		      }});
		
		
	}
	
	/**
	 *  执行返回操作。
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void goback() throws IOException, InterruptedException{
		while(true){
			Check check = new Check();
			Thread ck = new Thread(check);
			ck.start();
			Thread.sleep(3000);
		}
	}
	
	/**
	 * 检查当前activity如果为webview则执行点击返回按键。
	 * @author Administrator
	 *
	 */
	class Check extends Thread{
		
		public void run() {
			try {
					
				if(Monkey.isMonkeyRunning(DevicesName)){
					String currentActivity = getFocusedPackageAndActivity();
					
					if(currentActivity.contains("WebViewUI")||currentActivity.contains("ShareableWebViewActivity")||currentActivity.contains("RealTradeWebView")){
						Utils.ShellUtils.cmd("adb shell input keyevent 4");
						System.out.println("执行返回");
					}

				}
			} catch (IOException | InterruptedException | IndexOutOfBoundsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 *  返回当前界面的activit
	 * @return
	 */
	public String getFocusedPackageAndActivity() {
		Pattern pattern = Pattern.compile("([a-zA-Z0-9.]+/.[a-zA-Z0-9.]+)");
		
			
		Process ps = Utils.ShellUtils.shell("dumpsys window w | grep \\/ | grep name=",DevicesName);

		ArrayList<String> component = Utils.ReUtils.matchString(pattern,
		Utils.ShellUtils.getShellOut(ps));

		return component.get(0);
	}
	
		
		
	
	/**
	 * 开启屏幕监听程序，1分钟执行一次，判断屏幕是否锁定，若锁定则执行解锁。
	 */
	public static void startListen(){
		Thread listener = new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(isScreenLocked()){
					try {
						unlock();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(1000*60);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //休息一分钟。
			}
		});
	    listener.start();
	}
	
	/**
	 * 判断屏幕是否锁定
	 * @return
	 */
	public static Boolean isScreenLocked(){
		
		
		return true;
	}

	/**
	 * 执行解锁程序。
	 * 前提条件，必须安装io.appium.unlock 程序。
	 * @throws IOException 
	 */
	public static void unlock() throws IOException{			
		
		// collect the instrumentation bundle results using instrumentation test
		// should work even though no tests will actually be run
		
		
	   try{
		    Runtime runtime = Runtime.getRuntime();
		    Process proc = runtime.exec("adb shell am start io.appium.unlock/.Unlock");
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
		       
             
		    } 

        	catch (Exception StringIndexOutOfBoundsException){
        		}
        	}finally{
        		
        	}
        	
	    
	  }
	



	public void actionPerformed(ActionEvent e){

	 
	}

	 public void exit() throws IOException, InterruptedException {
		 exitCode =0;
		 if(Monkey.isMonkeyRunning(DevicesName)){
			 Monkey.killMonkey(DevicesName, Monkey.getMonkeyPid(DevicesName));
		 }
		 setVisible(false);

	 }
		

	
	
	
public static void Monkey() throws Exception{


	Thread j= new Thread();	
	j.start();
	Monkey_Menu m=new Monkey_Menu();
	//m.setState(JFrame.ICONIFIED); 
	m.setVisible(true);
	
	
	}

public static void main(String []args) throws Throwable{
	
	Monkey();
}



}