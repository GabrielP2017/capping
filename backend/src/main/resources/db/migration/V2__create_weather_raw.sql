/*
  ------------------------------------------------------------------
  목적   : 캠핑장별 KMA 초단기 실황·예보 원본을 저장할 테이블 생성
  대상 DB: PostgreSQL 14  (PostGIS 확장 설치됨)
  ------------------------------------------------------------------
*/

CREATE TABLE weather_raw (
  id           BIGSERIAL PRIMARY KEY,          -- PK
  camp_id      BIGINT NOT NULL                 -- camp_sites.id FK
                 REFERENCES camp_sites(id),
  base_time    TIMESTAMPTZ NOT NULL,           -- 예보 기준 시각
  temp         NUMERIC(4,1),                   -- 현재 기온 (℃)
  precip_prob  INT,                            -- 강수확률 (%)
  inserted_at  TIMESTAMPTZ DEFAULT now()       -- 행 입력 시각
);
