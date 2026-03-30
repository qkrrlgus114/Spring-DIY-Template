# Spring MVC 요청 처리 흐름

## 컨트롤러 호출하기
### HandlerMapping
- Handler들에 대한 매핑정보를 갖고 있는 객체
- 요청에 맞는 Handler를 찾아 반환시켜준다.

### HandlerAdapter
- HandlerMapping에서 받아온 Handler를 처리해주는 객체

### 참고
- https://stonehee99.tistory.com/24
- https://jaehee1007.tistory.com/3