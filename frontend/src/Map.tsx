import 'mapbox-gl/dist/mapbox-gl.css';
import { useEffect, useRef } from 'react';
import mapboxgl from 'mapbox-gl';
import { useCamps } from './hooks/useCamps';
import type { CampSite } from './types';

mapboxgl.accessToken = import.meta.env.VITE_MAPBOX_TOKEN;

interface Props { onSelect: (camp: CampSite) => void; }

export default function Map({ onSelect }: Props) {
  const mapRef = useRef<mapboxgl.Map | null>(null);
  const { data: camps, isSuccess } = useCamps();

  /* ① 지도 한 번만 초기화 */
  useEffect(() => {
    mapRef.current = new mapboxgl.Map({
      container: 'map',
      style: 'mapbox://styles/mapbox/streets-v12',
      center: [127.5, 36.5],
      zoom: 6,
    });
    return () => mapRef.current?.remove();
  }, []);

  /* ② 캠핑장 → Tailwind 마커 렌더링 */
  useEffect(() => {
    if (!isSuccess || !mapRef.current) return;

    camps!.forEach(c => {
      const el = document.createElement('div');
      el.className = 'size-[10px] rounded-full bg-[#1fff2c] ring-2 ring-white pointer-events-auto';

      el.addEventListener('click', () => {
        console.log('clicked', c.name);
        onSelect(c);
      });
      new mapboxgl.Marker({ element: el, anchor: 'center' })
        .setLngLat([c.lon, c.lat])
        .addTo(mapRef.current!);
    });
  }, [isSuccess, camps, onSelect]);

  return <div id="map" className="fixed inset-0" />;
}
