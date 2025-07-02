/* module.exports = {
  plugins: [
    require('@tailwindcss/postcss')(),   // ← v4 전용 PostCSS 플러그인
    require('autoprefixer'),
  ],
};
 */
import tailwindcssPlugin from '@tailwindcss/postcss';
import autoprefixer from 'autoprefixer';

export default {
  plugins: [
    tailwindcssPlugin(),   // ← 함수 호출!
    autoprefixer(),
  ],
};
