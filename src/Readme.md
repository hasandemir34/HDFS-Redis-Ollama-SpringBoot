🏢 Footballer Management System (Futbolcu Yönetim Sistemi)

Spring Boot 3.2, Redis Caching, Hadoop HDFS ve Local Ollama LLM (llama3.2) teknolojilerini birleştiren kurumsal düzeyde dağıtık web uygulaması.

📌 Proje Hakkında

Bu proje, futbolcu ve takım verilerini verimli bir şekilde yönetmek, performans optimizasyonu sağlamak ve modern yapay zeka servislerini sisteme entegre etmek amacıyla geliştirilmiştir.

✨ Temel Özellikler

Katmanlı Mimari (Layered Architecture): Model, DTO, Repository, Service ve Controller katmanları ile modüler kod yapısı.

İlişkisel Veri Birleştirme (JPQL JOIN): Futbolcuların takım adı ve şehir bilgilerini tek DTO sorgusunda getirir.

Yüksek Performanslı Önbellekleme (Redis Caching): @Cacheable anotasyonu ile MySQL veritabanı sorgu sonuçlarının Redis RAM belleğe alınması ve veritabanı yükünün %0'a indirilmesi.

Dağıtık Medya Depolama (Hadoop HDFS): Futbolcu fotoğraflarının Hadoop dağıtık dosya sisteminde tutulması ve WebHDFS REST API ile tarayıcıya aktarılması.

Yerel Yapay Zeka Entegrasyonu (Ollama LLM): Bilgisayarda çalışan llama3.2 modeline Spring RestTemplate ile HTTP POST istekleri atılarak geliştirilen sohbet modülü.

🏗️ Sistem Mimarisi

                                  ┌──────────────────────────┐
                                  │      İstemci (Browser)   │
                                  └────────────┬─────────────┘
                                               │ (HTTP 8090)
                                               ▼
                                  ┌──────────────────────────┐
                                  │    Spring Boot App       │
                                  │ (Controller & Service)   │
                                  └──────┬──────┬─────┬──────┘
                                         │      │     │
                 ┌───────────────────────┘      │     └──────────────────────┐
                 ▼                              ▼                            ▼
┌──────────────────────────┐   ┌──────────────────────────┐   ┌──────────────────────────┐
│       MySQL (3306)       │   │    Redis Cache (6379)    │   │   Hadoop HDFS (9870)     │
│   Relational Database    │   │    In-Memory Caching     │   │  Distributed Media Store │
└──────────────────────────┘   └──────────────────────────┘   └──────────────────────────┘
│
▼ (HTTP 11434)
┌──────────────────────────┐
│   Ollama Local AI Server │
│      (Llama 3.2 Model)   │
└──────────────────────────┘


🛠️ Kullanılan Teknolojiler

Backend: Java 17, Spring Boot 3.2.5, Spring Data JPA, Spring Cache, Spring Web

Build Tool: Gradle 8.x / 9.x

Veritabanı: MySQL Server (football şeması)

Önbellek: Redis Server

Dağıtık Dosya Sistemi: Apache Hadoop 3.3.6 (HDFS & WebHDFS API)

Yapay Zeka (AI): Ollama (llama3.2 modeli)

Frontend: HTML5, CSS3, Thymeleaf Template Engine, JavaScript (Fetch API)

📁 Proje Klasör Yapısı

src/main/java/com/sau/empsystem/
├── EmpSystemApplication.java    # Ana Çalıştırma ve @EnableCaching Sınıfı
├── controller/
│   └── FootballerController.java # Web ve API Endpoint Yönlendirmeleri
├── dto/
│   └── FootballerDto.java       # JOIN Edilmiş Veri Taşıyıcı Nesne
├── model/
│   ├── Footballer.java          # JPA Footballer Entity
│   └── Team.java                # JPA Team Entity
├── repository/
│   └── FootballerRepository.java # Futbolcu ve takım JOIN JPQL sorgusu
└── service/
├── FootballerService.java   # Redis Cache Mantığı (@Cacheable / @CacheEvict)
└── OllamaService.java       # Ollama Llama 3.2 API Entegrasyonu

src/main/resources/
├── application.properties       # Veritabanı, Redis, Hadoop ve Ollama Ayarları
└── templates/
├── index.html               # Futbolcu Listesi ve Fotoğraf Tablosu
└── chat.html                # Llama 3.2 Yapay Zeka Chat Arayüzü


🚀 Kurulum ve Çalıştırma Adımları

1. Ön Gereksinimler

Bilgisayarınızda aşağıdaki servislerin yüklü ve çalışır durumda olması gerekmektedir:

Java 17 JDK

MySQL Server 8.0+

Redis Server

Hadoop HDFS (NameNode & DataNode)

Ollama AI (ollama run llama3.2 komutu ile indirilmiş olmalı)

2. Veritabanı Hazırlığı

MySQL üzerinde football veritabanını oluşturun ve yetkili kullanıcıyı tanımlayın:

CREATE DATABASE football;
CREATE USER 'footballuser'@'localhost' IDENTIFIED BY 'footballpass';
GRANT ALL PRIVILEGES ON football.* TO 'footballuser'@'localhost';
FLUSH PRIVILEGES;


3. Windows hosts Dosyası Düzenlemesi (WebHDFS İçin)

Hadoop WebHDFS yönlendirmesinin (HTTP 307 Redirect) bilgisayarınızda çözülebilmesi için C:\Windows\System32\drivers\etc\hosts dosyasının en altına şu satırı ekleyin:

127.0.0.1   hasan.localdomain


4. application.properties Konfigürasyonu

src/main/resources/application.properties dosyasındaki ayarların kendi ortamınızla uyumlu olduğunu doğrulayın:

server.port=8080

# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/football?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=scottpass
spring.jpa.hibernate.ddl-auto=update

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# Hadoop HDFS
hdfs.web.url=http://127.0.0.1:9870/webhdfs/v1/emp_images/

# Ollama LLM
ollama.api.url=http://localhost:11434/api/generate


5. Uygulamayı Çalıştırma

Terminalden projeyi başlatmak için:

./gradlew bootRun


Veya IntelliJ IDEA içerisinde EmpSystemApplication.java sınıfını çalıştırabilirsiniz.

🖥️ Ekranlar ve Kullanım

Futbolcu Listesi Sayfası: http://localhost:8080/

Futbolcular, takımları ve HDFS'ten dinamik yüklenen fotoğrafları ile listelenir.

Yapay Zeka Chat Sayfası: http://localhost:8080/chat

Yerel llama3.2 modeli ile anlık sohbet imkanı sunar.

📝 Lisans

Bu proje Sakarya Üniversitesi ders projesi kapsamında eğitim amaçlı geliştirilmiştir.