CREATE TABLE IF NOT EXISTS users
(
    user_id BIGSERIAL NOT NULL,
    name    VARCHAR(200) NOT NULL,
    email   VARCHAR(200) UNIQUE NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS administrator
(
    user_id      BIGINT NOT NULL,
    admin_level  INTEGER NOT NULL,
    CONSTRAINT pk_admin PRIMARY KEY (user_id),
    CONSTRAINT fk_admin_users FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS author
(
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_author_user PRIMARY KEY (user_id),
    CONSTRAINT fk_author_user_users FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS post
(
    post_id     BIGSERIAL NOT NULL,
    created_on  TIMESTAMP NOT NULL,
    description VARCHAR(300) NOT NULL,
    CONSTRAINT pk_post PRIMARY KEY (post_id)
);

CREATE TABLE IF NOT EXISTS comment
(
    comment_id  SERIAL NOT NULL,
    created_on  TIMESTAMP NOT NULL,
    description VARCHAR(200) NOT NULL,
    user_id     BIGINT NOT NULL,
    post_id     BIGINT NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (comment_id),
    CONSTRAINT fk_comment_users FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_post
(
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_user_post PRIMARY KEY (post_id, user_id),
    CONSTRAINT fk_user_post_users FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_post_post FOREIGN KEY (post_id) REFERENCES post (post_id) ON DELETE CASCADE
);
