import "./index.css";             // ★ Tailwind 엔트리 (Map.tsx 에서 빼기)
import "mapbox-gl/dist/mapbox-gl.css";
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';


ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
