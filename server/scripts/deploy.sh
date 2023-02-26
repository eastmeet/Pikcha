#!/bin/bash
# 빌드 파일의 이름이 콘텐츠와 다르다면 다음 줄의 .jar 파일 이름을 수정하시기 바랍니다.
BUILD_JAR=$(ls /home/ubuntu/action/server/build/libs/project-0.0.1-SNAPSHOT.jar)
JAR_NAME=$(basename $BUILD_JAR)

echo "> 현재 시간: $(date)" >> /home/ubuntu/action/deploy.log

echo "> build 파일명: $JAR_NAME" >> /home/ubuntu/action/deploy.log

echo "> 현재 실행중인 애플리케이션 pid 확인" >> /home/ubuntu/action/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ubuntu/action/deploy.log
else
  echo "> kill -9 $CURRENT_PID" >> /home/ubuntu/action/deploy.log
  sudo kill -9 $CURRENT_PID
  sleep 5
fi

echo "> DEPLOY_JAR 배포"    >> /home/ubuntu/action/deploy.log
cd /home/ubuntu/action/server/build/libs

list=$(ls -al)
echo "> 내역: $list"    >> /home/ubuntu/action/deploy.log
sudo chmod 755 project-0.0.1-SNAPSHOT.jar
# nohup java -jar project-0.0.1-SNAPSHOT.jar &

nohup sudo java -jar $JAR_NAME >> /home/ubuntu/action/server/deploy.log 2>/home/ubuntu/action/server/deploy_err.log &

CUR=$(pwd)
echo "> DEPLOY_JAR 배포완료: $CUR"    >> /home/ubuntu/action/deploy.log

