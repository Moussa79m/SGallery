# S Gallery | معرض الصور 🖼️

![Kotlin](https://img.shields.io/badge/Kotlin-B125EA?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
![Clean Architecture](https://img.shields.io/badge/Clean%20Architecture-Success?style=for-the-badge)


## About The Project

A high-performance, fluid native Android gallery application built with modern Android development standards. **S Gallery** is engineered to deliver a premium user experience with physics-based animations, custom gesture handling, and seamless pinch-to-zoom interactions, rivaling top-tier OEM gallery applications.

## Key Features

* 📱 **Fluid Pinch-to-Zoom Grid**: Seamless transition between grid columns (1 to 8) without frame drops, maintaining visual stability.
* 🚀 **High-Performance Rendering**: Optimized memory caching and native OS thumbnail loading for instant, lag-free scrolling.
* ✋ **Physics-Based Gestures**: Custom touch interceptors allowing simultaneous pan, zoom, and swipe actions in the full-screen viewer.
* 📅 **Smart Grouping & Scroller**: Media automatically organized by dates with a custom, high-speed lateral fast-scroller.
* 🎬 **Integrated Video Playback**: Seamless video support within the grid and immersive full-screen viewer.

## Upcoming Features

* 🔒 **Secure Vault**: Hidden, encrypted folder for private media guarded by biometric authentication (Fingerprint/Face Unlock).
* 🎨 **Image Editor**: Built-in native editing capabilities including crop, rotate, and custom filters.
* 📁 **Advanced Album Management**: Create, move, hide, and manage custom albums intuitively.
* ☁️ **Cloud Sync**: Optional background backup to cloud storage for cross-device access.
* 📍 **Map View**: Organize and visualize media globally based on EXIF geolocation data.

## Technical Details & Stack

This project heavily emphasizes performance optimization and strict separation of concerns using **Clean Architecture** principles across a multi-module setup.

* **Language**: Kotlin
* **UI Framework**: Jetpack Compose (Material 3)
* **Architecture**: Clean Architecture (Multi-module) & MVVM Pattern
* **Concurrency**: Coroutines & StateFlow
* **Image Loading**: Coil (Heavily optimized bypassing `crossfade` for bulk loads and utilizing `MediaStore` Thumbnails).
* **Gesture Handling**: Deeply customized `PointerInput` (awaitPointerEventScope) to prevent touch starvation, alongside `dokar3/pinch-zoom-grid` for Grid scaling.
* **Data Layer**: Local storage querying via Android `MediaStore` API.
* **Performance Tools**: Benchmarked using Perfetto & Android Studio Profilers to eliminate UI Jank and optimize `Choreographer#doFrame` execution times.

## Project Structure

The application is modularized by layers to ensure scalability, maintainability, and fast build times.

```text
S-Gallery/
├── app/                     # App entry point, DI configuration
├── core/                    # Common utils, extensions, and base classes
├── data/                    # Repositories implementations, MediaStore queries & Mappers
├── domain/                  # Use cases, Domain Models (@Immutable), Repo Interfaces
└── presentation/            # UI Layer (Jetpack Compose)
    ├── UI/
    │   ├── Components/      # Reusable widgets (CustomGalleryGrid, MediaItemCard, Scroller)
    │   └── Screens/         # Main screens (GalleryScreen, FullScreenViewer)
    └── ViewModels/          # State management and UI logic
```

## Installation & Build Steps

To run this project locally, ensure you have Android Studio (latest version) and JDK 17+ installed.

**Clone the repository:**

```bash
git clone https://github.com/Moussa79m/S-Gallery.git
```

**Open the project:**
Launch Android Studio, select Open, and navigate to the cloned S-Gallery directory.

**Sync Gradle:**
Allow Android Studio to download the necessary dependencies and sync the project.

**Run the App:**
Select the app configuration and press Run (Shift + F10).

> **Note:** For the best experience and to accurately evaluate gesture physics and rendering performance, it is highly recommended to run the app on a physical Android device rather than an emulator.

---

## معرض الصور (S Gallery)

### عن المشروع

تطبيق معرض صور أصلي وعالي الأداء لنظام أندرويد، مبني بأحدث معايير هندسة البرمجيات. تم تصميم S Gallery لتقديم تجربة مستخدم استثنائية مع أنيميشن فيزيائي سلس، ومعالجة متقدمة لإيماءات اللمس، وتفاعل احترافي للتكبير والتصغير، لينافس أقوى تطبيقات المعرض الافتراضية للشركات الكبرى.

### المميزات الحالية

* 📱 **شبكة صور تفاعلية**: تكبير وتصغير (Pinch-to-Zoom) بسلاسة تامة والتنقل بين الأعمدة (من 1 إلى 8) بدون أي تقطيع في الإطارات.
* 🚀 **أداء صاروخي**: تحميل فوري للصور أثناء التمرير بفضل الاعتماد على الصور المصغرة للنظام (OS Thumbnails) والإدارة الذكية للذاكرة.
* ✋ **إيماءات لمس فيزيائية**: معالجة مخصصة للمس تتيح السحب، التكبير، والتمرير بين الصور في نفس الوقت وبنعومة فائقة في وضع ملء الشاشة.
* 📅 **تجميع ذكي وشريط تمرير**: تنظيم تلقائي للوسائط حسب التواريخ، مع شريط تمرير جانبي سريع للوصول اللحظي لأي فترة زمنية.
* 🎬 **مشغل فيديو مدمج**: دعم كامل وتشغيل سلس لمقاطع الفيديو من داخل الشبكة وبوضع ملء الشاشة.

### مميزات قادمة

* 🔒 **الخزنة السرية**: مجلد مخفي ومشفر لحماية الصور الحساسة باستخدام قفل البصمة أو التعرف على الوجه.
* 🎨 **محرر صور**: أدوات مدمجة لتعديل الأبعاد، التدوير، وإضافة فلاتر بصرية احترافية.
* 📁 **إدارة الألبومات**: إنشاء، نقل، إخفاء، وإدارة الألبومات المخصصة بكل سهولة.
* ☁️ **المزامنة السحابية**: نسخ احتياطي اختياري في الخلفية للوصول للصور من أجهزة متعددة.
* 📍 **عرض الخريطة**: استعراض الصور على خريطة تفاعلية بناءً على بيانات الموقع الجغرافي (EXIF).

### التفاصيل التقنية

يعتمد هذا المشروع بشكل أساسي على تحسين الأداء والفصل الصارم بين طبقات الكود باستخدام معمارية Clean Architecture في بيئة متعددة الوحدات (Multi-module).

* **لغة البرمجة**: Kotlin
* **واجهة المستخدم**: Jetpack Compose (Material 3)
* **المعمارية**: Clean Architecture (Multi-module) و نمط MVVM
* **تعدد المهام**: Coroutines و StateFlow
* **تحميل الصور**: مكتبة Coil (محسنة لتقليل استهلاك الموارد وتخطي الـ crossfade للتحميل الجماعي).
* **معالجة اللمس**: استخدام متقدم لـ `PointerInput` لمنع تداخل الإيماءات، مع مكتبة `dokar3/pinch-zoom-grid` لشبكة العرض.
* **طبقة البيانات**: استعلامات سريعة من الذاكرة المحلية عبر واجهة `MediaStore`.
* **أدوات قياس الأداء**: تم اختبار وتحسين الكود باستخدام أدوات Perfetto و Android Studio Profilers للقضاء على التقطيع (Jank) وتقليل أوقات المعالجة.

### هيكلية المشروع

تم تقسيم التطبيق إلى وحدات (Modules) مستقلة لضمان سهولة التطوير، الصيانة، وتسريع وقت البناء.

```text
S-Gallery/
├── app/                     # نقطة انطلاق التطبيق وإعدادات حقن الاعتماديات (DI)
├── core/                    # الأدوات المساعدة، الإضافات، والكلاسات الأساسية المشتركة
├── data/                    # التعامل مع البيانات، استعلامات MediaStore والـ Mappers
├── domain/                  # نماذج البيانات (Models) وحالات الاستخدام (Use Cases)
└── presentation/            # طبقة واجهة المستخدم (Jetpack Compose)
    ├── UI/
    │   ├── Components/      # العناصر القابلة لإعادة الاستخدام (الكروت، شريط التمرير، الشبكة)
    │   └── Screens/         # الشاشات الرئيسية (شاشة المعرض، شاشة العرض الكامل)
    └── ViewModels/          # إدارة الحالة (State) والمنطق الخاص بواجهة المستخدم
```

### خطوات التثبيت والتشغيل

لتشغيل هذا المشروع على جهازك، تأكد من تثبيت أحدث إصدار من Android Studio وتوافر بيئة عمل JDK 17 أو أحدث.

**استنساخ المستودع (Clone):**

```bash
git clone https://github.com/Moussa79m/S-Gallery.git
```

**فتح المشروع:**
قم بتشغيل Android Studio، واختر Open، ثم حدد مجلد S-Gallery الذي قمت بتنزيله.

**مزامنة جاردل (Gradle Sync):**
انتظر حتى ينتهي Android Studio من تحميل المكتبات المطلوبة ومزامنة المشروع بالكامل.

**تشغيل التطبيق:**
تأكد من تحديد وحدة app من القائمة العلوية واضغط على تشغيل (Shift + F10).

> **ملاحظة هامة:** لضمان الحصول على التجربة الحقيقية واختبار سلاسة الإيماءات الفيزيائية والتمرير بشكل دقيق، يُنصح بشدة بتشغيل التطبيق على هاتف أندرويد حقيقي بدلاً من المحاكي (Emulator).
