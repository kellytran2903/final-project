pipeline {
    agent any // Chạy trên bất kỳ máy nào rảnh (agent)

    tools {
            // 'Maven-Local' là tên bạn vừa đặt trong: Manage Jenkins -> Tools -> Maven installations
            // ⚠️ LƯU Ý: Nếu bạn đặt tên khác (vd: 'MyMaven'), hãy sửa lại dòng dưới cho khớp 100%
            maven 'Maven-Local'
        }

    // 1️⃣ KHAI BÁO THAM SỐ ĐẦU VÀO (Sẽ hiển thị trên giao diện Jenkins)
    parameters {
        // Dropdown chọn File Suite
        choice(
            name: 'SUITE_FILE',
            choices: [
                'testng-login.xml',
                'testng-product.xml',
                'testng-profile.xml',
                'testng-cart.xml',
                'testng-e2e.xml',
                'testng-smoke.xml'
            ],
            description: 'Chọn bộ test suite (XML) muốn chạy'
        )

        // Dropdown chọn Group (Nếu chọn all thì chạy hết)
        choice(
            name: 'GROUPS',
            choices: ['all', 'smoke', 'regression', 'login', 'function', 'E2E'],
            description: 'Chọn nhóm test (TestNG Groups)'
        )

        // Dropdown chọn Trình duyệt
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'edge', 'firefox', 'safari'],
            description: 'Chọn trình duyệt để test'
        )
    }

    stages {
        // 2️⃣ GIAI ĐOẠN 1: LẤY CODE
        stage('Checkout Code') {
            steps {
                // Tự động lấy code từ nhánh bạn cấu hình trong Job
                checkout scm
            }
        }

        // 3️⃣ GIAI ĐOẠN 2: CHẠY TEST
        stage('Run Selenium Tests') {
            steps {
                script {
                    echo "🚀 Đang chạy Suite: ${params.SUITE_FILE} | Group: ${params.GROUPS} | Browser: ${params.BROWSER}"

                    // Xử lý logic Group: Nếu chọn 'all' thì không truyền tham số group
                    def groupCmd = ""
                    if (params.GROUPS != 'all') {
                        groupCmd = "-Dgroups=${params.GROUPS}"
                    }

                    // Lệnh chạy Maven
                    // Lưu ý: -DsuiteTestFile phải khớp với biến trong pom.xml
                    // Lưu ý: -DHEADLESS=true để chạy ẩn danh trên server
                    sh "mvn clean test -DsuiteTestFile=${params.SUITE_FILE} ${groupCmd} -DHEADLESS=true -DBROWSER=${params.BROWSER}"
                }
            }
        }
    }

    // 4️⃣ GIAI ĐOẠN SAU KHI CHẠY: XUẤT REPORT
    post {
        always {
            echo "📊 Đang tạo Allure Report..."
            // Yêu cầu Jenkins đã cài Allure Plugin
            allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
        }
    }
}