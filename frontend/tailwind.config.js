/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",      // ← js·ts·jsx·tsx 모두 스캔
  ],
  theme: {
    extend: {},
  },
  plugins: [],
};
