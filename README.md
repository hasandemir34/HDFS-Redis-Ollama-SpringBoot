# Footballer Management System

Spring Boot ile geliştirilmiş futbolcu yönetim uygulamasıdır. Projede futbolcu ve takım bilgileri **MySQL**, önbellekleme **Redis**, fotoğraflar **Hadoop HDFS** ve yapay zekâ sohbeti **Ollama (Llama 3.2)** ile yönetilir.

## Kullanılan Teknolojiler

- Java 17 ve Spring Boot
- Spring Data JPA / MySQL
- Redis
- Hadoop HDFS / WebHDFS
- Ollama - Llama 3.2
- Thymeleaf

## Gereksinimler

Uygulamayı çalıştırmadan önce aşağıdaki servisler açık olmalıdır:

- MySQL: `localhost:3306`
- Redis: `localhost:6379`
- Hadoop WebHDFS: `localhost:9870`
- Ollama: `localhost:11434`

MySQL içinde `football` veritabanı ve `teams` ile `footballers` tabloları bulunmalıdır. Ollama için model kurulumu:

```bash
ollama pull llama3.2:latest
```

## Çalıştırma

Proje klasöründe Windows terminalini açın ve çalıştırın:

```powershell
.\gradlew.bat bootRun
```

Uygulama başladıktan sonra:

- Ana sayfa: http://localhost:8080/
- Yapay zekâ sohbeti: http://localhost:8080/chat

## API

| Metot | Adres | Açıklama |
|---|---|---|
| `GET` | `/footballers/all` | Tüm futbolcuları listeler |
| `GET` | `/footballers/{id}` | ID ile futbolcu getirir |
| `POST` | `/footballers/add` | Yeni futbolcu ekler |
| `PUT` | `/footballers/{id}` | Futbolcu günceller |
| `DELETE` | `/footballers/{id}` | Futbolcu siler |
| `POST` | `/api/chat` | Ollama'ya mesaj gönderir |

## Proje Yapısı

```text
src/main/java/com/sau/empsystem/
├── controller/   # Web ve API istekleri
├── service/      # İş mantığı, Redis ve Ollama işlemleri
├── repository/   # Veritabanı sorguları
├── model/        # Entity sınıfları
└── dto/          # Veri transfer nesneleri
```
