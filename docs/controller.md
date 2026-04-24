# Controller
## 1. 인터페이스 방식 (Legacy)
- `Controller`인터페이스를 직접 구현하거나, `AbstractController`, `SimpleFormController`같은 추상 클래스를 상속받아 구현하는 방식.
- 특징:
  - `handleRequest(HttpServletRequest, HttpServletResponse)` 메소드를 필수적으로 구현해야 함.
  - 계층형 구조: 컨트롤러가 특정 프레임워크 클래스에 의존함.
  - URL 맵핑: XML 설정 파일(dispatcher-servlet.xml)에 `SimpleUrlHandlerMapping` 등을 사용하여 컨트롤러와 URL을 직접 매핑해야 함.
- 단점: 컨트롤러 하나당 클래스가 하나씩 필요하여 코드량이 많아지고, URL 매핑 정보가 설정 파일에 분산되어 관리하기 어려움.
## 2. 어노테이션 방식 (Modern)
- `@Controller` 또는 `@RestController` 어노테이션을 클래스에 붙여 POJO(Plain Old Java Object) 형태로 개발하는 방식.
- 특징:
  - POJO 스타일: 인터페이스 구현이나 상속 없이 자유로운 클래스 구조 사용 가능.
  - 유연한 메소드 시그니처: 메소드 이름, 파라미터(DTO, 모델 등), 리턴 타입을 자유롭게 설정 가능.
  - 어노테이션 기반 매핑: `@RequestMapping`, `@GetMapping`, `@PostMapping` 등을 사용하여 메소드 수준에서 URL과 요청 메소드를 명시함.
- 장점: 코드가 간결해지고, URL 매핑 정보를 한눈에 볼 수 있어 유지보수가 용이함.