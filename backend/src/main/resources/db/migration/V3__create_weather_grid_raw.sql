CREATE TABLE weather_grid_raw (
  id          BIGSERIAL PRIMARY KEY,
  base_date   CHAR(8)     NOT NULL,  -- 예보 기준 날짜   yyyyMMdd
  base_time   CHAR(4)     NOT NULL,  -- 예보 기준 시각   HHmm
  nx          NUMERIC     NOT NULL,  -- 격자 X
  ny          NUMERIC     NOT NULL,  -- 격자 Y
  category    VARCHAR(10) NOT NULL,  -- TMP / POP …
  fcst_date   CHAR(8)     NOT NULL,  -- 예보 대상 날짜
  fcst_time   CHAR(4)     NOT NULL,  -- 예보 대상 시각
  fcst_value  VARCHAR(20),
  created_at  TIMESTAMP DEFAULT now()
);
