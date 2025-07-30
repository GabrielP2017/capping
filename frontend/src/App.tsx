import { useState } from 'react';
import Map from './Map';
import CampModal from './CampModal';
import type { CampSite } from './types';

export default function App() {
  // 모달에서 사용할 선택 상태
  const [selected, setSelected] =
    useState<{ camp: CampSite; risk: 0 | 1 | 2 | 3 | 4 } | null>(null);

  return (
    <>
      {/* Map에 onSelect 콜백 전달 */}
      <Map onSelect={(camp, risk) => setSelected({ camp, risk })} />

      {/* 선택되면 모달 표시 */}
      {selected && (
        <CampModal
          camp={selected.camp}
          riskLevel={selected.risk}
          onClose={() => setSelected(null)}
        />
      )}
    </>
  );
}
