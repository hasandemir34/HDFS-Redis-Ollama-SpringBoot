# Footballer Management System (SWE 307 - Big Data Project 2)

Bu proje; ilişkisel veritabanı (MySQL), bellek içi önbellekleme (Redis), dağıtık dosya depolama (Hadoop HDFS / WebHDFS) ve yerel yapay zeka (Ollama - Llama 3.2) teknolojilerini tek bir Spring Boot çatısı altında birleştiren mimari bir web uygulamasıdır.

---

## 1. Projenin Çalışma Mantığı ve Mimarisi

Uygulama 4 temel bileşen üzerinde çalışır:

1. **İlişkisel Veritabanı (MySQL & Spring Data JPA):**
   * Futbolcu (`footballers`) ve takım (`teams`) tabloları tutulur.
   * Futbolcu ve takım bilgileri `JOIN` sorgusuyla birleştirilerek DTO (`FootballerDto`) olarak tek seferde çekilir.
2. **Bellek İçi Önbellek (Redis Cache):**
   * Veritabanı (MySQL) disk tabanlı çalıştığı için sık tekrarlanan sorgular sunucu yanıt süresini artırır.
   * Redis, verileri RAM üzerinde saklayarak sorgu süresini milisaniyeler seviyesine indirir.
   * `@Cacheable`, `@CachePut` ve `@CacheEvict` anotasyonları ile veri okuma işlemlerinde önce Redis kontrol edilir, yeni kayıt veya silme işlemlerinde cache güncellenir.
3. **Dağıtık Dosya Sistemi (Hadoop HDFS / WebHDFS):**
   * Futbolcu fotoğrafları gibi büyük medya dosyaları MySQL'de değil, dağıtık ve ölçeklenebilir Hadoop HDFS dosya sisteminde (`/football` dizininde) saklanır.
   * Fotoğraflar tarayıcıya Java uygulamasını araya sokup sunucuyu yormadan, Hadoop'un yerleşik **WebHDFS** HTTP servisi (`http://127.0.0.1:9870/webhdfs/v1/football/{fotoğraf}?op=OPEN`) üzerinden doğrudan aktarılır.
4. **Yerel Yapay Zeka Sohbeti (Ollama - Llama 3.2):**
   * Yerel bilgisayarda çalışan Ollama servisi (`localhost:11434`), Llama 3.2 modeli ile Spring Boot üzerinden haberleşir.
   * `/chat` sayfasında ChatGPT benzeri modern ve asenkron bir sohbet arayüzü sunulur.

---

## 2. Dosya Yapısı ve Dosyaların Görevleri

```text
src/
├── main/
│   ├── java/com/sau/empsystem/
│   │   ├── FootballerApplication.java       # Spring Boot ana başlangıç sınıfı (@EnableCaching)
│   │   ├── controller/
│   │   │   └── FootballerController.java    # Web sayfaları ve REST CRUD endpoint'lerini yöneten denetleyici
│   │   ├── dto/
│   │   │   └── FootballerDto.java           # Futbolcu ve Takım JOIN sorgusunun sonucunu tutan veri transfer nesnesi
│   │   ├── model/
│   │   │   ├── Footballer.java              # 'footballers' tablosu JPA Entity sınıfı
│   │   │   └── Team.java                    # 'teams' tablosu JPA Entity sınıfı
│   │   ├── repository/
│   │   │   └── FootballerRepository.java    # Veritabanı sorguları ve JOIN sorgusunu barındıran JPA Repository
│   │   └── service/
│   │       ├── FootballerService.java       # İş mantığı ve Redis cache yönetimini gerçekleştiren servis
│   │       └── OllamaService.java           # Ollama REST API ile haberleşen yerel LLM servisi
│   └── resources/
│       ├── application.properties           # Veritabanı, Redis, WebHDFS ve Ollama bağlantı ayarları
│       └── templates/
│           ├── index.html                   # Futbolcu tablosu ve HDFS resimlerini listeleyen ana sayfa
│           └── chat.html                    # Ollama Llama 3.2 ile sohbet edilen chat arayüzü
```

### Detaylı Dosya Açıklamaları:

* **`FootballerApplication.java`:**
  * Uygulamanın giriş noktasıdır (`main` metodu).
  * Üzerindeki `@EnableCaching` anotasyonu sayesinde Spring'in Redis önbellek mekanizmasını tüm projede aktif hale getirir.
* **`FootballerController.java`:**
  * İstemciden (tarayıcıdan veya Postman'den) gelen istekleri karşılar.
  * `GET /`: Ana sayfayı (`index.html`) açar; futbolcu listesini ve HDFS URL ön ekini Thymeleaf modeline ekler.
  * `GET /chat`: Yapay zeka sohbet sayfasını (`chat.html`) açar.
  * `GET /footballers/all`: Tüm futbolcuları ve takım detaylarını JSON formatında döner.
  * `GET /footballers/{id}`: ID'si verilen futbolcuyu Redis/MySQL üzerinden getirir.
  * `POST /footballers/add`: Yeni futbolcu kaydeder.
  * `PUT /footballers/{id}`: Mevcut futbolcuyu günceller.
  * `DELETE /footballers/{id}`: Futbolcuyu siler.
  * `POST /api/chat`: Sohbet ekranından gelen soruyu `OllamaService`'e iletir ve modelin cevabını döner.
* **`FootballerService.java`:**
  * Veritabanı işlemleri ile önbellek koordinasyonunu sağlar:
    * `getAllFootballerDetails()`: Futbolcu ve takım detaylarını JOIN sorgusuyla getirir.
    * `findFootballer(id)`: `@Cacheable(value = "footballers", key = "#id")` ile önce Redis'e bakar, yoksa MySQL'den okuyup Redis'e kaydeder.
    * `saveFootballer(footballer)`: `@CachePut(value = "footballers", key = "#footballer.footballerid")` ile hem MySQL'e yazar hem de Redis'i günceller.
    * `deleteFootballer(id)`: `@CacheEvict(value = "footballers", key = "#id")` ile kaydı MySQL'den silerken Redis önbelleğinden de temizler.
* **`OllamaService.java`:**
  * Spring `RestTemplate` kullanarak `http://localhost:11434/api/generate` adresine POST isteği gönderir.
  * JSON gövdesinde `model: "llama3.2:latest"` ve kullanıcının sorduğu `prompt` bilgisini göndererek gelen cevabı metin olarak döndürür.
* **`FootballerRepository.java`:**
  * Spring Data JPA arayüzüdür.
  * İçerisindeki `@Query` anotasyonu ile `Footballer` tablosu ile `Team` tablosunu `JOIN` eden özel JPQL sorgusu yer alır ve sonucu `FootballerDto` nesnesi olarak üretir.
* **`Footballer.java` & `Team.java`:**
  * MySQL'deki tablolara karşılık gelen JPA Entity modelleridir.
  * Futbolcu ve Takım arasında `@ManyToOne` ilişkisi kurulmuştur. Redis'te serileştirilebilmeleri için `Serializable` arayüzünü uygularlar.
* **`FootballerDto.java`:**
  * Futbolcu adı-soyadı, forma numarası, piyasa değeri, fotoğrafı ile takım adı ve şehrini bir arada taşımak için kullanılan Data Transfer Object sınıfıdır.
* **`application.properties`:**
  * MySQL bağlantı URL'si, kullanıcı adı ve şifresi.
  * Redis host ve port bilgisi (`localhost:6379`).
  * WebHDFS dosya erişim adresi (`http://127.0.0.1:9870/webhdfs/v1/football/`).
  * Ollama servis adresi (`http://localhost:11434/api/generate`).
* **`index.html`:**
  * Thymeleaf şablonudur.
  * Futbolcu listesini tablo olarak gösterir.
  * Resim etiketinde `th:src="@{${hdfsWebUrl} + ${f.photo} + '?op=OPEN'}"` kullanarak fotoğrafları HDFS'ten çeker.
  * Chat sayfasına geçiş için bağlantı içerir.
* **`chat.html`:**
  * Kullanıcının Llama 3.2 modeliyle mesajlaşabileceği sohbet arayüzüdür.
  * `fetch("/api/chat")` ile asenkron istek atarak sayfayı yenilemeden ChatGPT tarzında yanıtları ekrana ekler.

---

## 3. Gerekli Servisler ve Kurulum Adımları

Projeyi çalıştırmadan önce sisteminizde aşağıdaki servislerin açık olması gerekir:

1. **MySQL (Port 3306):**
   * `football` adında veritabanı olmalıdır.
   * `teams` ve `footballers` tabloları tanımlı olmalı ve takım-futbolcu verileri bulunmalıdır.
2. **Redis (Port 6379):**
   * Yerel Redis sunucusunun çalışır durumda olması gerekir.
3. **Hadoop Single-Node Cluster & WebHDFS (Port 9870):**
   * NameNode ve DataNode servisleri başlatılmalıdır (`start-dfs.cmd` / `start-dfs.sh`).
   * HDFS üzerinde `/football` klasörü oluşturulmalı ve futbolcu resimleri buraya yüklenmelidir:
     ```bash
     hdfs dfs -mkdir -p /football
     hdfs dfs -put osimhen.jpg /football/
     hdfs dfs -put icardi.jpg /football/
     ```
4. **Ollama Sunucusu (Port 11434):**
   * Ollama arka planda çalışmalı ve Llama 3.2 modeli indirilmiş olmalıdır:
     ```bash
     ollama run llama3.2:latest
     ```

---

## 4. Projeyi Çalıştırma

Terminalde proje ana dizininde aşağıdaki komutu çalıştırın:

```powershell
.\gradlew.bat bootRun
```

Uygulama ayağa kalktığında tarayıcınızdan aşağıdaki adreslere erişebilirsiniz:

* **Futbolcu Listesi (HDFS Resimleri & JOIN Tablosu):**
  `http://localhost:8080/`
* **Ollama LLM Chat Sayfası:**
  `http://localhost:8080/chat`

---

## 5. REST API Endpoint Tablosu

| Metot | URL | Açıklama |
| :--- | :--- | :--- |
| `GET` | `/` | Ana sayfa (Thymeleaf - JOIN Tablosu & HDFS Fotoğrafları) |
| `GET` | `/chat` | Chat sayfası (Thymeleaf - Ollama Sohbet Ekranı) |
| `GET` | `/footballers/all` | Tüm futbolcuların detaylı listesi (JSON) |
| `GET` | `/footballers/{id}` | ID'ye göre futbolcu bilgisi (Redis Cache destekli) |
| `POST` | `/footballers/add` | Yeni futbolcu ekleme (JSON gövdesi ile) |
| `PUT` | `/footballers/{id}` | Mevcut futbolcuyu güncelleme (JSON gövdesi ile) |
| `DELETE` | `/footballers/{id}` | Futbolcu silme (Cache temizlenir) |
| `POST` | `/api/chat` | Ollama Llama 3.2 modeline prompt gönderme (`{"prompt": "..."}`) |
