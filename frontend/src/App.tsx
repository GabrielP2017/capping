import { useState } from 'react';
import Map from './Map';
import CampModal from './CampModal';
import type { CampSite } from './types';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const qc = new QueryClient();

export default function App() {
  const [selected, setSelected] = useState<CampSite | null>(null);

  return (
    <QueryClientProvider client={qc}>
      <Map onSelect={setSelected} />
      {selected && (
        <CampModal camp={selected} close={() => setSelected(null)} />
      )}
    </QueryClientProvider>
  );
}
