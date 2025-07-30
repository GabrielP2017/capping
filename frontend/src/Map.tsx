/* ------------------------------------------------------------------
 *  Map.tsx ― Mapbox GL + 캠핑장 + 산불 위험 뱃지 마커
 * ------------------------------------------------------------------ */
import 'mapbox-gl/dist/mapbox-gl.css';
import mapboxgl from 'mapbox-gl';
import { useEffect, useRef, useState } from 'react';
import { createRoot, type Root } from 'react-dom/client';
import { useCamps } from './hooks/useCamps';
import { useWildfire } from './api/wildfire';
import { RiskBadge } from './RiskBadge';
import type { CampSite } from './types';

mapboxgl.accessToken = import.meta.env.VITE_MAPBOX_TOKEN;

export default function Map({
  onSelect,
}: {
  onSelect: (c: CampSite, l: 0 | 1 | 2 | 3 | 4) => void;
}) {
  const mapRef = useRef<mapboxgl.Map | null>(null);
  const [loaded, setLoaded] = useState(false);
  const { data: camps, isSuccess } = useCamps();

  useEffect(() => {
    mapRef.current = new mapboxgl.Map({
      container: 'map',
      style: 'mapbox://styles/mapbox/streets-v12',
      center: [127.5, 36.5],
      zoom: 6,
    }).on('load', () => setLoaded(true));

    return () => mapRef.current?.remove();
  }, []);

  if (!loaded)      return <div id="map" className="fixed inset-0" />;
  if (!isSuccess)   return <p className="p-4">로딩 중…</p>;

  return (
    <>
      <div id="map" className="fixed inset-0" />
      {camps!.map((c) => (
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
function WildfireMarker({
  camp, map, onSelect,
}: {
  camp: CampSite
  map: mapboxgl.Map
  onSelect: (camp: CampSite, l: 0|1|2|3|4) => void
}) {
  const { data, isLoading, isError } = useWildfire(camp.regionCode)

  const rootRef   = useRef<Root | null>(null)
  const markerRef = useRef<mapboxgl.Marker | null>(null)

  /* ⓐ 마커 DOM + React root → 최초 한 번 */
  useEffect(() => {
    if (markerRef.current) return   // 이미 있으면 스킵

    const el = document.createElement('div')
    Object.assign(el.style, {
      width: '28px', height: '28px',
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      cursor: 'pointer',
    })

    rootRef.current  = createRoot(el)
    markerRef.current = new mapboxgl.Marker(el)
      .setLngLat([camp.lon, camp.lat])
      .addTo(map)

    const handleClick = () =>
      onSelect(camp, (data?.riskLevel ?? 0) as 0|1|2|3|4)
    el.addEventListener('click', handleClick)

    /* clean-up */
    return () => {
      el.removeEventListener('click', handleClick)
      rootRef.current?.unmount()     // ⬅ 즉시 언마운트 (지연 제거)
      rootRef.current = null
      markerRef.current?.remove()
      markerRef.current = null
    }
  }, [])      // 의존성 비움 ⇒ 한 번만

  /* ⓑ 데이터 변화 → 렌더만 갱신 */
  useEffect(() => {
    if (!rootRef.current) return

    const Dot = (hex: string) => (
      <span style={{
        width: 20, height: 20, borderRadius: '50%',
        background: hex, display: 'inline-block',
      }}/>
    )

    if (isLoading)             rootRef.current.render(Dot('#9ca3af'))
    else if (isError || !data) rootRef.current.render(Dot('#6b7280'))
    else                       rootRef.current.render(<RiskBadge level={data.riskLevel}/>)
  }, [data, isLoading, isError])

  return null
}