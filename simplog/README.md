# Blog API

게시글을 작성하고, 다른 사용자의 게시글에 댓글을 작성할 수 있는 블로그 API 서버입니다.

* [Blog API Docs v1.0.0](https://shorturl.at/sUXZ1)

## Tech Stacks

<div align="center">
    <div>
    <img alt="Java17" src="https://img.shields.io/badge/Java17-000000?style=for-the-badge&logo=OpenJDK&logoColor=white">
    <img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">
    </div>
    <div>
    <img alt="Spring Security" src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
    <img alt="JWT" src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON Web Tokens&logoColor=white">
    <img alt="Spring Data JPA" src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=Hibernate&logoColor=white">
    <img alt="QueryDSL" src="https://img.shields.io/badge/QueryDSL-000000?style=for-the-badge&logo=Hibernate&logoColor=white">
    </div>
    <div>
    <img alt="MariaDB" src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB&logoColor=white">
    <img alt="Redis" src ="https://img.shields.io/badge/Redis-DC382D?&style=for-the-badge&logo=redis&logoColor=white"/>
    </div>
    <div>
    <img alt="GitHub Actions" src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white" >
    <img alt="JUnit5" src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white" >
    <img alt="Gradle" src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white" >
    <img alt="Docker" src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white" >
    </div>
    <div>
    <img alt="AWS EC2" src="https://img.shields.io/badge/AWS EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white" >
    <img alt="AWS RDS" src="https://img.shields.io/badge/AWS RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white" >
    </div>
</div>

## Design

### Hexagonal Architecture

![hexagonal-architecture](https://user-images.githubusercontent.com/67671991/204727244-7aa8c148-5265-4945-888b-c58015c71be7.png)
<p align="center"><em>Post Domain v0.0.1</em></p>

### System Infrastructure

...

### CI/CD Pipeline

...

## Use case

### User

* 사용자는 소셜 인증(`Google`)을 통해 회원가입 할 수 있다.
* 사용자는 회원의 프로필을 조회할 수 있다.
* 사용자는 게시글을 조회할 수 있다.
* 사용자는 게시글과 관련 댓글을 조회할 수 있다.

### Member

* 회원은 소셜 인증(`Google`)을 통해 로그인할 수 있다.
* 회원은 로그인한 경우 로그아웃할 수 있다.
* 회원은 회원탈퇴할 수 있다.
* 회원은 자신의 프로필을 조회할 수 있다.
* 회원은 자신의 프로필을 수정할 수 있다.

### Post Author

* 게시글 작성자는 자신의 게시글을 수정할 수 있다.
* 게시글 작성자는 자신의 게시글을 삭제할 수 있다.

### Comment Author

* 댓글 작성자는 자신의 댓글을 수정할 수 있다.
* 댓글 작성자는 자신의 댓글을 삭제할 수 있다.

### Admin (미구현)

* ~~관리자는 게시글을 비활성화 수 있다.~~
* ~~관리자는 댓글을 비활성화할 수 있다.~~
* ~~관리자는 사용자를 비활성화할 수 있다.~~

## Convention

### Code Style

* [Google Code Style](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml)

### Commit Message

* [Udacity Commit Message Style Guide](https://udacity.github.io/git-styleguide/)
