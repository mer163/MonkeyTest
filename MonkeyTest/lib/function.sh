#!/system/bin/sh

########## 全局变量 ##########
if [ -f /data/local/tmp/busybox ];then
	export bb="/data/local/tmp/busybox"
else
	echo "No /data/local/tmp/busybox"
	exit
fi
if [ -f /data/local/tmp/stop ];then
	$bb rm /data/local/tmp/stop
fi
#测试结果文件夹位置，清除上次结果
export testresult="/data/local/tmp/monkey"
if [ -d $testresult ];then
	$bb rm -r $testresult
fi
mkdir $testresult
#屏幕物理分辨率
export wm_size_x=`wm size|$bb awk -F" |x" '{print $(NF-1)}'`
export wm_size_y=`wm size|$bb awk -F" |x" '{print $NF}'`

########## 功能函数 ##########

#添加备注到comments.csv
comment(){
if [ ! -f $testresult/comments.csv ];then
    echo "Date,Tag,Comments,Info" >$testresult/comments.csv
fi
echo "`date +%m-%d" "%H":"%M":"%S`,$1,$2,$3" >>$testresult/comments.csv
}

#筛查指定log文件的函数
check_log=0
output_error(){
if [ -f $1 ];then
    local times1=`$bb grep -c "ANR in" $1`
	local times2=`$bb grep -c "FATAL EXCEPTION" $1`
	local times3=`$bb grep -c "Build fingerprint" $1`
	#复现问题，额外增加抓取指定log
	if [ $check_log -ne 5 ];then
		if [ `$bb grep -c "StartParsing Read Failed" $1` -ne 0 ];then
			check_log=$((check_log+1))
			mkdir $testresult/check_log
			time_name="`date +%Y%m%d%H%M%S`"
			$bb cp $1 $testresult/check_log/$time_name.log
			screencap $testresult/log/$time_name.png
			bugreport -o $testresult/log/$time_name.bugreport
			$bb tar -zcvf $testresult/log/$time_name.tar.gz -C $testresult/log $time_name.png $time_name.bugreport
			$bb rm $testresult/log/$time_name.png
			$bb rm $testresult/log/$time_name.bugreport
			comment "output_error" "StartParsing Read Failed" "check_log/$time_name.log"
		fi
	fi
	#ANR/FC/Tombstone
	if [ $times1 -ne 0 -o $times2 -ne 0 -o $times3 -ne 0 ];then
	    if [ ! -d $testresult/log ];then
		    mkdir $testresult/log
		fi
		time_name="`date +%Y%m%d%H%M%S`"
	    $bb mv $1 $testresult/log/$time_name.log
		screencap $testresult/log/$time_name.png
		bugreport -o $testresult/log/$time_name.bugreport
		$bb tar -zcvf $testresult/log/$time_name.tar.gz -C $testresult/log $time_name.png $time_name.bugreport
		$bb rm $testresult/log/$time_name.png
		$bb rm $testresult/log/$time_name.bugreport
		local result="Error:"
		if [ $times1 -ne 0 -a ! -z "`ls /data/anr 2>/dev/null`" ];then
			mkdir $testresult/log/$time_name
			$bb mv /data/anr/* $testresult/log/$time_name/
			local result=$result"ANR;"
		fi
		if [ $times2 -ne 0 ];then
			local result=$result"FATAL;"
		fi
		if [ $times3 -ne 0 -a ! -z "`ls /data/tombstones 2>/dev/null`" ];then
			if [ ! -d $testresult/log/$time_name ];then
				mkdir $testresult/log/$time_name
			fi
			$bb mv /data/tombstones/* $testresult/log/$time_name
			local result=$result"tombstones;"
		fi
		comment "output_error" "$result" "$time_name.log"
	else
		$bb rm $1
	fi
fi
}

#创建及处理log函数
c_log(){
local log_f="/data/local/tmp/check_error.log"
if [ -f $log_f ];then
	while true;do
		if [ -f $testresult/check_error.log ];then
			sleep 1
		else
			break
		fi
	done
	local tmp=`$bb ps`
	local pid=`echo "$tmp"|$bb grep "logcat -b main -v time"|$bb awk '{print $1}'`
	if [ ! -z "$pid" ];then
		kill -9 `echo $pid`
	fi
	$bb mv $log_f $testresult/check_error.log
fi
if [ $1 -eq 0 ];then
	logcat -b main -c
	logcat -b main -v time > $log_f &
fi
if [ -f $testresult/check_error.log ];then
	output_error $testresult/check_error.log
fi
}

#间隔$1秒筛查log
check_log_error(){
local check_log_begin=`$bb awk -F. 'NR==1{print $1}' /proc/uptime`
c_log 0
while true;do
	local check_log_end=`$bb awk -F. 'NR==1{print $1}' /proc/uptime`
	if [ $((check_log_end-check_log_begin)) -gt $1 ];then
		local check_log_begin=$chek_end
		c_log 0
	else
		sleep 1
	fi
	if [ -f /data/local/tmp/stop ];then
		c_log 1
		break
	fi
done
}

#锁屏函数$1:0--lock;1--unlock
lock(){
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
				input keyevent 26&&sleep 1&&input swipe $((wm_size_x/2)) $((4*wm_size_y/5)) $((wm_size_x/2)) $((wm_size_y/5))
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

########## monkey function ##########

#长按操作保持对应app显示：$1-长按键值；$2-对应包名；$3-持续时间
longpress(){
local chek_begin=`$bb awk -F. 'NR==1{print $1}' /proc/uptime`
while true;do
	if [ `dumpsys SurfaceFlinger |$bb grep -c "$2"` -eq 0 ];then
		input keyevent --longpress $1
	fi
	local chek_end=`$bb awk -F. 'NR==1{print $1}' /proc/uptime`
	if [ $((chek_end-chek_begin)) -ge $3 ];then
		break
	else
		sleep 1
	fi
done
}

#指定间隔交替长按主页键/返回键：$1-主页键对应包名；$2-返回键对应包名；$3-间隔；$4-持续时间
longkey(){
local l_key=3
local chek_begin=`$bb awk -F. 'NR==1{print $1}' /proc/uptime`
local package=$1
while true;do
	if [ `dumpsys SurfaceFlinger |$bb grep -c "$package"` -eq 0 ];then
		input keyevent --longpress $l_key
	fi
	sleep $1
	local chek_end=`$bb awk -F. 'NR==1{print $1}' /proc/uptime`
	if [ $((chek_end-chek_begin)) -ge $4 ];then
		break
	else
		sleep $3
	fi
	case $l_key in
		3)
			local l_key=4
			local package=$2
		;;
		4)
			local l_key=3
			local package=$1
		;;
	esac
done
}

#确保显示指定包界面：
#方式1：两个参数 $1-包名；$2-持续时间；通过 dumpsys activity $1 获取ACTIVITY
#方式2：三个参数 $1-包名；$2-持续时间；$3-指定activity；多个则|分隔，依次逐个启动
longam(){
case $# in 
	2)
		local activity=`dumpsys activity $1|$bb grep ACTIVITY|$bb awk 'NR==1{print $2}'`
	;;
	3)
		local activity="$3"
	;;
esac
local a_loops=`echo "$3"|$bb awk -F"|" '{print NF}'`
if [ ! -z "$activity" ];then
	local chek_begin=`$bb awk -F. 'NR==1{print $1}' /proc/uptime`
	local i=1
	while true;do
		if [ `dumpsys SurfaceFlinger |$bb grep -c "$1"` -eq 0 ];then
			if [ $a_loops -eq 1 ];then
				local activity="$3"
			elif [ $a_loops -gt 1 ];then
				local activity="`echo "$3"|$bb awk -F"|" -v P=$i '{print $i}'`"
				if [ $i -le $a_loops ];then
					local i=$((i+1))
				else
					local i=1
				fi
			fi
			local am_start=`am start $activity 2>&1`
			if [ `echo "$am_start"|$bb grep -c "Error"` -ne 0 ];then
				comment "longam" "am_error" "$am_start"
			fi
		fi
		local chek_end=`$bb awk -F. 'NR==1{print $1}' /proc/uptime`
		if [ $((chek_end-chek_begin)) -ge $2 ];then
			break
		else
			sleep 1
		fi
	done
else
	comment "longam" "No activity" "$1"
	wait_time $1
fi
}

monkey_test(){
case $# in
	3)
		sh /data/local/tmp/case.sh $1 $2 "$3" &
	;;
	4)
		sh /data/local/tmp/case.sh $1 "$2" "$3" "$4" &
	;;
	*)
		echo "Args are wrong!!!"
		exit
	;;
esac
}

#超时判定，超时则kill“case.sh”子进程：$1-等待时常；$2-额外等待超时判定的时间
monkey_timeout(){
wait_time $(($1+610))
local tmp=`$bb ps`
if [ `echo "$tmp"|$bb grep -c case.sh` -ne 0 ];then
	$bb pkill com.android.commands.monkey
	local pid=`echo "$tmp"|$bb grep "case.sh"|$bb awk '{print $1}'`
	if [ ! -z "$pid" ];then
		kill -9 `echo $pid`
	fi
fi
}
