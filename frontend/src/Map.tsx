/* ------------------------------------------------------------------
 *  Map.tsx ― Mapbox GL + 캠핑장 + 산불 위험 뱃지 마커
 * ------------------------------------------------------------------ */
import 'mapbox-gl/dist/mapbox-gl.css';
import mapboxgl from 'mapbox-gl';
import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { createRoot, type Root } from 'react-dom/client';
import { useCamps } from './hooks/useCamps';
import { useWildfire } from './api/wildfire';
import { RiskBadge } from './RiskBadge';
import type { CampSite } from './types';

mapboxgl.accessToken = import.meta.env.VITE_MAPBOX_TOKEN;

const MIN_MARKER_ZOOM = 6;
const VIEW_MARGIN_DEG = 0.25;
// ✅ 줌 기준(초기 화면 줌) — 초기 크기 유지용
const BASE_ZOOM = 6;
const MARKER_ZOOM_MIN = 4;     // 원하는 하한
const MARKER_ZOOM_MAX = 14;    // 원하는 상한
const MARKER_SCALE_MIN = 0.50;  // 0.65 → 0.50 (더 작아짐)
const MARKER_SCALE_MAX = 1.60;  // 1.80 → 1.60 (너무 커지지 않게)
const SCALE_PER_STEP   = 0.10;  // 0.12 → 0.10 (변화량 완만)

// (참고: 전역 CSS 변수 방식 보조 함수 — 내부에선 클램프 계산을 사용)
function zoomToScale(z: number) {
  const s = 1 + 0.12 * (z - BASE_ZOOM); // 줌 1 변할 때 12% 변화
  return Math.max(0.6, Math.min(2.0, s));
}

export default function Map({
  onSelect,
}: {
  onSelect: (c: CampSite, l: 0 | 1 | 2 | 3 | 4) => void;
}) {
  const mapRef = useRef<mapboxgl.Map | null>(null);
  const [loaded, setLoaded] = useState(false);
  const { data: camps, isSuccess } = useCamps();

  const [visibleCamps, setVisibleCamps] = useState<CampSite[]>([]);
  const campsRef = useRef<CampSite[]>([]);
  campsRef.current = camps ?? [];

  // 부모 onSelect가 리렌더 때마다 새로 생겨도 마커 effect가 흔들리지 않게 ref로 고정
  const onSelectRef = useRef(onSelect);
  useEffect(() => { onSelectRef.current = onSelect; }, [onSelect]);

  useEffect(() => {
    const map = new mapboxgl.Map({
      container: 'map',
      style: 'mapbox://styles/mapbox/streets-v12',
      center: [127.5, 36.5],
      zoom: BASE_ZOOM,
      interactive: true,
    });
    mapRef.current = map;

    // 제스처 enable
    map.dragPan.enable();
    map.scrollZoom.enable();
    map.boxZoom.enable();
    map.keyboard.enable();
    map.touchZoomRotate.enable();

    map.addControl(new mapboxgl.NavigationControl({ showCompass: true, visualizePitch: true }), 'top-right');
    map.addControl(new mapboxgl.ScaleControl({ unit: 'metric' }));

    // === (변경 1) 드래그 보강: 텍스트 선택 방지 + 터치 제스처 맵 우선 ===
    const container = map.getContainer() as HTMLElement;
    Object.assign(container.style, {
      userSelect: 'none',
      WebkitUserSelect: 'none',
      msUserSelect: 'none',
      touchAction: 'none',
      cursor: 'grab',
      zIndex: '10',
    });
    map.on('dragstart', () => (container.style.cursor = 'grabbing'));
    map.on('dragend',   () => (container.style.cursor = 'grab'));

    // === (변경 2) 마커 스케일 전역 1개 리스너로 통합 ===
    const applyScale = () => {
      const z  = map.getZoom();
      const zc = Math.min(MARKER_ZOOM_MAX, Math.max(MARKER_ZOOM_MIN, z));
      let s    = 1 + SCALE_PER_STEP * (zc - BASE_ZOOM);
      if (s < MARKER_SCALE_MIN) s = MARKER_SCALE_MIN;
      if (s > MARKER_SCALE_MAX) s = MARKER_SCALE_MAX;
      container.style.setProperty('--marker-scale', s.toFixed(3));
    };
    container.style.setProperty('--marker-scale', '1'); // 초기 크기
    map.on('render', applyScale);                       // 프레임당 1회만 계산
    map.on('load', () => { setLoaded(true); applyScale(); });

    return () => {
      map.off('dragstart', () => (container.style.cursor = 'grabbing'));
      map.off('dragend',   () => (container.style.cursor = 'grab'));
      map.off('render', applyScale);
      map.remove();
    };
  }, []);

  const computeVisible = useCallback(() => {
    const map = mapRef.current;
    if (!map || !campsRef.current.length) return;

    const zoom = map.getZoom();
    if (zoom < MIN_MARKER_ZOOM) {
      setVisibleCamps([]);
      return;
    }

    const bounds = map.getBounds() as mapboxgl.LngLatBounds;
    const west  = bounds.getWest()  - VIEW_MARGIN_DEG;
    const east  = bounds.getEast()  + VIEW_MARGIN_DEG;
    const south = bounds.getSouth() - VIEW_MARGIN_DEG;
    const north = bounds.getNorth() + VIEW_MARGIN_DEG;

    const filtered = campsRef.current.filter(c =>
      c.lon >= west && c.lon <= east && c.lat >= south && c.lat <= north
    );
    setVisibleCamps(filtered);
  }, []);

  useEffect(() => {
    const map = mapRef.current;
    if (!map) return;

    let raf = 0;
    const onMove = () => {
      cancelAnimationFrame(raf);
      raf = requestAnimationFrame(computeVisible);
    };
    map.on('move', onMove);
    map.on('moveend', computeVisible);

    if (loaded && isSuccess) computeVisible();

    return () => {
      cancelAnimationFrame(raf);
      map.off('move', onMove);
      map.off('moveend', computeVisible);
    };
  }, [loaded, isSuccess, computeVisible]);

  useEffect(() => {
    if (loaded && isSuccess) computeVisible();
  }, [loaded, isSuccess, computeVisible]);

  if (!loaded)    return <div id="map" className="fixed inset-0 pointer-events-auto" />;
  if (!isSuccess) return <p className="p-4">로딩 중…</p>;

  return (
    <>
      <div id="map" className="fixed inset-0 pointer-events-auto" />
      {visibleCamps.map((c) => (
        <WildfireMarker
          key={c.id}
          camp={c}
          map={mapRef.current!}
          onSelect={onSelect}
        />
      ))}
    </>
  );
}

/* ------------ 마커 ------------ */
type Lvl = 0 | 1 | 2 | 3 | 4;

const WildfireMarker = React.memo(function WildfireMarker({
  camp, map, onSelect,
}: {
  camp: CampSite;
  map: mapboxgl.Map;
  onSelect: (camp: CampSite, l: Lvl) => void;
}) {
  const { data, isLoading, isError } = useWildfire(camp.regionCode);

  const rootRef   = useRef<Root | null>(null);
  const markerRef = useRef<mapboxgl.Marker | null>(null);

  // 최신 위험도(ref)
  const latestRiskRef = useRef<Lvl>(0);
  useEffect(() => {
    if (isLoading || isError || !data) latestRiskRef.current = 0;
    else latestRiskRef.current = (data.riskLevel ?? 0) as Lvl;
  }, [data, isLoading, isError]);

  // onSelect ref
  const onSelectRef = useRef<(c: CampSite, l: Lvl) => void>(onSelect);
  useEffect(() => { onSelectRef.current = onSelect; }, [onSelect]);

  // 마커 mount/cleanup (data 의존 X)
  useEffect(() => {
    if (markerRef.current) return;

    const el = document.createElement('div');
    Object.assign(el.style, {
      width: '18px',
      height: '18px',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      cursor: 'pointer',
      borderRadius: '9999px',
      boxShadow: '0 1px 4px rgba(0,0,0,0.18)',
      backdropFilter: 'blur(2px)',
      willChange: 'transform',
      pointerEvents: 'auto',
      zIndex: '10',
      // === (변경 3) 전역 CSS 변수 기반 스케일만 사용 ===
      transform: 'translateZ(0) scale(var(--marker-scale, 1))',
      transformOrigin: 'center center',
    });

    rootRef.current   = createRoot(el);
    markerRef.current = new mapboxgl.Marker(el)
      .setLngLat([camp.lon, camp.lat])
      .addTo(map);

    const handleClick = (e?: MouseEvent) => {
      if (e) { e.stopPropagation(); e.preventDefault(); }
      onSelectRef.current(camp, latestRiskRef.current);
    };
    el.addEventListener('click', handleClick);

    return () => {
      el.removeEventListener('click', handleClick);

      // StrictMode 대응: 안전한 비동기 정리
      const root = rootRef.current;
      const marker = markerRef.current;
      rootRef.current = null;
      markerRef.current = null;
      setTimeout(() => { root?.unmount(); marker?.remove(); }, 0);
    };
  }, [camp.lat, camp.lon, map]); // onSelect는 ref로 고정되어 있으니 의존 X

  // 표시 콘텐츠만 교체
  useEffect(() => {
    if (!rootRef.current) return;

    const Dot = (hex: string) => (
      <span style={{ width: 14, height: 14, borderRadius: '50%', background: hex, display: 'inline-block' }} />
    );

    if (isLoading)             rootRef.current.render(Dot('#9ca3af'));
    else if (isError || !data) rootRef.current.render(Dot('#6b7280'));
    else                       rootRef.current.render(<RiskBadge level={data.riskLevel} />);
  }, [data, isLoading, isError]);

  return null;
});