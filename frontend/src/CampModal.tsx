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
      {/* 오버레이: 모달 열렸을 때만 지도 입력을 막음(정상 동작) */}
      <div
        className="fixed inset-0 bg-black/30 backdrop-blur-[1px] z-40"
        onClick={onClose}
      />

      <aside
        className={`fixed inset-y-0 left-0 z-50 w-[22rem] md:w-[26rem]
                    bg-white shadow-2xl border-r border-gray-100
                    flex flex-col
                    transform transition-transform duration-300
                    ${enter ? 'translate-x-0' : '-translate-x-full'}`}
        onClick={(e) => e.stopPropagation()}
        role="dialog"
        aria-modal="true"
      >
        <header className="sticky top-0 bg-white/95 backdrop-blur px-6 py-4 border-b border-gray-100 flex items-start justify-between">
          <h2 className="flex items-center gap-2 text-lg md:text-xl font-semibold text-gray-900">
            <span className="truncate max-w-[16rem] md:max-w-[20rem]" title={camp.name}>{camp.name}</span>
            <RiskBadge level={riskLevel} />
          </h2>
          <button aria-label="닫기" className="text-gray-500 hover:text-gray-800" onClick={onClose}>✕</button>
        </header>

        <div className="px-6 py-4 space-y-4 overflow-y-auto">
          <p className="text-sm text-gray-600">{camp.address}</p>
          <div className="grid grid-cols-2 gap-3 text-sm">
            {'phone' in camp && (camp as any).phone && (
              <div className="col-span-2">
                <div className="text-gray-500">전화</div>
                <div className="font-medium text-gray-900">{(camp as any).phone}</div>
              </div>
            )}
            <div><div className="text-gray-500">위도</div><div className="font-medium text-gray-900">{camp.lat}</div></div>
            <div><div className="text-gray-500">경도</div><div className="font-medium text-gray-900">{camp.lon}</div></div>
            {'regionCode' in camp && (
              <div className="col-span-2">
                <div className="text-gray-500">지역코드</div>
                <div className="font-medium text-gray-900">{(camp as any).regionCode}</div>
              </div>
            )}
          </div>
        </div>
      </aside>
    </>
  );
}