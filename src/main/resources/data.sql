INSERT INTO users (id, created, updated, email, password)
VALUES
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'user1@yandex.ru', '$2a$12$oh6c0bn/N1IWJU4MWnmqgeKis9M3wtQ1T3/swgYBJ.v2OQpaQfZRO')
;

INSERT INTO color (id, created, updated, name, user_id)
VALUES
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Сизый', 100000), --100001
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Серый', 100000)  --100002
;

INSERT INTO section (id, created, updated, name, type, user_id, parent_id)
VALUES
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Основная голубятня', 'DOVECOTE', 100000, NULL), --100003
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1 этаж', 'ROOM', 100000, 100003),               --100004
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '2 этаж', 'ROOM', 100000, 100003),               --100005
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Гнездо 1-А', 'NEST', 100000, 100004),           --100006
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Гнездо 2-А', 'NEST', 100000, 100004),           --100007
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Гнездо 3-А', 'NEST', 100000, 100004),           --100008
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Гнездо 1-Б', 'NEST', 100000, 100005)            --100009
;

INSERT INTO pigeon (id, created, updated, birthdate, condition_status, is_male, is_native, name, ring_number,
                    user_id, color_id, father_id, section_id, photo_id, mate_id, mother_id)
VALUES
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2017, 12, 1), 'HEALTH', true, true, 'Дедушка по папе', '2314213',
     100000, 100001, null, 100006, null, null, null), --100010
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2017, 3, 1), 'HEALTH', false, true, 'Бабушка по маме', '23142215',
     100000, 100001, null, 100006, null, 100012, null), --100011
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2017, 1, 1), 'HEALTH', true, true, 'Дедушка по маме', '23144215',
     100000, 100001, null, 100006, null, 100011, null), --100012
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2018, 6, 1), 'HEALTH', true, true, 'Папа', '23114215',
     100000, 100001, 100010, 100006, null, 100014, null), --100013
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2018, 12, 1), 'HEALTH', false, true, 'Мама', '26314215',
     100000, 100001, 100012, 100006, null, 100013, 100011), --100014
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2018, 12, 1), 'HEALTH', false, true, 'Сестра папы', '23145215',
     100000, 100001, 100010, 100006, null, null, null), --100015
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2018, 12, 1), 'HEALTH', true, true, 'Брат папы', '23142155',
     100000, 100001, 100010, 100006, null, null, null), --100016
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2019, 12, 1), 'HEALTH', true, true, 'Сын', '23142152',
     100000, 100001, 100013, 100006, null, null, 100014), --100017
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2019, 12, 1), 'HEALTH', false, true, 'Дочь', '231134215',
     100000, 100001, 100013, 100006, null, null, 100014), --100018
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2019, 12, 1), 'HEALTH', false, true, 'Дочь', '314215',
     100000, 100001, 100013, 100006, null, null, 100014), --100019
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2019, 12, 1), 'HEALTH', true, true, 'Сын брата папы', '24215',
     100000, 100001, 100016, 100006, null, null, null), --100020
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2018, 12, 1), 'HEALTH', true, true, 'Друг семьи', '244215',
     100000, 100001, null, 100006, null, null, null)  --100021
;

INSERT INTO departure_location (id, created, updated, name, user_id)
VALUES
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Можайск', 100000)
;

-- INSERT INTO flight (id, created, updated, departure, distance, is_training, user_id, location_id)
-- VALUES
--     (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_timestamp())