# Redis 활용 프로젝트 

이 프로젝트는 고성능 서비스를 위해 Redis 이해하고 최적화하기 위해 설계된 사이드 프로젝트입니다.

## SortedSet Redis 을 통한 리더보드 기능

리더보드 : 랭킹 시스템
- RDB 사용 시 업데이트문은 하나의 행에서 이루어지므로 속도가 괜찮지만, 조회 시 `COUNT` 등의 집계 함수나 많은 데이터로 인해 조회 속도가 느려짐
- 이를 해결하기 위해 `SortedSet`을 활용한 Redis를 사용

### Redis 사용 시 장점
- 순위 데이터에 적합한 `SortedSet` 자료 구조를 사용하면 `score`를 통해 자동으로 정렬됨
- 용도에 특화된 오퍼레이션 (`ZADD`, `ZRANGE`, `ZSCORE` 등)이 존재하므로 사용이 간편함
- 자료구조의 특성으로 인해 데이터 조회가 빠름 (범위 검색, 특정 값의 순위 확인)
- 빈번한 액세스에 유리한 인메모리 DB의 속도를 활용 가능

--- 
# 프로젝트를 통한 기술적/실무적 학습 내용
## Redis 개요
Redis는 다양한 자료 구조를 활용할 수 있는 인메모리 데이터 저장소

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

### 캐시

- **캐시 적중 (Cache Hit)**: 캐시에 접근해 데이터를 발견함<br/>
- **캐시 미스 (Cache Miss)**: 캐시에 접근했으나 데이터를 발견하지 못함<br/>
- **캐시 삭제 정책 (Eviction Policy)**: 캐시의 데이터 공간 확보를 위해 저장된 데이터를 삭제<br/>
- **캐시 전략**: 환경에 따라 적합한 캐시 운영 방식 선택 가능 (Cache-Aside, Write-Through)

#### 캐시 전략

##### Cache-Aside (Lazy Loading)
항상 캐시를 먼저 체크하고 없으면 원본에서 읽어온 후에 캐시에 저장<br/>
**장점**: 필요한 데이터만 캐시에 저장되며, Cache Miss가 있어도 치명적이지 않음<br/>
**단점**: 최초 접근은 느림, 업데이트 주기가 일정하지 않아 캐시가 최신 데이터가 아닐 수 있음<br/>
**절차**: `1) 읽기 시도 -> 2) 원본 읽기 -> 3) 캐시 쓰기`

##### Write-Through
데이터를 쓸 때 항상 캐시를 업데이트하여 최신 상태를 유지<br/>
**장점**: 캐시가 항상 동기화되어 있어 데이터가 최신 상태를 유지<br/>
**단점**: 자주 사용하지 않는 데이터도 캐시에 저장되며, 쓰기 지연 시간이 증가<br/>
**절차**: `1) 캐시 쓰기 -> 2) DB 쓰기`

##### Write-Back
데이터를 캐시에만 쓰고 일정 주기로 DB에 업데이트<br/>
**장점**: 쓰기가 많은 경우 DB 부하를 줄일 수 있음<br/>
**단점**: 캐시가 DB에 쓰기 전에 장애가 발생하면 데이터 유실 가능<br/>
**절차**: `1) 캐시에만 쓰기 -> 2) 일정 주기로 DB 업데이트`

#### 데이터 제거 방식

- **Expiration**: 각 데이터에 TTL(Time-to-Live)을 설정해 시간 기반으로 삭제<br/>
- **Eviction Algorithm**: 공간을 확보해야 할 경우 어떤 데이터를 삭제할지 결정
  - **LRU (Least Recently Used)**: 가장 오랫동안 사용되지 않은 데이터 삭제
  - **LFU (Least Frequently Used)**: 가장 적게 사용된 데이터를 삭제 (최근에 삭제되었더라도)
  - **FIFO (First-In-First-Out)**: 가장 먼저 들어온 데이터 삭제