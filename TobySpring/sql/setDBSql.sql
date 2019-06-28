CREATE SCHEMA `spring_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ; 
CREATE USER dohyun@localhost IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* to dohyun@localhost; 
SHOW GRANTS for dohyun@localhost;