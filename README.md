# API, env 는 개인적으로 공유!!

1. Docker Desktop 설치
    - 로컬에서 Docker 이미지 빌드 및 테스트하려면 Docker 설치가 필요함
    - 설치 링크: https://www.docker.com/products/docker-desktop/

2. gradlew 실행 권한 부여
    - Git에서는 실행 권한이 기본으로 공유되지 않음
    - 실행 권한 없으면 CI에서 `./gradlew` 실행 시 오류 발생
    - 아래 명령어로 권한 부여 후 커밋(GitBash 터미널로 실행):  
        git update-index --chmod=+x backend/gradlew  
        git commit -m "Fix: add executable permission to gradlew"  
        git push origin <브랜치명>  

3. JDK 21 설치 및 환경변수 설정

4. pnpm 설치 (MapBox 사용시 필요)
    - cd frontend
    - pnpm을 사용하는 경우, 다음 명령어로 글로벌 설치 필요:
    npm install -g pnpm
    - 설치 후 의존성 설치:
    pnpm install

* .env 파일 디코에 올리겠음


* ⚠️ gradlew 실행 권한 문제  
    - 위 실행 권한 부여 후에도 가끔 권한이 사라지는 문제가 있음 (Windows 문제)  
    - 그럴 경우 다시 터미널에서 git update-index --chmod=+x backend/gradlew 명령어 입력. (GitBash)

* ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ