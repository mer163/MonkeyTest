#!/system/bin/sh

########## 初始设置 ##########

#加载全局变量和函数
source /data/local/tmp/function.sh
#子进程间隔10秒筛查log
#check_log_error 10 &

########## 完成设置 ##########


########## monkey测试逻辑 ##########

# 执行实例：
# 方式1 执行case.sh已写好的case：$1时长?秒; $2-用例编号；$3-用例参数
# monkey_test 60 0 "com.letv.app.appstore"
# monkey_timeout 60 
# 方式2 按参数执行moneky语句：$1-时长; $2-限制范围；$3-事件比例; $4-monkey log文件名
# monkey_test 60 "-p com.letv.games" "--pct-touch 20 --pct-trackball 5 --pct-motion 35 --pct-flip 5 --pct-appswitch 30 --pct-anyevent 5" "letv_games"
# monkey_timeout 60 

echo "测试炒股两个小时。"
monkey_test 7200 0 "com.jhss.youguu"
##保持am唤起主activity，间隔1S如不显示则唤起
longam "com.jhss.youguu" 7200 "com.jhss.youguu/.SplashActivity"
monkey_timeout 0

echo "测试理财两个小时。"
monkey_test 7200 0 "com.jhss.youguu"
##保持am唤起主activity，间隔1S如不显示则唤起
longam "jhss.yhouguu.finance" 7200 "com.jhss.youguu/.ui.DesktopActivity"
monkey_timeout 0

########## monkey逻辑结束 ##########


########## 测试结束确保子进程已退出 ##########
PID=`/system/bin/ps |grep $$|$bb awk -v pid=$$ '{if($2!=pid)print $2}'`
kill $PID
#通知停止监控
echo "`date +%m-%d" "%H":"%M":"%S` stop monitor scripts." > /data/local/tmp/stop
