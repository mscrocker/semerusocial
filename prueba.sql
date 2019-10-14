


UPDATE UserTable a JOIN (SELECT * FROM UserTable) b 
    ON a.id=b.id
    SET a.criteriaSex=b.sex
    WHERE a.criteriaSex LIKE ''
        AND (b.sex LIKE 'female'
        OR b.sex LIKE 'male');

UPDATE UserTable 
    SET criteriaSex='other'
    WHERE criteriaSex LIKE '';



SET a.criteriaSex = (SELECT b.sex 
    FROM UserTable b
    WHERE a.id=b.id
    AND (b.sex LIKE 'female'
        OR b.sex LIKE 'female'))
WHERE a.criteriaSex IS NULL




INSERT INTO UserTable (userName, password, date, sex, city, suggestion, criteriaSex)
VALUES ('pruebaa', 'passa', '2019-10-09 22:00:00', 'Female', 'asd', NULL, NULL);










CREATE TABLE CITIESCRITERIA(
    userId BIGINT NOT NULL,
    city VARCHAR(30) NOT NULL,
    CONSTRAINT fk_userIdForCities FOREIGN KEY(userId)
        REFERENCES UserTable(id),
    CONSTRAINT pk_userIdAndCity PRIMARY KEY(userId, city)

)

default



UPDATE UserTable a INNER JOIN UserTable b
    ON a.id=b.id
SET a.prueba = b.password
WHERE a.prueba='prueba';






ALTER TABLE UserTable ADD p2 BIGINT NULL; 

ALTER TABLE UserTable ADD FOREIGN KEY (p2) REFERENCES UserTable(id) 
ON DELETE SET NULL;

INSERT INTO UserTable (userName, password, date, sex, city, suggestion, p)
VALUES ('prueba', 'pass', '2019-10-09 22:00:00', 'Female', 'asd', NULL, NULL);

INSERT INTO UserTable (userName, password, date, sex, city, suggestion, criteriaSex, criteriaMinAge, criteriaMaxAge)
VALUES ('prueba2', 'pass2', '2019-10-09 22:00:00', 'Female', 'asd', NULL, 'F',0,10);


INSERT INTO UserTable (userName, password, date, sex, city, suggestion, p)
VALUES ('prueba3', 'pass3', '2019-10-09 22:00:00', 'Female', 'asd', NULL, NULL)

UPDATE UserTable SET p2=2 WHERE id=3;

UPDATE UserTable SET id=13 WHERE id=1
DELETE FROM UserTable WHERE id=2;










DROP TABLE DATABASECHANGELOG; DROP TABLE DATABASECHANGELOGLOCK; DROP TABLE UserTable; DROP TABLE example_entities; DROP TABLE ImageTable; DROP TABLE CitiesCriteria;
