-- CREATE DATABASE travel_db;
-- \c travel_db;

-- ==========================
-- USERS
-- ==========================
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS schedules CASCADE;
DROP TABLE IF EXISTS tours CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
                       id            BIGSERIAL PRIMARY KEY,
                       email         VARCHAR(255) NOT NULL UNIQUE,
                       first_name    VARCHAR(100) NOT NULL,
                       last_name     VARCHAR(100) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role          VARCHAR(20)  NOT NULL DEFAULT 'USER'  -- USER / ADMIN
);

-- ==========================
-- TOURS
-- ==========================
CREATE TABLE tours (
                       id            BIGSERIAL PRIMARY KEY,
                       title         VARCHAR(255) NOT NULL,
                       description   TEXT         NOT NULL,
                       price         NUMERIC(12,2) NOT NULL CHECK (price > 0),
                       duration_days INT          NOT NULL CHECK (duration_days > 0),
                       destination   VARCHAR(255) NOT NULL,
                       tags          VARCHAR(255)
);

-- ==========================
-- BOOKINGS (M2M users <-> tours)
-- ==========================
CREATE TABLE bookings (
                          id         BIGSERIAL PRIMARY KEY,
                          user_id    BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          tour_id    BIGINT NOT NULL REFERENCES tours(id) ON DELETE CASCADE,
                          status     VARCHAR(20) NOT NULL DEFAULT 'NEW',   -- NEW / CONFIRMED / CANCELED
                          created_at TIMESTAMP   NOT NULL DEFAULT now()
);

CREATE INDEX idx_bookings_user ON bookings(user_id);
CREATE INDEX idx_bookings_tour ON bookings(tour_id);

-- ==========================
-- REVIEWS (user -> tour)
-- ==========================
CREATE TABLE reviews (
                         id         BIGSERIAL PRIMARY KEY,
                         user_id    BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                         tour_id    BIGINT NOT NULL REFERENCES tours(id) ON DELETE CASCADE,
                         rating     INT    NOT NULL CHECK (rating BETWEEN 1 AND 5),
                         comment    TEXT   NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_reviews_tour ON reviews(tour_id);
CREATE INDEX idx_reviews_user ON reviews(user_id);

-- ==========================
-- SCHEDULES (O2M: один тур - много дат)
-- ==========================
CREATE TABLE schedules (
                           id         BIGSERIAL PRIMARY KEY,
                           tour_id    BIGINT NOT NULL REFERENCES tours(id) ON DELETE CASCADE,
                           start_date DATE   NOT NULL,
                           end_date   DATE   NOT NULL,
                           CHECK (end_date >= start_date)
);

CREATE INDEX idx_schedules_tour ON schedules(tour_id);

-- ==========================
-- ткстовые данные
-- ==========================

INSERT INTO tours (title, description, price, duration_days, destination, tags) VALUES
                                                                                    ('Barbie Weekend in Paris',
                                                                                     'Романтический розовый уикенд в Париже: шопинг, фотосессии, розовое шампанское и беспощадный гламур.',
                                                                                     799.99, 3, 'Paris', 'glamour,shopping,paris'),
                                                                                    ('Malibu Bimbo Retreat',
                                                                                     'Неделя в стиле Malibu Barbie: пляж, коктейли, йога и zero thoughts в расписании.',
                                                                                     1299.00, 7, 'Malibu', 'beach,malibu,relax'),
                                                                                    ('Tokyo Kawaii Tour',
                                                                                     'Kawaii-рай: Harajuku, аниме-магазины, косплей и розовые кафе.',
                                                                                     1499.50, 6, 'Tokyo', 'kawaii,anime,tokyo');
