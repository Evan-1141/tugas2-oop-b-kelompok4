# Tugas 2 Pemrograman Berorientasi Objek - REST API Manajemen Event & Ticketing

## 1. Deskripsi Singkat Proyek

REST API Manajemen Event & Ticketing merupakan aplikasi berbasis Java yang digunakan untuk mengelola data pengguna, venue, event, dan tiket secara terstruktur menggunakan arsitektur REST API.

Project ini menerapkan konsep Object Oriented Programming (OOP) seperti abstract class, inheritance, interface, polymorphism, custom exception, repository pattern, service layer, dan SQLite sebagai media penyimpanan data.

Fitur utama yang tersedia meliputi:

* Manajemen User
* Manajemen Venue
* Manajemen Event
* Pembelian Tiket
* Refund Tiket
* Price Summary Event
* Sales Report Event

---

## 2. Cara Menjalankan Server

### 1. Clone Repository

```bash
git clone <https://github.com/Evan-1141/tugas2-oop-b-kelompok4.git>
```

### 2. Masuk ke Folder Project

```bash
cd project
```

### 3. Compile Source Code

Compile seluruh file Java sesuai struktur project.

Contoh:

```bash
javac -cp ".;../lib/*" App.java server/*.java database/*.java repository/*.java service/*.java model/*.java handler/*.java exception/*.java
```

> Sesuaikan perintah compile apabila struktur project berbeda.

### 4. Jalankan Server

```bash
java -cp ".;../lib/*" App
```

### 5. Server Berjalan

Server berjalan pada:

```
http://localhost:8080
```

---

# 3. Daftar Endpoint API

Base URL

```text
http://localhost:8080
```

---

# User

## GET /api/users

### Request

```http
GET /api/users
```

### Response

```json
{
    "status": "success",
    "data": [
        {
            "id": "USR-001",
            "name": "Bali Event Organizer",
            "email": "organizer@mail.com",
            "role": "organizer",
            "createdAt": "2026-06-26 12:00:00",
            "updatedAt": null
        }
    ]
}
```

---

## GET /api/users/{id}

### Request

```http
GET /api/users/USR-001
```

### Response

```json
{
    "status": "success",
    "data": {
        "id": "USR-001",
        "name": "Bali Event Organizer",
        "email": "organizer@mail.com",
        "role": "organizer",
        "createdAt": "2026-06-26 12:00:00",
        "updatedAt": null
    }
}
```

---

## POST /api/users

### Request

```json
{
    "name": "John Doe",
    "email": "john@mail.com",
    "role": "buyer"
}
```

### Response

```json
{
    "status": "success",
    "data": {
        "id": "USR-005",
        "name": "John Doe",
        "email": "john@mail.com",
        "role": "buyer",
        "createdAt": "2026-06-26 13:00:00",
        "updatedAt": null
    }
}
```

---

## PUT /api/users/{id}

### Request

```json
{
    "name": "John Updated",
    "email": "johnupdated@mail.com",
    "role": "buyer"
}
```

### Response

```json
{
    "status": "success",
    "data": {
        "id": "USR-005",
        "name": "John Updated",
        "email": "johnupdated@mail.com",
        "role": "buyer",
        "createdAt": "2026-06-26 13:00:00",
        "updatedAt": "2026-06-26 14:00:00"
    }
}
```

---

## DELETE /api/users/{id}

### Request

```http
DELETE /api/users/USR-005
```

### Response

```json
{
    "status": "success",
    "message": "User berhasil dihapus."
}
```

---

# Venue

## GET /api/venues

### Request

```http
GET /api/venues
```

### Response

```json
{
    "status": "success",
    "data": [
        {
            "id": "VNU-001",
            "name": "GWK Cultural Park",
            "address": "Jimbaran",
            "maxCapacity": 10000
        }
    ]
}
```

---

## GET /api/venues/{id}

### Request

```http
GET /api/venues/VNU-001
```

### Response

```json
{
    "status": "success",
    "data": {
        "id": "VNU-001",
        "name": "GWK Cultural Park",
        "address": "Jimbaran",
        "maxCapacity": 10000,
        "createdAt": "2026-06-26 12:00:00",
        "updatedAt": null,
        "events": [
            {
                "id": "EVT-001",
                "name": "Bali Music Festival 2026"
            }
        ]
    }
}
```

---

## POST /api/venues

### Request

```json
{
    "name":"GWK Bali",
    "address":"Jl. Raya Uluwatu",
    "maxCapacity":9000
}
```

### Response

```json
{
    "status":"success",
    "data":{
        "id":"VNU-004",
        "name":"GWK Bali",
        "address":"Jl. Raya Uluwatu",
        "maxCapacity":9000,
        "createdAt":"2026-06-26 12:00:00",
        "updatedAt":null,
        "events":[]
    }
}
```

---

## PUT /api/venues/{id}

### Request

```json
{
    "name":"GWK Bali Updated",
    "address":"Uluwatu",
    "maxCapacity":9500
}
```

### Response

```json
{
    "status":"success",
    "data":{
        "id":"VNU-004",
        "name":"GWK Bali Updated",
        "address":"Uluwatu",
        "maxCapacity":9500,
        "createdAt":"2026-06-26 12:00:00",
        "updatedAt":"2026-06-26 13:00:00",
        "events":[]
    }
}
```

---

## DELETE /api/venues/{id}

### Request

```http
DELETE /api/venues/VNU-004
```

### Response

```json
{
    "status":"success",
    "message":"Venue berhasil dihapus."
}
```

---

# Event

## GET /api/events

### Request

```http
GET /api/events
```

### Response

```json
{
    "status":"success",
    "data":[]
}
```

---

## GET /api/events/{id}

### Request

```http
GET /api/events/EVT-001
```

### Response

```json
{
    "status":"success",
    "data":{
        "id":"EVT-001",
        "type":"concert",
        "name":"Bali Music Festival 2026",
        "venue":{
            "id":"VNU-001",
            "name":"GWK Cultural Park"
        },
        "organizer":{
            "id":"USR-001",
            "name":"Bali Event Organizer"
        },
        "date":"2027-08-15",
        "basePrice":250000,
        "priceList":{
            "vip":750000,
            "regular":250000,
            "festival":175000
        },
        "remainingCapacity": {
            "vip": 90,
            "regular": 350,
            "festival": 850
        },
        "refundable":true,
        "refundPolicy":"100% if >14 days, 50% if 7-14 days, 0% if <7 days"
    }
}
```

---

## POST /api/events

### Request

```json
{
    "type":"concert",
    "name":"Bali Music Festival 2026",
    "venueId":"VNU-001",
    "organizerId":"USR-001",
    "date":"2027-08-15",
    "basePrice":250000
}
```

### Response

```json
{
    "status":"success",
    "data":{
        "id":"EVT-001"
    }
}
```

---

## PUT /api/events/{id}

### Request

```json
{
    "name":"Bali Music Festival Updated"
}
```

### Response

```json
{
    "status":"success",
    "data":{
        "id":"EVT-001",
        "updatedAt":"2026-06-26 15:00:00"
    }
}
```

---

## DELETE /api/events/{id}

### Request

```http
DELETE /api/events/EVT-001
```

### Response

```json
{
    "status":"success",
    "message":"Event berhasil dihapus."
}
```

---

## GET /api/events/price-summary

### Request

```http
GET /api/events/price-summary
```

### Response

```json
{
    "status":"success",
    "data":[
        {
            "id":"EVT-001",
            "name":"Bali Music Festival 2026",
            "type":"concert",
            "prices":{
                "vip":750000,
                "regular":250000,
                "festival":175000
            }
        }
    ]
}
```

---

# Ticket

## GET /api/tickets

### Request

```http
GET /api/tickets
```

atau

```http
GET /api/tickets?eventId=EVT-001
GET /api/tickets?userId=USR-003
GET /api/tickets?status=active
```

### Response

```json
{
    "status":"success",
    "data":[]
}
```

---

## GET /api/tickets/{id}

### Request

```http
GET /api/tickets/TKT-001
```

### Response

```json
{
    "status":"success",
    "data":{
        "id":"TKT-001",
        "event":"Bali Music Festival 2026",
        "eventType":"concert",
        "buyer":{
            "id":"USR-003",
            "name":"Kadek Surya"
        },
        "category":"VIP",
        "quantity":2,
        "unitPrice":750000,
        "totalPrice":1500000,
        "purchaseDate":"2026-06-26",
        "status":"active"
    }
}
```

---

## POST /api/tickets

### Request

```json
{
    "eventId":"EVT-001",
    "userId":"USR-003",
    "category":"VIP",
    "quantity":2
}
```

### Response

```json
{
    "status":"success",
    "data":{
        "id":"TKT-001",
        "event":"Bali Music Festival 2026",
        "eventType":"concert",
        "buyer":{
            "id":"USR-003",
            "name":"Kadek Surya"
        },
        "category":"VIP",
        "quantity":2,
        "unitPrice":750000,
        "totalPrice":1500000,
        "purchaseDate":"2026-06-26",
        "status":"active"
    }
}
```

---

## PUT /api/tickets/{id}/refund

### Request

```http
PUT /api/tickets/TKT-001/refund
```

### Response

```json
{
    "status":"success",
    "data":{
        "id":"TKT-001",
        "event":"Bali Music Festival 2026",
        "totalPaid":1500000,
        "refundPercentage":100,
        "refundAmount":1500000,
        "status":"refunded"
    }
}
```

---

# Report

## GET /api/reports/sales?eventId=EVT-001

### Request

```http
GET /api/reports/sales?eventId=EVT-001
```

### Response

```json
{
    "status":"success",
    "data":{
        "event":"Bali Music Festival 2026",
        "ticketsSold":2,
        "revenue":1500000,
        "refunds":1,
        "refundAmount":1500000
    }
}
```

## 4. Struktur Proyek

```text
tugas2-oop-b-kelompok4/
│
├── lib/
│   ├── jackson-annotations-2.13.3.jar
│   ├── jackson-core-2.13.3.jar
│   ├── jackson-databind-2.13.3.jar
│   └── sqlite-jdbc-3.49.1.0.jar
│
├── src/
│   ├── App.java
│   │
│   ├── database/
│   │   └── DatabaseManager.java
│   │
│   ├── exception/
│   │   ├── EventNotFoundException.java
│   │   ├── RefundNotAllowedException.java
│   │   └── TicketSoldOutException.java
│   │
│   ├── handler/
│   │   ├── EventHandler.java
│   │   ├── TicketHandler.java
│   │   ├── UserHandler.java
│   │   └── VenueHandler.java
│   │
│   ├── model/
│   │   ├── Concert.java
│   │   ├── Event.java
│   │   ├── Refundable.java
│   │   ├── Seminar.java
│   │   ├── SportMatch.java
│   │   ├── Ticket.java
│   │   ├── User.java
│   │   └── Venue.java
│   │
│   ├── repository/
│   │   ├── EventRepository.java
│   │   ├── TicketRepository.java
│   │   ├── UserRepository.java
│   │   └── VenueRepository.java
│   │
│   ├── server/
│   │   ├── Request.java
│   │   ├── Response.java
│   │   ├── RouteHandler.java
│   │   └── Server.java
│   │
│   └── service/
│       ├── EventService.java
│       ├── TicketService.java
│       ├── UserService.java
│       └── VenueService.java
│
├── .gitignore
└── README.md
```

---

## 5. Pembagian Tugas Anggota

| Anggota                              | NIM        | Tanggung Jawab                                                     |
| ------------------------------------ | ---------- | ------------------------------------------------------------------ |
| Danendra Evan Arkananta              | 2505551126 | Seluruh Service, DatabaseManager, EventHandler                     |
| Ida Bagus Weda Astawa                | 2505551123 | Seluruh Model                                                      |
| Kadek Angga Dwiyana Putra            | 2505551130 | Seluruh Repository                                                 |
| Putu Aldo Chrisna Arkananta Suwidana | 2505551167 | UserHandler, VenueHandler, TicketHandler, Seluruh Custom Exception |
