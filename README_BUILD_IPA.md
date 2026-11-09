# HƯỚNG DẪN ĐẨY LÊN GITHUB & BUILD FILE IPA (NSO IOS)

Dự án này là mã nguồn Xcode được export từ Unity cho game **Ninja School Online (NSO)**. Dự án đã được cấu hình tự động để build ra file `.ipa` hoàn chỉnh thông qua **GitHub Actions** (chạy trên máy chủ macOS của GitHub hoàn toàn miễn phí), đảm bảo **100% không bao giờ bị thiếu hình ảnh hay mất texture game**.

---

## ⚠️ NGUYÊN NHÂN CHÍNH DẪN ĐẾN LỖI "THIẾU HÌNH ẢNH GAME"

Trong các dự án game Unity:
- Toàn bộ hình ảnh, texture, sprite nhân vật, bản đồ, icon vật phẩm... đều nằm trong file:  
  📁 `Data/resources.assets.resS` (Dung lượng: **~201.3 MB**).
- Thư viện runtime của Unity nằm trong:  
  📁 `Frameworks/UnityRuntime.framework/UnityRuntime` (Dung lượng: **~244.7 MB**).

**GitHub có quy định giới hạn: Mỗi file đẩy lên không được vượt quá 100 MB.**
- Nếu người dùng không biết, GitHub sẽ từ chối lệnh `git push` (báo lỗi `GH001: Large files detected`).
- Nhiều người xử lý sai bằng cách thêm file `.resS` hoặc thư mục `Data/` vào `.gitignore` để đẩy lên được. Kết quả là khi build ra IPA, app vẫn chạy nhưng **mất toàn bộ hình ảnh, trắng xoá, đen màn hình hoặc văng game!**
- Hoặc nếu dùng Git LFS, GitHub Actions nếu không kéo đủ dữ liệu thì file `.resS` chỉ là một file text rỗng 130 bytes, dẫn đến việc build ra file IPA không có ảnh game.

---

## 🛠️ GIẢI PHÁP ĐÃ ĐƯỢC THIẾT LẬP SẴN

Chúng tôi đã thiết lập 2 phương pháp an toàn tuyệt đối. **Khuyên dùng Cách 1** vì hoàn toàn miễn phí và không bị giới hạn 1GB băng thông hàng tháng của Git LFS.

### CÁCH 1: CẮT FILE TỰ ĐỘNG BẰNG SCRIPT (KHUYÊN DÙNG ⭐⭐⭐⭐⭐)
> **Ưu điểm**: Không cần cài thêm Git LFS, không bao giờ bị tính dung lượng Git LFS trên GitHub, đẩy lên GitHub 100% thành công không bị chặn. Khi GitHub Actions chạy, máy chủ macOS sẽ tự động nối các phần lại nguyên vẹn trước khi build!

#### Bước 1: Mở PowerShell tại thư mục dự án và chạy:
```powershell
.\split_large_files.ps1
```
*Script sẽ cắt `resources.assets.resS` và `UnityRuntime` thành các phần nhỏ dưới 75MB (`.part00`, `.part01`...).*

#### Bước 2: Đẩy lên GitHub:
```bash
git init
git add .
git commit -m "NSO iOS full assets ready to build"
git branch -M main
git remote add origin https://github.com/TÊN_GITHUB_CỦA_BẠN/TÊN_REPO.git
git push -u origin main
```

*(Nếu muốn khôi phục lại các file trên máy tính của bạn bất kỳ lúc nào, chỉ cần chạy: `.\restore_large_files.ps1`)*

---

### CÁCH 2: DÙNG GIT LFS (NẾU BẠN CÓ TÀI KHOẢN GIT LFS)
Nếu máy bạn đã cài Git LFS (`git lfs --version`):
```bash
git init
git lfs install
git add .
git commit -m "NSO iOS with Git LFS"
git branch -M main
git remote add origin https://github.com/TÊN_GITHUB_CỦA_BẠN/TÊN_REPO.git
git push -u origin main
```
*File `.gitattributes` đã được cấu hình sẵn để theo dõi `resources.assets.resS` và `UnityRuntime`.*

---

## 🚀 CÁCH KÍCH HOẠT BUILD VÀ TẢI FILE IPA TỪ GITHUB ACTIONS

1. Vào trang Repository của bạn trên GitHub.
2. Bấm vào tab **Actions** ở menu phía trên.
3. Ở cột bên trái, chọn workflow **Build iOS IPA (NSO)**.
4. Bấm nút **Run workflow** (chọn branch `main` và ấn **Run workflow** màu xanh).
5. Chờ GitHub Actions máy chủ macOS biên dịch (khoảng 3 - 6 phút).
6. Khi icon chuyển sang màu xanh lá (Success):
   - Bấm vào tên lượt chạy vừa hoàn thành.
   - Kéo xuống phần **Artifacts** ở dưới cùng.
   - Bấm vào **Nso_Free_iOS_IPA** để tải file `.ipa` về máy tính hoặc điện thoại!

---

## 📲 CÁCH CÀI ĐẶT FILE IPA LÊN IPHONE / IPAD

File `.ipa` xuất ra đã được ký mã chuẩn (Ad-Hoc), bạn có thể cài đặt dễ dàng bằng các công cụ sau:
- **TrollStore**: Mở file `.ipa` trực tiếp bằng TrollStore trên máy đã hỗ trợ -> Cài đặt vĩnh viễn không bao giờ bị thu hồi chứng chỉ (Revoke).
- **Scarlet / Esign / Gbox**: Ký bằng chứng chỉ cá nhân hoặc chứng chỉ doanh nghiệp và cài trực tiếp trên máy.
- **Sideloadly / AltStore**: Cắm iPhone vào máy tính (Windows hoặc Mac), mở Sideloadly/AltStore, kéo file `.ipa` vào và nhập Apple ID để cài đặt.

