-- Создание таблиц

DROP TABLE users;
CREATE TABLE users
(
	user_id SERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	surname VARCHAR(100) NOT NULL,
	last_name VARCHAR(100),
	email VARCHAR(100) NOT NULL UNIQUE,
	hd_password VARCHAR(255) NOT NULL,
	birth_date DATE NOT NULL CHECK (birth_date < CURRENT_DATE),
	sex VARCHAR(7) NOT NULL,
	photo TEXT,
	registration_date DATE NOT NULL DEFAULT CURRENT_DATE,
	subscription_id INT NOT NULL DEFAULT 1,

	FOREIGN KEY (subscription_id) REFERENCES subscription (subscription_id)
);

DROP TABLE subscription cascade;
CREATE TABLE subscription
(
	subscription_id SERIAL PRIMARY KEY,
	subscription_type_id INT NOT NULL,
	start_date DATE DEFAULT CURRENT_DATE,
	end_date DATE,
	is_active BOOLEAN NOT NULL DEFAULT true,

	FOREIGN KEY (subscription_type_id) REFERENCES subscription_type (subscription_type_id)
);

INSERT INTO subscription (subscription_type_id, start_date, end_date, is_active)
VALUES (1, NULL, NULL, true);

CREATE TABLE subscription_type
(
	subscription_type_id SERIAL PRIMARY KEY,
	title VARCHAR(100)
);

INSERT INTO subscription_type (title) VALUES ('стандарт'), ('про');

-- CREATE TYPE sex_enum AS ENUM ('male', 'female', 'other');
-- DROP TYPE sex_enum;

select * from subscription;


CREATE TABLE payment_method (
    payment_method_id SERIAL PRIMARY KEY,
    title VARCHAR(100)
);
insert into payment_method (title) values ('Карта'), ('СБП');

drop table payment;
CREATE TABLE payment (
    payment_id SERIAL PRIMARY KEY,
    subscription_id INTEGER NOT NULL REFERENCES subscription(subscription_id),
    price INTEGER NOT NULL CHECK (price >= 0),
    paid_date_time timestamp NOT NULL,
    is_paid BOOLEAN NOT NULL DEFAULT false,
    method_id INTEGER NOT NULL REFERENCES payment_method(payment_method_id)
);

drop table notification;
CREATE TABLE notification (
    notification_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    message TEXT NOT NULL,
    send_date_time timestamp NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE
);


CREATE TABLE category (
    category_id SERIAL PRIMARY KEY,
    title VARCHAR(100)
);

CREATE TABLE article (
    article_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    title VARCHAR(200),
    info TEXT NOT NULL,
    published_date DATE NOT NULL,
    category_id INTEGER NOT NULL REFERENCES category(category_id)
);

CREATE TABLE images (
    image_id SERIAL PRIMARY KEY,
    article_id INTEGER NOT NULL REFERENCES article(article_id),
    url TEXT NOT NULL,
    uploaded_date DATE NOT NULL
);

CREATE TABLE like_log (
    like_log_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    article_id INTEGER NOT NULL REFERENCES article(article_id),
    like_date_time timestamp NOT NULL DEFAULT NOW()
);

CREATE TABLE like_stats (
    like_stats_id SERIAL PRIMARY KEY,
    article_id INTEGER NOT NULL UNIQUE REFERENCES article(article_id),
    like_count BIGINT NOT NULL
);

drop table comment;
CREATE TABLE comment (
    comment_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    article_id INTEGER NOT NULL REFERENCES article(article_id),
    comment_text VARCHAR(3000) NOT NULL,
    published_date timestamp NOT NULL default now()
);

CREATE TABLE view_log (
    view_log_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    article_id INTEGER NOT NULL REFERENCES article(article_id),
    view_date_time timestamp NOT NULL default now()
);

CREATE TABLE view_stats (
    view_stats_id SERIAL PRIMARY KEY,
    article_id INTEGER NOT NULL UNIQUE REFERENCES article(article_id),
    view_count BIGINT NOT NULL default 0
);




select * from subscription;
delete from subscription where start_date = '2025-06-05';
-- Заполнение таблиц данными
INSERT INTO subscription (subscription_type_id, start_date, end_date, is_active)
VALUES 
(2, '2025-01-01', '2025-12-31', true),
(2, '2025-06-05', '2026-06-05', true);

INSERT INTO users (name, surname, last_name, email, hd_password, birth_date, sex, subscription_id)
VALUES 
('Иван', 'Иванов', 'Петрович', 'ivan@example.com', '123', '1990-05-10', 'male', 1),
('Анна', 'Смирнова', NULL, 'anna@example.com', '456', '1985-08-21', 'female', 2),
('Алексей', 'Козлов', 'Игоревич', 'alex@example.com', '789', '2000-12-15', 'male', 3);

INSERT INTO payment (subscription_id, price, paid_date_time, is_paid, method_id)
VALUES 
(2, 1500, NOW(), true, 2),
(3, 800, NOW(), true, 1);

INSERT INTO notification (user_id, message, send_date_time, is_read)
VALUES 
(1, 'Добро пожаловать!', NOW(), false),
(2, 'Новая статья доступна.', NOW(), true),
(3, 'Подписка заканчивается через 3 дня.', NOW(), false);

INSERT INTO category (title)
VALUES ('Здоровье'), ('Фитнес'), ('Питание');

INSERT INTO article (user_id, title, info, published_date, category_id)
VALUES 
(1, 'Польза утренней зарядки', 'Очень полезно начинать день активно.', '2025-05-01', 1),
(2, 'Лучшие упражнения для спины', 'Комплекс упражнений для снятия боли.', '2025-05-02', 2),
(3, 'Сбалансированное питание', 'Что есть, чтобы быть в форме.', '2025-05-03', 3);

INSERT INTO images (article_id, url, uploaded_date)
VALUES 
(1, 'https://example.com/img1.jpg', '2025-05-01'),
(2, 'https://example.com/img2.jpg', '2025-05-02'),
(3, 'https://example.com/img3.jpg', '2025-05-03');

INSERT INTO like_log (user_id, article_id)
VALUES 
(2, 1),
(1, 2),
(3, 3);

INSERT INTO like_stats (article_id, like_count)
VALUES 
(1, 1),
(2, 1),
(3, 1);

INSERT INTO comment (user_id, article_id, comment_text)
VALUES 
(1, 1, 'Отличная статья!'),
(2, 2, 'Очень полезно, спасибо!'),
(3, 3, 'Интересный подход к питанию.');

INSERT INTO view_log (user_id, article_id)
VALUES 
(1, 1),
(2, 1),
(3, 2);

INSERT INTO view_stats (article_id, view_count)
VALUES 
(1, 2),
(2, 1),
(3, 0);