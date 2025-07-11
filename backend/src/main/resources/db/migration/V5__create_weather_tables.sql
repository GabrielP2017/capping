/* ========== weather_raw ========== */
/* “캠핑장별 현재 데이터 & 선택 카테고리(TMP·POP 등)” 저장 */
CREATE TABLE weather_raw (
  id           BIGSERIAL PRIMARY KEY,
  camp_id      BIGINT       NOT NULL,
  base_date    CHAR(8)      NOT NULL,   -- 예: 20250709
  base_time    CHAR(4)      NOT NULL,   -- 예: 0200
  category     VARCHAR(10)  NOT NULL,   -- TMP, POP …
  fcst_date    CHAR(8),
  fcst_time    CHAR(4),
  fcst_value   VARCHAR(20),
  nx           NUMERIC,
  ny           NUMERIC,
  temp         NUMERIC(4,1),
  precip_prob  INT,
  inserted_at  TIMESTAMPTZ  DEFAULT now()
);

/* ========== weather_grid_raw ========== */
/* KMA 원본 예보(격자·카테고리 전부) 장기 보관 */
CREATE TABLE weather_grid_raw (
  id          BIGSERIAL PRIMARY KEY,
  base_date   CHAR(8)      NOT NULL,
  base_time   CHAR(4)      NOT NULL,
  nx          NUMERIC      NOT NULL,
  ny          NUMERIC      NOT NULL,
  category    VARCHAR(10)  NOT NULL,
  fcst_date   CHAR(8)      NOT NULL,
  fcst_time   CHAR(4)      NOT NULL,
  fcst_value  VARCHAR(20),
  created_at  TIMESTAMPTZ  DEFAULT now()
);
