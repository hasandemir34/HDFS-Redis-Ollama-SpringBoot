# Footballer Management System

Bu proje, futbolcu ve takım bilgilerini MySQL üzerinde tutan basit bir Spring Boot uygulamasıdır.
Projenin amacı Redis cache, Hadoop HDFS ve yerel Ollama modelinin aynı uygulamada kullanılmasını göstermektir.

## Kullanılan teknolojiler

- Java 17 ve Spring Boot 3.2.5
- MySQL ve Spring Data JPA
- Redis cache
- Hadoop HDFS / WebHDFS
- Ollama `llama3.2`
- Thymeleaf

## Proje yapısı

```text
src/main/java/com/sau/empsystem/
├── controller/FootballerController.java
├── dto/FootballerDto.java
├── model/Footballer.java
├── model/Team.java
├── repository/FootballerRepository.java
└── service/
    ├── FootballerService.java
    ├── HdfsService.java
    └── OllamaService.java
```

Ana sayfadaki liste, futbolcu ve takım tablolarının JOIN sorgusu ile hazırlanır.
Liste sonucu Redis'te cache'lenir. Yeni kayıt, güncelleme, silme veya fotoğraf yükleme işleminden
sonra liste cache'i temizlenir.

## Gerekli servisler

- MySQL: `localhost:3306`, veritabanı: `football`
- Redis: `localhost:6379`
- HDFS NameNode: `hdfs://localhost:9000`
- WebHDFS: `http://127.0.0.1:9870`
- Ollama: `http://localhost:11434`

MySQL kullanıcı bilgileri ortam değişkenleri ile verilebilir:

```text
DB_USERNAME=football_app
DB_PASSWORD=your_password
```

Uygulama ayarları `src/main/resources/application.properties` dosyasındadır.

## Çalıştırma

```text
gradlew.bat bootRun
```

Sayfalar:

- Futbolcu listesi: `http://localhost:8080/`
- Ollama chat: `http://localhost:8080/chat`

## REST işlemleri

```text
GET    /footballers
GET    /footballers/{id}
POST   /footballers
PUT    /footballers/{id}
DELETE /footballers/{id}
POST   /footballers/{id}/photo
POST   /api/chat
```

Fotoğraf yükleme isteği `multipart/form-data` tipinde ve `file` alanı ile gönderilir.
Dosya HDFS içindeki `/football` klasörüne yazılır. Ana sayfadaki fotoğraf bağlantısı WebHDFS
üzerinden bu dosyayı okur.
