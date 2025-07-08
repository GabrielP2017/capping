-- H2는 GiST·PostGIS 함수 미지원
CREATE INDEX IF NOT EXISTS camp_sites_geo_idx
    ON camp_sites (lon, lat);