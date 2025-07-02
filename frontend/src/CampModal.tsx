import { Dialog } from '@headlessui/react';
import type { CampSite } from './types';

interface Props {
  camp: CampSite;
  close: () => void;
}

export default function CampModal({ camp, close }: Props) {
  return (
    <Dialog
      open
      onClose={close}
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/30"
    >
      <Dialog.Panel className="w-80 rounded-2xl bg-white p-6 shadow-xl">
        <Dialog.Title className="text-xl font-bold">{camp.name}</Dialog.Title>
        <p className="mt-1 text-sm text-gray-600">{camp.address}</p>
        {camp.phone && (
          <p className="mt-1 text-sm text-gray-600">☎ {camp.phone}</p>
        )}

        {/* ---- Dev B 날씨 컴포넌트 자리 ---- */}
        {/* <WeatherNow campId={camp.id} /> */}

        <button
          onClick={close}
          className="mt-4 w-full rounded-xl bg-green-600 px-4 py-2 text-sm font-semibold text-white hover:bg-green-700"
        >
          닫기
        </button>
      </Dialog.Panel>
    </Dialog>
  );
}
