# Arkanoid — Bài tập lớn Lập trình hướng đối tượng (Nhóm 4)

**Thành viên**

* Nguyễn Phú Hùng — Nhóm trưởng
* Nguyễn Huy Dũng
* Nguyễn Danh Hải Đăng
* Nguyễn Đức Bảo Trung

**Mô tả dự án**
Game Arkanoid (phiên bản đơn giản) được viết bằng **Java + Swing**. Đây là bài tập lớn môn Lập trình hướng đối tượng, yêu cầu áp dụng các nguyên lý OOP, thiết kế lớp, quản lý tài nguyên (âm thanh, ảnh), và (tùy chọn) tính năng nhiều người chơi.

## Yêu cầu

* Java 24+ (JDK 24 hoặc mới hơn)
* Git 2.48+
* (Tùy chọn) IntelliJ IDEA để phát triển và gỡ lỗi

## Chạy game

### 1) Chạy nhanh (Windows)

* Mở thư mục gốc của project
* Chạy `runGame.bat` (double-click hoặc từ terminal)

### 2) Chạy trong IntelliJ

* Nếu bạn dùng IntelliJ, trước tiên chuyển sang branch chứa cấu hình IntelliJ:

```bash
git switch intellij
```

* Mở project bằng IntelliJ (File → Open → chọn `project-root`)
* Thiết lập cấu hình chạy (Run Configuration) nếu cần, rồi chạy.

## Ghi chú Git / Nhánh

* **Nếu không dùng IDE**: làm việc trực tiếp trên `main` hoặc các feature branch.
* **Nếu dùng IntelliJ**: team đã chuẩn bị 1 branch `intellij` (chứa cấu hình/IDE files). Khi làm việc trong IntelliJ, `git switch intellij` để khớp cấu hình (nhớ không đẩy file IDE cá nhân lên nếu không muốn).
* Quy ước đặt tên branch: `feature/<tên>`, `bugfix/<tên>`, `hotfix/<tên>`.

## Tính năng hiện tại

* Cơ chế va chạm ball — paddle — bricks
* Nhiều loại gạch (brick)
* Quản lý mạng (các lớp Network / GameServer / GameClient) 
* Âm thanh nền và hiệu ứng
* Menu, pause, game over
* Lưu quá trình chơi khi ấn nút thoát khi tạm dừng

## Cách báo lỗi / góp ý

* Tạo issue trên repo git
* Hoặc liên hệ trực tiếp với nhóm trưởng: **Nguyễn Phú Hùng** 

## Luồng phát triển / đóng góp

1. Tạo branch mới từ `main`: `git switch -c feature/<tên>`
2. Thực hiện commit rõ ràng, test local
3. Tạo Pull Request lên `main` — mô tả thay đổi và cách kiểm thử
4. Người review sẽ comment / merge

## Ghi chú thêm cho giảng viên

* Thầy/cô kiểm tra code ở branch main, thầy/cô chạy code bằng cách 1 được nên ở trên. Branch intellij chỉ chứa thêm những file cấu hình dành cho những ai sử dụng intellij

---

Cảm ơn đã xem — chúc thầy/cô và các bạn kiểm tra game vui vẻ!









