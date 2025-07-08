/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{js,ts,jsx,tsx}',
  ],

  // 👇 런타임 문자열로 등장하는 클래스를 JIT가 날려버리지 않도록 보존
  safelist: [
    'bg-green-600',
    'ring-2',
    'ring-white',
    'hover:scale-125',

    // ② 가변 픽셀값 패턴은 정규식으로 한 번에
    {
      pattern: /(size|w|h)-\[\d+px\]/,   // size-[10px] · w-[10px] · h-[…] 다 잡힘
    },
  ],
};
