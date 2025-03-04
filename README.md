# JustPvP-Sectors

## Opis projektu
JustPvP-Sectors to zaawansowany system sektorów dla serwerów Minecraft oparty na Redis. Sektory pozwalają na dynamiczne rozdzielanie graczy między różnymi instancjami serwera, zwiększając skalowalność i wydajność gry. System umożliwia synchronizację danych między sektorami, przechowywanie informacji w bazach danych oraz obsługę różnych typów sektorów (np. spawn, kopalnia, magazyn). System sektórów dostosowany jest do trybu ChestPvP Minecraft

## Funkcjonalności
- **Obsługa sektorów** – podział serwera na niezależne sektory, które mogą działać w różnych instancjach.
- **Komunikacja między sektorami** – wykorzystanie Redis do przesyłania pakietów między sektorami.
- **Obsługa użytkowników** – teleportacja graczy między sektorami, zarządzanie statystykami i synchronizacja ekwipunku.
- **System ekonomii** – obsługa sklepów (ItemShop), aukcji oraz płatności między graczami.
- **Zarządzanie rangami i uprawnieniami** – system rang i poziomów uprawnień dla graczy.
- **Magazyny i przechowalnie** – sektory magazynowe dla graczy.
- **System gildii** – wsparcie dla sojuszy, wojen gildii i zarządzania członkami.
- **System kopalń** – dynamiczne kopalnie z limitem surowców.
- **Obsługa przedmiotów specjalnych** – przedmioty premium, efekty i ulepszenia.
- **Zarządzanie wydarzeniami** – system globalnych eventów oraz Halloweenowych lokalizacji.
- **System anty-makro** – ochrona przed nieuczciwym farmieniem surowców.
- **Obsługa czatu** – zarządzanie wiadomościami, admin chat oraz kontrola spamu.
- **System backupów** – automatyczne tworzenie kopii zapasowych ekwipunku gracza.

## Baza danych
- **Redis** – wykorzystywany do przechowywania użytkowników, statystyk, sektorów i synchronizacji danych w czasie rzeczywistym.
- **MySQL** – stosowany do długoterminowego przechowywania danych, np. sklepów, aukcji oraz rankingu graczy.

## Instalacja i konfiguracja
### 1. Pobranie i instalacja
1. Pobierz plugin i umieść go w katalogu `plugins` serwera Minecraft.
2. Uruchom serwer, aby wygenerować pliki konfiguracyjne.

### 2. Konfiguracja bazy danych (`config.yml`)
```yaml
config:
  enabled: true
  database:
    mode: mysql
    tableprefix: chestpvp_
    mysql:
      host: localhost
      port: 3306
      user: root
      pass: yourpassword
      name: chestpvp
  socket:
    password: ZAQ!2wsx
    port: 1337
```

### 3. Konfiguracja sektorów (`sectors.yml`)
```yaml
sectors:
  'spawn1':
    name: spawn1
    type: SPAWN
  'magazyn':
    name: magazyn
    type: STORAGE
  'kopalnia':
    name: kopalnia
    type: MINE
```

## Struktura systemu
### 1. **Główne komponenty**
- **Core** – centralny moduł rejestrujący wszystkie funkcjonalności.
- **RedisChannel** – kanały komunikacyjne Redis odpowiedzialne za synchronizację danych.
- **SectorManager** – obsługa teleportacji graczy i rejestracja sektorów.
- **PacketManager** – rejestracja pakietów wymienianych między sektorami.
- **BackupManager** – zarządzanie kopiami zapasowymi graczy.
- **GuildManager** – obsługa systemu gildii.
- **StorageManager** – obsługa przechowalni graczy.
- **DropManager** – obsługa systemu dropów i generatorów.
- **ChatManager** – moderacja i funkcje administracyjne czatu.

### 2. **Przykładowy pakiet Redis**
Pakiet `ChatMessagePacket` odpowiedzialny za przesyłanie wiadomości między sektorami:
```java
public class ChatMessagePacket extends RedisPacket {
    private String message;

    public ChatMessagePacket(String message) {
        this.message = message;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getMessage() {
        return message;
    }
}
```

### 3. **Rejestracja pakietu w `PacketManager`**
```java
static {
    registerPacket(1, ChatMessagePacket.class);
}
```

## Podsumowanie
JustPvP-Sectors to elastyczny system zarządzania sektorami dla serwerów Minecraft, oparty na Redis. Dzięki modularnej budowie, obsłudze różnych typów sektorów oraz synchronizacji użytkowników między instancjami, system znacząco poprawia wydajność i stabilność dużych serwerów.

## Autorzy
- **CzarnaWoda** – Główny deweloper projektu
