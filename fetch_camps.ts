import fs from 'fs/promises';
import axios from 'axios';
import 'dotenv/config';   // .env 자동 로드

(async () => {
  const key = process.env.SERVICE_KEY!;
  const url = 'https://apis.data.go.kr/B551011/GoCamping/basedList';

  const { data } = await axios.get(url, {
    params: {
      serviceKey: key,
      MobileOS: 'ETC',           // 필수
      MobileApp: 'CampFetcher',  // 필수: 임의의 앱 이름
      numOfRows: 3000,
      pageNo: 1,
      _type: 'json'
    }
  });

  const items = data.response.body.items.item;   // ← 구버전은 .item 배열

  let csv = 'name,address,lat,lon,phone\n';
  for (const it of items) {
    csv += [
      (it.facltNm || '').replace(/[,\\n]/g, ' '),
      (it.addr1 || '').replace(/[,\\n]/g, ' '),
      it.mapY,
      it.mapX,
      (it.tel || '').replace(/[,\\n]/g, ' ')
    ].join(',') + '\n';
  }

  await fs.mkdir('backend/src/main/resources/db', { recursive: true });
  await fs.writeFile('backend/src/main/resources/db/camps.csv', csv, 'utf8');
  console.log('✅ CSV 생성 완료, 총', items.length, '행');
})();
