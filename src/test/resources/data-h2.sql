-- 초기화 데이터는 ddl-auto 보다 먼저 실행되는듯...
-- schema 가 있는 상황이나 ddl-auto 를 사용하지 않는 상황에서 사용하는 것이 좋아보임
-- Test member
-- password: test-password!
insert into
member
(
    id,
    created_date,
    last_modified_date,
    name,
    password,
    username
)
values
(
    default,
    PARSEDATETIME('2022-05-05 12:00:00','yyyy-MM-dd hh:mm:ss'),
    PARSEDATETIME('2022-05-05 12:00:00','yyyy-MM-dd hh:mm:ss'),
    'test-name', '{bcrypt}$2a$10$BJUgE3MmrD6LOoXQFBK4BOknKjVPaQtHhAa.bdd38pOl.mpma7Tne',
    'test-username'
);