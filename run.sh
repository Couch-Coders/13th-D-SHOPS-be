# 윈도우에서도 리눅스의 실행권한을 부여하여 저장소에 추가하는 방법
# git add --chmod +x run.sh
# https://hbesthee.tistory.com/1723

# git에 올라 가면 안되는 json 파일을 SECURE_FILE이라는 환경변수에 저장한 후
# secureFile.json파일로 변경하였습니다.
# 이런식으로 git에 공개할 수 없는 파일은 2에서 소개한 환경변수에 저장하여 사용할 수 있습니다.
# 밑에는 gradle java project 실행 스크립트입니다.
# qoddi 기본 스크립트에 ./build/libs/*로 실행하여 jar파일을 정확히 못잡는 버그가 있어 파일명을 제대로 명시해 주었습니다.
# gradle build를 통해 파일을 확인 할 수 있습니다.
# https://www.couchcoding.kr/blogs/couchcoding/%ED%8F%AC%ED%8A%B8%ED%8F%B4%EB%A6%AC%EC%98%A4%EC%9A%A9%20%EB%AC%B4%EB%A3%8C%20%EB%B0%B1%EC%97%94%EB%93%9C%20%EC%84%9C%EB%B2%84%20%EB%A7%8C%EB%93%A4%EA%B8%B0%20(Qoddi)%20-%20Spring%20%EB%B0%B0%ED%8F%AC%20%EC%98%88%EC%A0%9C
# echo ${FIREBASE_SERVICE_ACCOUNT} >> env.json
# sed 's/*/"/g' env.json >> secureFile.json
# 그리고 {application_name} 부분을 실제 앱 이름으로 바꿔주시길 바랍니다. 보통 프로젝트 이름인데 헷갈리면
# https://junghyun100.github.io/Gradle%EC%97%90%EC%84%9C-Jar-%ED%8C%8C%EC%9D%BC-%EC%83%9D%EC%84%B1%ED%95%98%EA%B8%B0/
# java -Dserver.port=$PORT $JAVA_OPTS  -jar ./build/libs/{application_name}-0.0.1-SNAPSHOT.jar
echo ${SECURE_FILE} >> env.json 
sed 's/*/"/g' env.json >> secureFile.json
java -Dserver.port=$PORT $JAVA_OPTS -Dspring.profiles.active=prod -jar ./build/libs/demo-0.0.1-SNAPSHOT.jar
