-- PostGIS 확장: 공간 함수·인덱스 사용을 위해 필요
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS timescaledb;

-- 캠핑장 기본 테이블
CREATE TABLE IF NOT EXISTS camp_sites (
    id           SERIAL         PRIMARY KEY,
    name         TEXT           NOT NULL,
    address      TEXT,
    lat          NUMERIC(9,6)   NOT NULL,   -- 위도
    lon          NUMERIC(9,6)   NOT NULL,   -- 경도
    phone        TEXT,
    last_updated TIMESTAMP      DEFAULT NOW()

);

-- 산불 위험 원본 테이블
CREATE TABLE IF NOT EXISTS wildfire_risks (
  id          SERIAL        NOT NULL,
  region_code TEXT          NOT NULL,
  risk_level  INT           NOT NULL CHECK (risk_level BETWEEN 0 AND 4),
  fetched_at  TIMESTAMPTZ   NOT NULL,
  lat         NUMERIC(9,6)  NOT NULL,
  lon         NUMERIC(9,6)  NOT NULL,
  PRIMARY KEY (fetched_at, id)
);

-- ③ 이 부분이 **하이퍼테이블 전환** 핵심
SELECT create_hypertable('wildfire_risks','fetched_at', if_not_exists => TRUE);


-- 경도·위도를 하나의 geometry로 묶어 GiST 인덱스 생성
CREATE INDEX IF NOT EXISTS camp_sites_geo_idx
    ON camp_sites
        USING GIST ( ST_SetSRID( ST_MakePoint(lon, lat), 4326 ) );

-- ④ 산불 인덱스
CREATE INDEX IF NOT EXISTS idx_wildfire_time_desc
    ON wildfire_risks (fetched_at DESC);
