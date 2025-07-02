import type { CampSite } from './types';

interface Props {
  camp: CampSite;          // 이미 있음
  onClose: () => void;     // ★ 추가 (이름 맞추기)
}

/* 모달창 위치나 css는 나중에 의논해서 */
export default function CampModal({ camp, onClose }: Props) {
  return (
    <>
      <div
        className="fixed inset-0 bg-[#00000066] z-40"
        onClick={onClose}
      />
      <div
        className="fixed inset-x-0 top-1/4 mx-auto w-96
                   bg-[#ffffff] rounded-lg p-6 shadow-xl z-50"
      >
        <h2 className="text-xl font-semibold mb-2">{camp.name}</h2>
        <p className="text-sm text-gray-600 mb-4">{camp.address}</p>

        <button
          className="mt-4 px-4 py-2 rounded modal-btn
                     bg-[#16a34a] hover:bg-[#15803d] text-white"
          onClick={onClose}
        >
          닫기
        </button>
      </div>
    </>
  );
}
