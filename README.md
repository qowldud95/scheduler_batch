# scheduler_batch

💡 Discriotion
동적 데이터 맵핑 프로그램 (배치) + 스케쥴링

예) member1 테이블과 member2 테이블이 맵핑되어있다.
만약, member1 테이블의 데이터가 member2 테이블에 존재하지 않으면 insert(등록) 시켜주고,
존재한다면 update (수정) 해준다 -> primary key로 비교
이때, 한번의 실행으로 끝나는것이 아니라 2초마다 배치가 돌도록 스케쥴을 걸어준다.


💡 Environment
Window,
Java springboot,
Gradle,
H2 database, mysql database, mysql workbench
lombok, jdbc


💡prerequisite
작성한 코드를 실행하기 전에 db setting을 해준다
H2 database
URL = 
ID = 
PW =

Mysql database
URL = 
ID = 
PW =

dml ddl

💡 Files
해당 파일이 어떤 역할을 하는지 (전반적인 맥락 파악)

💡Usage Example
작성한 코드를 어떻게 실행해야하는지

