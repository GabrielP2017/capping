-- PostGIS 확장: 공간 함수·인덱스 사용을 위해 필요
CREATE EXTENSION IF NOT EXISTS postgis;

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

-- 경도·위도를 하나의 geometry로 묶어 GiST 인덱스 생성
CREATE INDEX IF NOT EXISTS camp_sites_geo_idx
    ON camp_sites
    USING GIST ( ST_SetSRID( ST_MakePoint(lon, lat), 4326 ) );
