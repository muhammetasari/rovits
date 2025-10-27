import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { getRepositoryToken } from '@nestjs/typeorm';
import { Poi, PoiCategory, DayOfWeek } from './poi/poi.entity';
import { SpecialDay } from './poi/special-day.entity';
import { Repository } from 'typeorm';

async function bootstrap() {
  const app = await NestFactory.createApplicationContext(AppModule);
  
  const poiRepo = app.get<Repository<Poi>>(getRepositoryToken(Poi));
  const specialDayRepo = app.get<Repository<SpecialDay>>(getRepositoryToken(SpecialDay));

  console.log('🌱 Seeding database...');

  // Clear existing data
  await poiRepo.delete({});
  await specialDayRepo.delete({});

  // ==================== POIs ====================
  
  // 1. Ayasofya
  const ayasofya = poiRepo.create({
    category: PoiCategory.MUSEUM,
    latitude: 41.0086,
    longitude: 28.9802,
    score: 98,
    avgDuration: 120,
    queueTime: 30,
    translations: [
      {
        locale: 'tr',
        name: 'Ayasofya',
        description: 'Bizans ve Osmanlı mimarisinin muhteşem eseri. 537 yılında inşa edilmiş tarihi yapı.'
      },
      {
        locale: 'en',
        name: 'Hagia Sophia',
        description: 'Magnificent masterpiece of Byzantine and Ottoman architecture. Historic structure built in 537 AD.'
      },
      {
        locale: 'es',
        name: 'Santa Sofía',
        description: 'Magnífica obra maestra de la arquitectura bizantina y otomana.'
      },
      {
        locale: 'de',
        name: 'Hagia Sophia',
        description: 'Herrliches Meisterwerk byzantinischer und osmanischer Architektur.'
      },
      {
        locale: 'ar',
        name: 'آيا صوفيا',
        description: 'تحفة رائعة من العمارة البيزنطية والعثمانية'
      }
    ],
    weeklySchedule: [
      { day: DayOfWeek.MONDAY, open: '09:00', close: '19:00', isClosed: false },
      { day: DayOfWeek.TUESDAY, open: '09:00', close: '19:00', isClosed: false },
      { day: DayOfWeek.WEDNESDAY, open: '09:00', close: '19:00', isClosed: false },
      { day: DayOfWeek.THURSDAY, open: '09:00', close: '19:00', isClosed: false },
      { day: DayOfWeek.FRIDAY, open: '09:00', close: '13:00', isClosed: false },
      { day: DayOfWeek.FRIDAY, open: '14:30', close: '19:00', isClosed: false },
      { day: DayOfWeek.SATURDAY, open: '09:00', close: '19:00', isClosed: false },
      { day: DayOfWeek.SUNDAY, open: '09:00', close: '19:00', isClosed: false },
    ]
  });
  await poiRepo.save(ayasofya);

  // 2. Topkapı Sarayı
  const topkapi = poiRepo.create({
    category: PoiCategory.PALACE,
    latitude: 41.0115,
    longitude: 28.9833,
    score: 95,
    avgDuration: 180,
    queueTime: 45,
    translations: [
      {
        locale: 'tr',
        name: 'Topkapı Sarayı',
        description: 'Osmanlı İmparatorluğunun 400 yıl boyunca idare merkezi olan görkemli saray.'
      },
      {
        locale: 'en',
        name: 'Topkapi Palace',
        description: 'Magnificent palace that served as the administrative center of the Ottoman Empire for 400 years.'
      },
      {
        locale: 'es',
        name: 'Palacio de Topkapi',
        description: 'Magnífico palacio que sirvió como centro administrativo del Imperio Otomano durante 400 años.'
      }
    ],
    weeklySchedule: [
      { day: DayOfWeek.MONDAY, open: '09:00', close: '18:00', isClosed: false },
      { day: DayOfWeek.TUESDAY, open: '00:00', close: '00:00', isClosed: true },
      { day: DayOfWeek.WEDNESDAY, open: '09:00', close: '18:00', isClosed: false },
      { day: DayOfWeek.THURSDAY, open: '09:00', close: '18:00', isClosed: false },
      { day: DayOfWeek.FRIDAY, open: '09:00', close: '18:00', isClosed: false },
      { day: DayOfWeek.SATURDAY, open: '09:00', close: '18:00', isClosed: false },
      { day: DayOfWeek.SUNDAY, open: '09:00', close: '18:00', isClosed: false },
    ]
  });
  await poiRepo.save(topkapi);

  // 3. Galata Kulesi
  const galata = poiRepo.create({
    category: PoiCategory.LANDMARK,
    latitude: 41.0256,
    longitude: 28.9741,
    score: 94,
    avgDuration: 90,
    queueTime: 20,
    translations: [
      {
        locale: 'tr',
        name: 'Galata Kulesi',
        description: 'İstanbul\'un panoramik manzarasını sunan tarihi Ceneviz kulesi.'
      },
      {
        locale: 'en',
        name: 'Galata Tower',
        description: 'Historic Genoese tower offering panoramic views of Istanbul.'
      },
      {
        locale: 'ar',
        name: 'برج غلطة',
        description: 'برج جنوة التاريخي الذي يوفر مناظر بانورامية لإسطنبول'
      }
    ],
    weeklySchedule: [
      { day: DayOfWeek.MONDAY, open: '08:30', close: '23:00', isClosed: false },
      { day: DayOfWeek.TUESDAY, open: '08:30', close: '23:00', isClosed: false },
      { day: DayOfWeek.WEDNESDAY, open: '08:30', close: '23:00', isClosed: false },
      { day: DayOfWeek.THURSDAY, open: '08:30', close: '23:00', isClosed: false },
      { day: DayOfWeek.FRIDAY, open: '08:30', close: '23:00', isClosed: false },
      { day: DayOfWeek.SATURDAY, open: '08:30', close: '23:00', isClosed: false },
      { day: DayOfWeek.SUNDAY, open: '08:30', close: '23:00', isClosed: false },
    ]
  });
  await poiRepo.save(galata);

  // 4. Sultanahmet Camii
  const sultanahmet = poiRepo.create({
    category: PoiCategory.MOSQUE,
    latitude: 41.0054,
    longitude: 28.9768,
    score: 90,
    avgDuration: 60,
    queueTime: 0,
    translations: [
      {
        locale: 'tr',
        name: 'Sultanahmet Camii',
        description: 'Mavi Cami olarak da bilinen, altı minareli muhteşem Osmanlı camisi.'
      },
      {
        locale: 'en',
        name: 'Blue Mosque',
        description: 'Magnificent Ottoman mosque with six minarets, also known as the Blue Mosque.'
      }
    ],
    weeklySchedule: [
      { day: DayOfWeek.MONDAY, open: '08:30', close: '11:30', isClosed: false },
      { day: DayOfWeek.MONDAY, open: '13:00', close: '14:30', isClosed: false },
      { day: DayOfWeek.MONDAY, open: '15:30', close: '18:00', isClosed: false },
      { day: DayOfWeek.TUESDAY, open: '08:30', close: '11:30', isClosed: false },
      { day: DayOfWeek.TUESDAY, open: '13:00', close: '14:30', isClosed: false },
      { day: DayOfWeek.TUESDAY, open: '15:30', close: '18:00', isClosed: false },
      { day: DayOfWeek.WEDNESDAY, open: '08:30', close: '11:30', isClosed: false },
      { day: DayOfWeek.WEDNESDAY, open: '13:00', close: '14:30', isClosed: false },
      { day: DayOfWeek.WEDNESDAY, open: '15:30', close: '18:00', isClosed: false },
      { day: DayOfWeek.THURSDAY, open: '08:30', close: '11:30', isClosed: false },
      { day: DayOfWeek.THURSDAY, open: '13:00', close: '14:30', isClosed: false },
      { day: DayOfWeek.THURSDAY, open: '15:30', close: '18:00', isClosed: false },
      { day: DayOfWeek.FRIDAY, open: '14:30', close: '18:00', isClosed: false },
      { day: DayOfWeek.SATURDAY, open: '08:30', close: '11:30', isClosed: false },
      { day: DayOfWeek.SATURDAY, open: '13:00', close: '14:30', isClosed: false },
      { day: DayOfWeek.SATURDAY, open: '15:30', close: '18:00', isClosed: false },
      { day: DayOfWeek.SUNDAY, open: '08:30', close: '11:30', isClosed: false },
      { day: DayOfWeek.SUNDAY, open: '13:00', close: '14:30', isClosed: false },
      { day: DayOfWeek.SUNDAY, open: '15:30', close: '18:00', isClosed: false },
    ]
  });
  await poiRepo.save(sultanahmet);

  // 5. İstiklal Caddesi
  const istiklal = poiRepo.create({
    category: PoiCategory.STREET,
    latitude: 41.0369,
    longitude: 28.9784,
    score: 85,
    avgDuration: 120,
    queueTime: 0,
    translations: [
      {
        locale: 'tr',
        name: 'İstiklal Caddesi',
        description: 'İstanbul\'un en ünlü alışveriş ve yaya caddesi.'
      },
      {
        locale: 'en',
        name: 'Istiklal Street',
        description: 'Istanbul\'s most famous shopping and pedestrian street.'
      }
    ],
    weeklySchedule: [
      { day: DayOfWeek.MONDAY, open: '00:00', close: '23:59', isClosed: false },
      { day: DayOfWeek.TUESDAY, open: '00:00', close: '23:59', isClosed: false },
      { day: DayOfWeek.WEDNESDAY, open: '00:00', close: '23:59', isClosed: false },
      { day: DayOfWeek.THURSDAY, open: '00:00', close: '23:59', isClosed: false },
      { day: DayOfWeek.FRIDAY, open: '00:00', close: '23:59', isClosed: false },
      { day: DayOfWeek.SATURDAY, open: '00:00', close: '23:59', isClosed: false },
      { day: DayOfWeek.SUNDAY, open: '00:00', close: '23:59', isClosed: false },
    ]
  });
  await poiRepo.save(istiklal);

  console.log('✅ POIs seeded');

  // ==================== Special Days ====================
  
  // Cumhuriyet Bayramı 2025
  const cumhuriyet2025 = specialDayRepo.create({
    date: '2025-10-29',
    affectedCategories: ['museum', 'palace'],
    isClosed: false,
    translations: [
      {
        locale: 'tr',
        name: 'Cumhuriyet Bayramı',
        note: 'Müzeler ve saraylar kapalı, Taksim ve meydanlar kalabalık'
      },
      {
        locale: 'en',
        name: 'Republic Day',
        note: 'Museums and palaces closed, Taksim and squares crowded'
      },
      {
        locale: 'es',
        name: 'Día de la República',
        note: 'Museos y palacios cerrados'
      }
    ]
  });
  await specialDayRepo.save(cumhuriyet2025);

  // Ramazan Bayramı 2025
  const ramazan2025_1 = specialDayRepo.create({
    date: '2025-03-30',
    affectedCategories: ['museum', 'palace'],
    isClosed: false,
    translations: [
      { locale: 'tr', name: 'Ramazan Bayramı 1. Gün', note: 'Müzeler ve saraylar kapalı' },
      { locale: 'en', name: 'Eid al-Fitr Day 1', note: 'Museums and palaces closed' }
    ]
  });
  await specialDayRepo.save(ramazan2025_1);

  console.log('✅ Special days seeded');
  console.log('🎉 Database seeding completed successfully!');

  await app.close();
}

bootstrap().catch(err => {
  console.error('❌ Seeding failed:', err);
  process.exit(1);
});
