-- Создание таблиц с типами данных, совместимыми с Hibernate (BIGINT вместо INTEGER)

CREATE TABLE subscription_type (
                                   subscription_type_id BIGSERIAL PRIMARY KEY,
                                   title VARCHAR(100)
);

CREATE TABLE subscription (
                              subscription_id BIGINT PRIMARY KEY,
                              subscription_type_id BIGINT NOT NULL,
                              start_date DATE DEFAULT CURRENT_DATE,
                              end_date DATE,
                              is_active BOOLEAN NOT NULL DEFAULT true,
                              FOREIGN KEY (subscription_type_id) REFERENCES subscription_type (subscription_type_id)
);

CREATE TABLE users (
                       user_id BIGINT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       surname VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100),
                       email VARCHAR(100) NOT NULL UNIQUE,
                       hd_password VARCHAR(255) NOT NULL,
                       birth_date DATE NOT NULL CHECK (birth_date < CURRENT_DATE),
                       sex VARCHAR(7) NOT NULL,
                       photo TEXT,
                       registration_date DATE NOT NULL DEFAULT CURRENT_DATE,
                       subscription_id BIGINT NOT NULL DEFAULT 1,
                       FOREIGN KEY (subscription_id) REFERENCES subscription (subscription_id)
);

CREATE TABLE payment_method (
                                payment_method_id BIGSERIAL PRIMARY KEY,
                                title VARCHAR(100)
);

CREATE TABLE payment (
                         payment_id BIGINT PRIMARY KEY,
                         subscription_id BIGINT NOT NULL REFERENCES subscription(subscription_id),
                         price INTEGER NOT NULL CHECK (price >= 0),
                         paid_date_time TIMESTAMP NOT NULL,
                         is_paid BOOLEAN NOT NULL DEFAULT false,
                         payment_method_id BIGINT NOT NULL REFERENCES payment_method(payment_method_id)
);

CREATE TABLE notification (
                              notification_id BIGINT PRIMARY KEY,
                              user_id BIGINT NOT NULL REFERENCES users(user_id),
                              message TEXT NOT NULL,
                              send_date_time TIMESTAMP NOT NULL,
                              is_read BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE category (
                          category_id BIGSERIAL PRIMARY KEY,
                          title VARCHAR(100)
);

CREATE TABLE article (
                         article_id BIGINT PRIMARY KEY,
                         user_id BIGINT NOT NULL REFERENCES users(user_id),
                         title VARCHAR(200),
                         info TEXT NOT NULL,
                         published_date DATE NOT NULL,
                         category_id BIGINT NOT NULL REFERENCES category(category_id)
);

CREATE TABLE images (
                        image_id BIGINT PRIMARY KEY,
                        article_id BIGINT NOT NULL REFERENCES article(article_id),
                        url TEXT NOT NULL,
                        uploaded_date DATE NOT NULL
);

CREATE TABLE like_log (
                          like_log_id BIGINT PRIMARY KEY,
                          user_id BIGINT NOT NULL REFERENCES users(user_id),
                          article_id BIGINT NOT NULL REFERENCES article(article_id),
                          like_date_time TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE like_stats (
                            like_stats_id BIGINT PRIMARY KEY,
                            article_id BIGINT NOT NULL UNIQUE REFERENCES article(article_id),
                            like_count BIGINT NOT NULL
);

CREATE TABLE comment (
                         comment_id BIGINT PRIMARY KEY,
                         user_id BIGINT NOT NULL REFERENCES users(user_id),
                         article_id BIGINT NOT NULL REFERENCES article(article_id),
                         comment_text VARCHAR(3000) NOT NULL,
                         published_date TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE view_log (
                          view_log_id BIGINT PRIMARY KEY,
                          user_id BIGINT NOT NULL REFERENCES users(user_id),
                          article_id BIGINT NOT NULL REFERENCES article(article_id),
                          view_date_time TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE view_stats (
                            view_stats_id BIGINT PRIMARY KEY,
                            article_id BIGINT NOT NULL UNIQUE REFERENCES article(article_id),
                            view_count BIGINT NOT NULL DEFAULT 0
);
