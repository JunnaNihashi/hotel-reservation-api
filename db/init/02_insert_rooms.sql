
-- 部屋タイプの初期データを登録
INSERT INTO room_types (id, room_type, price, capacity) VALUES
(1, 'THE HORIZON', 30000, 4),
(2, 'THE TERRACE', 40000, 5),
(3, 'THE RESIDENCE', 50000, 6);

-- 宿泊プランの初期データを登録
INSERT INTO plans (id, stay_plan, price) VALUES
(1, '素泊まり', 0),
(2, '食事つき', 5000);

-- 客室の初期データを登録
INSERT INTO rooms (id, room_type_id, room_number) VALUES
(1, 1, '1-1'),
(2, 1, '1-2'),
(3, 1, '1-3'),
(4, 2, '2-1'),
(5, 2, '2-2'),
(6, 2, '2-3'),
(7, 3, '3-1'),
(8, 3, '3-2'),
(9, 3, '3-3');

-- 明示的に登録したIDに合わせて採番位置を調整
SELECT setval(pg_get_serial_sequence('room_types', 'id'), 3);
SELECT setval(pg_get_serial_sequence('plans', 'id'), 2);
SELECT setval(pg_get_serial_sequence('rooms', 'id'), 9);
