**LayBare**
- Google 이미지 검색 Custom Api를 통한 이미지 검색
- Landmark Recognition api를 통한 위치 검색
- ML Kit 의 Text, Entity Extraction을 라이브러리를 활용한 이미지의 텍스트 분석
- ML Kit 의 Image Labeling을 통한 이미지의 특징 분석

**부가 설명**
- MVVM 디자인 패턴 적용(MVI migration 진행중)
- Shared 와 State Flow를 활용한 UI state 업데이트
- Flow 와 Retrofit2 기반 HTTP 통신
- Clean Architecture 적용
- 아키텍처 계층 모듈화
- Navigation을 통한 페이지 이동
- Hilt를 활용한 의존성 주입

**라이브러리**
Navigation, Hilt, Retrofit2, Glide, Text-Recognition,Entity-Extraction, Google Map, Orbit, Compose



**현재 구현된 기능**
- Custom Api를 통한 사진 검색
- 이미지에서 추출한 텍스트를 가져와 편집, 복사 기능
- Cloud vision의 Landmark Recognition으로 사진의 랜드마크를 검색
  - 위치를 Google Map으로 표시
  - 이름, 주소, 관련 사진 데이터 표시
- 추출한 텍스트에서 Entity Extraction을 통해 가져온 이메일, 전화번호 데이터를 사용한 연락처 생성 기능
- Image Labeling 라이이브러리를 사용해 추출한 이미지의 특징으로 유사한 사진 검색 기능

**Compose Migration**
- Similar Image 페이지 완료
- Search Image 페이지 완료
- Text Result 페이지 진행중

**MVI 패턴 적용**
- Similar Image 페이지 완료
- Search Image 페이지 완료
- Text Result 페이지 완료


