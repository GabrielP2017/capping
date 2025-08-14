import { Flame, AlertTriangle } from 'lucide-react';
import FlameOff from './icons/FlameOff';   // 새로 추가

const levelMap = [
  { hex: '#d1d5db', icon: FlameOff },                  // 0 gray-300
  { hex: '#facc15', icon: Flame },                     // 1 yellow-400
  { hex: '#f97316', icon: Flame },                     // 2 orange-500
  { hex: '#dc2626', icon: Flame },                     // 3 red-600
  { hex: '#991b1b', icon: AlertTriangle, pulse: true } // 4 red-800
];

export function RiskBadge({ level }: { level: 0 | 1 | 2 | 3 | 4 }) {
  const { hex, icon: Icon, pulse } = levelMap[level];

  return (
    <span
      title={`산불 위험도 ${level}`}
      aria-label={`산불 위험도 ${level}`}
      className={`inline-flex items-center justify-center rounded-full w-6 h-6
                  ring-1 ring-white/70 shadow-sm ${pulse ? 'animate-pulse' : ''} z-10`}
      style={{ backgroundColor: hex }}
    >
      <Icon size={14} className="text-white" />
    </span>
  );
}