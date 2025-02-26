# GamePedia Uygulaması

GamePedia, video oyunlarını keşfetmek ve favorilere eklemek için tasarlanmış bir **Clean Architecture** yapısına sahip Android uygulamasıdır. Kullanıcılar popüler oyunları görüntüleyebilir, detaylarına ulaşabilir ve favorilerine ekleyebilir.

## Özellikler
- **Oyun Listesi:** API'den alınan popüler oyunları listeleme.
- **Oyun Detayları:** Seçilen oyuna ait bilgileri görüntüleme.
- **Favorilere Ekleme:** Beğendiğiniz oyunları favori listenize kaydetme.
- **Offline Desteği:** Room Database kullanılarak favori oyunlarınızı çevrimdışı görüntüleme.
- **MVVM Mimari Kullanımı:** UI ve veri kaynağını yönetmek için ViewModel kullanımı.
- **Hilt Desteği:** Bağımlılık enjeksiyonu için.

## Kullanılan Teknolojiler
- **Kotlin**: Android uygulama geliştirme dili.
- **Clean Architecture**: Katmanlı mimari (Data, Domain, Presentation).
- **Retrofit**: API istekleri için.
- **Room Database**: Yerel veri saklama ve çevrimdışı destek.
- **LiveData & ViewModel**: UI güncellemeleri için.
- **RecyclerView**: Listeleme için.
- **Hilt**: Dependency Injection (Bağımlılık enjeksiyonu) için.

## Proje Yapısı

### **Ana Modüller**
- **data**: API ve veritabanı işlemlerini yönetir.
- **domain**: İş kuralları ve kullanım senaryolarını içerir.
- **presentation**: Kullanıcı arayüzü bileşenleri (Activity/Fragment/ViewModel).
- **util**: Yardımcı sınıflar ve genel fonksiyonlar.

## Kullanım
1. **Uygulamayı başlatın.**
2. **Popüler oyunları listeleyin.**
3. **Bir oyunu seçerek detaylarını görüntüleyin.**
4. **Favori listenize ekleyin ve daha sonra görüntüleyin.**

## Kurulum
1. **Projeyi klonlayın:**
   ```sh
   git clone https://github.com/kullaniciadi/GamePedia.git
   ```
2. **Android Studio ile açın.**
3. **API anahtarını ve gerekli yapılandırmaları tamamlayın.**
4. **Cihaz veya emülatör seçerek çalıştırın.**

## Katkıda Bulunma
Projeye katkıda bulunmak için bir pull request oluşturabilir veya issue açabilirsiniz.

## Lisans
Bu proje MIT Lisansı ile lisanslanmıştır. Daha fazla bilgi için LICENSE dosyasına bakınız.

![WhatsApp Görsel 2025-02-26 saat 16 43 45_27cef50e](https://github.com/user-attachments/assets/b4377d3c-3578-4d64-99c4-745146a9bd8b) ![WhatsApp Görsel 2025-02-26 saat 16 43 45_5652f53b](https://github.com/user-attachments/assets/6de818fa-958a-43c0-8fb0-e8bf028799cf) ![WhatsApp Görsel 2025-02-26 saat 16 43 44_1d617782](https://github.com/user-attachments/assets/777ed88e-dd56-4202-975a-779f9f00f5ee)
 ![WhatsApp Görsel 2025-02-26 saat 16 43 44_01931f06](https://github.com/user-attachments/assets/dcf69410-8fa4-41ab-975b-c871a481f7ee)  ![WhatsApp Görsel 2025-02-26 saat 16 43 44_301c7f38](https://github.com/user-attachments/assets/dfb20862-d399-40ab-8685-21ac6f35bda8) ![WhatsApp Görsel 2025-02-26 saat 16 43 43_3e2e12fd](https://github.com/user-attachments/assets/66eaae35-dc94-42b3-8527-c8d299ebd603)
![WhatsApp Görsel 2025-02-26 saat 16 43 43_58a9e79e](https://github.com/user-attachments/assets/9e167357-717d-45d7-b30b-d1af98a820cb)




