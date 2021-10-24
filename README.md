
# 💡 Background

비콘의 BLE(Bluetooth Low Energy) 기술과 웹뷰를 이용한 어플리케이션입니다.

비콘이 설치된 장소에서의 출입을 자동으로 관리해주는 시스템을 목적으로 합니다.

언텍트 시대에 맞게 QR체크인 대신 비 접촉으로 간편하게 출입을 관리할 수 있습니다.

---


# 🛠 Development
- 담당 사항 : 백엔드
- Heroku 배포 : https://ble-auto-attendance.herokuapp.com/

## Tech Stack

- Beacon
- Mobile -Web View
- Web : Spring(Security, Thymeleaf), PostgreSQL, Mail Service, Spring data JPA, Bootstrap

- Spring Security를 이용하여 회원가입 및 로그인을 구현합니다.
- remember-me를 사용하여 로그인 유지 기능을 구현하였습니다.
- JavaMailService와 SMTP를 사용하여 회원 가입시 사용된 Email로 인증 메일을 전송합니다
- Password Encoder를 사용하여. 패스워드는 암호화하여 관리합니다.
- Bootstrap기반의 반응형 웹
- 템플릿 엔진으로는 Thymeleaf를 사용하였습니다.
- JPA로 연관관계를 설정하였습니다.

## 기능
- 비콘을 서버에 등록할 수 있고, 비콘으로 현재 장소를 등록할 수 있습니다. 장소의 생성자가 해당 장소의 관리자가 되며, 출입 기록을 관리할 수 있습니다.
- 사용자는 장소에 자신을 등록하여  출입기록을 직접 확인할 수 있습니다. 
- 블루투스 기반의 비콘이 근처에 있을 때 비콘 탐색 버튼을 누르면 현재 인식되는 비콘 목록이 출력되고, 이를 선택하여 비콘을 등록하거나 출입체크 할 수 있습니다.

QR코드 인증은 자신의 동선을 확인하는 것이 번거롭지만, 해당 시스템에서는 간편하게 자신의 동선을 확인할 수 있습니다.

## 엔티티 설계
![attendance_entity](https://user-images.githubusercontent.com/61380786/138599922-a6018d5b-76f7-401d-9a11-3f372ac484c7.png)


## 구동 화면
![image](https://user-images.githubusercontent.com/61380786/138601017-26af6ebe-d46c-44d6-9d40-5082b31a3f4e.png)

![image](https://user-images.githubusercontent.com/61380786/138601081-0473e497-62e5-4212-bab1-adb2196c607d.png)

![image](https://user-images.githubusercontent.com/61380786/138601154-292b9e98-fe8f-40fd-8c98-d55a3d93c547.png)



