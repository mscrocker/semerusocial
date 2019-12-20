
TRUNCATE TABLE RequestTable;
TRUNCATE TABLE RejectedTable;
TRUNCATE TABLE MatchTable;
TRUNCATE TABLE ImageTable;
TRUNCATE TABLE CitiesCriteria;
DELETE FROM UserTable;

INSERT INTO UserTable (id, userName, password, date, sex, city, criteriaSex, criteriaMinAge, criteriaMaxAge, description) VALUES
(1, 'User1', 'pass1',   DATEADD('YEAR',-19, CURRENT_TIMESTAMP), 'Female', 'Coruña',
'Female', '18', '99', 'User1 - edad 19'),
(2, 'User2', 'pass2',   DATEADD('YEAR',-100, CURRENT_TIMESTAMP ), 'Female', 'Coruña',
'ANY', '18', '99', 'User2 - edad 100'),
(3, 'User3', 'pass3',  DATEADD('YEAR',-18, CURRENT_TIMESTAMP ), 'Female', 'Coruña',
'ANY', '18', '99', 'User3 - edad 18'),
(4, 'User4', 'pass4',  DATEADD('YEAR',-98, CURRENT_TIMESTAMP ), 'Female', 'Coruña',
'ANY', '18', '99', 'User4 - edad 98'),


(5, 'User5', 'pass5',  DATEADD('YEAR',-100, CURRENT_TIMESTAMP ), 'Male', 'Coruña',
'ANY', '18', '99', 'User5 - hombre - edad 100'),
(6, 'User6', 'pass6',  DATEADD('YEAR',-19, CURRENT_TIMESTAMP ), 'Patata', 'Coruña',
'ANY', '18', '99', 'User6 - Patata'),
(7, 'User7', 'pass7',  DATEADD('YEAR',-30, CURRENT_TIMESTAMP ), 'Other', 'Coruña',
'ANY', '18', '99', 'User7 - Other - edad 30'),
(8, 'User8', 'pass8',  DATEADD('YEAR',-19, CURRENT_TIMESTAMP ), 'Female', 'Coruña',
'ANY', '18', '99', 'User8 - edad 19'),


(9, 'User9', 'pass9',  DATEADD('YEAR',-19, CURRENT_TIMESTAMP ), 'Female', 'Coruña',
'ANY', '18', '99', 'User9 - Amigo de User1'),
(10, 'User10', 'pass10',  DATEADD('YEAR',-19, CURRENT_TIMESTAMP ), 'Female', 'Coruña',
'ANY', '18', '99', 'User10 - Amigo de User1'),


(11, 'User11', 'pass11',   DATEADD('YEAR',-19, CURRENT_TIMESTAMP ), 'Female', 'Coruña',
'ANY', '18', '99', 'User11 - Rechazado por User1'),
(12, 'User12', 'pass12',   DATEADD('YEAR',-19, CURRENT_TIMESTAMP), 'Female', 'Coruña',
'ANY', '18', '99', 'User12 - Rechazo a User1'),

(13, 'User13', 'pass13',   DATEADD('YEAR',-19, CURRENT_TIMESTAMP), 'Female', 'Coruña',
'ANY', '18', '99', 'User13 - User1 lo acecpto'),
(14, 'User14', 'pass14',  DATEADD('YEAR',-19, CURRENT_TIMESTAMP), 'Female', 'Coruña',
'ANY', '18', '99', 'User14 - Acepto a User1');

INSERT INTO MatchTable (user1, user2, date) VALUES
(1, 9, NOW()),
(10,1, NOW());

INSERT INTO RejectedTable(subject, object, date) VALUES
(1, 11, NOW()),
(12, 1, NOW());

INSERT INTO RequestTable(subject, object, date) VALUES
(1, 13, NOW()),
(14, 1, NOW());


