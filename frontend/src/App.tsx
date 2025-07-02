// src/App.tsx  (예시)
import { useState } from 'react';
import Map from './Map';
import CampModal from './CampModal';
import type { CampSite } from './types';

export default function App() {
  const [selected, setSelected] = useState<CampSite|null>(null);
  return (
    <>
      <Map onSelect={setSelected}/>
      {selected && (console.log('modal for', selected.name),
        <CampModal camp={selected} onClose={() => setSelected(null)}/>
      )}
    </>
  );
}
