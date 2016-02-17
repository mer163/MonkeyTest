#!/system/bin/sh

if [ -f /data/local/tmp/busybox ];then
	export bb="/data/local/tmp/busybox"
else
	echo "No /data/local/tmp/busybox"
	exit
fi

${testresult="/sdcard/monkey"} 2>/dev/null
if [ ! -f $testresult/monkey_log ];then
	mkdir -p $testresult/monkey_log
fi

#参数：
# 方式1 执行case.sh已写好的case：$1时长; $2-用例编号；$3-case参数
# 方式2 按参数执行moneky语句：$1-时长; $2-限制范围；$3-事件比例; $4-monkey log文件名

#添加备注到comments.csv
comment(){
if [ ! -f $testresult/comments.csv ];then
    echo "Date,Tag,Comments,Info" >$testresult/comments.csv
fi
echo "`date +%m-%d" "%H":"%M":"%S`,$1,$2,$3" >>$testresult/comments.csv
}

#等待时间，替换sleep，避免sleep由于设备性能负载高导致等待时间不准。
wait_time(){
local chek_begin=`$bb awk -F. 'NR==1{print $1}' /proc/uptime`
while true;do
	local chek_end=`$bb awk -F. 'NR==1{print $1}' /proc/uptime`
	if [ $((chek_end-chek_begin)) -ge $1 ];then
		break
	else
		sleep 1
	fi
done
}

#锁屏函数$1:0--lock;1--unlock
lock(){
${wm_size_x=`wm size|$bb awk -F" |x" '{print $(NF-1)}'`} 2>/dev/null
${wm_size_y=`wm size|$bb awk -F" |x" '{print $NF}'`} 2>/dev/null
while true;do
	case `dumpsys SurfaceFlinger|$bb grep "|....|"|$bb awk 'BEGIN{r="o"}{if($NF=="com.android.systemui.ImageWallpaper"||$NF=="com.android.systemui.keyguard.leui.KeyguardImageWallpaper")r="l"}END{print NR-1 r}'` in
		"1o")
			local state=0
		;;
		"3l")
			local state=1
		;;
		*)
			local state=2
		;;
	esac
	case $1 in
		0)
			if [ $state -ne 0 ];then
				input keyevent 26
			else
				break
			fi
			sleep 1
		;;
		1)
			if [ $state -eq 0 ];then
				input keyevent 26&&sleep 1&&input swipe $((v/2)) $((4*wm_size_y/5)) $((wm_size_x/2)) $((wm_size_y/5))
			elif [ $state -eq 1 ];then
				input swipe $((wm_size_x/2)) $((4*wm_size_y/5)) $((wm_size_x/2)) $((wm_size_y/5))
			else
				break
			fi
			sleep 1
		;;
	esac
done
}

#等待指定时间，如果不存在com.android.commands.monkey进程，则重新执行monkey语句
run_monkey(){
$bb pkill com.android.commands.monkey
while [ $((`$bb awk -F. '{print $1}' /proc/uptime`-case_start_time)) -lt $1 ];do
	local tmp=`/system/bin/ps`
	local check=`echo "$tmp"|$bb grep -c com.android.commands.monkey`
	if [ $check -eq 0 ];then
		lock 1
		comment "run_monkey" "$2" "Start test: $3 $1""S"
		monkey $2 --throttle 100 --ignore-crashes --monitor-native-crashes --ignore-timeouts --ignore-native-crashes $3 -v -v -v 10000 1>>$testresult/monkey_log/$4.txt 2>&1 &
		sleep 3
		#3秒后未启动记录并重试
		local tmp=`/system/bin/ps`
		if [ `echo "$tmp"|$bb grep -c com.android.commands.monkey` -eq 0 ];then
			comment "run_monkey" "$2" "Error: After 3S no monkey commands!!!"
		fi
	elif [ $check -eq 1 ];then
		#如果锁屏则解锁
		lock 1
		#如果有activity包含"login"则返回
		if [ `dumpsys SurfaceFlinger|$bb grep -c "login"` -ne 0 ];then
			input keyevent 4
		fi
		#如果有webview则返回。
		if [ `dumpsys SurfaceFlinger|$bb grep -c "WebViewUI"` -ne 0  ];then
			input keyevent 4
		fi
		#部分内嵌wap页所在activity,进入后造成无法退出，故进入内嵌wap则直接返回。
		if [`dumpsys SurfaceFlinger|$bb grep -c "ShareableWebViewActivity"` -ne 0 ];then
			input keyevent 4
		fi
		if [`dumpsys SurfaceFlinger|$bb grep -c "RealTradeWebView"` -ne 0 ];then
			input keyevent 4
		fi
	#多于2个则kill
	elif [ $check -gt 1 ];then
		comment "run_monkey" "$2" "Error: $check monkey commands, killed!!!"
		$bb pkill com.android.commands.monkey
	fi
	sleep 1
done
comment "run_monkey" "$2" "Finish test."
$bb pkill com.android.commands.monkey
#执行时间到达后锁屏
lock 0
}

#固定case执行
caselist(){
case "$1" in
	#单包测试
	0)
		case "$3" in
			"com.jhss.youguu")
				run_monkey $2 "-p $3" "--pct-touch 20 --pct-trackball 5 --pct-motion 35 --pct-flip 5 --pct-appswitch 30 --pct-anyevent 5" "case$1_$3"
				wait_time 600
				am force-stop $3
			;;
			"jhss.youguu.finance")
				run_monkey $2 "-p $3" "--pct-touch 20 --pct-trackball 5 --pct-motion 35 --pct-flip 5 --pct-appswitch 30 --pct-anyevent 5" "case$1_$3"
				wait_time 600
				am force-stop $3
			;;
			"com.letv.app.appstore"|"com.letv.games")
				run_monkey $2 "-p $3" "--pct-touch 20 --pct-trackball 5 --pct-motion 35 --pct-flip 5 --pct-appswitch 30 --pct-anyevent 5" "case$1_$3"
				wait_time 600
				am force-stop $3
			;;
			"com.android.launcher3")
				run_monkey $2 "-p $3" "--pct-touch 20 --pct-trackball 10 --pct-motion 50 --pct-flip 5  --pct-appswitch 10 --pct-anyevent 5" "case$1_$3"
				wait_time 600
				am force-stop $3
			;;
			"com.letv.android.quicksearchbox"|"com.letv.android.voiceassistant")
				run_monkey $2 "-p com.android.launcher3 -p $3" "--pct-touch 30 --pct-trackball 10 --pct-motion 45 --pct-flip 5 --pct-appswitch 5 --pct-anyevent 5" "case$1_$3"
				wait_time 600
				am force-stop $3
			;;
			"com.letv.android.client"|"com.letv.android.letvlive")
				run_monkey $2 "-p $3" "--pct-touch 10 --pct-trackball 10 --pct-motion 10 --pct-flip 10 --pct-appswitch 55 --pct-anyevent 5" "case$1_$3"
				wait_time 600
				am force-stop $3
			;;
			"com.letv.lesophoneclient")
				run_monkey $2 "-p $3 -p com.letv.android.client" "--pct-touch 30 --pct-trackball 10 --pct-motion 45 --pct-flip 5 --pct-appswitch 5 --pct-anyevent 5" "case$1_$3"
				wait_time 600
				am force-stop $3
				am force-stop com.letv.android.client
			;;
			"com.android.gallery3d"|"com.android.browser")
				run_monkey $2 "-p $3" "--pct-touch 10 --pct-trackball 10 --pct-motion 10 --pct-flip 5 --pct-appswitch 50 --pct-anyevent 5 --pct-pinchzoom 10" "case$1_$3"
				wait_time 600
				am force-stop $3
			;;
			*)
				run_monkey $2 "-p $3" "--pct-touch 10 --pct-trackball 10 --pct-motion 10 --pct-flip 10 --pct-appswitch 55 --pct-anyevent 5" "case$1_$3"
				wait_time 600
				am force-stop $3
			;;
		esac
	;;
	#多包appswitch 70%
	1)
		local packages=`echo "$3"|$bb awk -F "|" '{for(i=1;i<NF;i++)printf "-p "$i" ";printf "-p "$NF}'`
		run_monkey $2 "$packages" "--pct-touch 5 --pct-trackball 5 --pct-motion 10 --pct-flip 5 --pct-appswitch 70 --pct-anyevent 5" "case$1_appswitch70"
		wait_time 600
	;;
	*)
		comment "caselist" "No case" "$1"
	;;
esac
}

#main
case_start_time=`$bb awk -F. '{print $1}' /proc/uptime`
case $# in
	3)
		caselist $2 $1 "$3"
	;;
	4)
		#10000事件数monkey语句，循环执行，达到指定时间则kill monkey进程
		run_monkey $1 "$2" "$3" "$4"
	;;
	*)
		comment "case" "Args are wrong!!!" "$*"
		exit
	;;
esac
