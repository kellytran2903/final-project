# Selenium Test Automation (Java + Maven + TestNG)

Project automation test UI dùng **Selenium WebDriver + TestNG**, quản lý dependency bằng **Maven**, hỗ trợ **Allure Report** và chạy theo **TestNG Suite XML**.

## Tech stack

- Java 17
- Maven
- Selenium `4.40.0`
- TestNG `7.4.0`
- Allure TestNG `2.31.0`

## Cấu trúc thư mục chính

- `src/main/java/`: core framework (driver, helpers, report...)
- `src/test/java/`: test cases
  - `com.hang.common.BaseTest`: khởi tạo/đóng WebDriver, hỗ trợ headless
  - `com.hang.listeners.TestListener`: listener chụp screenshot khi fail, record video (khi chạy UI)
- `src/test/resources/configs/config.properties`: cấu hình chạy test
- `src/test/resources/suites/`: các file TestNG suite XML để chạy theo bộ
- `target/allure-results/`: kết quả thô của Allure sau khi chạy test
- `exports/`: logs / screenshots / videos (tuỳ cấu hình)

## Yêu cầu môi trường

- Cài **JDK 17**
- Cài **Maven**
- Trình duyệt:
  - **Chrome** (khuyến nghị)
  - **Safari** (macOS)

> Selenium 4 có **Selenium Manager** nên thường không cần tự cài chromedriver, nhưng nếu máy bạn không tự resolve được driver thì hãy cài driver phù hợp và đảm bảo driver có trong `PATH`.

## Cấu hình chạy test

File cấu hình mặc định:

- `src/test/resources/configs/config.properties`

Một số key quan trọng:

- `BROWSER`: `chrome` hoặc `safari`
- `HEADLESS`: `true/false`
- `EXPLICIT_WAIT`, `STEP_TIME`
- `SCREENSHOT_PATH`, `RECORDVIDEO_PATH`

Lưu ý về ưu tiên cấu hình:

- `HEADLESS` và `BROWSER` có thể được truyền từ command line qua `-DHEADLESS=...` / `-DBROWSER=...`
- Nếu không truyền, hệ thống sẽ đọc từ `config.properties`

## Chạy test

Project chạy test thông qua **Maven Surefire + TestNG suite XML**.

### Chạy suite mặc định

Suite mặc định đang được set trong `pom.xml`:

- `suiteTestFile=testng-login.xml`

Chạy:

```bash
mvn clean test
```

### Chạy một suite cụ thể

Ví dụ chạy smoke suite:

```bash
mvn clean test -DsuiteTestFile=testng-smoke.xml
```

Các suite có sẵn nằm tại `src/test/resources/suites/`, ví dụ:

- `testng-login.xml`
- `testng-product.xml`
- `testng-cart.xml`
- `testng-profile.xml`
- `testng-e2e.xml`
- `testng-smoke.xml`

### Chạy với browser/headless tuỳ chọn

Chạy Chrome headless:

```bash
mvn clean test -DsuiteTestFile=testng-login.xml -DBROWSER=chrome -DHEADLESS=true
```

Chạy Chrome UI (có record video):

```bash
mvn clean test -DsuiteTestFile=testng-login.xml -DBROWSER=chrome -DHEADLESS=false
```

> Khi `HEADLESS=false`, framework sẽ cố gắng **record video** trong listener. Khi chạy headless (ví dụ CI), record sẽ được bỏ qua để tránh lỗi môi trường.

## Allure Report

Sau khi chạy test, kết quả Allure được xuất ra:

- `target/allure-results`

Project có kèm sẵn Allure CLI tại:

- `.allure/allure-2.20.1/bin/allure` (macOS/Linux)

### Generate report (static)

```bash
./.allure/allure-2.20.1/bin/allure generate target/allure-results -o target/allure-report --clean
```

### Open report (serve)

```bash
./.allure/allure-2.20.1/bin/allure serve target/allure-results
```

## Troubleshooting nhanh

- Nếu chạy trên CI/headless bị lỗi UI hoặc viewport: đã set `--window-size=1920,1080` khi headless trong `BaseTest`.
- Nếu Safari không chạy headless: Safari thường **không hỗ trợ headless** theo config Selenium thông thường.
- Nếu report Allure không có dữ liệu: kiểm tra `target/allure-results/` có được tạo và test có thực sự chạy hay không.

