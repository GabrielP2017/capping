import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import type { CampSite } from '../types';

export const useCamps = () =>
  useQuery({
    queryKey: ['camps'],
    queryFn: async () => {
      const res = await axios.get('/api/campsites', {
        params: { page: 0, size: 3000 },
      });
      console.log('🚚 camps raw', res);
      return res.data.content as CampSite[];
    },
  });
