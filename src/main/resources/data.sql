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

INSERT INTO pigeon (id, created, updated, birthdate, condition_status, sex, is_native, name, ring_number,
                    user_id, color_id, father_id, section_id, photo_id, mate_id, mother_id)
VALUES
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2017, 12, 1), 'HEALTH', 'MALE', true, 'Дедушка по папе', '2314213',
     100000, 100001, null, 100006, null, null, null), --100010
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2017, 3, 1), 'DISEASED', 'FEMALE', true, 'Бабушка по маме', '23142215',
     100000, 100001, null, 100006, null, 100012, null), --100011
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2017, 1, 1), 'HEALTH', 'MALE', true, 'Дедушка по маме', '23144215',
     100000, 100001, null, 100007, null, 100011, null), --100012
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2018, 6, 1), 'HEALTH', 'MALE', true, 'Папа', '23114215',
     100000, 100001, 100010, 100007, null, 100014, null), --100013
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2018, 12, 1), 'HEALTH', 'FEMALE', true, 'Мама', '26314215',
     100000, 100001, 100012, 100008, null, 100013, 100011), --100014
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2018, 12, 1), 'HEALTH', 'FEMALE', true, 'Сестра папы', '23145215',
     100000, 100001, 100010, 100008, null, null, null), --100015
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2018, 12, 1), 'HEALTH', 'MALE', true, 'Брат папы', '23142155',
     100000, 100001, 100010, 100009, null, null, null), --100016
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2019, 12, 1), 'LOST', 'MALE', true, 'Сын', '23142152',
     100000, 100001, 100013, 100009, null, null, 100014), --100017
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2019, 12, 1), 'HEALTH', 'FEMALE', true, 'Дочь', '231134215',
     100000, 100001, 100013, 100004, null, null, 100014), --100018
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2019, 12, 1), 'HEALTH', 'FEMALE', true, 'Дочь', '314215',
     100000, 100001, 100013, 100004, null, null, 100014), --100019
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2019, 12, 1), 'HEALTH', 'MALE', true, 'Сын брата папы', '24215',
     100000, 100001, 100016, 100005, null, null, null), --100020
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2018, 12, 1), 'DEAD', 'MALE', true, 'Друг семьи', '244215',
     100000, 100001, null, 100005, null, null, null)  --100021
;

INSERT INTO section (id, created, updated, name, type, user_id, parent_id)
VALUES
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Конюшня', 'DOVECOTE', 100000, NULL),       --100022
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Основная секция', 'ROOM', 100000, 100022), --100023
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Гнездо 1', 'NEST', 100000, 100023),        --100024
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Гнездо 2', 'NEST', 100000, 100023),        --100025
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Гнездо 3', 'NEST', 100000, 100023),        --100026
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Гнездо 4', 'NEST', 100000, 100023),        --100027
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Гнездо 5', 'NEST', 100000, 100023)         --100028
;

INSERT INTO pigeon (id, created, updated, birthdate, condition_status, sex, is_native, name, ring_number,
                    user_id, color_id, father_id, section_id, photo_id, mate_id, mother_id)
VALUES
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2018, 12, 30), 'HEALTH', 'MALE', true, null, '31234',
     100000, 100001, null, 100024, null, 100031, null), --100029
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2017, 3, 14), 'DISEASED', 'FEMALE', true, null, '3423566',
     100000, 100001, null, 100023, null, null, null), --100030
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2017, 4, 15), 'HEALTH', 'FEMALE', true, null, '3264623',
     100000, 100001, null, 100024, null, 100029, null), --100031
    (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_date(2018, 6, 13), 'HEALTH', 'MALE', true, null, '134156',
     100000, 100001, null, 100027, null, null, null) --100032
;

-- INSERT INTO flight (id, created, updated, departure, distance, is_training, user_id, location_id)
-- VALUES
--     (nextval('global_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, make_timestamp())