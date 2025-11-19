CREATE TABLE IF NOT EXISTS users (
    id           BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username     VARCHAR(50)  NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    avatar_url   VARCHAR(255),
    bio          VARCHAR(255),
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS posts (
    id            BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id       BIGINT       NOT NULL,
    content       TEXT         NOT NULL,
    markdown      TINYINT(1)   NOT NULL DEFAULT 0,
    images        JSON,
    visibility    VARCHAR(20)  NOT NULL DEFAULT 'PUBLIC',
    like_count    INT          NOT NULL DEFAULT 0,
    comment_count INT          NOT NULL DEFAULT 0,
    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_posts_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS post_likes (
    id         BIGINT    NOT NULL PRIMARY KEY AUTO_INCREMENT,
    post_id    BIGINT    NOT NULL,
    user_id    BIGINT    NOT NULL,
    created_at DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_post_user (post_id, user_id),
    CONSTRAINT fk_likes_post FOREIGN KEY (post_id) REFERENCES posts (id),
    CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments (
    id         BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    post_id    BIGINT      NOT NULL,
    parent_id  BIGINT,
    user_id    BIGINT      NOT NULL,
    content    TEXT        NOT NULL,
    created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts (id),
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users (id)
);

