import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';

export enum PoiCategory {
  MUST_SEE = 'must_see',
  FOOD = 'food',
  SHOPPING = 'shopping',
  NATURE = 'nature'
}

@Entity('poi')
export class Poi {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ unique: true })
  placeId: string;

  // MANUEL DATA
  @Column()
  score: number;

  @Column({ default: 120 })
  avgDuration: number;

  @Column({
    type: 'enum',
    enum: PoiCategory,
    default: PoiCategory.MUST_SEE
  })
  category: PoiCategory;

  // CACHED DATA (FROM API)
  @Column({ nullable: true })
  name: string;

  @Column({ nullable: true })
  address: string;

  @Column({ type: 'timestamp', default: () => 'CURRENT_TIMESTAMP' })
  nameUpdatedAt: Date;

  @Column({ type: 'float', nullable: true })
  rating: number;

  @Column({ type: 'timestamp', default: () => 'CURRENT_TIMESTAMP' })
  ratingUpdatedAt: Date;

  @Column({ type: 'jsonb', nullable: true })
  openingHours: any;

  @Column({ type: 'timestamp', default: () => 'CURRENT_TIMESTAMP' })
  hoursUpdatedAt: Date;

  @Column({ type: 'jsonb', nullable: true })
  photos: any;

  @Column({ type: 'decimal', precision: 10, scale: 7, nullable: true })
  latitude: number;

  @Column({ type: 'decimal', precision: 10, scale: 7, nullable: true })
  longitude: number;
}