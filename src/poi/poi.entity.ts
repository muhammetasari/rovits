import { Entity, Column, PrimaryGeneratedColumn, OneToMany, ManyToOne } from 'typeorm';

export enum PoiCategory {
  MUSEUM = 'museum',
  MOSQUE = 'mosque',
  LANDMARK = 'landmark',
  PALACE = 'palace',
  STREET = 'street',
  SQUARE = 'square',
  VIEWPOINT = 'viewpoint',
  MARKET = 'market',
  WATERFRONT = 'waterfront',
  RESTAURANT = 'restaurant'
}

export enum DayOfWeek {
  MONDAY = 'Monday',
  TUESDAY = 'Tuesday',
  WEDNESDAY = 'Wednesday',
  THURSDAY = 'Thursday',
  FRIDAY = 'Friday',
  SATURDAY = 'Saturday',
  SUNDAY = 'Sunday'
}

@Entity('poi')
export class Poi {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({
    type: 'enum',
    enum: PoiCategory,
  })
  category: PoiCategory;

  @Column('decimal', { precision: 10, scale: 7 })
  latitude: number;

  @Column('decimal', { precision: 10, scale: 7 })
  longitude: number;

  @Column()
  score: number;

  @Column()
  avgDuration: number;

  @Column({ nullable: true })
  queueTime: number;

  @OneToMany(() => PoiTranslation, translation => translation.poi, { eager: true, cascade: true })
  translations: PoiTranslation[];

  @OneToMany(() => WeeklySchedule, schedule => schedule.poi, { eager: true, cascade: true })
  weeklySchedule: WeeklySchedule[];

  // Helper methods
  getName(locale: string = 'tr'): string {
    const translation = this.translations.find(t => t.locale === locale);
    return translation?.name || this.translations[0]?.name || 'Unknown';
  }

  getDescription(locale: string = 'tr'): string {
    const translation = this.translations.find(t => t.locale === locale);
    return translation?.description || '';
  }
}

@Entity('poi_translation')
export class PoiTranslation {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  poiId: number;

  @Column({ length: 5 })
  locale: string; // 'tr', 'en', 'es', 'de', 'fr', 'ar', 'ru', 'zh', etc.

  @Column()
  name: string;

  @Column('text', { nullable: true })
  description: string;

  @ManyToOne(() => Poi, (poi: Poi) => poi.translations, { onDelete: 'CASCADE' })
  poi: Poi;
}

@Entity('weekly_schedule')
export class WeeklySchedule {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({
    type: 'enum',
    enum: DayOfWeek,
  })
  day: DayOfWeek;

  @Column()
  open: string;

  @Column()
  close: string;

  @Column()
  isClosed: boolean;

  @Column()
  poiId: number;

  @ManyToOne(() => Poi, (poi: Poi) => poi.weeklySchedule, { onDelete: 'CASCADE' })
  poi: Poi;
}
