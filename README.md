# Redis 활용 프로젝트 

이 프로젝트는 고성능 서비스를 위해 Redis 이해하고 최적화하기 위해 설계된 사이드 프로젝트입니다.

## Redis 개요
Redis는 다양한 자료 구조를 활용할 수 있는 인메모리 데이터 저장소이다.

## Redis의 특징

### String
- 단순 키-값 저장
- 활용: 원자적 연산이 필요할 때 유용 (예: `INCR`, `DECR`)
- 명령어:
  - `SET key value` : 값 설정
  - `GET key` : 값 조회
  - `INCR key` : 값 증가 (원자적)
  - `DECR key` : 값 감소 (원자적)
  - `APPEND key value` : 기존 값에 문자열 추가
  - `MSET key1 value1 key2 value2 ...` : 여러 키에 값 설정

### Lists
- 큐(Queue)와 스택(Stack)처럼 사용 가능
- 왼쪽(LPUSH, LPOP) 또는 오른쪽(RPUSH, RPOP)에서 삽입/삭제 가능
- 활용: 메시지 큐, 작업 대기열
- 명령어:
  - `LPUSH key value` : 왼쪽 삽입
  - `RPUSH key value` : 오른쪽 삽입
  - `LPOP key` : 왼쪽 요소 제거
  - `RPOP key` : 오른쪽 요소 제거
  - `LRANGE key start stop` : 특정 범위 내 요소 조회
  - `LLEN key` : 리스트 길이 조회

### Sets
- 중복을 허용하지 않는 집합
- 활용: 일정 시기 쿠폰 발행 후 한 번만 받을 수 있도록 관리, 빠른 데이터 존재 확인 (`SISMEMBER` 사용)
- 명령어:
  - `SADD key value` : 값 추가
  - `SISMEMBER key value` : 값 존재 여부 확인
  - `SMEMBERS key` : 모든 값 조회
  - `SREM key value` : 특정 값 제거
  - `SCARD key` : 집합 내 요소 개수 조회

### Hashes
- 하나의 키 아래 여러 개의 필드-값(Field-Value) 쌍을 저장 가능
- JSON을 문자열로 저장할 경우 특정 필드 조회 시 전체 데이터를 조회해야 하지만, Hash를 사용하면 특정 필드만 조회 가능
- 활용: 카운터(예: `HINCRBY`를 이용한 유저 방문 횟수 증가)
- 명령어:
  - `HSET key field1 value1 field2 value2 ...` : 여러 필드 저장
  - `HGET key field` : 특정 필드 조회
  - `HINCRBY key field increment` : 특정 필드 값 증가
  - `HDEL key field` : 특정 필드 삭제
  - `HLEN key` : 필드 개수 조회

### Sorted Sets
- 순서가 없는 유니크한 값의 집합 + 각 값은 `score`와 함께 저장되며 정렬됨
- 정렬된 상태이므로 빠르게 최소/최댓값 조회 가능
- 활용: 순위 계산, 리더보드(게임 상위 랭크 구현)
- 명령어:
  - `ZADD key score1 value1 score2 value2 ...` : 값 추가
  - `ZRANGE key start stop` : 범위 내 값 조회
  - `ZREVRANGE key start stop` : 내림차순 값 조회
  - `ZREM key value` : 특정 값 제거
  - `ZSCORE key value` : 특정 값의 점수 조회
  - `ZCARD key` : 요소 개수 조회

### Bitmaps
- 비트 벡터(0,1)를 사용하여 공간 효율적으로 N개의 SET을 저장
- 활용: 방문 여부 확인, 사용자 활동 기록
- 명령어:
  - `SETBIT key offset value` : 특정 위치 비트 설정
  - `GETBIT key offset` : 특정 위치 비트 조회
  - `BITCOUNT key` : 1의 개수 조회
  - `BITOP operation destkey key1 key2 ...` : 비트 연산 수행 (AND, OR, XOR 등)

### HyperLogLog
- 확률적 자료구조를 사용 (0.81% 오차율 허용) 대신 2^64 개 유니크 값 계산 가능
- 활용: 방문자 수 측정 (비트 벡터는 인덱스를 활용해야 하지만, HyperLogLog는 문자열도 가능)
- 명령어:
  - `PFADD key value1 value2 ...` : 값 추가
  - `PFCOUNT key` : 유일한 값 개수 조회
  - `PFMERGE destkey key1 key2 ...` : 여러 HyperLogLog 병합
