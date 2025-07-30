import { useQuery } from '@tanstack/react-query';
import type { CampSite } from '../types';

/** 캠핑장 목록을 캐싱-하면서 가져오는 React Query 훅 */
export function useCamps(page = 0, size = 3000) {
  return useQuery<CampSite[]>({
    queryKey: ['camps', page, size],
    queryFn: async () => {
      const res = await fetch(`/api/campsites?page=${page}&size=${size}`);
      if (!res.ok) {
        throw new Error('캠핑장 목록 로드 실패');
      }
      const data = await res.json();
      return data.content as CampSite[];
    },
    staleTime: 1000 * 60 * 10,     // 10분
    refetchOnWindowFocus: false,
  });
}
