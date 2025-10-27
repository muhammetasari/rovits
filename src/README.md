# Istanbul Tour Planner API

Multi-language tour planning API with PostgreSQL database.

## Features
- ✅ Multi-language support (TR, EN, ES, DE, AR, RU, ZH)
- ✅ Smart POI clustering by geography
- ✅ Queue time estimation
- ✅ Ferry transfer detection
- ✅ Special days/holidays support
- ✅ Weekly schedule management
- ✅ 3 transport modes (walking, public, car)

## Tech Stack
- NestJS 10
- PostgreSQL 18
- TypeORM
- TypeScript

## Installation

### 1. Install Dependencies
```bash
npm install
```

### 2. Setup PostgreSQL 18
```bash
# Install PostgreSQL 18
# Ubuntu/Debian:
sudo apt install postgresql-18

# macOS:
brew install postgresql@18

# Windows: Download from postgresql.org
```

### 3. Create Database
```bash
psql -U postgres
CREATE DATABASE istanbul_tour;
\q
```

### 4. Configure Environment
Create `.env` file:
```env
DB_HOST=localhost
DB_PORT=5432
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_NAME=istanbul_tour
```

### 5. Seed Database
```bash
npm run seed
```

### 6. Start Server
```bash
npm run start:dev
```

Server runs at: `http://localhost:3000`

## API Endpoints

### Get All POIs
```
GET /poi?locale=tr
GET /poi?locale=en
GET /poi?locale=es
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Ayasofya",
    "description": "Bizans ve Osmanlı mimarisinin muhteşem eseri",
    "category": "museum",
    "latitude": 41.0086,
    "longitude": 28.9802,
    "score": 98,
    "avgDuration": 120,
    "queueTime": 30,
    "weeklySchedule": [...]
  }
]
```

### Get Single POI
```
GET /poi/1?locale=tr
```

### Get Top POIs
```
GET /poi/top/10?locale=en
```

### Generate Plan
```
GET /plan?days=3&transport=public_transport&locale=tr
GET /plan?days=5&transport=walking&locale=en
GET /plan?days=7&transport=car&locale=es
```

**Parameters:**
- `days`: Number of days (1-10)
- `transport`: walking | public_transport | car
- `locale`: tr | en | es | de | ar | ru | zh

**Response:**
```json
[
  {
    "day": 1,
    "date": "2025-10-28",
    "totalDuration": 150,
    "totalDurationFormatted": "2h 30m",
    "specialDay": null,
    "slots": [
      {
        "time": "09:00-11:30",
        "type": "activity",
        "poi": {...},
        "duration": 150,
        "travelTime": 0
      }
    ]
  }
]
```

## Database Schema

### Tables
- `poi` - Points of interest
- `poi_translation` - Multi-language names/descriptions
- `weekly_schedule` - Opening hours per day
- `special_days` - Holidays and special events
- `special_day_translation` - Multi-language holiday names

### Adding New Language

1. Update seed.ts:
```typescript
translations: [
  {
    locale: 'fr',
    name: 'Sainte-Sophie',
    description: 'Chef-d\'œuvre magnifique...'
  }
]
```

2. Re-seed:
```bash
npm run seed
```

3. Use it:
```
GET /poi?locale=fr
GET /plan?days=3&locale=fr
```

## Adding New POIs

Edit `src/seed.ts`:

```typescript
const newPoi = poiRepo.create({
  category: PoiCategory.MUSEUM,
  latitude: 41.1234,
  longitude: 28.5678,
  score: 85,
  avgDuration: 90,
  queueTime: 15,
  translations: [
    { locale: 'tr', name: 'Yeni Müze', description: '...' },
    { locale: 'en', name: 'New Museum', description: '...' }
  ],
  weeklySchedule: [...]
});
await poiRepo.save(newPoi);
```

Then re-seed: `npm run seed`

## Production Deployment

### 1. Update app.module.ts
```typescript
synchronize: false, // IMPORTANT: Disable auto-sync in production
```

### 2. Use Migrations
```bash
npm run typeorm migration:generate -- -n InitialSchema
npm run typeorm migration:run
```

### 3. Environment Variables
Use production PostgreSQL credentials

## Special Days Management

Currently hardcoded in seed.ts. For dynamic management:

1. Create admin endpoint:
```typescript
@Post('/special-days')
createSpecialDay(@Body() data: CreateSpecialDayDto) {
  // Add special day
}
```

2. Or use database GUI (pgAdmin, DBeaver)

## Contributing

1. Add new POIs to seed.ts
2. Add translations for all supported languages
3. Update weekly schedules accurately
4. Test with different locales

## License
MIT
