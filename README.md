# scheduler_batch

## ๐ก Discriotion
๋์  ๋ฐ์ดํฐ ๋งตํ ํ๋ก๊ทธ๋จ (๋ฐฐ์น) + ์ค์ผ์ฅด๋ง

table_mapping ํ์ด๋ธ์ ๋งตํํ์ด๋ธ ์ ๋ณด๋ค์ด ๋ค์ด์๋ค.\
๋ง์ฝ, member1ํ์ด๋ธ๊ณผ member2 ํ์ด๋ธ์ด ๋งตํ๋์ด์๋ค๊ณ  ๊ฐ์ .\
member1 ํ์ด๋ธ์ ๋ฐ์ดํฐ๊ฐ member2 ํ์ด๋ธ์ \
์กด์ฌํ์ง ์์ผ๋ฉด insert(๋ฑ๋ก) ์์ผ์ฃผ๊ณ ,\
์กด์ฌํ๋ค๋ฉด update (์์ ) ํด์ค๋ค -> primary key๋ก ๋น๊ต\
(member1, member2 ์ฒ๋ผ ์ ์ ์ผ๋ก ๋ฃ์ด์ค ํ์ด๋ธ์ด ์๋๋ผ, table_mapping ํ์ด๋ธ์ ๋งตํ๋ ์ ๋ณด๋ค์ ์ ๋ถ ๋์ ์ผ๋ก ๋ฐฐ์น์ฒ๋ฆฌํด์ค๋ค.)\
์ด๋, ํ๋ฒ์ ์คํ์ผ๋ก ๋๋๋๊ฒ์ด ์๋๋ผ 2์ด๋ง๋ค ๋ฐฐ์น๊ฐ ๋๋๋ก ๋์ ์ผ๋ก ์ค์ผ์ฅด์ ๊ฑธ์ด์ค๋ค.

## ๐ก Environment
Window,
Java springboot,
Gradle,
H2 database, mysql database, mysql workbench
lombok, jdbc


## ๐กprerequisite
- h2 db \
spring.datasource.url=jdbc:h2:tcp://localhost/~/test \
spring.datasource.username=sa \
spring.datasource.password=

![image](https://user-images.githubusercontent.com/77472180/165682729-ab3a8b6e-e969-47d7-9091-a10b6314f85b.png)


- mysql db \
#spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver \
#spring.datasource.url=jdbc:mysql://localhost:3306/test_db?serverTimezone=UTC&characterEncoding=UTF-8 \
#spring.datasource.username=root \
#spring.datasource.password=1234

![image](https://user-images.githubusercontent.com/77472180/165682841-6bccd73d-892c-48e8-9be6-14bcaa4c2377.png) \
![image](https://user-images.githubusercontent.com/77472180/165682888-2356ba23-3dd8-4d22-b66c-fdaf3234733b.png)

<์ฐธ๊ณ > dml & ddl \
create table column_mapping ( \
table_Key varchar(40), \
source_columnname varchar(1000), \
target_columnname varchar(1000), \
is_primarykey bit, \
PRIMARY KEY (table_Key, source_columnname, target_columnname) \
)

CREATE TABLE TABLE_MAPPING ( \
table_Key varchar(40) primary key, \
source_tableName varchar(100), \
target_tableName varchar(100), \
source_conn varchar(1000), \
source_id varchar(100), \
source_password varchar(200), \
target_conn varchar(1000), \
target_id varchar(100), \
target_password varchar(200) \
)

SET @UU=random_uuid(); \
insert into TABLE_MAPPING(TABLE_KEY,SOURCE_TABLENAME ,TARGET_TABLENAME ,SOURCE_CONN ,SOURCE_ID ,SOURCE_PASSWORD ,TARGET_CONN ,TARGET_ID ,TARGET_PASSWORD ) VALUES(@UU,'MEMBER','MEMBER2','jdbc:h2:tcp://localhost/\~/test','sa','','jdbc:h2:tcp://localhost/~/test','sa','')

SET @UU=random_uuid(); \
insert into TABLE_MAPPING(TABLE_KEY,SOURCE_TABLENAME ,TARGET_TABLENAME ,SOURCE_CONN ,SOURCE_ID ,SOURCE_PASSWORD ,TARGET_CONN ,TARGET_ID ,TARGET_PASSWORD ) VALUES(@UU,'MEMBER','MEMBER3','jdbc:h2:tcp://localhost/~/test','sa','','jdbc:mysql://localhost:3306/test_db?serverTimezone=UTC&characterEncoding=UTF-8','root','1234')

SET @UU=random_uuid(); \
insert into TABLE_MAPPING(TABLE_KEY,SOURCE_TABLENAME ,TARGET_TABLENAME ,SOURCE_CONN ,SOURCE_ID ,SOURCE_PASSWORD ,TARGET_CONN ,TARGET_ID ,TARGET_PASSWORD ) VALUES(@UU,'MEMBER3','MEMBER4','jdbc:mysql://localhost:3306/test_db?serverTimezone=UTC&characterEncoding=UTF-8','root','1234','jdbc:mysql://localhost:3306/test_db?serverTimezone=UTC&characterEncoding=UTF-8','root','1234')

SET @UU=random_uuid(); \
insert into TABLE_MAPPING(TABLE_KEY,SOURCE_TABLENAME ,TARGET_TABLENAME ,SOURCE_CONN ,SOURCE_ID ,SOURCE_PASSWORD ,TARGET_CONN ,TARGET_ID ,TARGET_PASSWORD ) VALUES(@UU,'MEMBER4','MEMBER','jdbc:mysql://localhost:3306/test_db?serverTimezone=UTC&characterEncoding=UTF-8','root','1234','jdbc:h2:tcp://localhost/~/test','sa','')

insert into column_mapping values('aaf44063-fb99-4978-b9cd-8dd94acabfe9','MEMBER_NAME','MEMBER_NAME', 0),('aaf44063-fb99-4978-b9cd-8dd94acabfe9','MEMBER_ID','MEMBER_ID',1),('aaf44063-fb99-4978-b9cd-8dd94acabfe9','MONEY','MONEY', 0)

insert into column_mapping values('a894d858-af0b-42bd-9b9d-15857c57f346','MEMBER_NAME','MEMBER_NAME', 0),('a894d858-af0b-42bd-9b9d-15857c57f346','MEMBER_ID','MEMBER_ID',1),('a894d858-af0b-42bd-9b9d-15857c57f346','MONEY','MONEY', 0)

insert into column_mapping values('03932d34-e9d0-431b-ab65-fd80fe47dbec','MEMBER_NAME','M_NAME', 0),('03932d34-e9d0-431b-ab65-fd80fe47dbec','MEMBER_ID','M_ID',1),('03932d34-e9d0-431b-ab65-fd80fe47dbec','MONEY','M_MONEY', 0)

insert into column_mapping values('c588a3d0-e024-450a-bd08-fd76f94634b6','M_NAME','MEMBER_NAME', 0),('c588a3d0-e024-450a-bd08-fd76f94634b6','M_ID','MEMBER_ID',1),('c588a3d0-e024-450a-bd08-fd76f94634b6','M_MONEY','MONEY', 0)


## ๐ก Files
src/main/java/scheduling/dynamicScheduler/controller/AdminController.java \
-> ์ค์ผ์ค๋ฌ ๋์์๊ฐ ์ปจํธ๋กค 

src/main/java/scheduling/dynamicScheduler/controller/StartController.java \
-> ์ฒซ ํ๋ฉด ์ด์ด์ฃผ๋ ์ปจํธ๋กค๋ฌ (index.jsp)

src/main/java/scheduling/dynamicScheduler/dto/ConnectionDTO.java \
-> DBConnection ์ ๋ณด. Url, Id, pw

src/main/java/scheduling/dynamicScheduler/dynamic/DynamicChangeScheduler.java \
-> ์ค์ผ์ค๋ฌ ๋์์๊ฐ ์ปจํธ๋กค

src/main/java/scheduling/dynamicScheduler/repository/TableRepository.java \
-> DB ํ์ด๋ธ ์ ๋ณด select, insert, update

src/main/java/scheduling/dynamicScheduler/service/ScheduleTask.java \
-> ๋ฐฐ์น service ๋จ


