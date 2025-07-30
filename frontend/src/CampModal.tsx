import { useEffect, useState } from 'react';
import { RiskBadge } from './RiskBadge';
import type { CampSite } from './types';

interface Props {
  camp: CampSite;
  riskLevel: 0 | 1 | 2 | 3 | 4;
  onClose: () => void;
}

/** 좌측 슬라이딩 패널 (네이버 지도 스타일) */
export default function CampModal({ camp, riskLevel, onClose }: Props) {
  const [enter, setEnter] = useState(false);
  useEffect(() => setEnter(true), []);

  return (
    <>
      {/* 어두운 오버레이 ― 클릭 시 패널 닫힘 */}
      <div
        className="fixed inset-0 bg-black/20 z-40"
        onClick={onClose}
      />

      {/* ----- 슬라이딩 패널 ----- */}
      <aside
        className={`fixed inset-y-0 left-0 z-50 w-80 md:w-96
                    bg-white shadow-xl p-6 overflow-y-auto
                    transform transition-transform duration-300
                    ${enter ? 'translate-x-0' : '-translate-x-full'}`}
        onClick={(e) => e.stopPropagation()}
      >
        <header className="flex items-start justify-between mb-4">
          <h2 className="flex items-center gap-2 text-xl font-semibold">
            {camp.name}
            <RiskBadge level={riskLevel} />
          </h2>

          {/* Tailwind → HEX 직접 지정 */}
          <button
            aria-label="닫기"
            className="text-[#6B7280] hover:text-[#1F2937]"
            onClick={onClose}
          >
            ✕
          </button>
        </header>

        <p className="text-sm" style={{ color: '#4B5563' /* gray-600 */ }}>
          {camp.address}
        </p>

        {/* 확장 영역 (전화번호·날씨 등) */}
      </aside>
    </>
  );
}