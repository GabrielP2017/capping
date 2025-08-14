import { useQuery } from '@tanstack/react-query';

export interface WildfireDto {
  riskLevel: 0 | 1 | 2 | 3 | 4;
}

export function useWildfire(regionCode: string) {
  return useQuery<WildfireDto>({
    queryKey: ['wildfire', regionCode],
    queryFn : async () => {
      const r = await fetch(`/api/wildfire?regionCode=${regionCode}`);
      if (!r.ok) {
        // 404·500 → 기본 Level 0 반환
        return { riskLevel: 0 } as WildfireDto;  // 타입 단언 사용
      }
      return r.json();
    },
    staleTime: 5 * 60_000,
    enabled  : !!regionCode,
  });
}


export default useWildfire;
