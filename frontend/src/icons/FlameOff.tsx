import React from 'react';
import { Flame, Slash } from 'lucide-react';

type Props = React.ComponentProps<typeof Flame>;

/** Flame + Slash = FlameOff */
export default function FlameOff(props: Props) {
  return (
    <span
      style={{
        position: 'relative',
        display: 'inline-block',
        lineHeight: 0,
      }}
    >
      <Flame {...props} />
      <Slash
        {...props}
        style={{ position: 'absolute', top: 0, left: 0 }}
      />
    </span>
  );
}
