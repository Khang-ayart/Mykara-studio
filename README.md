# Mykara Studio

Ứng dụng Android để vẽ và tạo hoạt hình 2D đơn giản cho MV, đi cùng [Mykara](https://github.com/Khang-ayart/Mykara). Studio tập trung vào vẽ và chuyển động; Mykara lo âm nhạc, lời bài hát và đồng bộ. Hai app trao đổi bằng file `.mkclip`.

## Bản thử hiện tại (0.1)

Xưởng vẽ một màn hình, giao diện tối, chạy trong WebView (`app/src/main/assets/index.html`):

- Bút, tẩy, màu, độ dày nét, hoàn tác/làm lại.
- Nhiều lớp (thêm, xóa, ẩn, đổi thứ tự), tối đa 10.
- Thanh thời gian, hành động chỉnh theo giây:
  - Chung: di chuyển, phóng to, thu nhỏ, xoay, mờ dần, rung.
  - Người: đi, đứng, nhảy, chạy, cười, vẫy tay.
  - Chim: vỗ cánh, sà xuống, rỉa lông.
- Xuất PNG (nền trong suốt) vào thư viện ảnh.

Chưa có: nhập ảnh, phóng to giấy bằng hai ngón, nhóm lớp (gắn tay vào thân), đổi hình theo thời gian, xuất `.mkclip` và MP4.

## Tải bản thử

Mỗi lần đẩy lên `main`, GitHub Actions tự build APK và đăng lên mục **Releases** (tag `latest`). Cài đè được vì dùng khóa ký thử cố định.

## Cấu trúc

```
app/src/main/assets/index.html          xưởng vẽ (giao diện + vẽ + hoạt hình)
app/src/main/java/.../MainActivity.kt   WebView, lưu PNG vào thư viện ảnh
.github/workflows/build.yml             tự build và đăng APK
```
