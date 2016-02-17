#!/system/bin/sh

########## 初始设置 ##########

#加载全局变量和函数
source /data/local/tmp/function.sh
#子进程间隔10秒筛查log
check_log_error 10 &

########## 完成设置 ##########


########## monkey测试逻辑 ##########

# 执行实例：
# 方式1 执行case.sh已写好的case：$1时长?秒; $2-用例编号；$3-用例参数
# monkey_test 60 0 "com.letv.app.appstore"
# monkey_timeout 60 
# 方式2 按参数执行moneky语句：$1-时长; $2-限制范围；$3-事件比例; $4-monkey log文件名
# monkey_test 60 "-p com.letv.games" "--pct-touch 20 --pct-trackball 5 --pct-motion 35 --pct-flip 5 --pct-appswitch 30 --pct-anyevent 5" "letv_games"
# monkey_timeout 60 

#系统测试 Memory Leak
#理财测试 2小时。
monkey_test 7200 0 "jhss.youguu.finance"
monkey_timeout 7200
#炒股测试2小时、
monkey_test 7200 0 "com.jhss.youguu"
monkey_timeout 7200
#store测试后，基于已安装一些app环境下继续测试各重点app，1小时
#monkey_test 3600 0 "com.letv.games"
#monkey_timeout 3600
#monkey_test 3600 0 "com.android.launcher3"
#monkey_timeout 3600
#monkey_test 3600 0 "com.letv.android.quicksearchbox"
##将长按主页键对应操作修改为万象搜索，如果不显示万象搜索则长按唤起
#longpress 3 "com.letv.android.quicksearchbox" 3600
#Monkey测试后锁屏10分钟
monkey_timeout 0
monkey_test 3600 0 "com.jhss.youguu"
##将长按返回键对应操作修改为语音，如果不显示语音则长按唤起
#longpress 4 "com.letv.android.voiceassistant" 3600
#Monkey测试后锁屏10分钟
monkey_timeout 0
monkey_test 3600 0 "jhss.youguu.finance"
monkey_timeout 3600
monkey_test 3600 0 "com.jhss.youguu"
monkey_timeout 3600
monkey_test 3600 0 "jhss.youguu.finance"
##保持am唤起主activity，间隔1S如不显示则唤起，由于“dumpsys activity 包名”获取不到乐搜activity传参
#longam "com.letv.lesophoneclient" 3600 "com.letv.lesophoneclient/.ui.MainActivity"
#monkey_timeout 0
#monkey_test 3600 0 "com.android.music"
#monkey_timeout 3600
#monkey_test 3600 0 "com.lesports.glivesports"
#monkey_timeout 3600
#onkey_test 3600 0 "com.letv.android.wallpaperonline"
#monkey_timeout 3600
#monkey_test 3600 0 "com.letv.letvshop"
#monkey_timeout 3600
#monkey_test 3600 0 "com.letv.bbs"
#monkey_timeout 3600
#monkey_test 3600 0 "com.android.gallery3d"
#monkey_timeout 3600
#monkey_test 3600 0 "com.android.browser"
#monkey_timeout 3600
#monkey_test 3600 0 "com.letv.android.filemanager"
#monkey_timeout 3600
#monkey_test 3600 0 "com.android.calendar"
#onkey_timeout 3600
#monkey_test 3600 0 "sina.mobile.tianqitongletv"
#monkey_timeout 3600
#monkey_test 3600 0 "com.letv.android.note"
#monkey_timeout 3600
#monkey_test 3600 0 "com.android.deskclock"
#monkey_timeout 3600
#monkey_test 3600 0 "com.baidu.BaiduMap"
#monkey_timeout 3600
##切换百度输入法
#ime disable com.sohu.inputmethod.sogou.leshi/.SogouIME
#ime enable com.baidu.input_letv/.ImeService
##强化app间切换测试70%，5小时
All_package="com.android.launcher3|com.letv.android.quicksearchbox|com.letv.android.voiceassistant|com.letv.lesophoneclient|com.letv.android.client|com.letv.android.letvlive|com.letv.games|com.android.music|com.lesports.glivesports|com.letv.android.wallpaperonline|com.letv.letvshop|com.letv.bbs|com.android.gallery3d|com.android.browser|com.letv.android.filemanager|com.android.calendar|sina.mobile.tianqitongletv|com.letv.android.note|com.android.deskclock|com.baidu.BaiduMap"
monkey_test 18000 1 "$All_package"
#长按主页键对应万象搜索；长按返回键对应语音；间隔10秒，持续5小时，交替唤醒
#longkey "com.letv.android.quicksearchbox" "com.letv.android.voiceassistant" 10 18000
monkey_timeout 0

########## monkey逻辑结束 ##########


########## 测试结束确保子进程已退出 ##########
PID=`/system/bin/ps |grep $$|$bb awk -v pid=$$ '{if($2!=pid)print $2}'`
kill $PID
#通知停止监控
echo "`date +%m-%d" "%H":"%M":"%S` stop monitor scripts." > /data/local/tmp/stop
