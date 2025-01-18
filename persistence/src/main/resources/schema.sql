-- Создание таблицы posts
CREATE TABLE posts (
    post_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,  -- для H2 используем IDENTITY, для PostgreSQL будет работать как SERIAL
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Создание таблицы tags
CREATE TABLE tags (
    tag_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,  -- для H2 используем IDENTITY, для PostgreSQL будет работать как SERIAL
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Создание таблицы post_tags
CREATE TABLE post_tags (
    post_id BIGINT NOT NULL,  -- используем BIGINT, чтобы он был совместим с постами и тегами
    tag_id BIGINT NOT NULL,   -- используем BIGINT, чтобы он был совместим с постами и тегами
    is_deleted BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE
);

-- Создание таблицы comments
CREATE TABLE comments (
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,  -- для H2 используем IDENTITY, для PostgreSQL будет работать как SERIAL
    post_id BIGINT NOT NULL,
    content VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE
);

-- Создание таблицы likes
CREATE TABLE likes (
    like_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,  -- для H2 используем IDENTITY, для PostgreSQL будет работать как SERIAL
    post_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE
);
