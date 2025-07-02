# API, env 는 개인적으로 공유!!

1. Docker Desktop 설치 (필수)
- 로컬에서 Docker 이미지 빌드 및 테스트하려면 Docker 설치가 필요함
- 설치 링크: https://www.docker.com/products/docker-desktop/

2. gradlew 실행 권한 부여 (필수)
- Git에서는 실행 권한이 기본으로 공유되지 않음
- 실행 권한 없으면 CI에서 `./gradlew` 실행 시 오류 발생
- 아래 명령어로 권한 부여 후 커밋:
  git update-index --chmod=+x backend/gradlew
  git commit -m "Fix: add executable permission to gradlew"
  git push origin <브랜치명>  

3. JDK 21 설치

✅ pnpm 설치 (프론트엔드 개발 시 필수)
- 프로젝트에서 pnpm을 사용하는 경우, 다음 명령어로 글로벌 설치 필요:
  npm install -g pnpm
- 설치 후 의존성 설치:
  pnpm install

* .env 파일 디코에 올리겠음

* ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ