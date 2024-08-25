CREATE TABLE IF NOT EXISTS users
(
    user_id SERIAL NOT NULL,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(200) UNIQUE NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);