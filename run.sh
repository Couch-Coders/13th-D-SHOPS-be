# 윈도우에서도 리눅스의 실행권한을 부여하여 저장소에 추가하는 방법
# git add --chmod +x run.sh
# https://hbesthee.tistory.com/1723
# 그리고 {application_name} 부분을 실제 앱 이름으로 바꿔주시길 바랍니다. 보통 프로젝트 이름인데 헷갈리면
# https://junghyun100.github.io/Gradle%EC%97%90%EC%84%9C-Jar-%ED%8C%8C%EC%9D%BC-%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0/
# java -Dserver.port=$PORT $JAVA_OPTS  -jar ./build/libs/{application_name}-0.0.1-SNAPSHOT.jar
java -Dserver.port=$PORT $JAVA_OPTS  -jar ./build/libs/demo-0.0.1-SNAPSHOT.jar