/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",      // ← js·ts·jsx·tsx 모두 스캔
  ],
    safelist: [
    "w-2.5",
    "h-2.5",
    "bg-green-600",
    "rounded-full",
    "ring-2",
    "ring-white",
    "hover:scale-125",
    "transition",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
};
