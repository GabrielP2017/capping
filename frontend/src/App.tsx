import { useState } from 'react';
import Map from './Map';
import CampModal from './CampModal';
import type { CampSite } from './types';

export default function App() {
  // 모달에서 사용할 선택 상태
  const [selected, setSelected] =
    useState<{ camp: CampSite; risk: 0 | 1 | 2 | 3 | 4 } | null>(null);

  return (
    <div className="w-screen h-screen">
      <Map onSelect={(camp, risk) => setSelected({ camp, risk })} />
      {selected && (
        <CampModal
          camp={selected.camp}
          riskLevel={selected.risk}
          onClose={() => setSelected(null)}
        />
      )}
    </div>
  );
}
