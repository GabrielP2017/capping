import 'mapbox-gl/dist/mapbox-gl.css';
import { useEffect, useRef } from 'react';
import mapboxgl from 'mapbox-gl';
import { useCamps } from './hooks/useCamps';
import type { CampSite } from './types';
import "./index.css";

mapboxgl.accessToken = import.meta.env.VITE_MAPBOX_TOKEN;

interface Props {
  onSelect: (camp: CampSite) => void;
}

export default function Map({ onSelect }: Props) {
  const mapRef = useRef<mapboxgl.Map | null>(null);
  const { data: camps, isSuccess } = useCamps();

  /** 지도 초기화 (한 번만) */
  useEffect(() => {
    mapRef.current = new mapboxgl.Map({
      container: 'map',
      style: 'mapbox://styles/mapbox/streets-v12',
      center: [127.5, 36.5],
      zoom: 6,
    });
    

    return () => mapRef.current?.remove();
  }, []);



  /** 캠핑장 → 마커 렌더링 */
  useEffect(() => {
    if (!isSuccess || !mapRef.current) return;

    camps!.forEach((c) => {
      const el = document.createElement('div');
      el.className = 'w-2.5 h-2.5 bg-green-600 rounded-full ring-2 ring-white hover:scale-125 transition';
      el.addEventListener('click', () => onSelect(c));

      new mapboxgl.Marker(el)
        .setLngLat([c.lon, c.lat])
        .addTo(mapRef.current!);
    });
  }, [isSuccess, camps, onSelect]);

  return <div id="map" className="fixed inset-0" />;
}
