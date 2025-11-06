# Arkanoid — Bài tập lớn Lập trình hướng đối tượng (Nhóm 4)

**Thành viên**

* Nguyễn Phú Hùng — Nhóm trưởng
* Nguyễn Huy Dũng
* Nguyễn Danh Hải Đăng
* Nguyễn Đức Bảo Trung

**Mô tả dự án**
Game Arkanoid (phiên bản đơn giản) được viết bằng **Java + Swing**. Đây là bài tập lớn môn Lập trình hướng đối tượng, yêu cầu áp dụng các nguyên lý OOP, thiết kế lớp, quản lý tài nguyên (âm thanh, ảnh), và tính năng nhiều người chơi, lưu game.

## Yêu cầu

* Java 24+ (JDK 24 hoặc mới hơn)
* Git 2.48+
* (Tùy chọn) IntelliJ IDEA để phát triển và gỡ lỗi

## Chạy game

* Chạy `runGame.bat` (double-click hoặc từ terminal)

## Ghi chú Git / Nhánh

* Làm việc trực tiếp trên `main` hoặc các feature branch.
* Quy ước đặt tên branch: `feature/<tên>`, `bugfix/<tên>`, `hotfix/<tên>`.

## Tính năng hiện tại

* Cơ chế va chạm ball — paddle — bricks
* Nhiều loại gạch (brick)
* Nhiều loại kỹ năng (Power Up)
* Quản lý mạng (các lớp Network / GameServer / GameClient) 
* Âm thanh nền và hiệu ứng
* Menu, pause, game over
* Lưu quá trình chơi 
* Lưu thành tích chơi

## Cách báo lỗi / góp ý

* Tạo issue trên repo git
* Hoặc liên hệ trực tiếp với nhóm trưởng: **Nguyễn Phú Hùng** 

## Luồng phát triển / đóng góp

1. Tạo branch mới từ `main`: `git switch -c feature/<tên>`
2. Thực hiện commit rõ ràng, test local
3. Tạo Pull Request lên `main` — mô tả thay đổi và cách kiểm thử
4. Người review sẽ comment / merge

---

# Thư viện sử dụng trong dự án

Dưới đây là danh sách các file `.jar` được sử dụng trong thư mục `libs/` cùng với nguồn tải và mục đích sử dụng.

| Tên thư viện | Phiên bản | Nguồn chính thức | Mục đích |
|---------------|------------|------------------|-----------|
| apiguardian-api | 1.1.2 | [https://github.com/apiguardian-team/apiguardian](https://github.com/apiguardian-team/apiguardian) | Thư viện phụ trợ cho JUnit (annotation API Guardian) |
| gson | 2.11.0 | [https://github.com/google/gson](https://github.com/google/gson) | Chuyển đổi giữa JSON và Java object |
| junit-jupiter-api | 5.10.0 | [https://junit.org/junit5/](https://junit.org/junit5/) | API chính cho JUnit 5 |
| junit-jupiter-engine | 5.10.0 | [https://junit.org/junit5/](https://junit.org/junit5/) | Engine thực thi test cho JUnit 5 |
| junit-platform-commons | 1.10.0 | [https://junit.org/junit5/](https://junit.org/junit5/) | Thành phần chung của JUnit Platform |
| junit-platform-engine | 1.10.0 | [https://junit.org/junit5/](https://junit.org/junit5/) | Nền tảng thực thi test cho JUnit |
| kryo | 2.20 | [https://github.com/EsotericSoftware/kryo](https://github.com/EsotericSoftware/kryo) | Thư viện tuần tự hóa đối tượng hiệu năng cao |
| kryonet | 2.21 | [https://github.com/EsotericSoftware/kryonet](https://github.com/EsotericSoftware/kryonet) | Thư viện networking TCP/UDP dựa trên Kryo |
| logback-classic | 1.5.6 | [https://logback.qos.ch/](https://logback.qos.ch/) | Triển khai SLF4J backend cho logging |
| logback-core | 1.5.6 | [https://logback.qos.ch/](https://logback.qos.ch/) | Thành phần lõi của Logback |
| objenesis | 3.4 | [https://github.com/easymock/objenesis](https://github.com/easymock/objenesis) | Cho phép tạo object mà không cần gọi constructor (dùng trong serialization và mocking) |
| reflectasm | 1.11.9 | [https://github.com/EsotericSoftware/reflectasm](https://github.com/EsotericSoftware/reflectasm) | Reflection nhanh hơn cho Java, hỗ trợ Kryo |
| slf4j-api | 2.0.12 | [https://www.slf4j.org/](https://www.slf4j.org/) | API logging thống nhất cho Java |
| TinySound | - | [https://github.com/finnkuusisto/TinySound](https://github.com/finnkuusisto/TinySound) | Thư viện phát âm thanh đơn giản cho Java game |

---

## Ghi chú

- Các thư viện `JUnit` chỉ cần thiết khi chạy **unit test**.
- Các thư viện `Kryo`, `KryoNet`, `ReflectASM`, `Objenesis` được dùng cho phần **networking và serialization**.
- `Logback` và `SLF4J` dùng cho **ghi log** trong ứng dụng.
- `TinySound` phục vụ **âm thanh trong game**.
- `Gson` hỗ trợ **lưu trữ / trao đổi dữ liệu JSON**.

---

Cảm ơn đã xem — chúc thầy/cô và các bạn kiểm tra game vui vẻ!









