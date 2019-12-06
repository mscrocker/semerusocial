CREATE TABLE CitiesCriteria(
    userId BIGINT NOT NULL,
    city VARCHAR(30) NOT NULL,
    CONSTRAINT fk_userIdForCities FOREIGN KEY(userId)
        REFERENCES UserTable(id),
    CONSTRAINT pk_userIdAndCity PRIMARY KEY(userId, city)
)